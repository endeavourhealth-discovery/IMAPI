package org.endeavourhealth.imapi.model.cdm;

import com.fasterxml.jackson.annotation.JsonSetter;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTLiteral;
import org.endeavourhealth.imapi.model.tripletree.TTUtil;
import org.endeavourhealth.interfacemanager.model.IM;

public abstract class Entry extends TTEntity {

  public TTIriRef getDataController() {

    return (TTIriRef) TTUtil.get(this, new TTIriRef("dataController"), TTIriRef.class);
  }

  @JsonSetter
  public Entry setDataController(TTIriRef dataController) {
    set(new TTIriRef("dataController"), dataController);
    return this;
  }

  public String getDateOfEntry() {

    return (String) TTUtil.get(this, new TTIriRef(IM.DATE_OF_ENTRY), String.class);
  }

  public Entry setDateOfEntry(String dateOfEntry) {
    set(new TTIriRef(IM.DATE_OF_ENTRY), TTLiteral.literal(dateOfEntry));
    return this;
  }
}
