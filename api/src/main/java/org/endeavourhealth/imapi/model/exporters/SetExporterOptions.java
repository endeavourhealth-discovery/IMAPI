package org.endeavourhealth.imapi.model.exporters;

import lombok.Getter;
import org.endeavourhealth.imapi.model.set.SetOptions;

@Getter
public class SetExporterOptions extends SetOptions {
  private boolean ownRow;
  private boolean im1id;

  public SetExporterOptions(SetOptions options, boolean ownRow, boolean im1id) {
    super(options.getSetIri(), options.includeDefinition(), options.includeCore(), options.includeLegacy(), options.includeSubsets(), options.getSchemes(),
      options.getSubsumptions());
    this.ownRow = ownRow;
    this.im1id = im1id;
  }

  public SetExporterOptions setOwnRow(boolean ownRow) {
    this.ownRow = ownRow;
    return this;
  }

  public SetExporterOptions setIm1id(boolean im1id) {
    this.im1id = im1id;
    return this;
  }
}
