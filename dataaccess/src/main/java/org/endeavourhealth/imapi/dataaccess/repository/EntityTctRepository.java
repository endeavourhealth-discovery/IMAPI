package org.endeavourhealth.imapi.dataaccess.repository;

import org.endeavourhealth.imapi.dataaccess.ConnectionPool;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

@SuppressWarnings("java:S1192") // Disable "Literals as const" rule for SQL
public class EntityTctRepository extends BaseRepository{
    public List<TTIriRef> findAncestorsByType(String iri, String type, List<String> candidates) throws SQLException {
        List<TTIriRef> ancestors = new ArrayList<>();
        StringJoiner sql = new StringJoiner("\n")
                .add("SELECT a.iri AS anc_iri ,a.name AS anc_name")
                .add("FROM entity c")
                .add("JOIN tct tct ON tct.descendant = c.dbid")
                .add("LEFT JOIN entity a ON a.dbid = tct.ancestor")
                .add("LEFT JOIN entity t ON t.dbid = tct.type")
                .add("WHERE c.iri = ?")
                .add("AND t.iri = ?")
                .add("AND a.iri IN " + inList(candidates.size()));
        try (Connection conn = ConnectionPool.get()) {
            assert conn != null;
            try (PreparedStatement statement = conn.prepareStatement(sql.toString())) {
                statement.setString(1, iri);
                statement.setString(2,type);
                int i=3;
                for(String candidate : candidates) {
                    statement.setString(i++, candidate);
                }
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        ancestors.add(iri(rs.getString("anc_iri"), rs.getString("anc_name")));
                    }
                }
            }
        }
        return ancestors;
    }


}
