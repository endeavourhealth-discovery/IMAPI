package org.endeavourhealth.dataaccess.repository;

import org.endeavourhealth.dataaccess.ConnectionPool;
import org.endeavourhealth.dataaccess.Levenshtein;
import org.endeavourhealth.imapi.model.search.ConceptSummary;
import org.endeavourhealth.imapi.model.search.SearchRequest;
import org.endeavourhealth.imapi.model.tripletree.TTConcept;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class ConceptSearchRepository extends BaseRepository {
    ConceptTypeRepository typeRepo = new ConceptTypeRepository();

    public List<ConceptSummary> advancedSearch(SearchRequest request) throws Exception {
        String searchSql = generateSearchSql(request.getSchemeFilter(), request.getTypeFilter(), request.getStatusFilter());

        List<ConceptSummary> result = new ArrayList<>();

        try (Connection conn = ConnectionPool.get();
            PreparedStatement searchStmt = conn.prepareStatement(searchSql)) {

            setSearchParameters(request, searchStmt);

            try (ResultSet rs = searchStmt.executeQuery()) {

                while (rs.next()) {
                    ConceptSummary smry = getConceptSummary(request.getTermFilter(), rs);
                    smry.setConceptType(typeRepo.getConceptTypes(smry.getIri()));
                    result.add(smry);
                }
            }
        }

        return result;
    }

    private String generateSearchSql(List<String> schemeFilter, List<String> typeFilter, List<String> statusFilter) {
        StringJoiner sql = new StringJoiner("\n")
            .add("SELECT DISTINCT c.dbid, c.iri, c.name, c.description, c.code, c.scheme, c.status, cs.term, s.name AS scheme_name, t.name AS status_name")
            .add("FROM concept_search cs")
            .add("JOIN concept c ON cs.concept_dbid = c.dbid")
            .add("JOIN concept_type ct ON cs.concept_dbid = ct.concept")
            .add("JOIN concept t ON t.iri = c.status")
            .add("LEFT JOIN concept s ON s.iri = c.scheme")
            .add("WHERE MATCH(cs.term) AGAINST (? IN BOOLEAN MODE)");

        if (schemeFilter != null && !schemeFilter.isEmpty())
            sql.add("AND (c.scheme IS NULL OR c.scheme IN " + inList(schemeFilter.size()) + ")");

        if (typeFilter != null && !typeFilter.isEmpty())
            sql.add("AND ct.type IN " + inList(typeFilter.size()));

        if (statusFilter != null && !statusFilter.isEmpty())
            sql.add("AND c.status IN " + inList(statusFilter.size()));

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

    private ConceptSummary getConceptSummary(String termFilter, ResultSet rs) throws SQLException {
        return new ConceptSummary()
            .setIri(rs.getString("iri"))
            .setName(rs.getString("name"))
            .setDescription(rs.getString("description"))
            .setCode(rs.getString("code"))
            .setScheme(iri(rs.getString("scheme"), rs.getString("scheme_name")))
            .setMatch(rs.getString("term"))
            .setWeighting(Levenshtein.calculate(termFilter, rs.getString("term")))
            .setStatus(iri(rs.getString("status"), rs.getString("status_name")));
    }

    public ConceptSummary getSummary(String iri) throws SQLException {
        ConceptSummary summary = new ConceptSummary();
        StringJoiner sql = new StringJoiner("\n")
                .add("SELECT DISTINCT c.dbid, c.iri, c.name, c.description, c.code, c.scheme, c.status, cs.term, s.name AS scheme_name, t.name AS status_name")
                .add("FROM concept_search cs")
                .add("JOIN concept c ON cs.concept_dbid = c.dbid")
                .add("JOIN concept_type ct ON cs.concept_dbid = ct.concept")
                .add("JOIN concept t ON t.iri = c.status")
                .add("LEFT JOIN concept s ON s.iri = c.scheme")
                .add("WHERE c.iri=? ");
        try (Connection conn = ConnectionPool.get()) {
            assert conn != null;
            try (PreparedStatement statement = conn.prepareStatement(sql.toString())) {
                statement.setString(1, iri);
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        summary = getConceptSummary(null, rs);
                        summary.setConceptType(typeRepo.getConceptTypes(summary.getIri()));
                    }
                }
            }
        }
        return summary;
    }

}
