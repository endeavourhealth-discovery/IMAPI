package org.endeavourhealth.imapi.dataaccess;

import org.endeavourhealth.imapi.dataaccess.helpers.ConnectionPool;
import org.endeavourhealth.imapi.dataaccess.helpers.DALException;
import org.endeavourhealth.imapi.model.search.SearchResultSummary;
import org.endeavourhealth.imapi.model.search.SearchRequest;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import static org.endeavourhealth.imapi.dataaccess.helpers.JDBCHelper.inListParams;

@SuppressWarnings("java:S1192") // Disable "Literals as const" rule for SQL
public class EntitySearchRepositoryImpl implements EntitySearchRepository {
    private final EntityTypeRepositoryImpl typeRepo = new EntityTypeRepositoryImpl();

    @Override
    public List<SearchResultSummary> advancedSearch(SearchRequest request) throws DALException {
        String searchSql = generateSearchSql(request.getSchemeFilter(), request.getTypeFilter(), request.getStatusFilter());

        List<SearchResultSummary> result = new ArrayList<>();

        try (Connection conn = ConnectionPool.get();
             PreparedStatement searchStmt = conn.prepareStatement(searchSql)) {

            setSearchParameters(request, searchStmt);

            try (ResultSet rs = searchStmt.executeQuery()) {

                while (rs.next()) {
                    SearchResultSummary smry = getEntitySummary(rs);
                    smry.setEntityType(typeRepo.getEntityTypes(smry.getIri()));
                    result.add(smry);
                }
            }
        } catch (SQLException e) {
            throw new DALException("Advanced search failed", e);
        }

        return result;
    }

    private String generateSearchSql(List<String> schemeFilter, List<String> typeFilter, List<String> statusFilter) {
        StringJoiner sql = new StringJoiner("\n")
            .add("SELECT DISTINCT c.dbid, c.iri, c.name, c.description, c.code, c.scheme, c.status, cs.term, n.name AS scheme_name, t.name AS status_name")
            .add("FROM entity_search cs")
            .add("JOIN entity c ON cs.entity_dbid = c.dbid")
            .add("JOIN entity_type ct ON cs.entity_dbid = ct.entity")
            .add("JOIN entity t ON t.iri = c.status")
            .add("LEFT JOIN namespace n ON n.iri = c.scheme")
            .add("WHERE MATCH(cs.term) AGAINST (? IN BOOLEAN MODE)");

        if (schemeFilter != null && !schemeFilter.isEmpty())
            sql.add("AND (c.scheme IS NULL OR c.scheme IN " + inListParams(schemeFilter.size()) + ")");

        if (typeFilter != null && !typeFilter.isEmpty())
            sql.add("AND ct.type IN " + inListParams(typeFilter.size()));

        if (statusFilter != null && !statusFilter.isEmpty())
            sql.add("AND c.status IN " + inListParams(statusFilter.size()));

        sql.add("ORDER BY LENGTH(term), term")
            .add("LIMIT ?");

        return sql.toString();
    }

    private void setSearchParameters(SearchRequest request, PreparedStatement searchStmt) throws SQLException {
        int i = 0;
        searchStmt.setString(++i, toFreeTextTerms(request.getTermFilter()));

        if (request.getSchemeFilter() != null && !request.getSchemeFilter().isEmpty())
            for (String scheme : request.getSchemeFilter())
                searchStmt.setString(++i, scheme);

        if (request.getTypeFilter() != null && !request.getTypeFilter().isEmpty())
            for (String type : request.getTypeFilter())
                searchStmt.setString(++i, type);

        if (request.getStatusFilter() != null && !request.getStatusFilter().isEmpty())
            for (String status : request.getStatusFilter())
                searchStmt.setString(++i, status);

        searchStmt.setInt(++i, request.getSize());
    }

    private SearchResultSummary getEntitySummary(ResultSet rs) throws SQLException {
        return new SearchResultSummary()
            .setIri(rs.getString("iri"))
            .setName(rs.getString("name"))
            .setDescription(rs.getString("description"))
            .setCode(rs.getString("code"))
            .setScheme(TTIriRef.iri(rs.getString("scheme"), rs.getString("scheme_name")))
            .setMatch(rs.getString("term"))
            .setStatus(TTIriRef.iri(rs.getString("status"), rs.getString("status_name")));
    }

    @Override
    public SearchResultSummary getSummary(String iri) throws DALException {
        SearchResultSummary summary = new SearchResultSummary();
        StringJoiner sql = new StringJoiner("\n")
                .add("SELECT DISTINCT c.dbid, c.iri, c.name, c.description, c.code, c.scheme, c.status, cs.term, n.name AS scheme_name, t.name AS status_name")
                .add("FROM entity_search cs")
                .add("JOIN entity c ON cs.entity_dbid = c.dbid")
                .add("JOIN entity_type ct ON cs.entity_dbid = ct.entity")
                .add("JOIN entity t ON t.iri = c.status")
                .add("LEFT JOIN namespace n ON n.iri = c.scheme")
                .add("WHERE c.iri=? ");
        try (Connection conn = ConnectionPool.get();
             PreparedStatement statement = conn.prepareStatement(sql.toString())) {
            statement.setString(1, iri);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    summary = getEntitySummary(rs);
                    summary.setEntityType(typeRepo.getEntityTypes(summary.getIri()));
                }
            }
        } catch (SQLException e) {
            throw new DALException("Failed to get summary", e);
        }
        return summary;
    }

    private String toFreeTextTerms(String termFilter) {
        return Arrays.stream(termFilter
                .replace("-", " ")
                .replace("(", "")
                .replace(")", "")
                .split(" "))
            .filter(t -> t.trim().length() >= 3)
            .map(w -> "+" + w + "*")
            .collect(Collectors.joining(" "));
    }
}
