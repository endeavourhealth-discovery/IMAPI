package org.endeavourhealth.dataaccess.repository;

import org.endeavourhealth.dataaccess.entity.Config;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigRepository extends JpaRepository<Config, Integer> {

    Config findByName(String name);

}
