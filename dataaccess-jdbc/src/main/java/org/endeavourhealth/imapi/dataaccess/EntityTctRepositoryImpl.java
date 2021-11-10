package org.endeavourhealth.imapi.dataaccess;

import org.endeavourhealth.imapi.dataaccess.helpers.ConnectionPool;
import org.endeavourhealth.imapi.dataaccess.helpers.DALException;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import static org.endeavourhealth.imapi.dataaccess.helpers.JDBCHelper.inListParams;

@SuppressWarnings("java:S1192") // Disable "Literals as const" rule for SQL
public class EntityTctRepositoryImpl implements EntityTctRepository {
    @Override
    public List<TTIriRef> findAncestorsByType(String iri, String type, List<String> candidates) throws DALException {
        List<TTIriRef> ancestors = new ArrayList<>();
        StringJoiner sql = new StringJoiner("\n")
            .add("SELECT a.iri AS anc_iri ,a.name AS anc_name")
            .add("FROM entity c")
            .add("JOIN tct tct ON tct.descendant = c.dbid")
            .add("LEFT JOIN entity a ON a.dbid = tct.ancestor")
            .add("LEFT JOIN entity t ON t.dbid = tct.type")
            .add("WHERE c.iri = ?")
            .add("AND t.iri = ?")
            .add("AND a.iri IN " + inListParams(candidates.size()));
        try (Connection conn = ConnectionPool.get();
             PreparedStatement statement = conn.prepareStatement(sql.toString())) {
            statement.setString(1, iri);
            statement.setString(2, type);
            int i = 3;
            for (String candidate : candidates) {
                statement.setString(i++, candidate);
            }
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    ancestors.add(TTIriRef.iri(rs.getString("anc_iri"), rs.getString("anc_name")));
                }
            }
        } catch (SQLException e) {
            throw new DALException("Failed to find ancestors", e);
        }
        return ancestors;
    }


}
