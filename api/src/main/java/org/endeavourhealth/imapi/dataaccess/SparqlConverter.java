package org.endeavourhealth.imapi.dataaccess;

import org.apache.commons.lang3.StringUtils;
import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.model.tripletree.SourceType;
import org.endeavourhealth.imapi.model.tripletree.TTAlias;
import org.endeavourhealth.imapi.model.tripletree.TTContext;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.transforms.SetToSparql;
import org.endeavourhealth.imapi.transforms.TTManager;
import org.endeavourhealth.imapi.vocabulary.*;

import javax.xml.bind.DatatypeConverter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;

public class SparqlConverter {
	private Query query;
	private Update update;
	private final QueryRequest queryRequest;
	private String tabs="";
	private Set<String> aliases= new HashSet<>();
	int o=0;

	public SparqlConverter(QueryRequest queryRequest) {
		this.queryRequest= queryRequest;
		if (queryRequest.getQuery()!=null)
			this.query= queryRequest.getQuery();
		else
			this.update= queryRequest.getUpdate();
	}


	/**
	 * Takes an IMQ select query model and converts to SPARQL
	 * @return String of SPARQL
	 **/
	public String getSelectSparql(Set<TTIriRef> statusFilter) throws DataFormatException {
		validateQuery();

		StringBuilder selectQl = new StringBuilder();
		selectQl.append(getDefaultPrefixes());
		if (null != queryRequest.getTextSearch()){
			selectQl.append("PREFIX con-inst: <http://www.ontotext.com/connectors/lucene/instance#>\n")
				.append("PREFIX con: <http://www.ontotext.com/connectors/lucene#>\n");
		}
		if (query.isUsePrefixes())
			query.setContext(TTManager.getDefaultContext());

		selectQl.append("SELECT ");
		selectQl.append("distinct ");
		selectQl.append("?entity ");

		StringBuilder whereQl = new StringBuilder();
		whereQl.append("WHERE {");

		if (null != queryRequest.getTextSearch()){
			textSearch(whereQl);

		}


		froms(whereQl,"entity",query.getFrom());


		if (null != query.getSelect()){
			for (Select select:query.getSelect())
				select(selectQl,whereQl,select,"entity");
		}
			if (null != statusFilter && 0 != statusFilter.size()) {
				List<String> statusStrings = new ArrayList<>();
				for (TTIriRef status : statusFilter) {
					statusStrings.add("<" + status.getIri() + ">");
				}
				whereQl.append("Filter (?status in(" + String.join(",", statusStrings) + "))\n");
			}


		selectQl.append("\n");

		whereQl.append("}");

		selectQl.append(whereQl).append("\n");
		orderGroupLimit(selectQl,query);
		return selectQl.toString();

	}

	private void validateQuery() throws DataFormatException {
		if (null== query.getFrom())
			throw new DataFormatException("Query must have a from clause");
		if (null != query.getFrom()) {
				validateFrom(query.getFrom());
		}
		if (null != query.getSelect()){
			for (Select select: query.getSelect()){
				validateSelect(select);
			}
		}
	}

	private void validateFrom(From from) throws DataFormatException {
		if (from.getWhere()!=null)
			validateWhere(from.getWhere());
	}


	private void validateSelect(Select select) throws DataFormatException {
		if (null != select.getWhere()) {
				validateWhere(select.getWhere());
			}
	}

	private void validateWhere(Where where) throws DataFormatException {

		if (where.getWhere()!=null) {
			for (Where subWhere:where.getWhere())
				validateWhere(subWhere);
		}
		else {
			if (null == where.getIn()&& null== where.getValue()&&null== where.getId()
			&&null== where.getIri())
				throw new DataFormatException("Where clause must have a type,id, from ,or nested wheres");
		}
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
		whereQl.append("       con:entities ?entity.\n");
	}

