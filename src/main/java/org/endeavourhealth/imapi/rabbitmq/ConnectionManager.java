package org.endeavourhealth.imapi.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import lombok.Getter;
import org.endeavourhealth.imapi.logic.service.QueryService;
import org.endeavourhealth.imapi.logic.service.RequestObjectService;
import org.endeavourhealth.imapi.model.postgres.DBEntry;
import org.endeavourhealth.imapi.model.postgres.QueryExecutorStatus;
import org.endeavourhealth.imapi.model.requests.QueryRequest;
import org.endeavourhealth.imapi.postgres.PostgresService;
import org.endeavourhealth.imapi.utility.ThreadContext;
import org.endeavourhealth.imapi.vocabulary.Graph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

@Lazy
@Service
public class ConnectionManager {
  private static final Logger LOG = LoggerFactory.getLogger(ConnectionManager.class);
  private static final String EXCHANGE_NAME = "query_runner";
  private static final String QUEUE_NAME = "query.execute";
  @Getter
  private final Connection connection;
  private final ObjectMapper om = new ObjectMapper();
  private final RequestObjectService requestObjectService = new RequestObjectService();
  private final CachingConnectionFactory connectionFactory;
  private final PostgresService postgresService = new PostgresService();
  private QueryService queryService = new QueryService();
  private boolean createdChannel = false;

  public ConnectionManager() {
    connectionFactory = new CachingConnectionFactory();
    connectionFactory.setHost(System.getenv("RABBITMQ_HOST"));
    connectionFactory.setPort(5672);
    connectionFactory.setVirtualHost(System.getenv("RABBITMQ_VIRTUALHOST"));
    connectionFactory.setUsername(System.getenv("RABBITMQ_USERNAME"));
    connectionFactory.setPassword(System.getenv("RABBITMQ_PASSWORD"));
    connection = connectionFactory.createConnection();
  }

  public Channel getPublisherChannel(UUID userId) throws IOException {
    createExchange();
    Channel channel = getConnection().createChannel(false);
    channel.confirmSelect();
    channel.exchangeDeclare(EXCHANGE_NAME, "topic", true);
    LOG.info("Created exchange: query_runner");
    AMQP.Queue.DeclareOk queue = channel.queueDeclare(QUEUE_NAME, true, false, false, null);
    channel.queueBind(QUEUE_NAME, "query_runner", "query.execute." + userId);
    LOG.info("Created queue: {}", queue.getQueue());
    return channel;
  }

  public void createExchange() {
    TopicExchange topicExchange = new TopicExchange(EXCHANGE_NAME);
    AmqpAdmin admin = new RabbitAdmin(connectionFactory);
    admin.declareExchange(topicExchange);
  }

  public void createConsumerChannel(PostgresService postgresService) throws IOException {
    createExchange();
    try (Channel channel = getConnection().createChannel(false)) {
      channel.exchangeDeclare(EXCHANGE_NAME, "topic", true);
      channel.queueDeclare(QUEUE_NAME, true, false, false, null);
      channel.queueBind(QUEUE_NAME, "query_runner", "query.execute.#");
      DeliverCallback deliverCallback = (consumerTag, delivery) -> {
        String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
        String id = delivery.getProperties().getMessageId();
        UUID uuid = UUID.fromString(id);
        LOG.info("Received a message: {}", uuid);
        DBEntry entry;
        try {
          entry = postgresService.getById(uuid);
  //        Skip if cancelled. RabbitMQ has no remove functionality while queued
          if (null != entry && entry.getStatus().equals(QueryExecutorStatus.CANCELLED)) {
            channel.basicReject(delivery.getEnvelope().getDeliveryTag(), false);
            return;
          }
        } catch (SQLException e) {
          throw new RuntimeException(e);
        }
        if (null == entry) {
          throw new RuntimeException("Could not find entry with id " + id);
        }
        entry.setStatus(QueryExecutorStatus.RUNNING);
        try {
          postgresService.update(entry);
        } catch (SQLException e) {
          throw new RuntimeException(e);
        }
        List<Graph> userGraphs = requestObjectService.getUserGraphs(String.valueOf(entry.getUserId()));
        ThreadContext.setUserGraphs(userGraphs);
        try {
          QueryRequest queryRequest = om.readValue(message, QueryRequest.class);
          if (null == queryService) {
            queryService = new QueryService();
          }
          queryService.executeQuery(queryRequest);
          entry.setStatus(QueryExecutorStatus.COMPLETED);
          postgresService.update(entry);
        } catch (Exception e) {
          entry.setStatus(QueryExecutorStatus.ERRORED);
          entry.setError(e.getMessage());
          try {
            postgresService.update(entry);
          } catch (SQLException ex) {
            throw new RuntimeException(ex);
          }
        }
      };
      LOG.info("Waiting for query executor messages...");
      channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {
      });
    } catch (TimeoutException e) {
      throw new RuntimeException(e);
    }
  }

  public UUID publishToQueue(UUID userId, String userName, QueryRequest message) throws Exception {
    if (!createdChannel) {
      createConsumerChannel(postgresService);
      createdChannel = true;
    }
    Channel channel = getPublisherChannel(userId);
    UUID id = UUID.randomUUID();
    LOG.info("Publishing to queue: {}", id);
    AMQP.BasicProperties.Builder propertiesBuilder = new AMQP.BasicProperties().builder();
    AMQP.BasicProperties properties = propertiesBuilder
      .messageId(id.toString())
      .build();
    String jsonMessage = om.writeValueAsString(message);
    DBEntry entry = new DBEntry()
      .setId(id)
      .setQueryRequest(message)
      .setQueryIri(message.getQuery().getIri())
      .setQueryName(message.getQuery().getName())
      .setStatus(QueryExecutorStatus.QUEUED)
      .setUserId(userId)
      .setUserName(userName)
      .setQueuedAt(LocalDateTime.now());
    postgresService.create(entry);
    channel.basicPublish(EXCHANGE_NAME, "query.execute." + userId, properties, jsonMessage.getBytes());
    channel.waitForConfirmsOrDie(5_000);
    return id;
  }
}
