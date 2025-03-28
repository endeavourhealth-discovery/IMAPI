package org.endeavourhealth.imapi.postgress;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PostgresRepository extends JpaRepository<DBEntry, UUID> {
  @Query(
    value = "SELECT * FROM query_queue WHERE user_id = quote_literal(?1)",
    nativeQuery = true
  )
  List<DBEntry> findAllByUserId(String userId);

  @Query(
    value = "SELECT * FROM query_queue WHERE status = quote_literal(?1)",
    nativeQuery = true
  )
  List<DBEntry> findAllByStatus(@Param("status") QueryExecutorStatus status);

  @Query(
    value = "SELECT * FROM query_queue WHERE user_id = quote_literal(?1) AND status = quote_literal(?2)",
    nativeQuery = true
  )
  List<DBEntry> findAllByUserIdAndStatus(@Param("userId") String userId, @Param("status") QueryExecutorStatus status);
}
