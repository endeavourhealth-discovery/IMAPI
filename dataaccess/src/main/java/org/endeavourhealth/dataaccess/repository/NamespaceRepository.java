package org.endeavourhealth.dataaccess.repository;

import org.endeavourhealth.dataaccess.entity.Namespace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NamespaceRepository extends JpaRepository<Namespace, String>{

}
