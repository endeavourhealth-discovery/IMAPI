package org.endeavourhealth.imapi.model.customexceptions;

public class RabbitMQException extends Exception {
  public RabbitMQException(String message) {
    super(message);
  }

  public RabbitMQException(String message, Throwable exception) {
    super(message, exception);
  }
}
