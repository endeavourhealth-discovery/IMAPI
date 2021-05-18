package org.endeavourhealth.dataaccess.repository;

import org.endeavourhealth.dataaccess.ConnectionPool;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTLiteral;
import org.endeavourhealth.imapi.model.tripletree.TTNode;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class IndividualTplDataRepository {
    public TTNode findAllBySubjectIri(String iri) throws SQLException {
        TTNode result = new TTNode();

        try (
            Connection conn = ConnectionPool.get()) {
            try (PreparedStatement stmt = conn.prepareStatement("SELECT p.iri AS p_iri, p.name AS p_name, o.iri AS o_iri, o.name AS o_name, t.literal\n" +
                "FROM instance s\n" +
                "JOIN tpl_ins_data t ON t.subject = s.dbid\n" +
                "JOIN concept p ON p.dbid = t.predicate\n" +
                "JOIN concept o ON o.dbid = t.data_type\n" +
                "WHERE s.iri = ?")) {
                stmt.setString(1, iri);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        result.set(
                            new TTIriRef(rs.getString("p_iri"), rs.getString("p_name")),
                            new TTLiteral(rs.getString("literal"), new TTIriRef(rs.getString("o_iri"), rs.getString("o_name")))
                        );
                    }
                }
            }
        }
        return result;
    }
}