	private void excludeSelf(StringBuilder whereQl,TTAlias alias) throws DataFormatException {
			if (alias.isExcludeSelf())
				whereQl.append("Filter (?entity!= "+ iriFromAlias(alias)+")\n");
	}



	private void from(StringBuilder whereQl, String subject,From from) throws DataFormatException {

		if (from.getSourceType()== SourceType.type){
			type (whereQl,from,subject);
		}
		else {
			if (from.getIri() != null || from.getVariable() != null) {
				String inList= iriFromAlias((from));
				if (from.isIncludeSubtypes()) {
					o++;
					whereQl.append("?").append(subject).append(" im:isA ?").append("supertype").append(o).append(".\n");
					whereQl.append("Filter (?supertype").append(o);
					if (!inList.contains(","))
						whereQl.append(" =").append(inList).append(")\n");
					else
						whereQl.append(" in (").append(inList).append("))\n");
					excludeSelf(whereQl, from);
				}
				else {
					whereQl.append("?").append(subject).append(" rdf:type ").append("?type").append(".\n");
					whereQl.append("Filter (?").append(subject);
					if (!inList.contains(","))
						whereQl.append(" =").append(inList).append(")\n");
					else
						whereQl.append(" in (").append(inList).append("))\n");
				}
			}
		}

	}

	private void type(StringBuilder whereQl, TTAlias type,String subject) throws DataFormatException {
		if (type.isIncludeSubtypes()) {
			o++;
			whereQl.append("?").append(subject).append(" im:isA ?").append("supertype").append(o).append(".\n");
			whereQl.append("?supertype").append(o).append(" rdf:type").append(String.join(",", iriFromAlias(type))).append(".\n");
			excludeSelf(whereQl, type);
		} else {
			whereQl.append("?").append(subject).append(" rdf:type ").append(iriFromAlias(type)).append(".\n");
		}
	}


	private void froms(StringBuilder whereQl, String subject,From from) throws DataFormatException {
		if (from.getGraph()!=null){
			whereQl.append(" graph ").append(iriFromAlias(from.getGraph())).append(" {");
		}

		if (from.getFrom()==null){
			from(whereQl,subject,from);
		}
		else {
			if (from.getBool() == Bool.or) {
				for (int i = 0; i < from.getFrom().size(); i++) {
					if (i == 0)
						whereQl.append("{ \n");
					else
						whereQl.append("UNION {\n");
					froms(whereQl, subject,from.getFrom().get(i));
					whereQl.append("}\n");
				}
			}
			else if (from.getBool() == Bool.not) {
				for (From not : from.getFrom()) {
					whereQl.append(tabs).append(" FILTER NOT EXISTS {\n");
					froms(whereQl, subject,not);
					whereQl.append("}\n");
				}
			}
			else {
				for (From subFrom : from.getFrom()) {
					froms(whereQl, subject,subFrom);
				}
			}
		}
		if (from.getWhere()!=null){
			where(whereQl,subject,from.getWhere());
		}
		if (from.getGraph()!=null) {
			whereQl.append("}");
		}
	}






	/**
	 * Constructs a where clause
	 * @param whereQl the where Clause thus far
	 * @param subject the SPARQL subject passed in - always starts with ?entity in the outer where
	 * @param match the match clause of the query
	 */
	private void where(StringBuilder whereQl, String subject,Where match) throws DataFormatException {
		if (match.getSourceType()==SourceType.type) {
			type(whereQl, match, subject);
		}

		if (match.getIri() != null) {
			whereProperty(whereQl, subject, match);
		}
		else if (match.getWhere()!=null) {
			subWhere(whereQl, subject, match);
		}
	}

