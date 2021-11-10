package org.endeavourhealth.imapi.dataaccess;

import org.endeavourhealth.imapi.dataaccess.helpers.ConnectionPool;
import org.endeavourhealth.imapi.dataaccess.helpers.DALException;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.valuset.ValueSetMember;
import org.endeavourhealth.imapi.vocabulary.RDFS;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

@SuppressWarnings("java:S1192") // Disable "Literals as const" rule for SQL
public class ValueSetRepositoryImpl implements ValueSetRepository {
    @Override
    public List<ValueSetMember> expandMember(String iri) throws DALException {
        return expandMember(iri, null);
    }

    @Override
    public List<ValueSetMember> expandMember(String iri, Integer limit) throws DALException {
        List<ValueSetMember> members = new ArrayList<>();
        StringJoiner sql = new StringJoiner("\n")
            .add("SELECT m.iri AS entity_iri, m.name AS entity_name, m.code, m.scheme AS scheme_iri, n.name AS scheme_name")
            .add("FROM entity c")
            .add("JOIN tct tct ON tct.ancestor = c.dbid")
            .add("JOIN entity t ON t.dbid = tct.type AND t.iri = ?")
            .add("JOIN entity m ON m.dbid = tct.descendant")
            .add("LEFT JOIN namespace n ON n.iri = m.scheme")
            .add("WHERE c.iri = ?")
            .add("UNION")
            .add("SELECT m.iri AS entity_iri, tc.term AS entity_name, tc.code, m.scheme AS scheme_iri, n.name AS scheme_name")
            .add("FROM entity c")
            .add("JOIN tct tct ON tct.ancestor = c.dbid")
            .add("JOIN entity t ON t.dbid = tct.type AND t.iri = ?")
            .add("JOIN entity m ON m.dbid = tct.descendant")
            .add("JOIN term_code tc ON tc.entity = m.dbid")
            .add("LEFT JOIN namespace n ON n.iri = t.scheme")
            .add("WHERE c.iri = ?");
        if (limit != null)
            sql.add("LIMIT " + (limit + 1));
        try (Connection conn = ConnectionPool.get();
             PreparedStatement statement = conn.prepareStatement(sql.toString())) {
            statement.setString(1, RDFS.SUBCLASSOF.getIri());
            statement.setString(2, iri);
            statement.setString(3, RDFS.SUBCLASSOF.getIri());
            statement.setString(4, iri);
            try (ResultSet rs = statement.executeQuery()) {
                int rows = 0;
                while (rs.next() && (limit == null || rows <= limit)) {
                    members.add(new ValueSetMember()
                        .setEntity(TTIriRef.iri(rs.getString("entity_iri"), rs.getString("entity_name")))
                        .setCode(rs.getString("code"))
                        .setScheme(TTIriRef.iri(rs.getString("scheme_iri"), rs.getString("scheme_name"))));
                    rows++;
                }
            }

        } catch (SQLException e) {
            throw new DALException("Member expansion failed", e);
        }
        return members;
    }
}
