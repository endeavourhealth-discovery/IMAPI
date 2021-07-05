package org.endeavourhealth.imapi.dataaccess.repository;

import org.endeavourhealth.imapi.dataaccess.ConnectionPool;
import org.endeavourhealth.imapi.model.tripletree.TTArray;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.StringJoiner;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

@SuppressWarnings("java:S1192") // Disable "Literals as const" rule for SQL
public class EntityTypeRepository extends BaseRepository {
    public TTArray getEntityTypes(String iri) throws SQLException {
        TTArray types = new TTArray();

        StringJoiner sql = new StringJoiner("\n")
            .add("SELECT t.iri, t.name")
            .add("FROM entity c")
            .add("JOIN entity_type ct ON ct.entity = c.dbid")
            .add("JOIN entity t ON t.iri = ct.type")
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
