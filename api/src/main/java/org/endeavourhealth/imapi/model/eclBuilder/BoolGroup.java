package org.endeavourhealth.imapi.model.eclBuilder;

import lombok.Getter;
import org.endeavourhealth.imapi.model.imq.Bool;

import java.util.ArrayList;
import java.util.List;

@Getter
public class BoolGroup extends BuilderComponent {
    private Bool conjunction;
    private List<BuilderComponent> items;
    private Boolean attributeGroup;

    public BoolGroup() {
        super("BoolGroup");
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

    @Override
    public boolean isBoolGroup() {
        return true;
    }

    @Override
    public BoolGroup asBoolGroup() {
        return this;
    }
}
