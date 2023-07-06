package org.endeavourhealth.imapi.dataaccess;

import org.apache.commons.lang3.StringUtils;
import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.model.tripletree.TTContext;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.queryengine.QueryValidator;
import org.endeavourhealth.imapi.transforms.SetToSparql;
import org.endeavourhealth.imapi.vocabulary.*;

import javax.xml.bind.DatatypeConverter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;

public class SparqlConverter {
	private Query query;
	private Update update;
	private final QueryRequest queryRequest;
	private final String tabs="";
	private TTContext context;
	int o=0;


	String mainEntity;



	public SparqlConverter(QueryRequest queryRequest) throws QueryException {
		this.queryRequest= queryRequest;
		context= queryRequest.getAsContext();
		if (queryRequest.getQuery()!=null) {
			this.query = queryRequest.getQuery();
			new QueryValidator().validateQuery(this.query);
		}
		else
			this.update= queryRequest.getUpdate();

	}


	/**
	 * Takes an IMQ select query model and converts to SPARQL
	 * @return String of SPARQL
	 **/
	public String getSelectSparql(Set<TTIriRef> statusFilter) throws QueryException {

		StringBuilder selectQl = new StringBuilder();
		selectQl.append(getDefaultPrefixes());
		if (null != queryRequest.getTextSearch()){
			selectQl.append("PREFIX con-inst: <http://www.ontotext.com/connectors/lucene/instance#>\n")
				.append("PREFIX con: <http://www.ontotext.com/connectors/lucene#>\n");
		}

		selectQl.append("SELECT ");
		selectQl.append("distinct ");

		StringBuilder whereQl = new StringBuilder();
		whereQl.append("WHERE {");
		mainEntity= query.getMatch().get(0).getVariable();

		if (null != queryRequest.getTextSearch()){
			textSearch(whereQl);

		}

		for (Match match :query.getMatch()){
				match(whereQl,mainEntity, match);
		}

		if (query.getReturn()!=null) {
			for (Return aReturn : query.getReturn()) {
				convertReturn(selectQl, whereQl, aReturn);
			}
		}
		o++;
		String statusVar= "status"+o;


		if (null != statusFilter && 0 != statusFilter.size()) {
				List<String> statusStrings = new ArrayList<>();
				for (TTIriRef status : statusFilter) {
					statusStrings.add("<" + status.getIri() + ">");
				}
				whereQl.append("?").append(mainEntity).append(" im:status ?").append(statusVar).append(".\n");
				whereQl.append("Filter (?").append(statusVar).append(" in(").append(String.join(",", statusStrings)).append("))\n");
			}
		if (query.isActiveOnly()) {
					whereQl.append("?").append(mainEntity).append(" im:status im:Active.\n");
		}


		selectQl.append("\n");

		whereQl.append("}");

		selectQl.append(whereQl).append("\n");
		orderGroupLimit(selectQl,query);
		return selectQl.toString();

	}



	public static String getDefaultPrefixes(){
		TTContext prefixes= new TTContext();
		StringBuilder sparql= new StringBuilder();
		prefixes.add(RDFS.NAMESPACE,"rdfs");
		sparql.append("PREFIX rdfs: <"+ RDFS.NAMESPACE+">\n");
		prefixes.add(RDF.NAMESPACE,"rdf");
		sparql.append("PREFIX rdf: <"+ RDF.NAMESPACE+">\n");
		prefixes.add(IM.NAMESPACE,"im");
		sparql.append("PREFIX im: <"+ IM.NAMESPACE+">\n");
		prefixes.add(XSD.NAMESPACE,"xsd");
		sparql.append("PREFIX xsd: <"+ XSD.NAMESPACE+">\n");
		prefixes.add(SNOMED.NAMESPACE,"sn");
		sparql.append("PREFIX sn: <"+ SNOMED.NAMESPACE+">\n");
		prefixes.add(SHACL.NAMESPACE,"sh");
		sparql.append("PREFIX sh: <").append(SHACL.NAMESPACE).append(">\n\n");
		return sparql.toString();
	}


	private void textSearch(StringBuilder whereQl) {
		String text= queryRequest.getTextSearch();
		whereQl.append("[] a con-inst:im_fts;\n")
			.append("       con:query \"label:");
		String[] words= text.split(" ");
		for (int i=0; i<words.length; i++){
			boolean fuzzy = false;
			words[i]= words[i]+ ((!fuzzy) ?"*" : "~");
		}
		String searchText= String.join(" && ",words);
		whereQl.append("(").append(searchText).append(")").append("\" ;\n");
		whereQl.append("       con:entities ?").append(mainEntity).append(".\n");
	}




