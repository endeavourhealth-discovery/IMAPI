package org.endeavourhealth.dataaccess.repository;

import org.endeavourhealth.dataaccess.ConnectionPool;
import org.endeavourhealth.imapi.model.tripletree.TTInstance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class IndividualRepository {

    public TTInstance getByIri(String iri) throws SQLException {
        try (Connection conn = ConnectionPool.get()) {
            try (PreparedStatement stmt = conn.prepareStatement("SELECT dbid, name FROM instance WHERE iri = ?")) {
                stmt.setString(1, iri);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (!rs.next())
                        throw new IllegalStateException("Unknown instance [" + iri + "]");

                    return new TTInstance()
                        .setIri(iri)
                        .setName(rs.getString("name"));
                }
            }
        }
    }
}
