package com.endavourhealth.models.objectModel;

import java.util.ArrayList;
import java.util.List;

public class MapDomain {
    private String mapContext;
    private MapTerminal fromSource;
    private MapTerminal toTarget;
    private List<MapData> map;

    public String getMapContext() {
        return mapContext;
    }

    public MapDomain setMapContext(String mapContext) {
        this.mapContext = mapContext;
        return this;
    }

    public MapTerminal getFromSource() {
        return fromSource;
    }

    public MapDomain setFromSource(MapTerminal fromSource) {
        this.fromSource = fromSource;
        return this;
    }

    public MapTerminal getToTarget() {
        return toTarget;
    }

    public MapDomain setToTarget(MapTerminal toTarget) {
        this.toTarget = toTarget;
        return this;
    }

    public List<MapData> getMap() {
        return map;
    }

    public MapDomain setMap(List<MapData> map) {
        this.map = map;
        return this;
    }

    public MapDomain addMap(MapData map) {
        if (this.map == null)
            this.map = new ArrayList<>();
        this.map.add(map);
        return this;
    }
}
