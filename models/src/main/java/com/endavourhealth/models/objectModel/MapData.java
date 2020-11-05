package com.endavourhealth.models.objectModel;

import java.util.ArrayList;
import java.util.List;

public class MapData {
    private List<MapFrom> from;
    private String where;
    private List<MapCreate> create;
    private List<MapData> map;

    public List<MapFrom> getFrom() {
        return from;
    }

    public MapData setFrom(List<MapFrom> from) {
        this.from = from;
        return this;
    }

    public MapData addFrom(MapFrom from) {
        if (this.from == null)
            this.from = new ArrayList<>();
        this.from.add(from);
        return this;
    }

    public String getWhere() {
        return where;
    }

    public MapData setWhere(String where) {
        this.where = where;
        return this;
    }


    public List<MapCreate> getCreate() {
        return create;
    }

    public MapData setCreate(List<MapCreate> create) {
        this.create = create;
        return this;
    }

    public MapData addCreate(MapCreate create) {
        if (this.create == null)
            this.create = new ArrayList<>();
        this.create.add(create);
        return this;
    }

    public List<MapData> getMap() {
        return map;
    }

    public MapData setMap(List<MapData> map) {
        this.map = map;
        return this;
    }

    public MapData addMap(MapData map) {
        if (this.map == null)
            this.map = new ArrayList<>();
        this.map.add(map);
        return this;
    }
}
