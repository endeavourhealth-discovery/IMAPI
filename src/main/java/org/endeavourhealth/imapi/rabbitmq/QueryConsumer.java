package org.endeavourhealth.imapi.rabbitmq;

import org.endeavourhealth.imapi.postgress.PostgresService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Component
public class QueryConsumer implements ApplicationListener<ContextRefreshedEvent> {
  private Logger LOG = LoggerFactory.getLogger(QueryConsumer.class);

  @Autowired
  private ConnectionManager connectionManager;

  @Autowired
  private PostgresService postgresService;

  public QueryConsumer() throws IOException, TimeoutException {
  }

  @Override
  public void onApplicationEvent(ContextRefreshedEvent event) {
    try {
      connectionManager.createConsumerChannel(postgresService);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    LOG.info("Created inbound channel");
  }
}
