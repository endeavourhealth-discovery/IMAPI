package org.endeavourhealth.imapi.model.imq;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.List;

public interface HasPaths {
  List<Path> getPath();
  HasPaths setPath(List<Path> paths);
  String getIri();
  HasPaths setIri(String iri);
  HasPaths addPath(Path path);
  String getNode();
}
