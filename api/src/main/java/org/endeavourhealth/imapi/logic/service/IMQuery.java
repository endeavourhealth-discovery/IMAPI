package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import joptsimple.internal.Strings;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager;
import org.endeavourhealth.imapi.model.search.SearchResultSummary;
import org.endeavourhealth.imapi.model.sets.*;
import org.endeavourhealth.imapi.model.tripletree.TTContext;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTPrefix;
import org.endeavourhealth.imapi.transforms.SetToSparql;
import org.endeavourhealth.imapi.vocabulary.*;

import javax.xml.bind.DatatypeConverter;
import java.time.LocalDate;
import java.util.*;
import java.util.zip.DataFormatException;

/**
 * Methods to convert a Query object to its Sparql equivalent and return results as a json object
 */
public class IMQuery {
	private TTContext prefixes;
	private final Map<String,String> varProperty = new HashMap<>();
	private String tabs="";
	private int o=0;
	private Query query;
	private final Set<String> aliases = new HashSet<>();
	private final int nestLevel=4;
	private final Map<String, ObjectNode> valueMap = new HashMap<>();
	private final Map<Value, ObjectNode> entityMap = new HashMap<>();
	private final Set<String> predicates= new HashSet<>();
	private final Set<String> usedSelect= new HashSet<>();
	final ObjectMapper mapper = new ObjectMapper();


	public Map<String, String> getVarProperty() {
		return varProperty;
	}

	public Set<String> getPredicates() {
		return predicates;
	}


	public ObjectNode queryIM(Query query) throws DataFormatException, JsonProcessingException {
		validate(query);
		if (query.getReferenceDate() == null) {
			String now = LocalDate.now().toString();
			query.setReferenceDate(now);
		}
		String spq = buildSparql(query);
			return goGraphSearch(spq);
	}



	private String getInList(List<SearchResultSummary> osResult) {
		List<String> inArray = new ArrayList<>();
		for (SearchResultSummary res : osResult) {
			inArray.add(iri(res.getIri()));
		}
		return Strings.join(inArray, ",");
	}



