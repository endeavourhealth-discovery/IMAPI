package org.endeavourhealth.imapi.dataaccess;

import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.model.tripletree.TTContext;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.queryengine.QueryValidator;
import org.endeavourhealth.imapi.vocabulary.*;

import java.util.*;
import java.util.stream.Collectors;

public class SparqlConverter {
    private Query query;
    private Update update;
    private final QueryRequest queryRequest;
    private final String tabs = "";
    private TTContext context;
    int o = 0;
    private String labelVariable;


    String mainEntity;


    public SparqlConverter(QueryRequest queryRequest) throws QueryException {
        this.queryRequest = queryRequest;
        context = queryRequest.getAsContext();
        if (queryRequest.getQuery() != null) {
            this.query = queryRequest.getQuery();
            new QueryValidator().validateQuery(this.query);
        } else
            this.update = queryRequest.getUpdate();

    }


    /**
     * Takes an IMQ select query model and converts to SPARQL
     *
     * @return String of SPARQL
     **/
    public String getSelectSparql(Set<TTIriRef> statusFilter) throws QueryException {

        StringBuilder selectQl = new StringBuilder();
        addPrefixes(selectQl);

        selectQl.append("SELECT ");
        selectQl.append("distinct ");
        addWhereSparql(selectQl, statusFilter, true);

        orderGroupLimit(selectQl, query);
        return selectQl.toString();

    }

    public String getAskSparql(Set<TTIriRef> statusFilter) throws QueryException {

        StringBuilder askQl = new StringBuilder();
        addPrefixes(askQl);

        askQl.append("ASK ");
        addWhereSparql(askQl, statusFilter, false);

        return askQl.toString();

    }

    private void addPrefixes(StringBuilder sparql) {
        sparql.append(getDefaultPrefixes());
        if (null != queryRequest.getTextSearch()) {

            sparql.append("PREFIX con-inst: <http://www.ontotext.com/connectors/lucene/instance#>\n")
                    .append("PREFIX con: <http://www.ontotext.com/connectors/lucene#>\n");
        }
    }

    private void addWhereSparql(StringBuilder sparql, Set<TTIriRef> statusFilter, Boolean includeReturns) throws QueryException {
        mainEntity = "entity";
        if (null != query.getMatch().get(0).getVariable())
            mainEntity = query.getMatch().get(0).getVariable();
        StringBuilder whereQl = new StringBuilder();
        whereQl.append("WHERE {");
        if (query.getTypeOf() != null) {
            whereQl.append("?").append(mainEntity).append(" rdf:type ").append(iriFromString(query.getTypeOf().getIri()) + ".\n");
        }

        if (null != queryRequest.getTextSearch()) {
            textSearch(whereQl);
        }

        for (Match match : query.getMatch()) {
            match(whereQl, mainEntity, match);
        }

        if (includeReturns && null != query.getReturn()) {
            for (Return aReturn : query.getReturn()) {
                convertReturn(sparql, whereQl, aReturn);
            }
        }
        o++;
        String statusVar = "status" + o;


        if (null != statusFilter && !statusFilter.isEmpty()) {
            List<String> statusStrings = new ArrayList<>();
            for (TTIriRef status : statusFilter) {
                statusStrings.add("<" + status.getIri() + ">");
            }
            whereQl.append("?").append(mainEntity).append(" im:status ?").append(statusVar).append(".\n");
            whereQl.append("Filter (?").append(statusVar).append(" in (").append(String.join(",", statusStrings)).append("))\n");
        }
        if (query.isActiveOnly()) {
            whereQl.append("?").append(mainEntity).append(" im:status im:Active.\n");
        }


        sparql.append("\n");

        whereQl.append("}");

        sparql.append(whereQl).append("\n");
    }

