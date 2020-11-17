package com.endavourhealth.dataaccess.repository;

import com.endavourhealth.dataaccess.entity.Expression;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpressionRepository extends JpaRepository<Expression, String> {
    @Query(value = "SELECT e.*\n" +
        "FROM concept c\n" +
        "JOIN axiom a ON a.concept = c.dbid\n" +
        "LEFT JOIN expression e ON e.axiom = a.dbid\n" +
        "WHERE c.iri = :iri\n", nativeQuery = true)
	List<Expression> findByIri(@Param("iri") String iri);
}
