package org.endeavourhealth.imapi.model;

import lombok.Getter;
import org.endeavourhealth.imapi.model.iml.Concept;

import java.util.ArrayList;
import java.util.List;

@Getter
public class SetDiffObject {
  private List<Concept> membersA;
  private List<Concept> membersB;
  private List<Concept> sharedMembers;

  public SetDiffObject() {
    this.membersA = new ArrayList<>();
    this.membersB = new ArrayList<>();
    this.sharedMembers = new ArrayList<>();
  }

  public SetDiffObject setMembersA(List<Concept> membersA) {
    this.membersA = membersA;
    return this;
  }

  public SetDiffObject addMemberA(Concept memberA) {
    this.membersA.add(memberA);
    return this;
  }

  public SetDiffObject setMembersB(List<Concept> membersB) {
    this.membersB = membersB;
    return this;
  }

  public SetDiffObject addMemberB(Concept memberB) {
    this.membersB.add(memberB);
    return this;
  }

  public SetDiffObject setSharedMembers(List<Concept> sharedMembers) {
    this.sharedMembers = sharedMembers;
    return this;
  }

  public SetDiffObject addSharedMember(Concept sharedMember) {
    this.sharedMembers.add(sharedMember);
    return this;
  }
}