    public String getCountSparql(Set<TTIriRef> statusFilter) throws QueryException {

        StringBuilder selectQl = new StringBuilder();
        selectQl.append(getDefaultPrefixes());
        if (null != queryRequest.getTextSearch()) {

            selectQl.append("PREFIX con-inst: <http://www.ontotext.com/connectors/lucene/instance#>\n")
                    .append("PREFIX con: <http://www.ontotext.com/connectors/lucene#>\n");
        }

        selectQl.append("SELECT ");
        selectQl.append("(COUNT(distinct ?entity");
        mainEntity = "entity";
        if (query.getMatch().get(0).getVariable() != null)
            mainEntity = query.getMatch().get(0).getVariable();
        selectQl.append(") as ?cnt) ");
        StringBuilder whereQl = new StringBuilder();
        whereQl.append("WHERE {");
        if (query.getTypeOf() != null) {
            whereQl.append("?").append(mainEntity).append(" rdf:type ").append(iriFromString(query.getTypeOf().getIri()) + ".\n");
        }

        if (null != queryRequest.getTextSearch()) {
            textSearch(whereQl);
        }

        for (Match match : query.getMatch()) {
            match(whereQl, mainEntity, match);
        }

        o++;
        String statusVar = "status" + o;


        if (null != statusFilter && 0 != statusFilter.size()) {
            List<String> statusStrings = new ArrayList<>();
            for (TTIriRef status : statusFilter) {
                statusStrings.add("<" + status.getIri() + ">");
            }
            whereQl.append("?").append(mainEntity).append(" im:status ?").append(statusVar).append(".\n");
            whereQl.append("Filter (?").append(statusVar).append(" in (").append(String.join(",", statusStrings)).append("))\n");
        }
        if (query.isActiveOnly()) {
            whereQl.append("?").append(mainEntity).append(" im:status im:Active.\n");
        }


        selectQl.append("\n");

        whereQl.append("}");

        selectQl.append(whereQl).append("\n");
        return selectQl.toString();

    }

    public static String getDefaultPrefixes() {
        TTContext prefixes = new TTContext();
        StringBuilder sparql = new StringBuilder();
        prefixes.add(RDFS.NAMESPACE, "rdfs");
        sparql.append("PREFIX rdfs: <" + RDFS.NAMESPACE + ">\n");
        prefixes.add(RDF.NAMESPACE, "rdf");
        sparql.append("PREFIX rdf: <" + RDF.NAMESPACE + ">\n");
        prefixes.add(IM.NAMESPACE, "im");
        sparql.append("PREFIX im: <" + IM.NAMESPACE + ">\n");
        prefixes.add(XSD.NAMESPACE, "xsd");
        sparql.append("PREFIX xsd: <" + XSD.NAMESPACE + ">\n");
		prefixes.add(SNOMED.NAMESPACE,"sn");
		sparql.append("PREFIX sn: <"+ SNOMED.NAMESPACE+">\n");
        prefixes.add(SHACL.NAMESPACE, "sh");
        sparql.append("PREFIX sh: <").append(SHACL.NAMESPACE).append(">\n\n");
        return sparql.toString();
    }


    private String escape(String text) {
        return text.replaceAll("[(\\-){}^\"\\/~\\\\]", " ").replaceAll("\\s+", " ").trim();
    }

    private void textSearch(StringBuilder whereQl) {
        String text = queryRequest.getTextSearch();
        if (query.getReturn() == null) {
            query.return_(s -> s.property(p -> p.setIri(RDFS.LABEL)));
        }
        if (text.split(" ").length > 3) {
            whereQl.append("?").append(mainEntity).append(" rdfs:label ?labelText.\n");
            whereQl.append("filter (strstarts(?labelText,\"").append(text.replaceAll("\"", " ")).append("\"))\n");
        } else {
            text = escape(text);
            whereQl.append("[] a con-inst:im_fts;\n")
                    .append("       con:query ");
            String[] words = text.split(" ");
            for (int i = 0; i < words.length; i++) {
                boolean fuzzy = false;
                words[i] = "(label:" + words[i] + ((!fuzzy) ? "*" : "~") + ")";
            }
            String searchText = String.join(" && ", words);
            whereQl.append("\"" + searchText + "\" ;\n");
            whereQl.append("       con:entities ?").append(mainEntity).append(".\n");
        }
    }

