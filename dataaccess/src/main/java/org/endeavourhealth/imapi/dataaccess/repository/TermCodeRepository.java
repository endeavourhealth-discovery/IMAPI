package org.endeavourhealth.imapi.dataaccess.repository;

import org.endeavourhealth.imapi.dataaccess.ConnectionPool;
import org.endeavourhealth.imapi.model.TermCode;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

@SuppressWarnings("java:S1192") // Disable "Literals as const" rule for SQL
public class TermCodeRepository extends BaseRepository {

    public List<TermCode> findAllByIri(String iri) throws SQLException {
        List<TermCode> terms = new ArrayList<>();
        StringJoiner sql = new StringJoiner("\n")
                .add("SELECT tc.term, tc.code, s.iri AS scheme_iri, tc.entity_term_code, s.name AS scheme_name")
                .add("FROM entity c")
                .add("JOIN term_code tc ON tc.entity = c.dbid")
                .add("LEFT JOIN entity s ON s.dbid = tc.scheme")
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
                                .setEntityTermCode(rs.getString("entity_term_code")));
                    }
                }
            }
        }
        return terms;
    }

    public TTIriRef findByCodeAndScheme(String code, String scheme) throws SQLException {
        TTIriRef entity = new TTIriRef();
        StringJoiner sql = new StringJoiner("\n")
                .add("SELECT c.iri, c.name")
                .add("FROM term_code tc")
                .add("JOIN entity s ON s.dbid = tc.scheme")
                .add("JOIN entity c ON c.dbid = tc.entity")
                .add("WHERE tc.code = ?")
                .add(" AND s.iri = ?");
        try (Connection conn = ConnectionPool.get()) {
            assert conn != null;
            try (PreparedStatement statement = conn.prepareStatement(sql.toString())) {
                statement.setString(1, code);
                statement.setString(2, scheme);
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                       entity
                               .setIri(rs.getString("iri"))
                               .setName(rs.getString("name"));
                    }
                }
            }
        }
        return entity;
    }

    public Integer findDbidByCodeAndScheme(String code, String scheme) throws SQLException {
        StringJoiner sql = new StringJoiner("\n")
            .add("SELECT c.dbid")
            .add("FROM term_code tc")
            .add("JOIN entity s ON s.dbid = tc.scheme")
            .add("JOIN entity c ON c.dbid = tc.entity")
            .add("WHERE tc.code = ?")
            .add(" AND s.iri = ?");
        try (Connection conn = ConnectionPool.get()) {
            assert conn != null;
            try (PreparedStatement statement = conn.prepareStatement(sql.toString())) {
                statement.setString(1, code);
                statement.setString(2, scheme);
                try (ResultSet rs = statement.executeQuery()) {
                    if (rs.next())
                        return rs.getInt("dbid");
                    else
                        return null;
                }
            }
        }
    }
}
