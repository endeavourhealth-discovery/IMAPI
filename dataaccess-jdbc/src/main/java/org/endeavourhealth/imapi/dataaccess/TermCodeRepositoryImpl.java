package org.endeavourhealth.imapi.dataaccess;

import org.endeavourhealth.imapi.dataaccess.helpers.ConnectionPool;
import org.endeavourhealth.imapi.dataaccess.helpers.DALException;
import org.endeavourhealth.imapi.model.TermCode;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

@SuppressWarnings("java:S1192") // Disable "Literals as const" rule for SQL
public class TermCodeRepositoryImpl implements TermCodeRepository {

    @Override
    public List<TermCode> findAllByIri(String iri) throws DALException {
        List<TermCode> terms = new ArrayList<>();
        StringJoiner sql = new StringJoiner("\n")
                .add("SELECT tc.term, tc.code, n.name AS scheme")
                .add("FROM entity c")
                .add("JOIN term_code tc ON tc.entity = c.dbid")
                .add("JOIN namespace n ON n.iri = c.scheme")
                .add("WHERE c.iri = ?");
        try (Connection conn = ConnectionPool.get();
             PreparedStatement statement = conn.prepareStatement(sql.toString())) {
            statement.setString(1, iri);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    terms.add(new TermCode()
                        .setName(rs.getString("term"))
                        .setCode(rs.getString("code"))
                        .setScheme(rs.getString("scheme")));
                }
            }
        } catch (SQLException e) {
            throw new DALException("Failed to find terms for entity", e);
        }
        return terms;
    }
}