	private void subWhere(StringBuilder whereQl, String subject, Where match) throws DataFormatException {
		if (match.getBool() == Bool.or) {
			for (int i = 0; i < match.getWhere().size(); i++) {
				if (i == 0)
					whereQl.append("{ \n");
				else
					whereQl.append("UNION {\n");
				where(whereQl, subject, match.getWhere().get(i));
				whereQl.append("}\n");
			}
		}
		else if (match.getBool() == Bool.not) {
			for (Where not : match.getWhere()) {
				whereQl.append(tabs).append(" FILTER NOT EXISTS {\n");
				where(whereQl, subject, not);
				whereQl.append("}\n");
			}
		}
		else {
			for (Where subMatch : match.getWhere()) {
				where(whereQl, subject, subMatch);
			}
		}
		if (subject.equals("entity")) {
			if (query != null) {
				if (query.isActiveOnly()) {
					whereQl.append("?").append(subject).append(" im:status im:Active.\n");
				}
			}
		}
	}


	/**
	 * Proecesses the where property clause, the remaining match clause including subqueries
	 * @param whereQl the string builder sparql
	 * @param subject the parent subject passed to this where clause
	 * @param where the where clause
	 */
	private void whereProperty(StringBuilder whereQl, String subject,Where where) throws DataFormatException {
			String object=null;
		  if (where.getIn()!=null){
				if (where.getIn().get(0).getAlias()!=null) {
					object = where.getIn().get(0).getAlias();
					aliases.add(object);
				}
			}
			if (object==null) {
				o++;
				object = "o" + o;
			}
			if (where.getIri().equals(IM.IS_A.getIri())) {
				whereIsa(whereQl, subject, where);
			}
			else {
				if (where.isAnyRoleGroup()){
					whereQl.append("?").append(subject).append(" im:roleGroup ").append("?roleGroup").append(o).append(".\n");
					subject= "roleGroup"+o;
					o++;
				}
				TTAlias property = where;
				String inverse = property.isInverse() ? "^" : "";
				if (where.getWhere() != null) {
					whereQl.append("?").append(subject).append(" ").append(inverse).append(iriFromAlias(property))
						.append("?").append(object).append(".\n");
					subWhere(whereQl, object, where);
				}
				else {
					List<From> propertyIn = where.getIn();


					if (!property.isIncludeSubtypes()) {
						whereQl.append("?").append(subject).append(" ").append(inverse).append(iriFromAlias(property))
							.append(" ?").append(object).append(".\n");
					}
					else {
						o++;
						whereQl.append("?").append(subject).append(" ").append(inverse).append("?p").append(o)
							.append(" ?").append(object).append(".\n");
						whereQl.append("?p").append(o).append(" im:isA ").append(iriFromAlias(property)).append(".\n");
					}
					if (where.getIn() != null) {
						whereIn(whereQl, object, where.getIn(), where.getBool() == Bool.not);
					}
					else if (where.getValue()!=null){
						whereValue(whereQl,subject,where,where.getBool()==Bool.not);
					}
				}
			}
	}


	private void whereIsa(StringBuilder whereQl, String object, Where where){
		boolean isNot= (where.getBool()==Bool.not);
		String not = isNot ? "!" : "";
		if (null != where.getIn()){
			List<From> in= where.getIn();
			whereQl.append("Filter (?").append(object).append(not).append(" in (");
			if (in.size()==1) {
				String expansion = new SetToSparql().getExpansionSparql(object, in.get(0).getIri());
				if (!expansion.equals("")) {
					whereQl.append(expansion);
				} else {
					List<String> inList =
						in.stream().map(iri -> iriFromString(iri.getIri())).collect(Collectors.toList());
					String inString = String.join(",", inList);
					whereQl.append(inString);
				}
			}
			whereQl.append("))\n");
		}


	}


