package org.endeavourhealth.imapi.dataaccess;

import org.endeavourhealth.imapi.model.config.Config;

public interface ConfigRepository {
    Config findByName(String name);
}
