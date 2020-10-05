package com.endavourhealth.dataaccess.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.endavourhealth.dataaccess.entity.Namespace;

@Repository
public interface NamespaceRepository extends JpaRepository<Namespace, String>{

}
