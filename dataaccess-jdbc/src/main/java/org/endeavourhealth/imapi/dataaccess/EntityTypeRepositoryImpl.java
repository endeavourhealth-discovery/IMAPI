package org.endeavourhealth.imapi.dataaccess;

import org.endeavourhealth.imapi.dataaccess.helpers.ConnectionPool;
import org.endeavourhealth.imapi.dataaccess.helpers.DALException;
import org.endeavourhealth.imapi.model.tripletree.TTArray;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.StringJoiner;

@SuppressWarnings("java:S1192") // Disable "Literals as const" rule for SQL
public class EntityTypeRepositoryImpl implements EntityTypeRepository {
    public TTArray getEntityTypes(String iri) throws DALException {
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
                    types.add(TTIriRef.iri(rs.getString("iri"), rs.getString("name")));
                }
            }
        } catch (SQLException e) {
            throw new DALException("Failed to get entity types");
        }

        return types;
    }
}
