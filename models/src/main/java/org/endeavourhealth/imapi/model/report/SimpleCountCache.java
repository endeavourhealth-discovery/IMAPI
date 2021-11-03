package org.endeavourhealth.imapi.model.report;

import java.time.LocalDateTime;
import java.util.List;

public class SimpleCountCache {

    private LocalDateTime dateTime;
    private List<SimpleCount> data;

    public SimpleCountCache(List<SimpleCount> list) {
        this.data = list;
    }

    public SimpleCountCache() {

    }


    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public SimpleCountCache setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
        return this;
    }

    public List<SimpleCount> getData() {
        return data;
    }

    public SimpleCountCache setData(List<SimpleCount> data) {
        this.data = data;
        return this;
    }

    public boolean cacheOlderThan(Long timeOut) {
        return !dateTime.plusMinutes(timeOut).isAfter(LocalDateTime.now());
    }
}
