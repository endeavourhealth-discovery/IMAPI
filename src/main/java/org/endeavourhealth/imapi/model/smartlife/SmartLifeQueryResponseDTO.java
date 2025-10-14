package org.endeavourhealth.imapi.model.smartlife;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SmartLifeQueryResponseDTO {

  String query_execution_id;
  String status;
  int queue_position; //optional?
  String started_at;
  String finished_at;
  int progress;       // number?
  String error_message;

}
