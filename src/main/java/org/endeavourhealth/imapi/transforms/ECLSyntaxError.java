package org.endeavourhealth.imapi.transforms;

import lombok.Getter;
import org.endeavourhealth.imapi.model.imq.ECLStatus;

import java.util.UnknownFormatConversionException;

public class ECLSyntaxError extends UnknownFormatConversionException {
  @Getter
  private final ECLStatus errorData;

  public ECLSyntaxError(String message, ECLStatus errorData) {
    super(message);
    this.errorData = errorData;
  }


}
