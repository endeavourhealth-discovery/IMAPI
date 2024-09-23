package org.endeavourhealth.imapi.model.eclBuilder;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import org.endeavourhealth.imapi.json.BoolGroupDeserializer;
import org.endeavourhealth.imapi.json.TTEntityDeserializer;
import org.endeavourhealth.imapi.model.imq.Bool;

import java.util.ArrayList;
import java.util.List;

@Getter
@JsonDeserialize(using = BoolGroupDeserializer.class)
public class BoolGroup extends BuilderComponent {
  private Bool conjunction;
  private List<BuilderComponent> items;
  private Boolean attributeGroup;
  private Boolean exclude;

  public BoolGroup() {
    super("BoolGroup");
    this.conjunction = Bool.or;
  }

  public BoolGroup setConjunction(Bool conjunction) {
    this.conjunction = conjunction;
    return this;
  }

  public BoolGroup setItems(List<BuilderComponent> items) {
    this.items = items;
    return this;
  }

  public BoolGroup addItem(BuilderComponent item) {
    if (null == this.items) this.items = new ArrayList<>();
    this.items.add(item);
    return this;
  }

  public BoolGroup setAttributeGroup(Boolean attributeGroup) {
    this.attributeGroup = attributeGroup;
    return this;
  }

  public BoolGroup setExclude(Boolean exclude) {
    this.exclude = exclude;
    return this;
  }

  @Override
  public boolean isBoolGroup() {
    return true;
  }

  @Override
  public BoolGroup asBoolGroup() {
    return this;
  }
}
