package org.endeavourhealth.imapi.queryengine;

public class QueryGenerator {
    /*
    private static final Logger LOG = LoggerFactory.getLogger(QueryGenerator.class);

    private final EntityService svc = new EntityService();
    private final List<String> fields = new ArrayList<>();
    private Table mainTable;
    private final Map<String, Table> joins = new HashMap<>();
    private final List<String> conditions = new ArrayList<>();
    private final List<String> orderBy = new ArrayList<>();
    private Integer limit = null;

    public QueryGenerator getSelect(String iri) throws JsonProcessingException {
        Match json = loadProfile(iri);

        LOG.info("Generating Query");
        return generateQuery(json, "main");
    }

    private Match loadProfile(String iri) throws JsonProcessingException {
        LOG.info("Loading query");
        TTBundle bundle = svc.getBundle(iri, Query.of(IM.DEFINITION.getIri()), EntityService.UNLIMITED);

        TTArray def = bundle.getEntity().get(IM.DEFINITION);
        if (def == null)
            return null;

        String query = def.asLiteral().getValue();
        LOG.info(query);

        LOG.info("Deserializing");
        try (CachedObjectMapper om = new CachedObjectMapper()) {
            Match json = om.readValue(query, Match.class);
        }
        LOG.info(json.toString());

        return json;
    }

    private QueryGenerator generateQuery(Query profile, String alias) throws JsonProcessingException {
        return this.generateQuery(profile, alias, false);
    }
    private QueryGenerator generateQuery(Query profile, String alias, boolean pkOnly) throws JsonProcessingException {
        StringJoiner result = new StringJoiner(System.lineSeparator());

        try {
            String iri = profile.getEntityType().getIri();
            mainTable = new Table()
                .setIri(iri)
                .setName(QueryGenHelper.getEntitySource(iri))
                .setPk(QueryGenHelper.getPk(iri))
                .setAlias(alias);

            // fields.add(alias + "." + mainTable.getPk());
            for (Match matchProperty:profile.getMust()){
                if (matchProperty.getValueObject()!=null){
                    addJoin(mainTable, matchProperty);
                }
                else
                 conditions.add(getAnd(mainTable, matchProperty));
            }
            for (Match matchProperty:profile.getOr()){
                    conditions.add(getOr(mainTable, matchProperty));
            }


            else if (profile.getOr() != null && !profile.getOr().isEmpty()) {
                conditions.add(getOr(mainTable, profile.getOr()));
            }

            if (profile instanceof Match && ((Match)profile).getSort()!=null) {
                OrderLimit sortLimit = ((Match)profile).getSort();
                String direction = sortLimit.getDirection() == null || sortLimit.getDirection() == Order.ASCENDING ? " ASC" : " DESC";
                orderBy.add(QueryGenHelper.getTableField(mainTable, sortLimit.getOrderBy().getIri()) + direction);
                if (sortLimit.getCount() != null)
                    limit = sortLimit.getCount();
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

    private Table addJoin(Table parent, Match matchProperty) throws JsonProcessingException {
        Table table = createTable(matchProperty.getValueObject().getEntityType().getIri());

        StringJoiner join = new StringJoiner(System.lineSeparator());

        join.add(QueryGenHelper.getTableField(table, matchProperty.getPathTo().getIri()) + " = " + parent.getAlias() + "." + parent.getPk());

        if (matchProperty.getMatch() != null && !matchProperty.getMatch().isEmpty()) {
            join.add("AND " + getAnd(table, matchProperty.getMatch()));
        } else if (matchProperty.getOr() != null && !matchProperty.getOr().isEmpty()) {
            join.add("AND " + getOr(table, matchProperty.getOr()));
        }

        table.setJoin(join.toString());

        return table;
    }

    private String getAnd(Table table, List<Match> matchProperties) throws JsonProcessingException {
        return String.join("\nAND ", getStatements(table, matchProperties));
    }

    private String getOr(Table table, List<Match> matchProperties) throws JsonProcessingException {
        return String.join("\nOR ", getStatements(table, matchProperties));
    }

    private List<String> getStatements(Table table, List<Match> matchProperties) throws JsonProcessingException {
        List<String> result = new ArrayList<>();

        for(Match matchProperty : matchProperties) {
            if (matchProperty.getProperty()!=null) {
                String condition = getCondition(table, matchProperty);
                if (condition != null)
                    result.add(condition);
            } else if (matchProperty.getEntityType() != null) {
                subJoin(table, matchProperty);
            } else if (matchProperty.getMatch() != null && !matchProperty.getMatch().isEmpty()) {
                result.add("(" + getAnd(table, matchProperty.getMatch()) + ")");
            } else if (matchProperty.getOr() != null && !matchProperty.getOr().isEmpty()) {
                result.add("(" + getOr(table, matchProperty.getOr()) + ")");
            } else {
                throw new IllegalArgumentException("Unknown matchProperty type");
            }
        }

        return result;
    }

    private String getCondition(Table table, Match matchProperty) throws JsonProcessingException {
        if (IM.HAS_PROFILE.equals(matchProperty.getProperty())) {
            // Subselect
            StringJoiner subselect = new StringJoiner(" ");

            Match profile = loadProfile(matchProperty.getValueIn().get(0).getIri());
            QueryGenerator sub = new QueryGenerator().generateQuery(profile, "sub", true);

            subselect.add(table.getAlias() + "." + table.getPk());
            subselect.add("IN (");
            subselect.add(sub.build());
            subselect.add(")");

            if (matchProperty.getValueVar() != null)
                LOG.warn("Unknown field!");

            return subselect.toString();
        } else {
            String left;
            if (matchProperty.getPathTo() == null) {
                left = QueryGenHelper.getTableField(table, matchProperty.getProperty().getIri());
            } else {
                Table sub = addJoin(table, matchProperty);
                left = QueryGenHelper.getTableField(sub, matchProperty.getProperty().getIri());
            }

            String right = null;
            if (matchProperty.getValueIn() != null) {
                List<String> values = new ArrayList<>();
                matchProperty.getValueIn().forEach(i -> values.add("\"" + QueryGenHelper.getValue(i) + "\""));

                right = ("IN (" + String.join(", ", values) + ")");
            } else if (matchProperty.getValueCompare() != null) {
                right = (QueryGenHelper.getComparison(matchProperty.getValueCompare().getComparison())) + " " + (matchProperty.getValueCompare().getValue());
            } else if (matchProperty.getValueRange() != null) {
                right = "BETWEEN " + matchProperty.getValueRange().getFrom().getValue() + " AND " + matchProperty.getValueRange().getTo().getValue();
            } else if (matchProperty.isNotExist()) {
                right = ("IS NULL");
            }

            if (matchProperty.getValueVar() != null)
                fields.add(left + " AS " + matchProperty.getValueVar());

            return right == null ? null :(left + " " + right);
        }
    }

    private void subJoin(Table table, Match matchProperty) throws JsonProcessingException {
        Table sub = createTable(matchProperty.getEntityType().getIri());
        sub.setJoin(table.getAlias() + "." + table.getPk() + " = " + QueryGenHelper.getTableField(sub, matchProperty.getPathTo().getIri()));

        QueryGenerator qry = new QueryGenerator().generateQuery(matchProperty, sub.getAlias() + "_sub", true);

        qry.fields.forEach(f -> fields.add(f.replaceAll(sub.getAlias() + "_sub\\.", sub.getAlias() + ".")));
        qry.fields.clear();
        qry.fields.add(sub.getAlias() + "_sub." + sub.getPk());
        qry.conditions.add(table.getAlias() + "." + table.getPk() + " = " + sub.getAlias() + "_sub." + QueryGenHelper.getField(sub, matchProperty.getPathTo().getIri()));

        conditions.add(sub.getAlias() + "." + sub.getPk() + " = (" + qry.build() + ")");
        conditions.add(getCondition(sub, matchProperty.getTest()));

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

     */
}
