package org.endeavourhealth.imapi.model.openSearch;

public class Request {
    private int size;
    private Query query;

    public Request(int size, Query query) {
        this.size = size;
        this.query = query;
    }

    public Request(int size) {
        this.query = new Query();
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public Request setSize(int size) {
        this.size = size;
        return this;
    }

    public Query getQuery() {
        return query;
    }

    public Request setQuery(Query query) {
        this.query = query;
        return this;
    }
}
