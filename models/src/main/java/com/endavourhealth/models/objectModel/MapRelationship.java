package com.endavourhealth.models.objectModel;

public class MapRelationship {
    private String relationshipType;
    private String field;
    private MapCreate createTarget;
    private String referenceTarget;

    public String getRelationshipType() {
        return relationshipType;
    }

    public MapRelationship setRelationshipType(String relationshipType) {
        this.relationshipType = relationshipType;
        return this;
    }

    public String getField() {
        return field;
    }

    public MapRelationship setField(String field) {
        this.field = field;
        return this;
    }

    public MapCreate getCreateTarget() {
        return createTarget;
    }

    public MapRelationship setCreateTarget(MapCreate createTarget) {
        this.createTarget = createTarget;
        return this;
    }

    public String getReferenceTarget() {
        return referenceTarget;
    }

    public MapRelationship setReferenceTarget(String referenceTarget) {
        this.referenceTarget = referenceTarget;
        return this;
    }
}
