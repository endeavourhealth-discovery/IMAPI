package com.endavourhealth.dataaccess.repository;

import com.endavourhealth.dataaccess.entity.Expression;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpressionRepository extends JpaRepository<Expression, String> {
    List<Expression> findByTargetConcept_Iri(@Param("iri") String iri);

}