	private ObjectNode goGraphSearch(String spq) throws JsonProcessingException {
		try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
			ObjectNode result= mapper.createObjectNode();
			ObjectNode context= mapper.createObjectNode();
			//ObjectNode result = new ObjectNode();
			//ObjectNode context= new ObjectNode();
			result.set("@context", context);
			if (query.isUsePrefixes()) {
				for (TTPrefix prefix : prefixes.getPrefixes())
					context.put(prefix.getPrefix(), prefix.getIri());
			}
			TupleQuery qry= conn.prepareTupleQuery(spq);
			int count=0;
			try (TupleQueryResult rs= qry.evaluate()){
					while (rs.hasNext()){
						count++;
						BindingSet bs= rs.next();
						bindResults(bs,result);
				//	if (count % 100==0)
					//		System.out.println(count);
					}
				}

			if (!predicates.isEmpty()){
				Set<String> sparqlPredicates= new HashSet<>();
				predicates.forEach(p -> sparqlPredicates.add(iri(p)));
				String predlist= Strings.join(sparqlPredicates,",");
				String predLookup = getDefaultPrefixes() +
					"select ?predicate ?label \nwhere {" +
					"?predicate <" + RDFS.LABEL.getIri() + "> ?label.\n" +
					"filter (?predicate in (" + predlist + "))}";
				qry= conn.prepareTupleQuery(predLookup);
				try (TupleQueryResult rs= qry.evaluate()) {
					while (rs.hasNext()) {
						BindingSet bs = rs.next();
						Value predicate = bs.getValue("predicate");
						Value label = bs.getValue("label");
						if (label != null)
							context.put(predicate.stringValue(), label.stringValue());
					}
				}
			}
			return result;
		}

	}


	/**
	 * Takes an IMQ select query model and converts to SPARQL
	 * @param query Query Obiect containing select where
	 * @return String of SPARQL
	 **/

	public String buildSparql(Query query) throws DataFormatException{
		this.query = query;
		if (query.getResultFormat()==null){
			query.setResultFormat(inferResultFormat());
		}

		StringBuilder selectQl = new StringBuilder();
		selectQl.append(getDefaultPrefixes());
		selectQl.append("SELECT ");

		StringBuilder whereQl = new StringBuilder();
		whereQl.append("WHERE {");
		Select select= query.getSelect();

		if (query.getResultFormat() == ResultFormat.OBJECT||select.isDistinct()) {
			selectQl.append("distinct ");
		}
		select(selectQl, select,whereQl,0,"entity");
		selectQl.append("\n");

		whereQl.append("}");

		selectQl.append(whereQl);
		return selectQl.toString();
	}




	public String getDefaultPrefixes(){
		prefixes= new TTContext();
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



	private ResultFormat inferResultFormat() {
		if (query.getSelect()!=null){
			Select select= query.getSelect();
			if (select.getProperty()!=null)
					for (PropertySelect pob: select.getProperty())
				if (pob.getSelect()!=null)
					return ResultFormat.OBJECT;
		}
		return ResultFormat.RELATIONAL;
	}


	/**
	 * Processes the dataSet clause and binds to the variables created in the where clause
	 * @param selectQl Sparql dataSet clause thus far
	 * @param select the select statement to process
	 * @param  whereQl the associated where clause
	 * @param level the nested level
	 * @param subject the subject for the where clause
	 */
	private void select(StringBuilder selectQl, Select select,StringBuilder whereQl,
											int level,String subject) throws DataFormatException {
		String filterSubject= subject;
		if (select.getEntityIn() != null) {
			whereEntityIn(subject, select.getEntityIn(), whereQl);
		}
		if (select.getEntityType() != null) {
			whereEntityType(subject, select.getEntityType(), whereQl);
			if (select.getEntityType().isIncludeSubtypes())
				filterSubject= "super_"+subject;
			else if (select.getEntityType().isIncludeSupertypes())
				filterSubject= "sub_"+ subject;
		}
		else if (select.getEntityId() != null) {
			whereEntityId(subject, select.getEntityId(), whereQl);
			if (select.getEntityId().isIncludeSubtypes())
				filterSubject= "super_"+ subject;
			else if (select.getEntityId().isIncludeSupertypes())
				filterSubject= "sub_"+subject;
		}
		if (select.getMatch()!=null){
			where(whereQl,filterSubject,level,select.getMatch());
		}
		if (select.getProperty() != null) {
			for (PropertySelect property : select.getProperty()) {
				selectProperty(property, subject, selectQl, whereQl, level);
			}
		}
	}
	private void selectProperty(PropertySelect property, String subject,
															StringBuilder selectQl, StringBuilder whereQl, int level) throws DataFormatException {
				String path = property.getIri();
				if (path==null)
					path= "*";
				if (path.equals("*")) {
					property.setBinding("*");
					fullSelect(selectQl, whereQl,level, subject);
					if (property.getSelect() != null) {
						String object= nextObject();
						select(selectQl, property.getSelect(), whereQl, level + 1, object);
					}
				} else {
						for (int pathPart = 0; pathPart < path.split(" ").length; pathPart++) {
							String subPath = path.split(" ")[pathPart];
							if (!isId(subPath)) {
								String local = localName(subPath);
								o++;
								String object = local + "_" + o;
								property.setBinding(object);
								String alias = property.getAlias();
								if (pathPart == path.split(" ").length - 1) {
									if (alias != null) {
										if (aliases.contains(alias))
											throw new DataFormatException("Cannot have two aliases of the same name in the same query : "
												+ property.getAlias());
										selectQl.append("(").append("?").append(object).append(" as ").append("?").append(alias).append(") ");
										aliases.add(property.getAlias());
									} else
										selectQl.append("?").append(object).append(" ");
								}
								String inverse = "";
								if (property.isInverseOf())
									inverse = "^";
								whereQl.append("OPTIONAL { ").append("?").append(subject)
									.append(" ").append(inverse).append(iri(subPath)).append(" ?").append(object).append(".\n");
								if (pathPart < path.split(" ").length - 1) {
									subject = object;
								} else if (property.getSelect() != null)
									select(selectQl, property.getSelect(), whereQl, level + 1, object);
								whereQl.append("}\n");
							} else {
								property.setBinding(subject);
								selectQl.append("?").append(subject).append(" ");
							}
						}

				}
	 }

	 private String localName(String iri){
		String del="#";
		if (!iri.contains("#"))
			del=":";
		String[] iriSplit= iri.split(del);
		return iriSplit[iriSplit.length-1];
	 }
	private void fullSelect(StringBuilder selectQl, StringBuilder whereQl,int level,String subject) {
		selectQl.append(" ?").append(subject).append("_p").append(level)
			.append(" ?").append(subject).append("_o").append(level);
		for (int i=level+1;i<nestLevel;i++){
			selectQl.append(" ?").append(subject).append("_p").append(i)
				.append(" ?").append(subject).append("_o").append(i);
		}
		selectQl.append("\n");
		whereQl.append("OPTIONAL {");
		whereQl.append(tabs).append("?").append(subject)
			.append(" ?").append(subject).append("_p").append(level)
			.append(" ?").append(subject).append("_o").append(level).append(".\n");
		if (level==0)
			whereQl.append(" filter (?").append(subject).append("_p0 != im:isA)\n");
		for (int i=level+1;i<nestLevel; i++) {
			whereQl.append(tabs).append("OPTIONAL {?").append(subject).append("_o").append(i-1)
				.append(" ?").append(subject).append("_p").append(i)
				.append(" ?").append(subject).append("_o").append(i).append(".\n")
				.append(tabs).append("FILTER (isBlank(?").append(subject).append("_o").append(i-1).append("))\n");
		}
		whereQl.append("}".repeat(Math.max(0, nestLevel - level)));

	}



	/**
	 * is the variable im:id or iri i.e. the id
	 * @param predicate the predicate to test
	 * @return tru of the predicate is for an iri
	 */
	public boolean isId(String predicate){
		if (predicate.equals("id"))
			return true;
		if (predicate.equals(IM.NAMESPACE+"id"))
			return true;
		if (predicate.equals(IM.NAMESPACE+"iri"))
			return true;
		if (predicate.contains(":")) {
			if (predicate.substring(predicate.indexOf(":") + 1).equals("id"))
				return true;
			return predicate.substring(predicate.indexOf(":") + 1).equals("iri");
		}
		return false;
	}


	private String nextObject(){
		o++;
		return "o"+o;
	}

	private void indent(){
		tabs=tabs+"\t";
	}
	private void lessIndent(){
		if (!tabs.equals(""))
			tabs=tabs.substring(0,tabs.length()-1);
	}

	private String iri(String iri){
		if (iri.startsWith("http")||iri.startsWith("urn:"))
			return "<"+ iri+">";
		else return iri;
	}



	private void whereEntityId(String subject, ConceptRef entityId, StringBuilder whereQl) {
		if (entityId.isIncludeSubtypes()) {
			whereQl.append(tabs).append("?").append("super_").append(subject).append(" rdf:type ?any.\n");
			whereQl.append(tabs).append("?").append(subject).append(" im:isA ").append("?").append("super_").append(subject).append(".\n");
			whereQl.append(tabs).append("\tfilter (").append("?").append("super_").append(subject)
				.append("=").append(iri(entityId.getIri())).append(") \n");
		}
		else if (entityId.isIncludeSupertypes()) {
			whereQl.append(tabs).append("?").append("sub_").append(subject).append(" rdf:type ?any.\n");
			whereQl.append(tabs).append("?").append(subject).append(" ^im:isA ").append("?").append("sub_").append(subject).append(".\n");
			whereQl.append(tabs).append("\tfilter (").append("?").append("sub_").append(subject)
				.append("=").append(iri(entityId.getIri())).append(") \n");
		}
		else {
			whereQl.append(tabs).append("?").append(subject).append(" rdf:type ?any.\n");
			whereQl.append(tabs).append("\tfilter (").append("?").append(subject)
				.append("=").append(iri(entityId.getIri())).append(") \n");
		}
	}

	private void whereEntityType(String subject,ConceptRef entityType, StringBuilder whereQl) {
		if (entityType.isIncludeSubtypes()) {
			whereQl.append(tabs).append("?").append("super_").append(subject).append(" rdf:type ").append(iri(entityType.getIri())).append(".\n");
			whereQl.append(tabs).append("?").append(subject).append(" im:isA ").append("?").append("super_").append(subject).append(".\n");
		} else if (entityType.isIncludeSupertypes()) {
			whereQl.append(tabs).append("?").append("sub_").append(subject).append(" rdf:type ").append(iri(entityType.getIri())).append(".\n");
			whereQl.append(tabs).append("?").append(subject).append(" ^im:isA ").append("?").append("sub_").append(subject).append(".\n");
		} else
			whereQl.append(tabs).append("?").append(subject).append(" rdf:type ").append(iri(entityType.getIri())).append(".\n");
	}


	private void whereEntityIn(String subject,TTIriRef entityIn, StringBuilder whereQl) {
		whereQl.append(new SetToSparql().getExpansionSparql(subject, entityIn.getIri())).append("\n");
	}



	/**
	 * Constructs a whwee clause
	 * @param whereQl the where Clause thus far
	 * @param subject the SPARQL subject passed in - always starts with ?entity in the outer where
	 * @param level the nest level for nested selects
	 * @param where the where clause of the query
	 */

	private void where(StringBuilder whereQl, String subject, int level , Match where) throws DataFormatException {
		String originalSubject = subject;
		if (where.isNotExist()) {
			whereQl.append(tabs).append(" FILTER NOT EXISTS {\n");
		}

		whereGraph(whereQl, where);
		if (subject.equals("entity"))
			if (query.isActiveOnly())
				whereQl.append("?").append(subject).append(" im:status im:Active.\n");
		if (where.isIncludeSubEntities()) {
			o++;
			whereQl.append(tabs).append("?").append(subject)
				.append(" im:isA ?")
				.append("super_").append(subject).append(o).append(".\n");
			subject = "super_" + subject + o;
		}

		if (where.getEntityInValueSet() != null) {
			TTIriRef entityIn = where.getEntityInValueSet();
			whereQl.append(new SetToSparql().getExpansionSparql(subject, entityIn.getIri())).append("\n");
		}
		if (where.getEntityType() != null) {
			ConceptRef type = where.getEntityType();
			if (type.isIncludeSubtypes()) {
				whereQl.append(tabs).append("?").append("super_").append(subject).append(" rdf:type ").append(iri(where.getEntityType().getIri())).append(".\n");
				whereQl.append(tabs).append("?").append(subject).append(" im:isA ").append("?").append(subject).append(".\n");
			} else if (type.isIncludeSupertypes()) {
				whereQl.append(tabs).append("?").append("sub_").append(subject).append(" rdf:type ").append(iri(where.getEntityType().getIri())).append(".\n");
				whereQl.append(tabs).append("?").append(subject).append(" ^im:isA ").append("?").append(subject).append(".\n");
			} else
				whereQl.append(tabs).append("?").append(subject).append(" rdf:type ").append(iri(where.getEntityType().getIri())).append(".\n");
		}
		else if (where.getEntityId() != null) {
			whereQl.append(tabs).append("?").append(subject).append(" rdf:type ?any.\n");
			whereQl.append(tabs).append("\tfilter (").append("?").append(subject)
				.append("=").append(iri(where.getEntityId().getIri())).append(") ");

		}
		if (where.getAnd()!=null) {
			for (Match match : where.getAnd()) {
				where(whereQl, subject, level, match);
			}
		}
		if (where.getOptional()!=null){
			for (Match match :where.getOptional()) {
				whereQl.append("OPTIONAL {");
				where(whereQl, originalSubject, level, match);
				whereQl.append("}\n");
			}
		}
		if (where.getOr()!=null){
			boolean first=true;
			for (Match match :where.getOr()) {
				if (!first)
					whereQl.append("UNION ");
				first=false;
				whereQl.append(" {");
				where(whereQl, subject, level, match);
				whereQl.append("}\n");
			}
		}
		if (where.getProperty()!=null){
			whereProperty(whereQl,subject,level,where);
		}
		if (where.isNotExist())
			whereQl.append("}\n");

	}







	/**
	 * Proecesses the where property clause, the remaining match clause including subqueries
	 * @param whereQl the string builder sparql
	 * @param subject the parent subject passed to this where clause
	 * @param level the nested level built thus far to support nested selects
	 * @param where the where clause
	 */
	private void whereProperty(StringBuilder whereQl, String subject,int level, Match where) throws DataFormatException {
		//Open search?
		if (where.isIncludeSubEntities()){
			o++;
			whereQl.append(tabs).append("?").append(subject)
				.append(" im:isA ?")
				.append("super_").append(subject).append(o).append(".\n");
			subject="super_"+subject+o;
		}
		String predicate= where.getProperty().getIri();
		String object= where.getValueVar();
		if (object==null) {
			object = nextObject();
			where.setValueVar(object);
		}


		varProperty.put(object,predicate);
		String inverse="";
		if (where.isInverseOf())
			inverse="^";
		if (where.getProperty().isIncludeSubtypes()) {
			whereQl.append(tabs).append("?").append(subject).append(" ").append(inverse).append("?p").append(object).append(" ?")
				.append(object).append(".\n");
			whereQl.append(tabs).append("?p").append(object).append(" im:isA ").append(iri(predicate)).append(".\n");
		}
		else {
			whereQl.append(tabs).append("?").append(subject).append(" ").append(inverse).append(iri(predicate)).append(" ?")
				.append(object).append(".\n");

		}
		if (where.getIsConcept()!=null) {
			whereValueConcept(whereQl,object,where.getIsConcept());
		}
		else if (where.getMatch()!=null){
			level++;
			where(whereQl,object,level,where.getMatch());
		}
		else if (where.getValue()!=null){
			whereValueCompare(whereQl,object,where.getValue());

		}
		else if (where.getInSet()!=null){
			whereValueIn(whereQl,object,where.getInSet());

		}
	}

	private void whereValueCompare(StringBuilder whereQl, String object, Compare compare) throws DataFormatException {
		String value= compare.getValueData()!=null ?compare.getValueData() : compare.getValueVariable();
		if (compare.getComparison()==Comparison.STARTS_WITH){
			whereQl.append(tabs).append("  filter (strStarts(?").append(object).append(" , ")
				.append("\"").append(value).append("\"))\n");
		}
		else {
			 value= dataConverter(compare.getComparison(),value);
			whereQl.append(tabs).append("  filter (?").append(object).append(spqlOperator(compare.getComparison()))
				.append("\"").append(value).append("\")\n");
		}
	}


	private String dataConverter(Comparison comparison, String value) throws DataFormatException {
		if (value.startsWith("$"))
			value= resolveReference(value);
		if (comparison== Comparison.EQUAL)
			return value;
		if (comparison== Comparison.STARTS_WITH)
			return value;
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
				throw new DataFormatException("Invalid value "+ value+" with the operator "+comparison.toString()+
					".Value was tested for number and xsd date time format");
			}

			//throw new DataFormatException("Cannot use comparion operator " + comparison + "+ on a string");
		}

	}

	private String resolveReference(String value) throws DataFormatException {
		String variable= value.toLowerCase(Locale.ROOT);
		if (variable.equals("$referencedate"))
			return query.getReferenceDate();
		throw new DataFormatException("unknown parameter variable "+ value);
	}


	private void whereValueIn(StringBuilder whereQl, String object, List<TTIriRef> valueIn) throws DataFormatException {
		boolean first= true;
		for (TTIriRef in:valueIn) {
			if (!first)
				whereQl.append(tabs).append("union\n");
			whereQl.append(tabs).append("{");
			first = false;
			String expansion= new SetToSparql().getExpansionSparql(object,in.getIri());
			if (expansion.equals(""))
				throw new DataFormatException("Set "+ in.getIri()+" does not exist, or has no definition or has no members. Should you use valueConcept?");
			whereQl.append(expansion).append("}\n");
		}
	}

	private void whereValueConcept(StringBuilder whereQl, String object,List<ConceptRef> refs){
		boolean first = true;
		if (query.isActiveOnly())
			whereQl.append("?").append(object).append(" im:status im:Active.\n");
		for (ConceptRef in : refs) {
			if (!first)
				whereQl.append(tabs).append("union\n");
			whereQl.append(tabs).append("{");
			first = false;
			if (in.isIncludeSubtypes()) {
				whereQl.append(tabs).append("?").append(object).append(" im:isA ?")
					.append(iri(in.getIri())).append(".");
			} else if (in.isIncludeSupertypes()) {
				whereQl.append(tabs).append("?").append(object).append(" ^im:isA ?")
					.append(iri(in.getIri())).append(".");
			} else {
				whereQl.append(tabs).append(" filter (?").append(object).append(" = ")
					.append(iri(in.getIri())).append(")");
			}
			whereQl.append("}\n");
		}
	}

	private String spqlOperator(Comparison comp) throws DataFormatException {
		if (comp== Comparison.EQUAL)
			return "=";
		if (comp== Comparison.GREATER_THAN)
			return ">";
		if (comp==Comparison.GREATER_THAN_OR_EQUAL)
			return ">=";
		if (comp==Comparison.LESS_THAN)
			return "<";
		if (comp== Comparison.LESS_THAN_OR_EQUAL)
			return "<=";
		else
			throw new DataFormatException("comparison operator : "+ comp.toString()+" is not supported in graph query. use open search");
	}



	private void whereGraph(StringBuilder whereQl, Match where) {
		if (where!=null) {
			if (where.getGraph() != null) {
				indent();
				whereQl.append("graph ").append(iri(where.getGraph().getIri())).append(" { \n");
			}
		}
	}

	private String resultIri(String iri){
		if (!query.isUsePrefixes())
			return iri;
		return prefixes.prefix(iri);
	}



	private void bindResults(BindingSet bs,
													 ObjectNode result) {

		if (query.getResultFormat() == ResultFormat.OBJECT) {
			bindObjects(bs, result);
		} else {
			ObjectNode node = mapper.createObjectNode();
			addProperty(result, "entities", node);
			Select select = query.getSelect();
			bindSelect(bs,node,select,result);


		}
	}

	private void bindSelect(BindingSet bs, ObjectNode node, Select select,ObjectNode result) {
		for (PropertySelect property : select.getProperty()) {
			String var = property.getBinding();
			if (property.getAlias() != null)
				var = property.getAlias();
			if (var.equals("*")) {
				bindAll(bs, result);
			} else {
				Value bound = bs.getValue(var);
				if (bound != null) {
					String value;
					if (bound.isIRI())
						value = resultIri(bound.stringValue());
					else
						value = bound.stringValue();
					if (isId(var)) {
						node.put("@id", value);
					} else if (var.equals("entity")) {
						node.put("@id", value);
					} else {
						String alias = property.getAlias();
						if (alias != null) {
							addProperty(node, alias, value);
						} else {
							String path = property.getIri();
							if (!usedSelect.contains(path)) {
								addProperty(node, resultIri(path), value);
								predicates.add(path);
							} else {
								addProperty(node, var, value);
							}
						}
					}
					if (property.getSelect() != null){
						bindSelect(bs,node, property.getSelect(), result);
					}
				}
			}
		}
	}

	private void addProperty(ObjectNode node,String property,String value){
		if (node.get(property)==null) {
			node.set(property, mapper.createArrayNode());
			((ArrayNode) node.get(property)).add(value);
		}
		ArrayNode already= (ArrayNode) node.get(property);
		for (JsonNode n:already){
			if (n.asText().equals(value))
				return;
		}
		already.add(value);


	}

	private void addProperty(ObjectNode node,String property,ObjectNode value){
		if (node.get(property)==null)
			node.set(property,mapper.createArrayNode());
		((ArrayNode) node.get(property)).add(value);
	}
	private void addProperty(ObjectNode node,String property,JsonNode value){
		if (node.get(property)==null)
			node.set(property,mapper.createArrayNode());
		((ArrayNode) node.get(property)).add(value);
	}

	private void bindObjects(BindingSet bs, ObjectNode result) {
		Value entityValue= bs.getValue("entity");
		ObjectNode root= entityMap.get(entityValue);
		if (root==null){
			root= mapper.createObjectNode();
			root.put("@id",entityValue.stringValue());
			addProperty(result,"entities",root);
			entityMap.put(entityValue,root);
		}
		Select select = query.getSelect();
		for (PropertySelect property:select.getProperty()) {
			bindObject(bs, root, property, "", "entity", 0);
		}
	}

	private void bindObject(BindingSet bs, ObjectNode node, PropertySelect propertySelect, String path, String subject, int level) {
		String var= propertySelect.getBinding();
		if (var.equals("*")){
			bindAllForObject(bs,node,path,subject,level);
			return;
		}
		String bsVar= var;
		String alias= propertySelect.getAlias();
		String property= propertySelect.getIri();
		if (alias!=null)
			bsVar=alias;
		Value value=bs.getValue(bsVar);
		if (value==null)
			return;

		if (property==null) {
			property=varProperty.get(var);
			predicates.add(iri(property));
		}

		if (value.isIRI() || value.isBNode()) {
			ObjectNode subNode = valueMap.get(path+ (value.stringValue()));
			if (subNode == null) {
				subNode = mapper.createObjectNode();
				valueMap.put(path + (value.stringValue()), subNode);
				addProperty(node,resultIri(property),subNode);
				predicates.add(property);
				if (value.isIRI())
					addProperty(subNode,"@id", resultIri(value.stringValue()));
			}
			if (propertySelect.getSelect() != null) {
				level++;
				for (PropertySelect subProperty : propertySelect.getSelect().getProperty()) {
					bindObject(bs, subNode, subProperty,path+(value.stringValue()),var,level);
				}
			}
		} else {
			addProperty(node,resultIri(property),value.stringValue());
			predicates.add(property);
		}

	}

	private void bindAllForObject(BindingSet bs, ObjectNode rootNode, String path, String subject, int level) {
		ObjectNode node= rootNode;
		StringBuilder pathBuilder = new StringBuilder(path);
		for (int i = level; i<nestLevel; i++){
			Value prop= bs.getValue(subject+"_p"+i);
			Value ob= bs.getValue(subject+"_o"+i);
			if (prop!=null){
				String property= resultIri(prop.stringValue());
				predicates.add(iri(property));
				if (ob.isBNode()){
					ObjectNode obNode= valueMap.get(pathBuilder +(ob.stringValue()));
					if (obNode==null){
						obNode= mapper.createObjectNode();
						valueMap.put(pathBuilder +(ob.stringValue()),obNode);
						predicates.add(property);
						addProperty(node,property,obNode);
					}
					pathBuilder.append(ob.stringValue());
					node= obNode;
				}
				else if (ob.isIRI()) {
					ObjectNode obNode = valueMap.get(pathBuilder +(ob.stringValue()));
					if (obNode == null) {
						obNode = mapper.createObjectNode();
						addProperty(obNode,"@id", resultIri(ob.stringValue()));
						valueMap.put(pathBuilder +(ob.stringValue()), obNode);
						addProperty(node,property,obNode);
						predicates.add(property);
					}
					node=obNode;
					pathBuilder.append(ob.stringValue());
				}
				else {
					addProperty(node,property,ob.stringValue());
					predicates.add(property);

				}
			}
		}
	}

	private void bindAll(BindingSet bs, ObjectNode result){
		ObjectNode node= mapper.createObjectNode();
		addProperty(result,"entities",node);
		for (String binding:bs.getBindingNames()){
			if (bs.getValue(binding)!=null)
				addProperty(node,binding,bs.getValue(binding).stringValue());
		}
	}

	private void validate(Query query) throws DataFormatException {
		if (query.getSelect() == null) {
			query.setSelect(new Select()
				.setDistinct(true)
				.addProperty(new PropertySelect().setIri(IM.NAMESPACE+"id")
					.setAlias("id")));
		}
		else {
			if (query.getSelect().getProperty()==null){
				query.getSelect().addProperty(new PropertySelect().setIri(IM.NAMESPACE+"id")
					.setAlias("id"));
			}
			else {
				boolean hasId= false;
				for (PropertySelect property:query.getSelect().getProperty()){
					if (property.getIri()==null)
						if (property.getBinding()==null) {
							property.setBinding("*");
						}
					if (property.getIri()!=null)
						if (isId(property.getIri()))
							hasId= true;
				}
				if (!hasId){
					query.getSelect().getProperty()
						.add(0,new PropertySelect(IM.NAMESPACE+"id").setAlias("id"));
				}
			}
		}
		validateSelects(query,query.getSelect());


	}

	private void validateSelects(Query query,Select select) throws DataFormatException {

		for (PropertySelect property:select.getProperty()){
			if (property.getIri()==null){
				if (property.getBinding()==null) {
					throw new DataFormatException("Missing property or wild card binding in select statement");
				}
			}
			else {
				if (property.getAlias()==null) {
					if (property.getBinding()==null){
					if (query.getResultFormat() != ResultFormat.OBJECT) {
						if (property.getSelect()==null) {
							throw new DataFormatException("For a flat select you must either have an alias or a binding variable for the column : (" + property.getIri() + ")" +
								" =or else use OBJECT result format");
						}
						}
					}
				}
			}
			if (property.getSelect()!=null)
				validateSelects(query, property.getSelect());

		}
	}


}
