package org.endeavourhealth.imapi.websocket;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WebSocketDTO {
  private String toUser;
  private Object content;

  public WebSocketDTO setToUser(String user) {
    this.toUser = user;
    return this;
  }

  public WebSocketDTO setToUser(UUID user) {
    this.toUser = user.toString();
    return this;
  }

  public WebSocketDTO setContent(Object content) {
    this.content = content;
    return this;
  }
}