	private void whereIn(StringBuilder whereQl, String object, List<From> in, boolean isNot) throws DataFormatException {
		String not= isNot ?" not " : "";
		boolean subTypes= false;
		boolean superTypes= false;
		for (TTAlias item:in){
			if (item.isIncludeSubtypes()) {
				subTypes= true;
			}
		}
		for (TTAlias item:in){
			if (item.isIncludeSupertypes()) {
				superTypes= true;
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
		if (in.size()==1&& in.get(0).getSourceType()==SourceType.set) {
				String expansion = new SetToSparql().getExpansionSparql(object, in.get(0).getIri());
				if (!expansion.equals("")) {
					whereQl.append(expansion);
					whereQl.append("))\n");
					return;
				}
		}
		else {
			List<String> inList= new ArrayList<>();
			for (TTAlias iri:in){
				inList.add(iriFromAlias(iri));
			}
			String inString = String.join(",", inList);
			whereQl.append(inString);
			whereQl.append("))\n");
		}
	}

	private void whereValue(StringBuilder whereQl, String object, Where where, boolean isNot) throws DataFormatException {
		String not= isNot ?"!" : "";
		String comp= where.getOperator().getValue();
		String value= where.getValue();
		whereQl.append("Filter (?").append(object).append(not).append(comp).append(" ");
		whereQl.append(convertValue(value));
		whereQl.append(")\n");
	}


	private String convertValue(String value) throws DataFormatException {
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
				throw new DataFormatException("Invalid value "+ value+
					".Value was tested for number and xsd date time format");
			}

		}

	}

