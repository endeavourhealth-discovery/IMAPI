package org.endeavourhealth.imapi.dataaccess;

import org.endeavourhealth.imapi.dataaccess.helpers.ConnectionPool;
import org.endeavourhealth.imapi.dataaccess.helpers.DALException;
import org.endeavourhealth.imapi.model.TermCode;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

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

    @Override
    public TTIriRef findByCodeAndScheme(String code, String scheme) throws DALException {
        TTIriRef entity = new TTIriRef();
        StringJoiner sql = new StringJoiner("\n")
            .add("SELECT c.iri, c.name")
            .add("FROM term_code tc")
            .add("JOIN entity s ON s.dbid = tc.scheme")
            .add("JOIN entity c ON c.dbid = tc.entity")
            .add("WHERE tc.code = ?")
            .add(" AND s.iri = ?");
        try (Connection conn = ConnectionPool.get();
             PreparedStatement statement = conn.prepareStatement(sql.toString())) {
            statement.setString(1, code);
            statement.setString(2, scheme);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    entity
                        .setIri(rs.getString("iri"))
                        .setName(rs.getString("name"));
                }
            }
        } catch (SQLException e) {
            throw new DALException("Failed to find entity iri by code/scheme", e);
        }
        return entity;
    }

    @Override
    public Integer findDbidByCodeAndScheme(String code, String scheme) throws DALException {
        StringJoiner sql = new StringJoiner("\n")
            .add("SELECT c.dbid")
            .add("FROM term_code tc")
            .add("JOIN entity s ON s.dbid = tc.scheme")
            .add("JOIN entity c ON c.dbid = tc.entity")
            .add("WHERE tc.code = ?")
            .add(" AND s.iri = ?");
        try (Connection conn = ConnectionPool.get();
             PreparedStatement statement = conn.prepareStatement(sql.toString())) {
            statement.setString(1, code);
            statement.setString(2, scheme);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next())
                    return rs.getInt("dbid");
                else
                    return null;
            }
        } catch (SQLException e) {
            throw new DALException("Failed to find entity dbid by code/scheme");
        }
    }
}
