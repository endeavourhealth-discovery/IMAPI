package org.endeavourhealth.imapi.model.exporters;

import lombok.Getter;

import java.util.List;

public class SetExporterOptions {
  @Getter
  private String setIri;
  @Getter
  private boolean definition;
  @Getter
  private boolean core;
  @Getter
  private boolean legacy;
  private boolean includeSubsets;
  @Getter
  private boolean ownRow;
  @Getter
  private boolean im1id;
  @Getter
  private List<String> schemes;

  public SetExporterOptions(String setIri, boolean definition, boolean core, boolean legacy, boolean includeSubsets, boolean ownRow, boolean im1id, List<String> schemes) {
    this.setIri = setIri;
    this.definition = definition;
    this.core = core;
    this.legacy = legacy;
    this.includeSubsets = includeSubsets;
    this.ownRow = ownRow;
    this.im1id = im1id;
    this.schemes = schemes;
  }

  public boolean includeSubsets() {
    return includeSubsets;
  }
}
