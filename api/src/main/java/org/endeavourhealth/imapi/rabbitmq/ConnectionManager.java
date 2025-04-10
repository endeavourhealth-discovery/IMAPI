package org.endeavourhealth.imapi.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import org.endeavourhealth.imapi.errorhandling.SQLConversionException;
import org.endeavourhealth.imapi.logic.service.QueryService;
import org.endeavourhealth.imapi.model.imq.QueryRequest;
import org.endeavourhealth.imapi.model.postgres.DBEntry;
import org.endeavourhealth.imapi.postgress.PostgresService;
import org.endeavourhealth.imapi.postgress.QueryExecutorStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

@Service
public class ConnectionManager {
  private static final String EXCHANGE_NAME = "query_runner";
  private static final String QUEUE_CONSUME_NAME = "query.execute.consume";
  private CachingConnectionFactory connectionFactory;
  private Connection connection;
  private Logger LOG = LoggerFactory.getLogger(ConnectionManager.class);
  private ObjectMapper om = new ObjectMapper();
  private QueryService queryService = new QueryService();
  private PostgresService postgresService = new PostgresService();

  public ConnectionManager() throws IOException, TimeoutException {
    connectionFactory = new CachingConnectionFactory();
    connectionFactory.setHost(System.getenv("SPRING_RABBITMQ_HOST"));
    connectionFactory.setPort(5672);
    connectionFactory.setVirtualHost(System.getenv("SPRING_RABBITMQ_VIRTUALHOST"));
    connectionFactory.setUsername(System.getenv("SPRING_RABBITMQ_USERNAME"));
    connectionFactory.setPassword(System.getenv("SPRING_RABBITMQ_PASSWORD"));
    connection = connectionFactory.createConnection();
  }

  public Connection getConnection() {
    return connection;
  }

  public Channel getPublisherChannel(String username) throws IOException {
    createExchange();
    Channel channel = getConnection().createChannel(false);
    channel.confirmSelect();
    channel.exchangeDeclare(EXCHANGE_NAME, "topic", true);
    LOG.info("Created exchange: query_runner");
    AMQP.Queue.DeclareOk queue = channel.queueDeclare("query.execute.publish", true, false, false, null);
    channel.queueBind("query.execute.publish", "query_runner", "query.execute." + username);
    LOG.info("Created queue: {}", queue.getQueue());
    return channel;
  }

  public void createExchange() {
    TopicExchange topicExchange = new TopicExchange(EXCHANGE_NAME);
    AmqpAdmin admin = new RabbitAdmin(connectionFactory);
    admin.declareExchange(topicExchange);
  }

  public void createDeadLetterExchange() {

  }

  public void createConsumerChannel(PostgresService postgresService) throws IOException {
    createExchange();
    Channel channel = getConnection().createChannel(false);
    channel.exchangeDeclare(EXCHANGE_NAME, "topic", true);
    channel.queueDeclare(QUEUE_CONSUME_NAME, true, false, false, null);
    channel.queueBind(QUEUE_CONSUME_NAME, "query_runner", "query.execute.#");
    DeliverCallback deliverCallback = (consumerTag, delivery) -> {
      String message = new String(delivery.getBody(), "UTF-8");
      String id = delivery.getProperties().getMessageId();
      UUID uuid = UUID.fromString(id);
      LOG.info("Received a message: {}", message);
      DBEntry entry;
      try {
        entry = postgresService.getById(uuid);
//        Skip if cancelled. RabbitMQ has no remove functionality while queued
        if (entry.getStatus().equals(QueryExecutorStatus.CANCELLED)) {
          channel.basicReject(delivery.getEnvelope().getDeliveryTag(), false);
          return;
        }
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
      entry.setStatus(QueryExecutorStatus.RUNNING);
      try {
        postgresService.update(entry);
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
      try {
        QueryRequest queryRequest = om.readValue(message, QueryRequest.class);
        queryService.executeQuery(queryRequest);
        entry.setStatus(QueryExecutorStatus.COMPLETED);
        postgresService.update(entry);
      } catch (Exception | SQLConversionException e) {
        entry.setStatus(QueryExecutorStatus.ERRORED);
        try {
          postgresService.update(entry);
        } catch (SQLException ex) {
          throw new RuntimeException(ex);
        }
      }
    };
    LOG.info("Waiting for query executor messages...");
    channel.basicConsume(QUEUE_CONSUME_NAME, true, deliverCallback, consumerTag -> {
    });
  }

  public void publishToQueue(String userId, String userName, QueryRequest message) throws Exception {
    Channel channel = getPublisherChannel(userId);
    LOG.info("Publishing to queue: {}", message.getQuery().getIri());
    UUID id = UUID.randomUUID();
    AMQP.BasicProperties.Builder propertiesBuilder = new AMQP.BasicProperties().builder();
    AMQP.BasicProperties properties = propertiesBuilder
      .messageId(id.toString())
      .build();
    channel.basicPublish(EXCHANGE_NAME, "query.execute." + userId, properties, message.toString().getBytes());
    channel.waitForConfirmsOrDie(5_000);
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
  }
}