    private void match(StringBuilder whereQl, String parent, Match match) throws QueryException {
        if (match.isExclude()) {
            whereQl.append(tabs).append(" FILTER NOT EXISTS {\n");
        }
        String subject;
        if (match.getVariable() != null)
            subject = match.getVariable();
        else if (match.getNodeRef() != null)
            subject = match.getNodeRef();
        else
            subject = parent;
        if (match.getGraph() != null) {
            whereQl.append(" graph ").append(iriFromAlias(match.getGraph())).append(" {");
        }
        if (match.getMatch() != null) {
            if (match.getBool() == Bool.or) {
                for (int i = 0; i < match.getMatch().size(); i++) {
                    if (i == 0)
                        whereQl.append("{ \n");
                    else
                        whereQl.append("UNION {\n");
                    Match subMatch = match.getMatch().get(i);
                    match(whereQl, subject, subMatch);
                    whereQl.append("}\n");
                }
            } else {
                for (Match subMatch : match.getMatch()) {
                    match(whereQl, subject, subMatch);
                }
            }
        }

        o++;
        String object = "object" + o;
        if (match.getTypeOf() != null) {
            type(whereQl, match.getTypeOf(), subject);
        } else {
            if (match.getInstanceOf() != null) {
                Node instance = match.getInstanceOf();
                String inList = iriFromAlias(instance);
                if (instance.isDescendantsOrSelfOf()) {
                    o++;
                    whereQl.append("?").append(subject).append(" im:isA ?").append(object).append(".\n");
                    whereQl.append("Filter (?").append(object);
                    if (!inList.contains(","))
                        whereQl.append(" =").append(inList).append(")\n");
                    else
                        whereQl.append(" in (").append(inList).append("))\n");

                } else if (instance.isDescendantsOf()) {
                    o++;
                    whereQl.append("?").append(subject).append(" im:isA ?").append(object).append(".\n");
                    whereQl.append("Filter (?").append(object);
                    if (!inList.contains(","))
                        whereQl.append(" =").append(inList).append(")\n");
                    else
                        whereQl.append(" in (").append(inList).append("))\n");
                    whereQl.append("Filter (?").append(subject).append("!= ").append(inList).append(")\n");

                } else if (instance.isAncestorsOf()) {
                    o++;
                    whereQl.append("?").append(subject).append(" ^im:isA ?").append(object).append(".\n");
                    whereQl.append("Filter (?").append(object);
                    if (!inList.contains(","))
                        whereQl.append(" =").append(inList).append(")\n");
                    else
                        whereQl.append(" in (").append(inList).append("))\n");
                } else {
                    o++;
                    whereQl.append("?").append(subject).append(" rdf:type ?type.\n");
                    whereQl.append("Filter (?").append(subject);
                    if (!inList.contains(","))
                        whereQl.append(" =").append(inList).append(")\n");
                    else
                        whereQl.append(" in (").append(inList).append("))\n");

                }
            }
        }

        if (match.getProperty() != null) {
            if (match.getBool() == Bool.or) {
                for (int i = 0; i < match.getProperty().size(); i++) {
                    if (i == 0)
                        whereQl.append("{ \n");
                    else
                        whereQl.append("UNION {\n");
                    property(whereQl, subject, match.getProperty().get(i), null);
                    whereQl.append("}\n");
                }
            } else {
                for (Property where : match.getProperty()) {
                    property(whereQl, subject, where, null);
                }
            }
        }
        if (match.getIs() != null) {
            is(whereQl, subject, match.getIs(), false);
        }
        if (match.getGraph() != null) {
            whereQl.append("}");
        }
        if (match.isExclude())
            whereQl.append("}\n");

    }

