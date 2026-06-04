package org.endeavourhealth.imapi.model.iml;

import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.model.tripletree.TTIriRefExtended;

import java.util.HashSet;
import java.util.Set;

public class ConceptSet extends EntityExtended {
  private Query definition;
  private Set<TTIriRefExtended> hasMember;
  private Set<TTIriRefExtended> usedIn;
  private Boolean avoidReplacedBy;

  public Boolean getAvoidReplacedBy() {
    return avoidReplacedBy;
  }

  public void setAvoidReplacedBy(Boolean avoidReplacedBy) {
    this.avoidReplacedBy = avoidReplacedBy;
  }

  public Set<TTIriRefExtended> getUsedIn() {
    return usedIn;
  }

  public ConceptSet setUsedIn(Set<TTIriRefExtended> usedIn) {
    this.usedIn = usedIn;
    return this;
  }

  public ConceptSet addUsedIn(TTIriRefExtended query) {
    if (this.usedIn == null)
      this.usedIn = new HashSet<>();
    this.usedIn.add(query);
    return this;
  }

  public Query getDefinition() {
    return definition;
  }

  public ConceptSet setDefinition(Query definition) {
    this.definition = definition;
    return this;
  }

  public Set<TTIriRefExtended> getHasMember() {
    return hasMember;
  }

  public ConceptSet setHasMember(Set<TTIriRefExtended> hasMember) {
    this.hasMember = hasMember;
    return this;
  }

  public ConceptSet addHasMember(TTIriRefExtended member) {
    if (this.hasMember == null)
      this.hasMember = new HashSet<>();
    this.hasMember.add(member);
    return this;
  }
}