	private void match(StringBuilder whereQl, String subject, Match match) throws QueryException {
		if (match.isExclude()) {
			whereQl.append(tabs).append(" FILTER NOT EXISTS {\n");
		}
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
					match(whereQl, subject, match.getMatch().get(i));
					whereQl.append("}\n");
				}
			}
			else {
				for (Match subMatch : match.getMatch()) {
					match(whereQl, subject, subMatch);
				}
			}
		}

		o++;
		String object = "object" + o;
		if (match.getType() != null) {
			type(whereQl, match, subject);
		}
		else {
			if (match.getIri() != null || match.getParameter() != null) {
				String inList = iriFromAlias((match));
				if (match.isDescendantsOrSelfOf()) {
					o++;
					whereQl.append("?").append(subject).append(" im:isA ?").append(object).append(".\n");
					whereQl.append("Filter (?").append(object);
					if (!inList.contains(","))
						whereQl.append(" =").append(inList).append(")\n");
					else
						whereQl.append(" in (").append(inList).append("))\n");

				}
				else if (match.isDescendantsOf()) {
					o++;
					whereQl.append("?").append(subject).append(" im:isA ?").append(object).append(".\n");
					whereQl.append("Filter (?").append(object);
					if (!inList.contains(","))
						whereQl.append(" =").append(inList).append(")\n");
					else
						whereQl.append(" in (").append(inList).append("))\n");
					whereQl.append("Filter (?").append(subject).append("!= ").append(iriFromAlias(match)).append(")\n");

				}
				else if (match.isAncestorsOf()) {
					o++;
					whereQl.append("?").append(subject).append(" ^im:isA ?").append(object).append(".\n");
					whereQl.append("Filter (?").append(object);
					if (!inList.contains(","))
						whereQl.append(" =").append(inList).append(")\n");
					else
						whereQl.append(" in (").append(inList).append("))\n");
				}
				else {
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

		if (match.getProperty()!=null) {
			if (match.getBool() == Bool.or) {
				for (int i = 0; i < match.getProperty().size(); i++) {
					if (i == 0)
						whereQl.append("{ \n");
					else
						whereQl.append("UNION {\n");
					property(whereQl, subject, match.getProperty().get(i),null);
					whereQl.append("}\n");
				}
			}
			else{
				for (Property where : match.getProperty()) {
					property(whereQl, subject, where,null);
				}
			}
		}
		if (match.getIn()!=null){
			in(whereQl,subject,match.getIn(),false);
		}
		if (match.getGraph()!=null) {
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
		}
		else if(type.isDescendantsOf()){
			o++;
			whereQl.append("?").append(subject).append(" im:isA ?").append("supertype").append(o).append(".\n");
			whereQl.append("?supertype").append(o).append(" rdf:type").append(String.join(",", iriFromAlias(type))).append(".\n");
			whereQl.append("Filter (?").append(subject).append("!= ").append(iriFromAlias(type)).append(")\n");
		}
		else {
			whereQl.append("?").append(subject).append(" rdf:type ").append(iriFromAlias(type)).append(".\n");
		}
	}




	/**
	 * Proecesses the where property clause, the remaining match clause including subqueries
	 * @param whereQl the string builder sparql
	 * @param subject the parent subject passed to this where clause
	 * @param where the where clause
	 */
	private void property(StringBuilder whereQl, String subject, Property where, String parentVariable) throws QueryException, QueryException {
		String propertyVariable= where.getVariable()!=null ?where.getVariable() : parentVariable;
		if (where.isAnyRoleGroup()) {
			whereQl.append("?").append(subject).append(" im:roleGroup ").append("?roleGroup").append(o).append(".\n");
			subject = "roleGroup" + o;
			o++;
		}
		if (where.getIri()!=null||where.getParameter()!=null) {
			o++;
			String inverse = where.isInverse() ? "^" : "";
			String property;
			if (propertyVariable == null) {
				property = "p" + o;
			}
			else
				property = propertyVariable;
			if (where.getParameter() != null) {
				property = iriFromAlias(where);
			}
			where.setVariable(property);
			String object;
			if (where.getMatch()!=null)
				object= where.getMatch().getVariable();
			else if (where.getValueVariable()!=null)
				object= where.getValueVariable();
			else {
				o++;
				object= "o"+o;
			}
			if (where.getIn() != null) {
				if (where.getIn().get(0).getVariable() != null) {
					object = where.getIn().get(0).getVariable();
				}
			}
			if (where.isDescendantsOrSelfOf()) {
				whereQl.append("?").append(property).append(" im:isA ").append(iriFromString(where.getIri())).append(".\n");
				whereQl.append("?").append(subject).append(" ").append(inverse).append("?").append(property).append(" ?").append(object).append(".\n");
			}
			else {
				if (propertyVariable != null) {
					whereQl.append("?").append(subject).append(" ").append(inverse).append("?").append(property).append(" ?").append(object).append(".\n");
					whereQl.append("filter (?").append(property).append(" = ").append(iriFromString(where.getIri())).append(")\n");
				}
				else
					whereQl.append("?").append(subject).append(" ").append(inverse).append(iriFromString(where.getIri())).append(" ?").append(object).append(".\n");
			}
			if (where.getIn() != null) {
				in(whereQl, object, where.getIn(), false);
			}
			if (where.getNotIn() != null) {
				in(whereQl, object, where.getNotIn(), true);
			}
			else if (where.getValue() != null) {
				whereValue(whereQl, object, where);
			}
			else if (where.getMatch()!=null){
				match(whereQl,object,where.getMatch());
			}
		}
			if (where.getProperty()!=null){
				if (where.getBool() == Bool.or) {
					for (int i = 0; i < where.getProperty().size(); i++) {
						if (i == 0)
							whereQl.append("{ \n");
						else
							whereQl.append("UNION {\n");
						property(whereQl, subject, where.getProperty().get(i),null);
						whereQl.append("}\n");
				}
			}
			else{
				for (Property subWhere : where.getProperty()) {
					property(whereQl, subject, subWhere,null);
				}
			}
		}
	}



	private void in(StringBuilder whereQl, String object, List<Node> in, boolean isNot) throws QueryException {
		String not= isNot ?" not " : "";
		boolean subTypes= false;
		boolean superTypes= false;
		for (Element item:in){
			if (item.isDescendantsOrSelfOf()) {
				subTypes = true;
				break;
			}
		}
		for (Element item:in){
			if (item.isAncestorsOf()) {
				superTypes = true;
				break;
			}
		}
		if (subTypes){
			String superObject= "super"+object;
			whereQl.append("?").append(object).append(" im:isA ?").append(superObject).append(".\n");
			whereQl.append("Filter (?").append(superObject).append(not).append(" in (");
		}
		else 	if (superTypes){
			String subObject= "sub"+object;
			whereQl.append("?").append(subObject).append(" im:isA ?").append(object).append(".\n");
			whereQl.append("Filter (?").append(subObject).append(not).append(" in (");
		}
		else {
			whereQl.append("Filter (?").append(object).append(not).append(" in (");
		}
		if (in.size()==1&& in.get(0).getSet()!=null) {
			try {
				String expansion = new SetToSparql().getExpansionSparql(object, in.get(0).getSet());
				if (!expansion.equals("")) {
					whereQl.append(expansion);
					whereQl.append("))\n");
				}
			}
			catch (DataFormatException e){
				throw new QueryException(e.getMessage());
			}

		}
		else {
			List<String> inList= new ArrayList<>();
			for (Element iri:in){
				inList.add(iriFromAlias(iri));
			}
			String inString = String.join(",", inList);
			whereQl.append(inString);
			whereQl.append("))\n");
		}
	}

	private void whereValue(StringBuilder whereQl, String object, Property where) throws QueryException {
		String comp= where.getOperator().getValue();
		String value= where.getValue();
		whereQl.append("Filter (?").append(object).append(comp).append(" ");
		whereQl.append(convertValue(value));
		whereQl.append(")\n");
	}


	private String convertValue(String value) throws QueryException {
		if (StringUtils.isNumeric(value))
			return value;
		else {
			try {
				DatatypeConverter.parseDateTime(value);
				if (!value.contains("^^xsd"))
					value=value+"^^xsd:dateTime";
				return value;
			} catch (IllegalArgumentException e)
			{
				throw new QueryException("Invalid value "+ value+
					".Value was tested for number and xsd date time format");
			}

		}

	}

	/**
	 * Resolves a query $ vaiable value using the query request argument map
	 * @param value  the $alias in the query definition
	 * @param queryRequest the Query request object submitted via the API
	 * @return the value of the variable as a String
	 * @throws QueryException if the variable is unresolvable
	 */
	public static String resolveReference(String value,QueryRequest queryRequest) throws QueryException {

			if (value.equalsIgnoreCase("$referenceDate")) {
				if (null != queryRequest.getReferenceDate())
					return queryRequest.getReferenceDate();
			}
			value = value.replace("$", "");
			if (null != queryRequest.getArgument()) {
					for (Argument argument: queryRequest.getArgument()) {
						if (argument.getParameter().equals(value)) {
							if (null != argument.getValueData())
								return argument.getValueData();
							else if (null != argument.getValueIri())
								return argument.getValueIri().getIri();
							else if (null!= argument.getValueIriList()){
								return argument.getValueIriList().stream().map(TTIriRef::getIri).collect(Collectors.joining(","));
							} else if (null != argument.getValueVariable()) {
								return argument.getValueVariable();
							} else if (null!= argument.getValueDataList()) {
								return String.join(",", argument.getValueDataList());
							} else if (null != argument.getValueObject()) {
								return argument.getValueObject().toString();
							}
						}
					}
					throw new QueryException("Query Variable "+ value+" has not been assigned in the request");
			}
			else
					throw new QueryException("Query needs variable "+ value+" but this has not been assigned in the request");
	}


	private String getAs(String variable,String alias){
		if (alias!=null)
			return (" (?")+variable+" as ?"+alias+")";
		else
			return "?"+variable;
	}

	private void convertReturn(StringBuilder selectQl, StringBuilder whereQl,Return aReturn){
		String variable= aReturn.getNodeRef();
		if (variable!=null)
			selectQl.append(" ").append(getAs(aReturn.getNodeRef(),aReturn.getAs()));
		if (aReturn.getProperty()!=null){
			for (ReturnProperty path:aReturn.getProperty()) {
				String inverse= path.isInverse() ? "^": "";
				if (path.getPropertyRef() != null) {
					selectQl.append(" ").append(inverse).append(getAs(path.getPropertyRef(), path.getAs()));
				}
				if (path.getNode() != null) {
					if (path.getNode().getNodeRef() == null) {
						addOptionalPath(selectQl, whereQl, aReturn, path);
					}
					else
						convertReturn(selectQl, whereQl, path.getNode());
				}
				else {
					if (path.getValueVariable() == null) {
						o++;
						String objectVariable = "o" + o;
						path.setValueVariable(objectVariable);
						addOptionalProperty(selectQl, whereQl, aReturn.getNodeRef(), path.getIri(), objectVariable,path.getAs());
					}
					else{
						selectQl.append(" ?").append(path.getValueVariable());
					}
				}
			}
		}
	}
	private void addOptionalPath(StringBuilder selectQl, StringBuilder whereQl,Return aReturn,
															 ReturnProperty path){
		String inverse= path.isInverse() ? "^": "";
		whereQl.append("OPTIONAL {");
		whereQl.append(" ?").append(aReturn.getNodeRef());
		if (path.getIri()!=null){
			whereQl.append(" ").append(inverse).append(iriFromString(path.getIri()));
		}
		else
			whereQl.append(" ").append(inverse).append("?").append(path.getPropertyRef());
		if (path.getNode()!=null){
			if (path.getNode().getNodeRef()==null){
				o++;
				path.getNode().setNodeRef("o"+o);
			}
			whereQl.append(" ?").append(path.getNode().getNodeRef()).append(".\n");
			convertReturn(selectQl,whereQl,path.getNode());
		}
		else {
			o++;
			String object="o"+o;
			whereQl.append(" ?").append(object).append(".\n");
			selectQl.append(" ").append(getAs(object,path.getAs()));
		}
		whereQl.append("}\n");
	}

private void addOptionalProperty(StringBuilder selectQl, StringBuilder whereQl,String subject,
																 String predicate,String objectVariable,String alias){

		selectQl.append(" ").append(getAs(objectVariable,alias));
		whereQl.append("OPTIONAL {");
		whereQl.append(" ?").append(subject);
		whereQl.append(" ").append(iriFromString(predicate));
		whereQl.append(" ?").append(objectVariable).append(".");
		whereQl.append("}\n");
	}


	private void orderGroupLimit(StringBuilder selectQl,Query clause){
		if (null != queryRequest.getTextSearch()){
			if (clause.getReturn()==null){
				clause.return_(s->s.property(p->p.setIri(RDFS.LABEL.getIri()).setPropertyRef("label")));
			}
			selectQl.append("ORDER BY DESC(").append("strstarts(lcase(?").append("label")
					.append("),\"").append(queryRequest.getTextSearch().split(" ")[0])
					.append("\")) ASC(strlen(?").append("label").append("))\n");
		}
		if (null != clause.getGroupBy()){
			selectQl.append("Group by ");
			for (PropertyRef property:clause.getGroupBy()){
				if (property.getVariable()!=null) {
					selectQl.append(" ?").append(property.getVariable());
				}
			}
		}
		if (null != clause.getOrderBy()) {
			selectQl.append("Order by ");
			for (OrderLimit order : clause.getOrderBy()) {
				if (order.getDirection().equals(Order.descending))
					selectQl.append("DESC(");
				else
					selectQl.append("ASC(");
				selectQl.append("?").append(order.getIri());
				selectQl.append(")");
				if (order.getLimit() > 0) {
					selectQl.append("LIMIT ").append(order.getLimit()).append("\n");
				}
			}
		}
		else if (null != queryRequest.getPage()) {
			selectQl.append("LIMIT ").append(queryRequest.getPage().getPageSize()).append("\n");
			if (queryRequest.getPage().getPageNumber() > 1)
				selectQl.append("OFFSET ").append((queryRequest.getPage().getPageNumber() - 1) * (queryRequest.getPage().getPageSize())).append("\n");
		}

	}








	public String iriFromAlias(Element alias) throws QueryException {
		return getIriFromAlias(alias.getIri(), alias.getParameter(), alias.getVariable());
	}

	private String getIriFromAlias(String id, String parameter, String variable) throws QueryException {
		if (null == id) {
			if (null!= parameter){
				return iriFromString(resolveReference(parameter, queryRequest));
			}
			else if (null != variable) {
				return "?" + variable;
			}
			else
				throw new QueryException("Type has no iri or variable or parameter variable");
		}
		else
			return (iriFromString(id));
	}


	public String iriFromAlias(Node alias) throws QueryException {
		if (null!=alias.getType()){
			return iriFromString(alias.getType());
		}
		return getIriFromAlias(alias.getIri(), alias.getParameter(), alias.getVariable());
	}



	public static String iriFromString(String iri){
			if (iri.contains(",")){
				iri= iri.replaceAll(",",">,<");
				iri= "<"+ iri+">";
				return iri;
			}
		if (iri.startsWith("http")||iri.startsWith("urn:"))
			return "<"+ iri+">";
		else return iri;
	}



	public String getUpdateSparql() throws QueryException {


		StringBuilder updateQl = new StringBuilder();
		updateQl.append(getDefaultPrefixes());
		StringBuilder whereQl = new StringBuilder();
		whereQl.append("WHERE {");
		for (Match match :update.getMatch()){
			match(whereQl,"entity", match);
		}
		if (update.getDelete()!=null){
			updateQl.append("DELETE { ");
			for (Delete delete:update.getDelete()){
				delete(updateQl,whereQl, delete);
			}
			updateQl.append("}");
		}
		updateQl.append("\n");

		whereQl.append("}");

		updateQl.append(whereQl).append("\n");
		return updateQl.toString();

	}


	private void delete(StringBuilder updateQl, StringBuilder whereQl, Delete delete) throws QueryException {
			if (delete.getSubject()==null)
				updateQl.append("?").append("entity");
			if (delete.getPredicate()!=null){
				updateQl.append(" ").append(iriFromAlias(delete.getPredicate()));
				if (delete.getObject()!=null){
					updateQl.append(" ").append(iriFromAlias(delete.getObject())).append(".\n");
				}
				else {
					o++;
					updateQl.append(" ").append("?object").append(o).append(".\n");
					whereQl.append("?").append("entity").append(" ").append(iriFromAlias(delete.getPredicate())).append(" ").append(o).append("\n");
				}
			}
			else {
				o++;
				updateQl.append(" ").append("?predicate").append(o).append(" ?object").append(o).append(".\n");
				whereQl.append("?").append("entity").append(" ?predicate").append(o).append(" ?object").append(o).append(".\n");
			}
		}

}
