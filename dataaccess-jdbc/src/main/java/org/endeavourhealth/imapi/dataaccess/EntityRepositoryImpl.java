package org.endeavourhealth.imapi.dataaccess;

import org.endeavourhealth.imapi.dataaccess.helpers.ConnectionPool;
import org.endeavourhealth.imapi.dataaccess.helpers.DALException;
import org.endeavourhealth.imapi.model.search.SearchResultSummary;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.IM;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.StringJoiner;

@SuppressWarnings("java:S1192") // Disable "Literals as const" rule for SQL
public class EntityRepositoryImpl implements EntityRepository {
    @Override
    public TTIriRef getEntityReferenceByIri(String iri) throws DALException {
        TTIriRef ttIriRef = new TTIriRef();
        StringJoiner sql = new StringJoiner("\n")
                .add("SELECT c.name")
                .add("FROM entity c")
                .add("WHERE c.iri = ?");
        try (Connection conn = ConnectionPool.get();
             PreparedStatement statement = conn.prepareStatement(sql.toString())) {
            statement.setString(1, iri);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    ttIriRef.setIri(iri).setName(rs.getString("name"));
                }
            }
        } catch (SQLException e) {
            throw new DALException("Failed to get entity reference", e);
        }
        return ttIriRef;
    }

    @Override
    public SearchResultSummary getEntitySummaryByIri(String iri) throws DALException {
        SearchResultSummary entitySummary = new SearchResultSummary();
        StringJoiner sql = new StringJoiner("\n")
                .add("SELECT c.iri, c.name, c.code, n.iri AS schemeIri, n.name AS schemeName")
                .add("FROM entity c")
                .add("LEFT JOIN namespace n ON n.iri = c.scheme")
                .add("WHERE c.iri = ?");
        try (Connection conn = ConnectionPool.get();
             PreparedStatement statement = conn.prepareStatement(sql.toString())) {
            statement.setString(1, iri);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    entitySummary
                        .setIri(rs.getString("iri"))
                        .setName(rs.getString("name"))
                        .setCode(rs.getString("code"))
                        .setScheme(TTIriRef.iri(rs.getString("schemeIri"), rs.getString("schemeName")));
                }
            }

        } catch (SQLException e) {
            throw new DALException("Failed to get entity summary", e);
        }
        return entitySummary;
    }
}
