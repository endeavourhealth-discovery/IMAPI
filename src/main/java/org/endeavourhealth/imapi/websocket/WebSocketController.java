package org.endeavourhealth.imapi.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class WebSocketController {

  @Autowired
  private SimpMessagingTemplate simpMessagingTemplate;

  @MessageMapping("/private-message")
  public void processPrivateMessage(Principal principal, WebsocketDTO message) {
    simpMessagingTemplate.convertAndSendToUser(message.getToUser(), "/queue/messages", message.getContent());
  }
}
