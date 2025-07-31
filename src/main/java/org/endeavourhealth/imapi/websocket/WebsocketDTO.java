package org.endeavourhealth.imapi.websocket;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WebsocketDTO {
  private String toUser;
  private Object content;

  public WebsocketDTO setToUser(String user) {
    this.toUser = user;
    return this;
  }

  public WebsocketDTO setContent(Object content) {
    this.content = content;
    return this;
  }
}
