package org.endeavourhealth.dataaccess.repository;

import org.endeavourhealth.dataaccess.ConnectionPool;
import org.endeavourhealth.imapi.model.TermCode;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class TermCodeRepository extends BaseRepository {

    public List<TermCode> findAllByConcept_Iri(String iri) throws SQLException {
        List<TermCode> terms = new ArrayList<>();
        StringJoiner sql = new StringJoiner("\n")
                .add("SELECT tc.term, tc.code, s.iri AS scheme_iri, tc.concept_term_code, s.name AS scheme_name")
                .add("FROM concept c")
                .add("JOIN term_code tc ON tc.concept = c.dbid")
                .add("LEFT JOIN concept s ON s.dbid = tc.scheme")
                .add("WHERE c.iri = ?");
        try (Connection conn = ConnectionPool.get()) {
            assert conn != null;
            try (PreparedStatement statement = conn.prepareStatement(sql.toString())) {
                statement.setString(1, iri);
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        terms.add(new TermCode()
                                .setTerm(rs.getString("term"))
                                .setCode(rs.getString("code"))
                                .setScheme(iri(rs.getString("scheme_iri"), rs.getString("scheme_name")))
                                .setConcept_term_code(rs.getString("concept_term_code")));
                    }
                }
            }
        }
        return terms;
    }

}
