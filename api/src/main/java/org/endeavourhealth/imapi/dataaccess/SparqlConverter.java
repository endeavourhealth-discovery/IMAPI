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


		if (null != query.getFrom())
			from(whereQl,query.getFrom());

		if (null != query.getWhere()) {
			where(whereQl,  "entity", query.getWhere());
		}

		if (null != query.getSelect()){
			for (Select select:query.getSelect())
				select(selectQl,whereQl,select,"entity");
		}

		if (null != statusFilter && 0 != statusFilter.size()) {
			List<String> statusStrings = new ArrayList<>();
			for (TTIriRef status:statusFilter) {
				statusStrings.add("<" + status.getIri()+ ">");
			}
			whereQl.append("Filter (?status in(" + String.join(",",statusStrings) + "))\n");
		}

		selectQl.append("\n");

		whereQl.append("}");

		selectQl.append(whereQl).append("\n");
		orderGroupLimit(selectQl,query);
		return selectQl.toString();

	}

	private void validateQuery() throws DataFormatException {
		if (null == query.getFrom() && null == query.getWhere())
			throw new DataFormatException("Query must have a from or where clause");
		if (null != query.getWhere())
			validateWhere(query.getWhere());
		if (null != query.getSelect())
			for (Select select: query.getSelect())
				validateSelect(select);
	}

	private void validateSelect(Select select) throws DataFormatException {
		if (null != select.getWhere())
			validateWhere(select.getWhere());
	}

	private void validateWhere(Where where) throws DataFormatException {
		if(null == where.getProperty() && null == where.getPathTo()
			&& null == where.getAnd() && null == where.getOr() && null == where.getNotExist() && null == where.getFrom())
			throw new DataFormatException("Where clause must have a path, property or boolean and/or, or not exist");
		if (null != where.getProperty()) {
			if (null != where.getAnd() || null != where.getOr())
				throw new DataFormatException("Where clause contains a property and an and /or subclause, use 'path' instead");
		}
		if (null != where.getAnd())
			for (Where and:where.getAnd())
				validateWhere(and);
		if (null != where.getOr())
			for (Where or:where.getOr())
				validateWhere(or);
		if (null != where.getNotExist()) {
			validateWhere(where.getNotExist());
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

	private void excludeSelf(StringBuilder whereQl,List<TTAlias> types) throws DataFormatException {
		for (TTAlias type:types){
			if (type.isExcludeSelf())
				whereQl.append("Filter (?entity!= "+ iriFromAlias(type)+")\n");
		}
	}


	private void from(StringBuilder whereQl, List<TTAlias> fromList) throws DataFormatException {
		Map<String,List<String>> fromTypes= new HashMap<>();
		for (TTAlias from:fromList) {
			if (from.isType()) {
				if (from.isIncludeSubtypes()) {
					fromTypes.computeIfAbsent("subtypes", s -> new ArrayList<>());
					fromTypes.get("subtypes").add(iriFromAlias(from));
				}
				else {
					fromTypes.computeIfAbsent("types", s -> new ArrayList<>());
					fromTypes.get("types").add(iriFromAlias(from));
				}
			}
			else if (null != from.getAlias()) {
				if (null == from.getIri()) {
					fromTypes.computeIfAbsent("aliases", s -> new ArrayList<>());
					fromTypes.get("aliases").add("?" + from.getAlias());
				}
			}
			else if (from.isSet()) {
				fromTypes.computeIfAbsent("sets", s -> new ArrayList<>());
				fromTypes.get("sets").add("?"+from.getAlias());

			}
			else if (null != from.getVariable()){
				String resolved= resolveReference(from.getVariable(),queryRequest);
				if (resolved.startsWith("http")) {
					if (from.isIncludeSubtypes()) {
						fromTypes.computeIfAbsent("subinstances", s -> new ArrayList<>());
						fromTypes.get("subinstances").add(iriFromString(resolved));
					} else {
						fromTypes.computeIfAbsent("instances", s -> new ArrayList<>());
						fromTypes.get("instances").add(iriFromString(resolved));
					}
				}
				else {
					throw new DataFormatException("From clause cannot contain literal variable. User property value");
				}

			}
			else {
				if (from.isIncludeSubtypes()) {
					fromTypes.computeIfAbsent("subinstances", s -> new ArrayList<>());
					fromTypes.get("subinstances").add(iriFromAlias(from));
				} else {
					fromTypes.computeIfAbsent("instances", s -> new ArrayList<>());
					fromTypes.get("instances").add(iriFromAlias(from));
				}
			}


		}
		boolean union= fromTypes.entrySet().size()>1;
		boolean first= true;
		for (String key:fromTypes.keySet()) {
			if (union) {
				if (first) {
					whereQl.append("{ ");
					first = false;
				} else
					whereQl.append("UNION { ");
			}
			if (key.equals("instances")) {
				whereQl.append("?").append("entity").append(" rdf:type ?").append("type").append(".\n");
				whereQl.append("Filter (?entity").append(" in(").append(String.join(",",fromTypes.get(key))).append("))\n");
			}
			else if (key.equals("subinstances")) {
				o++;
				whereQl.append("?").append("entity").append(" im:isA ?").append("superinstance").append(o).append(".\n");
				whereQl.append("Filter (?superinstance").append(o).append(" in(").append(String.join(",",fromTypes.get(key))).append("))\n");
				excludeSelf(whereQl,fromList);

			}
			else if (key.equals("subtypes")) {
				o++;
				whereQl.append("?").append("entity").append(" rdf:type ?").append("supertype").append(o).append(".\n");
				whereQl.append("?").append("supertype").append(o).append(" im:isA ?").append("supersupertype").append(o).append(".\n");
				whereQl.append("Filter (?supersupertype").append(o).append(" in(").append(String.join(",",fromTypes.get(key))).append("))\n");
				excludeSelf(whereQl,fromList);
			}
			else if (key.equals("aliases")) {
				o++;
				whereQl.append("?").append("entity").append(" rdf:type ?").append("type").append(o).append(".\n");
				whereQl.append("Filter (?type").append(o).append(" in(").append(String.join(",",fromTypes.get(key))).append("))\n");

			}
			else if (key.equals("sets")){
				o++;
				whereQl.append("?").append("entity").append(" rdf:type ?").append("type").append(o).append(".\n");
				whereQl.append("Filter (?entity").append(" in(").append(String.join(",",fromTypes.get(key))).append("))\n");
			}
			else if (key.equals("variables")){
				o++;
				whereQl.append("?").append("entity").append(" rdf:type ?").append("type").append(o).append(".\n");
				whereQl.append("Filter (?type").append(o).append(" in(").append(String.join(",",fromTypes.get(key))).append("))\n");
			}
			else if (key.equals("types")){
				o++;
				whereQl.append("?").append("entity").append(" rdf:type ?").append("type").append(o).append(".\n");
				whereQl.append("Filter (?type").append(o).append(" in(").append(String.join(",",fromTypes.get(key))).append("))\n");
			}

			if (union)
				whereQl.append("} \n");
		}

	}




	/**
	 * Constructs a where clause
	 * @param whereQl the where Clause thus far
	 * @param subject the SPARQL subject passed in - always starts with ?entity in the outer where
	 * @param where the where clause of the query
	 */
	private void where(StringBuilder whereQl, String subject,Where where) throws DataFormatException {
		if (null != where) {
			if (null != where.getGraph()) {
				indent();
				whereQl.append("graph ").append(iriFromString(where.getGraph())).append(" { \n");
			}
			if (null != where.getFrom())
				from(whereQl,where.getFrom());
			if (null != where.getPathTo()){
				for (String prop:where.getPathTo().split(" ")){
					o++;
					String object= localName(prop)+o;
					whereQl.append("?").append(subject).append(" ").append(iriFromString(prop))
						.append(" ?").append(object).append(".\n");
					subject= object;
				}
			}
			if (null == where.getProperty() && null != where.getPathTo() && null != where.getWhere()){
					where(whereQl,subject,where.getWhere());
				}
				else if (null!= where.getProperty()) {
					whereProperty(whereQl, subject, where);
				}
				else {
					if (null != where.getAnd()) {
						for (Where and : where.getAnd()) {
							where(whereQl, subject, and);
						}
					}
					if (null != where.getOr()) {
						for (int i = 0; i < where.getOr().size(); i++) {
							if (i == 0)
								whereQl.append("{ \n");
							else
								whereQl.append("UNION {\n");
							where(whereQl, subject, where.getOr().get(i));
							whereQl.append("}\n");
						}

					}
				}
				if (subject.equals("entity")) {
					if (query.isActiveOnly()) {
						whereQl.append("?").append(subject).append(" im:status im:Active.\n");
					}
				}
			if (null != where.getNotExist()) {
				whereQl.append(tabs).append(" FILTER NOT EXISTS {\n");
				where(whereQl, subject, where.getNotExist());
				whereQl.append("}\n");
			}
			if (null != where.getGraph()) {
					whereQl.append("}\n");
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
		o++;
		String object = "o" + o;
		if (null == path){
			String propertyAlias= where.getProperty().getAlias();
			List<TTAlias> propertyIn= where.getPropertyIn();
			if (null == propertyIn)
				throw new DataFormatException("Where clause with a property alias must have a property in filter");
			List<String> inList =
				propertyIn.stream().map(iri -> iriFromString(iri.getIri())).collect(Collectors.toList());
			String inString = String.join(",", inList);
			whereQl.append("?").append(subject).append(" ").append("?").append(propertyAlias).append(" ?").append(o).append(".\n");
			whereQl.append("Filter (").append("?").append(propertyAlias).append(" in(").append(inString).append("))\n");

		}
		else {
			String inverse = where.getProperty().isInverse() ? "^" : "";
			if (!where.getProperty().isIncludeSubtypes()) {
				whereQl.append("?").append(subject).append(" ").append(inverse).append(iriFromString(path))
					.append(" ?").append(object).append(".\n");
			} else {
				o++;
				whereQl.append("?").append(subject).append(" ").append(inverse).append("?p").append(o)
					.append(" ?").append(object).append(".\n");
				whereQl.append("?p").append(o).append(" im:isA ").append(iriFromString(path)).append(".\n");
			}
		}

		if (null != where.getWhere()) {
			where(whereQl, object, where.getWhere());
		}
		if (null != where.getAnd()) {
			for (Where and : where.getAnd()) {
				where(whereQl, object, and);
			}
		}
		if (null != where.getOr()) {
			for (int i = 0; i < where.getOr().size(); i++) {
				if (i == 0)
					whereQl.append("{ \n");
				else
					whereQl.append("UNION {\n");
				where(whereQl, object, where.getOr().get(i));
			}
		}

		if (where.getProperty().equals(IM.IS_A)){
			whereIsa(whereQl,object,where);
		}
		else {
			if (null != where.getIs()) {
				whereIs(whereQl, object, where.getIs(), where.isNot());
			} else if (null != where.getIn()) {
				whereIn(whereQl, object, where.getIn(), where.isNot());
			} else if (null != where.getValue()) {
				whereValue(whereQl, object, where, where.isNot());
			}
		}
	}

	private void whereIsa(StringBuilder whereQl, String object, Where where) throws DataFormatException {
		boolean isNot= where.isNot();
		String not = isNot ? "!" : "";
		if (null != where.getIs()) {
			whereQl.append("Filter (?").append(object).append(not).append("=").append(iriFromAlias(where.getIs()))
				.append(")\n");

		}
		else if (null != where.getIn()){
			List<TTAlias> in= where.getIn();
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

	private void whereIs(StringBuilder whereQl, String object, TTAlias is,boolean isNot) throws DataFormatException {
		String not = isNot ? "!" : "";
		if (is.isSet()) {
			String expansion = new SetToSparql().getExpansionSparql(object, is.getIri());
			if (!expansion.equals("")) {
				whereQl.append("Filter (?").append(object).append(not).append(" in (");
				whereQl.append(expansion);
				whereQl.append("))\n");
				return;
			} else
				throw new DataFormatException(" where is -> iri " + is.getIri() + " is said to be a set but it has no members");
		}
		if (is.isIncludeSubtypes()){
			o++;
			whereQl.append("?").append(object).append(" im:isA ?").append("supertype").append(o).append(".\n");
			whereQl.append("Filter (?supertype").append(o).append(not).append("=").append(iriFromAlias(is)).append(")\n");
			if (is.isExcludeSelf())
				whereQl.append("Filter (?").append(object).append("!=").append(iriFromAlias(is)).append(")\n");
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



	private void whereIn(StringBuilder whereQl, String object, List<TTAlias> in, boolean isNot) {
		String not= isNot ?" not " : "";
		whereQl.append("Filter (?").append(object).append(not).append(" in (");
		if (in.size()==1) {
			String expansion = new SetToSparql().getExpansionSparql(object, in.get(0).getIri());
			if (!expansion.equals("")) {
				whereQl.append(expansion);
				whereQl.append("))\n");
				return;
			}
		}
		List<String> inList =
					in.stream().map(iri -> iriFromString(iri.getIri())).collect(Collectors.toList());
				String inString = String.join(",", inList);
				whereQl.append(inString);
		whereQl.append("))\n");
	}

	private void whereValue(StringBuilder whereQl, String object, Where where, boolean isNot) throws DataFormatException {
		String not= isNot ?"!" : "";
		Value value= where.getValue();
		whereQl.append("Filter (?").append(object).append(not).append(value.getComparison()).append(" ");
		whereQl.append(convertValue(value.getValue()));
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
							if (null != argument.getValueIri())
								return argument.getValueIri().getIri();
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
			if (null != select.getProperty()) {
				TTAlias property= select.getProperty();
				object= property.getAlias();
				if (null == object) {
					o++;
					object = "p" + o;
					property.setAlias(object);
				}
				String inverse= select.getProperty().isInverse() ?"^" : "";
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
			for (Select select:clause.getSelect()){
				if (null != select.getProperty())
					if (select.getProperty().getIri().equals(RDFS.LABEL.getIri())){
						return select.getProperty().getAlias();
					}
				}
			return "label";
	}


	private String getSelectAlias(Query clause, String property) throws DataFormatException {
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
			if (null != alias.getVariable()){
				return iriFromString(resolveReference(alias.getVariable(),queryRequest));
			}
			else if (null == alias.getIri()) {
				if (null != alias.getAlias()) {
					return iriFromString(resolveReference(alias.getAlias(), queryRequest));
				}
				else
					throw new DataFormatException("iri place holder has neither a variable nor an alias nor an actual iri");
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
			String del=":";
			if (iri.contains("#"))
				del="#";
			else if (iri.startsWith("http"))
				del="/";
		String[] iriSplit= iri.split(del);
		return iriSplit[iriSplit.length-1];
	}



}