    private void type(StringBuilder whereQl, Node type, String subject) throws QueryException {
        if (type.isDescendantsOrSelfOf()) {
            o++;
            whereQl.append("?").append(subject).append(" im:isA ?").append("supertype").append(o).append(".\n");
            whereQl.append("?supertype").append(o).append(" rdf:type").append(String.join(",", iriFromAlias(type))).append(".\n");
        } else if (type.isDescendantsOf()) {
            o++;
            whereQl.append("?").append(subject).append(" im:isA ?").append("supertype").append(o).append(".\n");
            whereQl.append("?supertype").append(o).append(" rdf:type").append(String.join(",", iriFromAlias(type))).append(".\n");
            whereQl.append("Filter (?").append(subject).append("!= ").append(iriFromAlias(type)).append(")\n");
        } else {
            whereQl.append("?").append(subject).append(" rdf:type ").append(iriFromAlias(type)).append(".\n");
        }
    }


    /**
     * Proecesses the where property clause, the remaining match clause including subqueries
     *
     * @param whereQl the string builder sparql
     * @param subject the parent subject passed to this where clause
     * @param where   the where clause
     */
    private void property(StringBuilder whereQl, String subject, Property where, String parentVariable) throws QueryException, QueryException {
        String propertyVariable = where.getVariable() != null ? where.getVariable() : parentVariable;
        if (where.isAnyRoleGroup()) {
            whereQl.append("?").append(subject).append(" im:roleGroup ").append("?roleGroup").append(o).append(".\n");
            subject = "roleGroup" + o;
            o++;
        }
        if (where.getIri() != null || where.getParameter() != null) {
            o++;
            String inverse = where.isInverse() ? "^" : "";
            String property;
            if (propertyVariable == null) {
                property = "p" + o;
            } else
                property = propertyVariable;
            if (where.getParameter() != null) {
                property = iriFromAlias(where);
            }
            where.setVariable(property);
            String object;
            if (where.getMatch() != null)
                object = where.getMatch().getVariable();
            else if (where.getValueVariable() != null)
                object = where.getValueVariable();
            else {
                o++;
                object = "o" + o;
            }
            if (where.getIs() != null) {
                if (where.getIs().get(0).getVariable() != null) {
                    object = where.getIs().get(0).getVariable();
                }
            }
            if (where.getIsNull()) {
                whereQl.append(tabs).append(" FILTER NOT EXISTS {\n");
            }
            if (where.isDescendantsOrSelfOf()) {
                whereQl.append("?").append(property).append(" im:isA ").append(iriFromString(where.getIri())).append(".\n");
                whereQl.append("?").append(subject).append(" ").append(inverse).append("?").append(property).append(" ?").append(object).append(".\n");
            } else {
                if (propertyVariable != null) {
                    whereQl.append("?").append(subject).append(" ").append(inverse).append("?").append(property).append(" ?").append(object).append(".\n");
                    whereQl.append("filter (?").append(property).append(" = ").append(iriFromString(where.getIri())).append(")\n");
                } else
                    whereQl.append("?").append(subject).append(" ").append(inverse).append(iriFromString(where.getIri())).append(" ?").append(object).append(".\n");
            }
            if (where.getIsNull()) {
                whereQl.append(tabs).append(" }");
            }
            if (where.getIs() != null) {
                in(whereQl, object, where.getIs(), false);
            }
            if (where.getIsNot() != null) {
                in(whereQl, object, where.getIsNot(), true);
            } else if (where.getValue() != null) {
                whereValue(whereQl, object, where);
            } else if (where.getMatch() != null) {
                match(whereQl, object, where.getMatch());
            }
        }
        if (where.getProperty() != null) {
            if (where.getBool() == Bool.or) {
                for (int i = 0; i < where.getProperty().size(); i++) {
                    if (i == 0)
                        whereQl.append("{ \n");
                    else
                        whereQl.append("UNION {\n");
                    property(whereQl, subject, where.getProperty().get(i), null);
                    whereQl.append("}\n");
                }
            } else {
                for (Property subWhere : where.getProperty()) {
                    property(whereQl, subject, subWhere, null);
                }
            }
        }
    }


