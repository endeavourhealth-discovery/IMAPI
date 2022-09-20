package org.endeavourhealth.imapi.dataaccess;

import org.apache.commons.lang3.StringUtils;
import org.endeavourhealth.imapi.model.iml.*;
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
	private final Query query;
	private final QueryRequest queryRequest;
	private String tabs="";
	int o=0;

	public SparqlConverter(QueryRequest queryRequest) {
		this.queryRequest= queryRequest;
		this.query= queryRequest.getQuery();
	}


	/**
	 * Takes an IMQ select query model and converts to SPARQL
	 * @return String of SPARQL
	 **/
	public String getSelectSparql() throws DataFormatException {

		StringBuilder selectQl = new StringBuilder();
		selectQl.append(getDefaultPrefixes());
		if (queryRequest.getTextSearch()!=null){
			selectQl.append("PREFIX con-inst: <http://www.ontotext.com/connectors/lucene/instance#>\n")
				.append("PREFIX con: <http://www.ontotext.com/connectors/lucene#>\n");
		}
		if (query.isUsePrefixes())
			query.setPrefix(TTManager.getDefaultContext());

		selectQl.append("SELECT ");
		selectQl.append("distinct ");
		selectQl.append("?entity ");

		StringBuilder whereQl = new StringBuilder();
		whereQl.append("WHERE {");

		if (queryRequest.getTextSearch()!=null){
			textSearch(whereQl);

		}


		if (query.getWith()!=null)
			with(whereQl,query.getWith());


		if (query.getWhere()!=null) {
			where(whereQl,  "entity", query.getWhere());
		}

		if (query.getSelect()!=null){
			for (Select select:query.getSelect())
				select(selectQl,whereQl,select,"entity");
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
		whereQl.append("       con:entities ?entity.\n");
	}



	private void with(StringBuilder whereQl, With with) throws DataFormatException {
		if (with.getInstance()!=null){
			TTAlias instance= with.getInstance();
			if (instance.isIncludeSubtypes()){
				o++;
				whereQl.append("?").append("entity").append(" im:isA ?").append("supertype").append(o).append(".\n");
				whereQl.append("Filter (?supertype").append(o).append("=").append(iriFromAlias(instance)).append(")\n");
			}
			else if (instance.isIncludeMembers()){
				o++;
				whereQl.append("?").append("entity").append(" ^im:hasMember ?").append("set").append(o).append(".\n");
				whereQl.append("Filter (?set").append(o).append("=").append(iriFromAlias(instance)).append(")\n");
			}
			else
				whereQl.append("?").append("entity ").append("=").append(iriFromAlias(instance)).append(".\n");
		}
		else if (with.getType()!=null){
			List<String> in= with.getType().stream().map(t-> {
				try {
					return iriFromAlias(t);
				} catch (DataFormatException e) {
					throw new RuntimeException(e);
				}
			}).collect(Collectors.toList());
			String inList= String.join(",",in);
			o++;
			String object="type"+o;
			whereQl.append("?").append("entity ").append(iriFromString(RDF.TYPE.getIri())).append(" ?").append(object).append(".\n");
			whereQl.append("Filter (?").append(object).append(" in (").append(inList).append("))\n");
		}
		else if (with.getQuery()!=null){
			QueryRequest withRequest= new QueryRequest();
			withRequest.setArgument(queryRequest.getArgument());
			withRequest.setQuery(with.getQuery());
			whereQl.append("{\n").append(new SparqlConverter(withRequest).getSelectSparql())
				.append("}\n");
		}

	}




	/**
	 * Constructs a where clause
	 * @param whereQl the where Clause thus far
	 * @param subject the SPARQL subject passed in - always starts with ?entity in the outer where
	 * @param where the where clause of the query
	 */
	private void where(StringBuilder whereQl, String subject,Where where) throws DataFormatException {
		if (where!=null) {
			if (where.getGraph() != null) {
				indent();
				whereQl.append("graph ").append(iriFromString(where.getGraph())).append(" { \n");
			}
			if (where.getNotExist() != null) {
				whereQl.append(tabs).append(" FILTER NOT EXISTS {\n");
				where(whereQl, subject, where.getNotExist());
				whereQl.append("}\n");
			} else {
				if (where.getAnd() != null) {
					for (Where and : where.getAnd()) {
						where(whereQl, subject, and);
					}
				} else if (where.getOr() != null) {
					for (int i = 0; i < where.getOr().size(); i++) {
						if (i == 0)
							whereQl.append("{ \n");
						else
							whereQl.append("UNION {\n");
						where(whereQl, subject, where.getOr().get(i));
					}
				} else {
					if (subject.equals("entity"))
						if (query.isActiveOnly())
							whereQl.append("?").append(subject).append(" im:status im:Active.\n");
					whereProperty(whereQl, subject, where);
				}
				if (where.getGraph() != null) {
					whereQl.append("}\n");
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
		String path = where.getProperty().getIri();
		String inverse= where.getProperty().isInverse() ? "^" : "";
		o++;
		String object= localName(path)+o;


		whereQl.append("?").append(subject).append(" ").append(inverse).append(iriFromString(path))
			.append(" ?").append(object).append(".\n");

		if (where.getWhere() != null) {
			where(whereQl, object, where.getWhere());
		}
		if (where.getIs() != null) {
			whereIs(whereQl, object,where.getIs(), where.isNot());
		}
		else if (where.getIn()!=null) {
			whereIn(whereQl,object,where.getIn(),where.isNot());
		}
		else if (where.getValue() != null) {
			whereValue(whereQl, object, where, where.isNot());
		}
	}

	private void whereIs(StringBuilder whereQl, String object, TTAlias is,boolean isNot) throws DataFormatException {
		String not = isNot ? "!" : "";
		if (is.isIncludeSubtypes()){
			o++;
			whereQl.append("?").append(object).append(" im:isA ?").append("supertype").append(o).append(".\n");
			whereQl.append("Filter (?supertype").append(o).append(not).append("=").append(iriFromAlias(is)).append(")\n");
		}
		else if (is.isIncludeSupertypes()){
			o++;
			whereQl.append("?").append(object).append(" ^im:isA ?").append("subtype").append(o).append(".\n");
			whereQl.append("Filter (?subtype").append(o).append(not).append("=").append(iriFromAlias(is)).append(")\n");

		}
		else if (is.isIncludeMembers()){
			o++;
			whereQl.append("?").append(object).append(" ^im:hasMember ?").append("set").append(o).append(".\n");
			whereQl.append("Filter (?set").append(o).append(not).append("=").append(iriFromAlias(is)).append(")\n");
		}
		else
			whereQl.append("Filter (?").append(object).append(not).append("=").append(iriFromAlias(is))
				.append(")\n");
	}


	private void whereIn(StringBuilder whereQl, String object, List<TTIriRef> in, boolean isNot) {

		String not= isNot ?" not " : "";
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

	private void whereValue(StringBuilder whereQl, String object, Where where, boolean isNot) throws DataFormatException {
		String not= isNot ?"!" : "";
		whereQl.append("Filter (?").append(object).append(not).append(where.getComparison()).append(" ");
		if (where.getValueVariable()!=null)
			whereQl.append(convertValue(resolveReference(where.getValueVariable(),queryRequest)));
		else
			whereQl.append(convertValue(where.getValue()));
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
		try {
			if (value.equalsIgnoreCase("$referenceDate")) {
				return queryRequest.getReferenceDate();
			}
			else {
				value = value.replace("$", "");
				if (queryRequest.getArgument().get(value) != null) {
					Object result = queryRequest.getArgument().get(value);
					if (result instanceof Map) {
						if (((Map<?, ?>) result).get("@id") != null)
							return (String) ((Map<?, ?>) result).get("@id");
					} else if (result instanceof Integer)
						return ((Integer) queryRequest.getArgument().get(value)).toString();
					else if (result instanceof Number)
						return String.valueOf(queryRequest.getArgument().get(value));
					else
						return (String) queryRequest.getArgument().get(value);
				} else
					throw new DataFormatException("unknown parameter variable " + value + ". ");
			}
		}
		catch (Exception e) {
			throw new DataFormatException("unknown parameter variable " + value);
		}
		throw new DataFormatException("unknown paramater variable "+ value);
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
			if (select.getProperty() != null) {
				TTAlias property= select.getProperty();
					o++;
					object = "p" + o;
					property.setAlias(object);
				String inverse= select.getProperty().isInverse() ?"^" : "";
				selectQl.append(" ?").append(object);
				whereQl.append(" OPTIONAL { ?").append(subject).append(" ").append(inverse).append(iriFromString(property.getIri()))
						.append(" ?").append(object).append(".");
				if (select.getWhere()!=null) {
					whereQl.append("\n");
					where(whereQl, object, select.getWhere());
				}
				if (select.getSelect()!=null){
					whereQl.append("\n");
					for (Select subSelect:select.getSelect()){
						select(selectQl,whereQl,subSelect,object);
					}
				}
				whereQl.append("}\n");

			}
		}

	private void orderGroupLimit(StringBuilder selectQl,QueryClause clause) throws DataFormatException {
		if (queryRequest.getTextSearch()!=null){
			String labelAlias= getLabelAlias(clause);
			if (labelAlias!=null)
				selectQl.append("ORDER BY DESC(").append("strstarts(lcase(?").append(labelAlias)
					.append("),\"").append(queryRequest.getTextSearch().split(" ")[0])
					.append("\")) ASC(strlen(?").append(labelAlias).append("))\n");
		}
		if (clause.getGroupBy()!=null){
			selectQl.append("Group by ");
			for (TTAlias property:clause.getGroupBy()){
				selectQl.append(" ?").append(getSelectAlias(clause,property.getIri()));
				selectQl.append(")");
			}
		}
		if (clause.getOrderBy()!=null){
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
		if (queryRequest.getPage()!=null) {
			selectQl.append("LIMIT ").append(queryRequest.getPage().getPageSize()).append("\n");
			if (queryRequest.getPage().getPageNumber() > 1)
				selectQl.append("OFFSET ").append((queryRequest.getPage().getPageNumber() - 1) * (queryRequest.getPage().getPageSize())).append("\n");
		}
		else if (clause.getLimit()!=null) {
			selectQl.append("LIMIT ").append(clause.getLimit()).append("\n");
		}
	}

	private String getLabelAlias(QueryClause clause) {
			for (Select select:clause.getSelect()){
				if (select.getProperty()!=null)
					if (select.getProperty().getIri().equals(RDFS.LABEL.getIri())){
						return select.getProperty().getAlias();
					}
				}
		return null;
	}


	private String getSelectAlias(QueryClause clause, String property) throws DataFormatException {
			for (Select select:clause.getSelect()) {
				if (select.getProperty().getIri().equals(property))
					return select.getProperty().getAlias();
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
			if (alias.getVariable()!=null){
				return iriFromString(resolveReference(alias.getVariable(),queryRequest));
			}
			else
				return (iriFromString(alias.getIri()));
	}

	public static String iriFromString(String iri){
		if (iri.startsWith("http")||iri.startsWith("urn:"))
			return "<"+ iri+">";
		else return iri;
	}



	private String localName(String iri){
		String del="#";
		if (!iri.contains("#"))
			del=":";
		String[] iriSplit= iri.split(del);
		return iriSplit[iriSplit.length-1];
	}



}
