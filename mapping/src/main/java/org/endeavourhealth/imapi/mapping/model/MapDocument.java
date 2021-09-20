package org.endeavourhealth.imapi.mapping.model;

import javax.persistence.Entity;

public class MapDocument {
    private int dbid;
    private String filename;
    private byte[] document;

    public MapDocument setDbid(int dbid) {
        this.dbid = dbid;
        return this;
    }

    public int getDbid() {
        return dbid;
    }

    public String getFilename() {
        return filename;
    }

    public MapDocument setFilename(String filename) {
        this.filename = filename;
        return this;
    }

    public byte[] getDocument() {
        return document;
    }

    public MapDocument setDocument(byte[] document) {
        this.document = document;
        return this;
    }
}
