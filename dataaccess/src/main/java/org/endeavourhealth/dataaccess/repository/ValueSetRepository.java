package org.endeavourhealth.dataaccess.repository;

import org.endeavourhealth.dataaccess.ConnectionPool;
import org.endeavourhealth.imapi.model.valuset.ValueSetMember;
import org.endeavourhealth.imapi.vocabulary.IM;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;


public class ValueSetRepository extends BaseRepository {

    public List<ValueSetMember> expandMember(String iri) throws SQLException {
        List<ValueSetMember> members = new ArrayList<>();
        StringJoiner sql = new StringJoiner("\n")
            .add("SELECT m.iri AS entity_iri, m.name AS entity_name, m.code, m.scheme AS scheme_iri, s.name AS scheme_name")
            .add("FROM entity c")
            .add("JOIN tct tct ON tct.ancestor = c.dbid")
            .add("JOIN entity t ON t.dbid = tct.type AND t.iri = ?")
            .add("JOIN entity m ON m.dbid = tct.descendant")
            .add("LEFT JOIN entity s ON s.iri = m.scheme")
            .add("WHERE c.iri = ?")
            .add("UNION")
            .add("SELECT m.iri AS entity_iri, tc.term AS entity_name, tc.code, m.scheme AS scheme_iri, s.name AS scheme_name")
            .add("FROM entity c")
            .add("JOIN tct tct ON tct.ancestor = c.dbid")
            .add("JOIN entity t ON t.dbid = tct.type AND t.iri = ?")
            .add("JOIN entity m ON m.dbid = tct.descendant")
            .add("JOIN term_code tc ON tc.entity = m.dbid")
            .add("LEFT JOIN entity s ON s.dbid = tc.scheme")
            .add("WHERE c.iri = ?");
        try (Connection conn = ConnectionPool.get()) {
            assert conn != null;
            try (PreparedStatement statement = conn.prepareStatement(sql.toString())) {
                statement.setString(1, IM.IS_A.getIri());
                statement.setString(2, iri);
                statement.setString(3, IM.IS_A.getIri());
                statement.setString(4, iri);
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        members.add(new ValueSetMember()
                            .setEntity(iri(rs.getString("entity_iri"), rs.getString("entity_name")))
                            .setCode(rs.getString("code"))
                            .setScheme(iri(rs.getString("scheme_iri"), rs.getString("scheme_name"))));
                    }
                }
            }
        }
        return members;
    }
}
