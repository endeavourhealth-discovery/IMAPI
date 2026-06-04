package org.endeavourhealth.imapi.model.set;

import org.endeavourhealth.imapi.model.tripletree.TTIriRefExtended;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ExportSet implements Serializable {
  private TTIriRefExtended valueSet;
  private List<SetMember> members = new ArrayList<>();
  private boolean limited = false;

  public TTIriRefExtended getValueSet() {
    return valueSet;
  }

  public ExportSet setValueSet(TTIriRefExtended valueSet) {
    this.valueSet = valueSet;
    return this;
  }

  public List<SetMember> getMembers() {
    return members;
  }

  public ExportSet setMembers(List<SetMember> includedMembers) {
    this.members = includedMembers;
    return this;
  }

  public ExportSet addMembers(SetMember vsm) {
    if (this.members == null)
      this.members = new ArrayList<>();
    this.members.add(vsm);
    return this;
  }

  public ExportSet addAllMembers(Collection<SetMember> vsm) {
    if (this.members == null)
      this.members = new ArrayList<>();
    this.members.addAll(vsm);
    return this;
  }

  public boolean isLimited() {
    return limited;
  }

  public ExportSet setLimited(boolean limited) {
    this.limited = limited;
    return this;
  }
}
