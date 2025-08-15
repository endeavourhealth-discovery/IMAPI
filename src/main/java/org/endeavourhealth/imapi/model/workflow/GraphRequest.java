package org.endeavourhealth.imapi.model.workflow;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.workflow.task.TaskHistory;
import org.endeavourhealth.imapi.model.workflow.task.TaskState;
import org.endeavourhealth.imapi.model.workflow.task.TaskType;
import org.endeavourhealth.imapi.vocabulary.Graph;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class GraphRequest extends Task {
  private Graph graph;

  public GraphRequest(TTIriRef id, TaskType type, String createdBy, String assignedTo, TaskState state, LocalDateTime dateCreated, List<TaskHistory> history, String hostUrl, Graph graph) {
    super(id, createdBy, type, state, assignedTo, dateCreated, history, hostUrl);
    this.graph = graph;
  }

  public GraphRequest setGraph(Graph graph) {
    this.graph = graph;
    return this;
  }
}
