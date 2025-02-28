package org.endeavourhealth.imapi.model.imq;

import java.util.function.Consumer;

public interface GraphNode{

  Path getPath();

  GraphNode setPath(Path path);

  GraphNode path (Consumer< Path > builder);

  String getVariable();

  GraphNode setVariable(String variable);
}
