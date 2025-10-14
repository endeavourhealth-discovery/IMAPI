package org.endeavourhealth.imapi.model.imq;

import java.util.List;

public interface HasPaths {
  List<Path> getPath();
  HasPaths setPath(List<Path> paths);
}
