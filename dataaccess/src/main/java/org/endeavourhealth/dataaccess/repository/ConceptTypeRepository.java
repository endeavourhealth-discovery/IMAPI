package org.endeavourhealth.dataaccess.repository;

import org.endeavourhealth.dataaccess.ConnectionPool;
import org.endeavourhealth.imapi.model.tripletree.TTArray;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.StringJoiner;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

@Repository
public class ConceptTypeRepository extends BaseRepository {
    public TTArray getConceptTypes(String iri) throws SQLException {
        TTArray types = new TTArray();

        StringJoiner sql = new StringJoiner("\n")
            .add("SELECT t.iri, t.name")
            .add("FROM concept c")
            .add("JOIN concept_type ct ON ct.concept = c.dbid")
            .add("JOIN concept t ON t.iri = ct.type")
            .add("WHERE c.iri = ?");
        try (Connection conn = ConnectionPool.get();
             PreparedStatement statement = conn.prepareStatement(sql.toString())) {
            statement.setString(1, iri);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    types.add(iri(rs.getString("iri"), rs.getString("name")));
                }
            }
        }

        return types;
    }
}
