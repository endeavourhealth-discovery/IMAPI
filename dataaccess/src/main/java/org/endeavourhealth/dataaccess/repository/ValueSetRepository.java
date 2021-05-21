package org.endeavourhealth.dataaccess.repository;

import org.endeavourhealth.dataaccess.ConnectionPool;
import org.endeavourhealth.imapi.model.valuset.ValueSetMember;

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
                .add("SELECT m.iri AS concept_iri, m.name AS concept_name, m.code, m.scheme AS scheme_iri, s.name AS scheme_name")
                .add("FROM concept c")
                .add("JOIN tct tct ON tct.ancestor = c.dbid")
                .add("JOIN concept t ON t.dbid = tct.type AND t.iri = 'http://endhealth.info/im#isA'")
                .add("JOIN concept m ON m.dbid = tct.descendant")
                .add("LEFT JOIN concept s ON s.iri = m.scheme")
                .add("WHERE c.iri = :iri")
                .add("UNION")
                .add("SELECT m.iri AS concept_iri, m.name AS concept_name, m.code, m.scheme AS scheme_iri, s.name AS scheme_name")
                .add("FROM concept c")
                .add("JOIN tct tct ON tct.ancestor = c.dbid")
                .add("JOIN concept t ON t.dbid = tct.type AND t.iri = 'http://endhealth.info/im#isA'")
                .add("JOIN tpl ON tpl.object = tct.descendant")
                .add("JOIN concept p ON p.dbid = tpl.predicate AND p.iri = 'http://endhealth.info/im#mappedFrom'")
                .add("JOIN concept m ON m.dbid = tpl.subject")
                .add("LEFT JOIN concept s ON s.iri = m.scheme")
                .add("WHERE c.iri = ?");
        try (Connection conn = ConnectionPool.get()) {
            assert conn != null;
            try (PreparedStatement statement = conn.prepareStatement(sql.toString())) {
                statement.setString(1, iri);
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        members.add(new ValueSetMember()
                                .setConcept(iri(rs.getString("concept_iri"),rs.getString("concept_name")))
                                .setCode(rs.getString("code"))
                                .setScheme(iri(rs.getString("scheme_iri"),rs.getString("scheme_name"))));
                    }
                }
            }
        }
        return members;
    }
}