    private void is(StringBuilder whereQl, String object, List<Node> in, boolean isNot) throws QueryException {
        if (isNot)
            whereQl.append("Filter not exists {\n");
        o++;
        String sets = "sets" + o;
        whereQl.append("?").append(object).append(" ^im:hasMember ").append("?").append(sets).append(".\n");

        List<String> inList = new ArrayList<>();
        for (Node iri : in) {
            if (iri.getRef() != null)
                inList.add("?" + iri.getRef());
            else
                inList.add(iriFromAlias(iri));
        }
        String inString = String.join(",", inList);
        whereQl.append("Filter (?").append(sets).append(" in (").append(inString).append("))\n");
        if (isNot)
            whereQl.append(" }");
    }


    private void in(StringBuilder whereQl, String object, List<Node> in, boolean isNot) throws QueryException {
        String not = isNot ? " not " : "";
        boolean subTypes = false;
        boolean superTypes = false;
        for (Element item : in) {
            if (item.isDescendantsOrSelfOf()) {
                subTypes = true;
                break;
            }
        }
        for (Element item : in) {
            if (item.isAncestorsOf()) {
                superTypes = true;
                break;
            }
        }
        if (subTypes) {
            String superObject = "super" + object;
            whereQl.append("?").append(object).append(" im:isA ?").append(superObject).append(".\n");
            whereQl.append("Filter (?").append(superObject).append(not).append(" in (");
        } else if (superTypes) {
            String subObject = "sub" + object;
            whereQl.append("?").append(subObject).append(" im:isA ?").append(object).append(".\n");
            whereQl.append("Filter (?").append(subObject).append(not).append(" in (");
        } else {
            whereQl.append("Filter (?").append(object).append(not).append(" in (");
        }
        List<String> inList = new ArrayList<>();
        for (Element iri : in) {
            if (iri.getRef() != null)
                inList.add("?" + iri.getRef());
            else
                inList.add(iriFromAlias(iri));
        }
        String inString = String.join(",", inList);
        whereQl.append(inString);
        whereQl.append("))\n");

    }

    private void whereValue(StringBuilder whereQl, String object, Property where) throws QueryException {
        String comp = where.getOperator() != null ? where.getOperator().getValue() : Operator.eq.getValue();
        String value = where.getValue();
        whereQl.append("Filter (?").append(object).append(comp).append(" ");
        whereQl.append(convertValue(where));
        whereQl.append(")\n");
    }


    private String convertValue(Assignable value) throws QueryException {
        String dataValue = "\"" + value.getValue() + "\"";
        if (value.getDataType() == null)
            return dataValue;
        else
            return value + value.getDataType().getIri();
    }


    /**
     * Resolves a query $ vaiable value using the query request argument map
     *
     * @param value        the $alias is the query definition
     * @param queryRequest the Query request object submitted via the API
     * @return the value of the variable as a String
     * @throws QueryException if the variable is unresolvable
     */
    public static String resolveReference(String value, QueryRequest queryRequest) throws QueryException {
        if (value.equalsIgnoreCase("$referenceDate")) {
            if (null != queryRequest.getReferenceDate())
                return queryRequest.getReferenceDate();
        }
        value = value.replace("$", "");
        if (null != queryRequest.getArgument()) {
            for (Argument argument : queryRequest.getArgument()) {
                if (argument.getParameter().equals(value)) {
                    if (null != argument.getValueData())
                        return argument.getValueData();
                    else if (null != argument.getValueIri())
                        return argument.getValueIri().getIri();
                    else if (null != argument.getValueIriList()) {
                        if (argument.getValueIriList().isEmpty())
                            throw new QueryException("Argument parameter " + value + " valueIriList cannot be empty");
                        return argument.getValueIriList().stream().map(TTIriRef::getIri).collect(Collectors.joining(","));
                    } else if (null != argument.getValueVariable()) {
                        return argument.getValueVariable();
                    } else if (null != argument.getValueDataList()) {
                        if (argument.getValueDataList().isEmpty())
                            throw new QueryException("Argument parameter " + value + " valueDataList cannot be empty");
                        return String.join(",", argument.getValueDataList());
                    } else if (null != argument.getValueObject()) {
                        return argument.getValueObject().toString();
                    }
                }
            }
        }
        return value;
    }


