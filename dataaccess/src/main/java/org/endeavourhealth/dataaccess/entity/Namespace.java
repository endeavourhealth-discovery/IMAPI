package org.endeavourhealth.dataaccess.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class Namespace {

	@Id()
	private Integer dbid;
	private String iri;
	private String prefix;
	private String name;
	private LocalDateTime updated;

	public Namespace() {
		super();
	}

	public Namespace(Integer dbid, String iri, String prefix) {
		super();
		this.dbid = dbid;
		this.iri = iri;
		this.prefix = prefix;
	}

    public Integer getDbid() {
        return dbid;
    }

    public Namespace setDbid(Integer dbid) {
        this.dbid = dbid;
        return this;
    }

    public String getIri() {
        return iri;
    }

    public Namespace setIri(String iri) {
        this.iri = iri;
        return this;
    }

    public String getPrefix() {
        return prefix;
    }

    public Namespace setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public String getName() {
        return name;
    }

    public Namespace setName(String name) {
        this.name = name;
        return this;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

    public Namespace setUpdated(LocalDateTime updated) {
        this.updated = updated;
        return this;
    }

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
        Namespace other = (Namespace) obj;
        if (dbid == null) {
            if (other.dbid != null)
                return false;
        } else if (!dbid.equals(other.dbid))
            return false;
        return true;
    }

}