	/**
	 * Resolves a query $ vaiable value using the query request argument map
	 * @param value  the $alias in the query definition
	 * @param queryRequest the Query request object submitted via the API
	 * @return the value of the variable as a String
	 * @throws DataFormatException if the variable is unresolvable
	 */
	public static String resolveReference(String value,QueryRequest queryRequest) throws DataFormatException {

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
								return String.join(",",
								argument.getValueIriList().stream().map(iri-> iri.getIri()).collect(Collectors.toList()));
							}
						}
					}
					throw new DataFormatException("Query Variable "+ value+" has not been assigned in the request");
			}
			else
					throw new DataFormatException("Query needs variable "+ value+" but this has not been assigned in the request");
	}



		/**
		 * Processes the dataSet clause and binds to the variables created in the where clause
		 * @param selectQl Sparql dataSet clause thus far
		 * @param select the select statement to process
		 * @param  whereQl the associated where clause
		 * @param subject the subject for the where clause
		 */
		private void select(StringBuilder selectQl, StringBuilder whereQl, Select select,
		String subject) throws DataFormatException {
			String object;
			if (null != select.getIri()) {
				TTAlias property= select;
				object= property.getAlias();
				if (null == object) {
					o++;
					object = "p" + o;
					property.setAlias(object);
				}
				String inverse= select.isInverse() ?"^" : "";
				selectQl.append(" ?").append(object);
				whereQl.append(" OPTIONAL { ?").append(subject).append(" ").append(inverse).append(iriFromString(property.getIri()))
						.append(" ?").append(object).append(".");
				if (null != select.getWhere()) {
					whereQl.append("\n");
					where(whereQl, object, select.getWhere());
				}
				if (null != select.getSelect()){
					whereQl.append("\n");
					for (Select subSelect:select.getSelect()){
						select(selectQl,whereQl,subSelect,object);
					}
				}
				whereQl.append("}\n");

			}
		}

	private void orderGroupLimit(StringBuilder selectQl,Query clause) throws DataFormatException {
		if (null != queryRequest.getTextSearch()){
			if (clause.getSelect()==null){
				clause.select(s->s.setIri(RDFS.LABEL.getIri()).setAlias("label"));
			}
			String labelAlias= getLabelAlias(clause);
			if (null != labelAlias)
				selectQl.append("ORDER BY DESC(").append("strstarts(lcase(?").append(labelAlias)
					.append("),\"").append(queryRequest.getTextSearch().split(" ")[0])
					.append("\")) ASC(strlen(?").append(labelAlias).append("))\n");
		}
		if (null != clause.getGroupBy()){
			selectQl.append("Group by ");
			for (TTAlias property:clause.getGroupBy()){
				selectQl.append(" ?").append(getSelectAlias(clause,property.getIri()));
				selectQl.append(")");
			}
		}
		if (null != clause.getOrderBy()){
			selectQl.append("Order by ");
			for (TTAlias property:clause.getOrderBy()){
				if (clause.getDirection().toLowerCase().startsWith("d"))
						selectQl.append("DESC(");
					else
						selectQl.append("ASC(");
					selectQl.append("?").append(getSelectAlias(clause,property.getIri()));
					selectQl.append(")");
				}
		}
		if (null != queryRequest.getPage()) {
			selectQl.append("LIMIT ").append(queryRequest.getPage().getPageSize()).append("\n");
			if (queryRequest.getPage().getPageNumber() > 1)
				selectQl.append("OFFSET ").append((queryRequest.getPage().getPageNumber() - 1) * (queryRequest.getPage().getPageSize())).append("\n");
		}
		else if (null != clause.getLimit()) {
			selectQl.append("LIMIT ").append(clause.getLimit()).append("\n");
		}
	}

	private String getLabelAlias(Query clause) {
			if (clause.getSelect()!=null) {
				for (Select select : clause.getSelect()) {
					if (null != select.getIri())
						if (select.getIri().equals(RDFS.LABEL.getIri())) {
							return select.getAlias();
						}
				}
			}
			return "label";
	}


	private String getSelectAlias(Query clause, String property) throws DataFormatException {
			for (Select select:clause.getSelect()) {
				if (select.getIri().equals(property))
					return select.getAlias();
			}
			throw new DataFormatException("Order property not in select clause");
	}




	private void indent(){
		tabs=tabs+"\t";
	}
	private void lessIndent(){
		if (!tabs.equals(""))
			tabs=tabs.substring(0,tabs.length()-1);
	}


	public String iriFromAlias(TTAlias alias) throws DataFormatException {
		if (null == alias.getIri()) {
			if (null != alias.getVariable()) {
				if (aliases.contains(alias.getVariable()))
					return "?"+alias.getVariable();
				else
					return iriFromString(resolveReference(alias.getVariable(), queryRequest));
			}
			else
				throw new DataFormatException("Type has no iri or variable");
		}
			else
				return (iriFromString(alias.getIri()));
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



	private String localName(String iri){
			String del=":";
			if (iri.contains("#"))
				del="#";
			else if (iri.startsWith("http"))
				del="/";
		String[] iriSplit= iri.split(del);
		return iriSplit[iriSplit.length-1];
	}


	public String getUpdateSparql() throws DataFormatException {


		StringBuilder updateQl = new StringBuilder();
		updateQl.append(getDefaultPrefixes());
		StringBuilder whereQl = new StringBuilder();
		whereQl.append("WHERE {");
		froms(whereQl,"entity",update.getFrom());
		if (update.getDelete()!=null){
			updateQl.append("DELETE { ");
			for (Delete delete:update.getDelete()){
				delete(updateQl,whereQl,"entity",delete);
			}
			updateQl.append("}");
		}
		updateQl.append("\n");

		whereQl.append("}");

		updateQl.append(whereQl).append("\n");
		return updateQl.toString();

	}


	private void delete(StringBuilder updateQl,StringBuilder whereQl,String subject,Delete delete) throws DataFormatException {
			if (delete.getSubject()==null)
				updateQl.append("?").append(subject);
			if (delete.getPredicate()!=null){
				updateQl.append(" ").append(iriFromAlias(delete.getPredicate()));
				if (delete.getObject()!=null){
					updateQl.append(" ").append(iriFromAlias(delete.getObject())).append(".\n");
				}
				else {
					o++;
					updateQl.append(" ").append("?object").append(o).append(".\n");
					whereQl.append("?").append(subject).append(" ").append(iriFromAlias(delete.getPredicate())).append(" ").append(o).append("\n");
				}
			}
			else {
				o++;
				updateQl.append(" ").append("?predicate").append(o).append(" ?object").append(o).append(".\n");
				whereQl.append("?").append(subject).append(" ?predicate").append(o).append(" ?object").append(o).append(".\n");
			}
		}

}
