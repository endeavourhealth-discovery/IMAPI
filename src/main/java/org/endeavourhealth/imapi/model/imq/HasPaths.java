package org.endeavourhealth.imapi.model.imq;

import org.endeavourhealth.interfacemanager.model.Path;

import java.util.List;

public interface HasPaths {
  List<Path> getPath();

  HasPaths setPath(List<Path> paths);

  HasPaths setIri(String iri);

  HasPaths addPath(Path path);

  String getNode();
}
