package org.endeavourhealth.imapi.model.cdm;

import com.fasterxml.jackson.annotation.JsonSetter;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRefExtended;
import org.endeavourhealth.imapi.model.tripletree.TTLiteral;
import org.endeavourhealth.imapi.model.tripletree.TTUtil;

public abstract class Entry extends TTEntity {

  public TTIriRefExtended getDataController() {

    return (TTIriRefExtended) TTUtil.get(this, new TTIriRefExtended("dataController"), TTIriRefExtended.class);
  }

  @JsonSetter
  public Entry setDataController(TTIriRefExtended dataController) {
    set(new TTIriRefExtended("dataController"), dataController);
    return this;
  }

  public String getDateOfEntry() {

    return (String) TTUtil.get(this, new TTIriRefExtended(ImVocab. DATE_OF_ENTRY),String.class);
  }

  public Entry setDateOfEntry(String dateOfEntry) {
    set(new TTIriRefExtended(ImVocab. DATE_OF_ENTRY),TTLiteral.literal(dateOfEntry));
    return this;
  }
}
