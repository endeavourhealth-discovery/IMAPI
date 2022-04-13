package org.endeavourhealth.imapi.queryengine;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.logic.service.EntityService;
import org.endeavourhealth.imapi.model.query.Match;
import org.endeavourhealth.imapi.model.query.Order;
import org.endeavourhealth.imapi.model.query.Profile;
import org.endeavourhealth.imapi.model.query.Sort;
import org.endeavourhealth.imapi.model.tripletree.TTArray;
import org.endeavourhealth.imapi.model.tripletree.TTBundle;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class QueryGenerator {
    private static final Logger LOG = LoggerFactory.getLogger(QueryGenerator.class);

    private final EntityService svc = new EntityService();
    private final List<String> fields = new ArrayList<>();
    private Table mainTable;
    private final Map<String, Table> joins = new HashMap<>();
    private final List<String> conditions = new ArrayList<>();
    private final List<String> orderBy = new ArrayList<>();
    private Integer limit = null;

    public QueryGenerator getSelect(String iri) throws JsonProcessingException {
        Profile json = loadProfile(iri);

        LOG.info("Generating Query");
        return generateQuery(json, "main");
    }

    private Profile loadProfile(String iri) throws JsonProcessingException {
        LOG.info("Loading query");
        TTBundle bundle = svc.getBundle(iri, Set.of(IM.DEFINITION.getIri()), EntityService.UNLIMITED);

        TTArray def = bundle.getEntity().get(IM.DEFINITION);
        if (def == null)
            return null;

        String query = def.asLiteral().getValue();
        LOG.info(query);

        LOG.info("Deserializing");
        Profile json = new ObjectMapper().readValue(query, Profile.class);
        LOG.info(json.toString());

        return json;
    }

    private QueryGenerator generateQuery(Profile profile, String alias) throws JsonProcessingException {
        return this.generateQuery(profile, alias, false);
    }
    private QueryGenerator generateQuery(Profile profile, String alias, boolean pkOnly) throws JsonProcessingException {
        StringJoiner result = new StringJoiner(System.lineSeparator());

        try {
            String iri = profile.getEntityType().getIri();
            mainTable = new Table()
                .setIri(iri)
                .setName(QueryGenHelper.getEntitySource(iri))
                .setPk(QueryGenHelper.getPk(iri))
                .setAlias(alias);

            // fields.add(alias + "." + mainTable.getPk());

            Match match = profile.getMatch();
            if (match != null) {
                addJoin(mainTable, match);
            } else if (profile.getAnd() != null && !profile.getAnd().isEmpty()) {
                conditions.add(getAnd(mainTable, profile.getAnd()));
            } else if (profile.getOr() != null && !profile.getOr().isEmpty()) {
                conditions.add(getOr(mainTable, profile.getOr()));
            }

            if (profile instanceof Match && ((Match)profile).getSort()!=null) {
                Sort sort = ((Match)profile).getSort();
                String direction = sort.getDirection() == null || sort.getDirection() == Order.ASCENDING ? " ASC" : " DESC";
                orderBy.add(QueryGenHelper.getTableField(mainTable, sort.getOrderBy().getIri()) + direction);
                if (sort.getCount() != null)
                    limit = sort.getCount();
            }
        } catch (Exception e) {
            LOG.error(result.toString());
            throw e;
        }

        if (pkOnly) {
            fields.clear();
        }
        fields.add(alias + "." + mainTable.getPk());

        return this;
    }

    public String build() {
        StringJoiner result = new StringJoiner(System.lineSeparator());
        result.add("SELECT " + String.join(", ", fields));
        result.add("FROM " + mainTable.getName() + " " + mainTable.getAlias());

        for(Table join : joins.values()) {
            result.add("JOIN " + join.getName() + " " + join.getAlias() + " ON " + join.getJoin());
        }

        if (!conditions.isEmpty()) {
            result.add("WHERE " + String.join("\nAND ", conditions));
        }

        if (!orderBy.isEmpty())
            result.add("ORDER BY " + String.join(", ", orderBy));

        if (limit != null)
            result.add("LIMIT " + limit);

        return result.toString();
    }

    private Table addJoin(Table parent, Match match) throws JsonProcessingException {
        Table table = createTable(match.getEntityType().getIri());

        StringJoiner join = new StringJoiner(System.lineSeparator());

        join.add(QueryGenHelper.getTableField(table, match.getPathTo().getIri()) + " = " + parent.getAlias() + "." + parent.getPk());

        if (match.getAnd() != null && !match.getAnd().isEmpty()) {
            join.add("AND " + getAnd(table, match.getAnd()));
        } else if (match.getOr() != null && !match.getOr().isEmpty()) {
            join.add("AND " + getOr(table, match.getOr()));
        }

        table.setJoin(join.toString());

        return table;
    }

    private String getAnd(Table table, List<Match> matches) throws JsonProcessingException {
        return String.join("\nAND ", getStatements(table, matches));
    }

    private String getOr(Table table, List<Match> matches) throws JsonProcessingException {
        return String.join("\nOR ", getStatements(table, matches));
    }

    private List<String> getStatements(Table table, List<Match> matches) throws JsonProcessingException {
        List<String> result = new ArrayList<>();

        for(Match match: matches) {
            if (match.getProperty()!=null) {
                String condition = getCondition(table, match);
                if (condition != null)
                    result.add(condition);
            } else if (match.getEntityType() != null) {
                subJoin(table, match);
            } else if (match.getAnd() != null && !match.getAnd().isEmpty()) {
                result.add("(" + getAnd(table, match.getAnd()) + ")");
            } else if (match.getOr() != null && !match.getOr().isEmpty()) {
                result.add("(" + getOr(table, match.getOr()) + ")");
            } else {
                throw new IllegalArgumentException("Unknown match type");
            }
        }

        return result;
    }

    private String getCondition(Table table, Match match) throws JsonProcessingException {
        if (IM.HAS_PROFILE.equals(match.getProperty())) {
            // Subselect
            StringJoiner subselect = new StringJoiner(" ");

            Profile profile = loadProfile(match.getValueIn().get(0).getIri());
            QueryGenerator sub = new QueryGenerator().generateQuery(profile, "sub", true);

            subselect.add(table.getAlias() + "." + table.getPk());
            subselect.add("IN (");
            subselect.add(sub.build());
            subselect.add(")");

            if (match.getValueVar() != null)
                LOG.warn("Unknown field!");

            return subselect.toString();
        } else {
            String left;
            if (match.getPathTo() == null) {
                left = QueryGenHelper.getTableField(table, match.getProperty().getIri());
            } else {
                Table sub = addJoin(table, match);
                left = QueryGenHelper.getTableField(sub, match.getProperty().getIri());
            }

            String right = null;
            if (match.getValueIn() != null) {
                List<String> values = new ArrayList<>();
                match.getValueIn().forEach(i -> values.add("\"" + QueryGenHelper.getValue(i) + "\""));

                right = ("IN (" + String.join(", ", values) + ")");
            } else if (match.getValueCompare() != null) {
                right = (QueryGenHelper.getComparison(match.getValueCompare().getComparison())) + " " + (match.getValueCompare().getValue());
            } else if (match.getValueRange() != null) {
                right = "BETWEEN " + match.getValueRange().getFrom().getValue() + " AND " + match.getValueRange().getTo().getValue();
            } else if (match.isNotExist()) {
                right = ("IS NULL");
            }

            if (match.getValueVar() != null)
                fields.add(left + " AS " + match.getValueVar());

            return right == null ? null :(left + " " + right);
        }
    }

    private void subJoin(Table table, Match match) throws JsonProcessingException {
        Table sub = createTable(match.getEntityType().getIri());
        sub.setJoin(table.getAlias() + "." + table.getPk() + " = " + QueryGenHelper.getTableField(sub, match.getPathTo().getIri()));

        QueryGenerator qry = new QueryGenerator().generateQuery(match, sub.getAlias() + "_sub", true);

        qry.fields.forEach(f -> fields.add(f.replaceAll(sub.getAlias() + "_sub\\.", sub.getAlias() + ".")));
        qry.fields.clear();
        qry.fields.add(sub.getAlias() + "_sub." + sub.getPk());
        qry.conditions.add(table.getAlias() + "." + table.getPk() + " = " + sub.getAlias() + "_sub." + QueryGenHelper.getField(sub, match.getPathTo().getIri()));

        conditions.add(sub.getAlias() + "." + sub.getPk() + " = (" + qry.build() + ")");
        conditions.add(getCondition(sub, match.getTest()));

    }

    private Table createTable(String iri) {
        Table result = new Table()
            .setIri(iri)
            .setName(QueryGenHelper.getEntitySource(iri))
            .setPk(QueryGenHelper.getPk(iri))
            .setAlias("t" + joins.size());

        joins.put(iri, result);

        return result;
    }
}
