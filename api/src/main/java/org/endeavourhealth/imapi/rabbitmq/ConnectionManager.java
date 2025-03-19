package org.endeavourhealth.imapi.rabbitmq;

import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

@Service
public class ConnectionManager {
  private ConnectionFactory connectionFactory;
  private Connection connection;
  private Map<String, Channel> channels = new HashMap<>();
  private Logger LOG = LoggerFactory.getLogger(ConnectionManager.class);


  public ConnectionManager() throws IOException, TimeoutException {
    connectionFactory = new ConnectionFactory();
    connectionFactory.setHost(System.getenv("SPRING_RABBITMQ_HOST"));
    connectionFactory.setPort(5672);
    connectionFactory.setVirtualHost(System.getenv("SPRING_RABBITMQ_VIRTUAL_HOST"));
    connectionFactory.setUsername(System.getenv("SPRING_RABBITMQ_USERNAME"));
    connectionFactory.setPassword(System.getenv("SPRING_RABBITMQ_PASSWORD"));
    connection = connectionFactory.newConnection();
  }

  public Connection getConnection() {
    return connection;
  }

  public Channel getPublisherChannel(String username) throws IOException {
    if (channels.containsKey(username)) {
      return channels.get(username);
    }
    Channel channel = getConnection().createChannel();
    channel.confirmSelect();
    AMQP.Exchange.DeclareOk exchange = channel.exchangeDeclare("query_runner", "topic", true);
    LOG.info("Created exchange: query_runner");
    AMQP.Queue.DeclareOk queue = channel.queueDeclare("query.execute.publish", true, false, false, null);
    AMQP.Queue.BindOk binding = channel.queueBind("query.execute.publish", "query_runner", "query.execute." + username);
    org.springframework.amqp.rabbit.connection.CachingConnectionFactory amqpConnectionFactory = new org.springframework.amqp.rabbit.connection.CachingConnectionFactory();
    AmqpAdmin admin = new RabbitAdmin(amqpConnectionFactory);
    channels.put(username, channel);
    LOG.info("Created queue: {}", queue.getQueue());
    return channel;
  }

  public void createConsumerChannel() throws IOException {
    Channel channel = getConnection().createChannel();
    channel.exchangeDeclare("query_runner", "topic", true);
    channel.queueDeclare("query.execute.consume", true, false, false, null);
    channel.queueBind("query.execute.consume", "query_runner", "query.execute.#");
    DeliverCallback deliverCallback = (consumerTag, delivery) -> {
      String message = new String(delivery.getBody(), "UTF-8");
      LOG.info("Received a message: {}", message);
    };
    LOG.info("Waiting for query executor messages...");
    channel.basicConsume("query.execute.consume", true, deliverCallback, consumerTag -> {
    });
  }

  public void publishToQueue(String username, String message) throws IOException, InterruptedException, TimeoutException {
    Channel channel = getPublisherChannel(username);
    LOG.info("Publishing to queue: {}", message);
    channel.basicPublish("query_runner", "query.execute." + username, null, message.getBytes());
    channel.waitForConfirmsOrDie(5_000);
  }
}