    private void convertReturn(StringBuilder selectQl, StringBuilder whereQl, Return aReturn) {
        if (aReturn.getNodeRef() != null)
            selectQl.append(" ?").append(aReturn.getNodeRef());
        else if (aReturn.getAs() != null) {
            selectQl.append(" ?").append(aReturn.getAs());
        }
        if (aReturn.getProperty() != null) {
            for (ReturnProperty path : aReturn.getProperty()) {
                addOptionalPath(selectQl, whereQl, aReturn, path);
            }
        }
    }

    private void addOptionalPath(StringBuilder selectQl, StringBuilder whereQl, Return aReturn,
                                 ReturnProperty path) {
        String inverse = path.isInverse() ? "^" : "";
        if (path.getPropertyRef() != null) {
            selectQl.append(" ").append(inverse).append(path.getPropertyRef());
        } else if (path.getAs() != null) {
            selectQl.append(" ").append(inverse).append("?").append(path.getAs());
        }
        ;
        whereQl.append("OPTIONAL {");
        if (aReturn.getAs() != null)
            whereQl.append(" ?").append(aReturn.getAs());
        else if (aReturn.getNodeRef() != null)
            whereQl.append(" ?").append(aReturn.getNodeRef());
        else
            whereQl.append(" ?").append(mainEntity);
        if (path.getIri() != null) {
            whereQl.append(" ").append(inverse).append(iriFromString(path.getIri()));
        } else
            whereQl.append(" ").append(inverse).append("?").append(path.getPropertyRef());
        String object = null;
        if (path.getReturn() != null) {
            if (path.getReturn().getAs() != null)
                object = path.getReturn().getAs();
            else if (path.getReturn().getNodeRef() != null)
                object = path.getReturn().getNodeRef();
            else if (path.getValueRef() != null) {
                object = path.getValueRef();
                path.getReturn().setAs(path.getValueRef());
            } else if (path.getAs() != null) {
                object = path.getAs();
                path.getReturn().setAs(path.getAs());
            }
        } else {
            if (path.getValueRef() != null)
                object = path.getValueRef();
            else if (path.getAs() != null)
                object = path.getAs();
        }
        if (object == null) {
            o++;
            object = "o" + o;
            path.setValueRef(object);
            if (path.getReturn() != null) {
                path.getReturn().setAs(object);
            }
        }
        whereQl.append(" ?").append(object).append(".\n");
        if (path.getReturn() != null) {
            convertReturn(selectQl, whereQl, path.getReturn());
        } else {
            if (!selectQl.toString().contains(object)) selectQl.append(" ?").append(object);
            if (labelVariable == null)
                if (path.getIri().equals(RDFS.LABEL))
                    labelVariable = object;
        }
        whereQl.append("}\n");
    }


