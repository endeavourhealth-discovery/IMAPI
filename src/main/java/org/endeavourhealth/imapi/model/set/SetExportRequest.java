package org.endeavourhealth.imapi.model.set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SetExportRequest {
  private boolean ownRow;
  private String format;
  private SetOptions options;

  public SetExportRequest setOwnRow(boolean ownRow) {
    this.ownRow = ownRow;
    return this;
  }

  public SetExportRequest setFormat(String format) {
    this.format = format;
    return this;
  }

  public SetExportRequest setOptions(SetOptions options) {
    this.options = options;
    return this;
  }
}
