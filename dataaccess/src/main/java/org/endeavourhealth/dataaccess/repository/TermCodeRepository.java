package org.endeavourhealth.dataaccess.repository;

import org.endeavourhealth.dataaccess.ConnectionPool;
import org.endeavourhealth.dataaccess.entity.Concept;
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

    public Concept findByCodeAndScheme_Iri(String code, String scheme) throws SQLException {
        Concept concept = new Concept();
        StringJoiner sql = new StringJoiner("\n")
                .add("SELECT c.iri, c.dbid")
                .add("FROM term_code tc")
                .add("JOIN concept s ON s.dbid = tc.scheme")
                .add("JOIN concept c ON c.dbid = tc.concept")
                .add("WHERE tc.code = ?")
                .add(" AND s.iri = ?");
        try (Connection conn = ConnectionPool.get()) {
            assert conn != null;
            try (PreparedStatement statement = conn.prepareStatement(sql.toString())) {
                statement.setString(1, code);
                statement.setString(2, scheme);
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                       concept
                               .setIri(rs.getString("iri"))
                               .setDbid(rs.getInt("dbid"));
                    }
                }
            }
        }
        return concept;
    }

}
