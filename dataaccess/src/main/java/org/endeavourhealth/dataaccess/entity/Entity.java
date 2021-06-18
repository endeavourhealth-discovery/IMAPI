package org.endeavourhealth.dataaccess.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Entity implements Serializable {
    private Integer dbid;

    private String iri;

    private String name;
    private String description;
    private String code;
    private Entity scheme;
    private Entity status;
    private String json;
    private LocalDateTime updated;

    public Integer getDbid() {
        return dbid;
    }

    public Entity setDbid(Integer dbid) {
        this.dbid = dbid;
        return this;
    }

    public String getIri() {
        return iri;
    }

    public Entity setIri(String iri) {
        this.iri = iri;
        return this;
    }

    public String getName() {
        return name;
    }

    public Entity setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Entity setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getCode() {
        return code;
    }

    public Entity setCode(String code) {
        this.code = code;
        return this;
    }
    public Entity getScheme() {
        return scheme;
    }

    public Entity setScheme(Entity scheme) {
        this.scheme = scheme;
        return this;
    }

    public Entity getStatus() {
        return status;
    }

    public Entity setStatus(Entity status) {
        this.status = status;
        return this;
    }

    public String getJson() {
        return json;
    }

    public Entity setJson(String definition) {
        this.json = definition;
        return this;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

    public Entity setUpdated(LocalDateTime updated) {
        this.updated = updated;
        return this;
    }

//    public List<EntityType> getType() {
//        return type;
//    }
//
//    public Entity setType(List<EntityType> types) {
//        this.type = types;
//        return this;
//    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((dbid == null) ? 0 : dbid.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Entity other = (Entity) obj;
        if (dbid == null) {
            if (other.dbid != null)
                return false;
        } else if (!dbid.equals(other.dbid))
            return false;
        return true;
    }
}
