package com.endavourhealth.models.objectModel;

public class MapTerminal {
    private String terminalContext;
    private Identifier provider;
    private Identifier system;
    private String schema;

    public String getTerminalContext() {
        return terminalContext;
    }

    public MapTerminal setTerminalContext(String terminalContext) {
        this.terminalContext = terminalContext;
        return this;
    }

    public Identifier getProvider() {
        return provider;
    }

    public MapTerminal setProvider(Identifier provider) {
        this.provider = provider;
        return this;
    }

    public Identifier getSystem() {
        return system;
    }

    public MapTerminal setSystem(Identifier system) {
        this.system = system;
        return this;
    }

    public String getSchema() {
        return schema;
    }

    public MapTerminal setSchema(String schema) {
        this.schema = schema;
        return this;
    }
}
