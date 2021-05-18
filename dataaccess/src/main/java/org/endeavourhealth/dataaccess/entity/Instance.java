package org.endeavourhealth.dataaccess.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
public class Instance implements Serializable {
    @Id()
    private Integer dbid;

    @Column(unique = true)
    private String iri;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="type", referencedColumnName = "iri", nullable = true)
    private Concept type;

    private String name;

    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL)
    private List<TplInsObject> objects;

/*    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL)
    private List<TplInsData> data;*/


    public Integer getDbid() {
        return dbid;
    }

    public Instance setDbid(Integer dbid) {
        this.dbid = dbid;
        return this;
    }

    public String getIri() {
        return iri;
    }

    public Instance setIri(String iri) {
        this.iri = iri;
        return this;
    }

    public Concept getType() {
        return type;
    }

    public Instance setType(Concept type) {
        this.type = type;
        return this;
    }

    public String getName() {
        return name;
    }

    public Instance setName(String name) {
        this.name = name;
        return this;
    }

    public List<TplInsObject> getObjects() {
        return objects;
    }

    public Instance setObjects(List<TplInsObject> objects) {
        this.objects = objects;
        return this;
    }

/*
    public List<TplInsData> getData() {
        return data;
    }

    public Instance setData(List<TplInsData> data) {
        this.data = data;
        return this;
    }
*/

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
        Instance other = (Instance) obj;
        if (dbid == null) {
            if (other.dbid != null)
                return false;
        } else if (!dbid.equals(other.dbid))
            return false;
        return true;
    }
}
