package org.endeavourhealth.imapi.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.model.Pageable;
import org.endeavourhealth.imapi.model.postgres.DBEntry;
import org.endeavourhealth.imapi.postgress.PostgresService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.sql.SQLException;
import java.util.UUID;

@Controller
public class WebSocketController {
  @Autowired
  private PostgresService postgresService = new PostgresService();

  @Autowired
  private SimpMessagingTemplate simpMessagingTemplate;

  @MessageMapping("/userQueryQueue")
  public void processPrivateMessage(Principal principal, WebSocketDTO message) {
    simpMessagingTemplate.convertAndSendToUser(message.getToUser(), "/queue/messages", message.getContent());
  }

  public void updateUserQueryQueue(UUID userId) throws SQLException, JsonProcessingException {
    Pageable<DBEntry> userQueue = postgresService.getAllByUserId(userId, 1, 20);
    simpMessagingTemplate.convertAndSendToUser(userId.toString(), "/queue/messages", userQueue);
  }
}