    private void orderGroupLimit(StringBuilder selectQl, Query clause) throws QueryException {
        if (null != queryRequest.getTextSearch()) {
            selectQl.append("ORDER BY DESC(").append("strstarts(lcase(?").append(labelVariable)
                    .append("),\"").append(escape(queryRequest.getTextSearch()).split(" ")[0])
                    .append("\")) ASC(strlen(?").append(labelVariable).append("))\n");
        }
        if (null != clause.getGroupBy()) {
            selectQl.append("Group by ");
            for (PropertyRef property : clause.getGroupBy()) {
                if (property.getVariable() != null) {
                    selectQl.append(" ?").append(property.getVariable());
                }
            }
        }
        if (null != clause.getOrderBy()) {
            if (null != clause.getOrderBy().getProperty()) {
                selectQl.append("Order by ");
                OrderDirection order = clause.getOrderBy().getProperty();
                if (null != order.getDirection() && order.getDirection().equals(Order.descending))
                    selectQl.append("DESC(");
                else
                    selectQl.append("ASC(");
                if (null != order.getIri()) selectQl.append("?").append(order.getIri());
                else if (null != order.getValueVariable()) selectQl.append("?").append(order.getValueVariable());
                else throw new QueryException("Order by missing identifier: iri / valueVariable");
                selectQl.append(")");
                if (null == queryRequest.getPage() && clause.getOrderBy().getLimit() > 0) {
                    selectQl.append("LIMIT ").append(clause.getOrderBy().getLimit()).append("\n");
                } else {
                    selectQl.append("\n");
                }
            }
        }
        if (null != queryRequest.getPage()) {
            selectQl.append("LIMIT ").append(queryRequest.getPage().getPageSize()).append("\n");
            if (queryRequest.getPage().getPageNumber() > 0)
                selectQl.append("OFFSET ").append((queryRequest.getPage().getPageNumber()) * (queryRequest.getPage().getPageSize())).append("\n");
        }

    }


    public String iriFromAlias(Element alias) throws QueryException {
        return getIriFromAlias(alias.getIri(), alias.getParameter(), alias.getVariable(), alias.getRef());
    }

    private String getIriFromAlias(String id, String parameter, String variable, String valueRef) throws QueryException {
        if (null == id) {
            if (null != parameter) {
                return iriFromString(resolveReference(parameter, queryRequest));
            } else if (null != variable) {
                return "?" + variable;
            } else if (null != valueRef)
                return "?" + valueRef;
            throw new QueryException("Type has no iri or variable or parameter variable");
        } else
            return (iriFromString(id));
    }


    public String iriFromAlias(Node alias) throws QueryException {
        return getIriFromAlias(alias.getIri(), alias.getParameter(), alias.getVariable(), alias.getRef());
    }


    public static String iriFromString(String iri) {
        if (iri.contains(",")) {
            iri = iri.replaceAll(",", ">,<");
            iri = "<" + iri + ">";
            return iri;
        }
        if (iri.startsWith("http") || iri.startsWith("urn:"))
            return "<" + iri + ">";
        else return iri;
    }


    public String getUpdateSparql() throws QueryException {


        StringBuilder updateQl = new StringBuilder();
        updateQl.append(getDefaultPrefixes());
        StringBuilder whereQl = new StringBuilder();
        whereQl.append("WHERE {");
        for (Match match : update.getMatch()) {
            match(whereQl, "entity", match);
        }
        if (update.getDelete() != null) {
            updateQl.append("DELETE { ");
            for (Delete delete : update.getDelete()) {
                delete(updateQl, whereQl, delete);
            }
            updateQl.append("}");
        }
        updateQl.append("\n");

        whereQl.append("}");

        updateQl.append(whereQl).append("\n");
        return updateQl.toString();

    }


    private void delete(StringBuilder updateQl, StringBuilder whereQl, Delete delete) throws QueryException {
        if (delete.getSubject() == null)
            updateQl.append("?").append("entity");
        if (delete.getPredicate() != null) {
            updateQl.append(" ").append(iriFromAlias(delete.getPredicate()));
            if (delete.getObject() != null) {
                updateQl.append(" ").append(iriFromAlias(delete.getObject())).append(".\n");
            } else {
                o++;
                updateQl.append(" ").append("?object").append(o).append(".\n");
                whereQl.append("?").append("entity").append(" ").append(iriFromAlias(delete.getPredicate())).append(" ").append(o).append("\n");
            }
        } else {
            o++;
            updateQl.append(" ").append("?predicate").append(o).append(" ?object").append(o).append(".\n");
            whereQl.append("?").append("entity").append(" ?predicate").append(o).append(" ?object").append(o).append(".\n");
        }
    }

}
