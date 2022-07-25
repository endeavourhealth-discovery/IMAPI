package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import joptsimple.internal.Strings;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.BooleanQuery;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager;
import org.endeavourhealth.imapi.model.search.SearchResultSummary;
import org.endeavourhealth.imapi.model.sets.*;
import org.endeavourhealth.imapi.model.tripletree.*;
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
	private final Map<String,String> matchVarProperty = new HashMap<>();
	private String tabs="";
	private int o=0;
	private Query query;
	private final Set<String> aliases = new HashSet<>();

	private final Map<String, ObjectNode> valueMap = new HashMap<>();
	private final Map<Value, ObjectNode> entityMap = new HashMap<>();
	private final Set<String> predicates= new HashSet<>();
	final ObjectMapper mapper = new ObjectMapper();
	private boolean fuzzy =false;
	private QueryRequest queryRequest;



	public Set<String> getPredicates() {
		return predicates;
	}


	public List<SearchResultSummary> entityQuery(QueryRequest queryRequest) throws DataFormatException, JsonProcessingException {
		this.queryRequest= queryRequest;
		if (queryRequest.getQueryIri()!=null)
			this.query= getQueryByIri(queryRequest.getQueryIri());
		else {
			this.query = queryRequest.getQuery();
			validate(query);
		}
		if (query==null)
			throw new DataFormatException("QueryRequest must either have a queryIri or a query property with a query object");

		Select select= selectAsSearchResult();
		select.setMatch(query.getSelect().getMatch());
		query.setSelect(select);
		query.setUsePrefixes(false);

		ObjectNode result= new OSQuery().openSearchQuery(queryRequest);
		if (result!=null){
			return convertToSummary(result);
		}
		else {
			checkReferenceDate();
			String spq = buildSparql(query);
			return convertToSummary(goGraphSearch(spq));
		}

	}

	private Select selectAsSearchResult() {
		Select select = new Select();
		select
			.property(p->p.setIri(IM.HAS_STATUS.getIri()).setAlias("status"))
			.property(p->p.setIri(RDF.TYPE.getIri()).setAlias("entityType"))
			.property(p->p.setIri(RDFS.LABEL.getIri()).setAlias("name"))
			.property(p->p.setIri(RDFS.COMMENT.getIri()).setAlias("description"))
			.property(p->p.setIri(IM.CODE.getIri()).setAlias("code"))
			.property(p->p.setIri(IM.HAS_SCHEME.getIri()).setAlias("scheme"));
		return select;
	}

	private List<SearchResultSummary> convertToSummary(ObjectNode genericResult) throws JsonProcessingException {
		List<SearchResultSummary> result = new ArrayList<>();
		ArrayNode entities = (ArrayNode) genericResult.get("entities");
		for (Iterator<JsonNode> it = entities.elements(); it.hasNext(); ) {
			JsonNode entity = it.next();
			SearchResultSummary summary= new SearchResultSummary();
			result.add(summary);
			Iterator<Map.Entry<String, JsonNode>> fields = entity.fields();
			while(fields.hasNext()) {
				Map.Entry<String, JsonNode> field = fields.next();
				String fieldName = field.getKey();
				JsonNode fieldValue = field.getValue();
				switch (fieldName) {
					case "@id":
						summary.setIri(fieldValue.asText());
						break;
					case (RDFS.NAMESPACE + "label"):
						summary.setName(getTextValue(fieldValue));
						break;
					case (RDFS.NAMESPACE + "comment"):
						summary.setDescription(getTextValue(fieldValue));
						break;
					case (IM.NAMESPACE + "code"):
						summary.setCode(getTextValue(fieldValue));
						break;
					case (IM.NAMESPACE + "scheme"):
						summary.setScheme(getTTValues(fieldValue).stream().findFirst().get());
						break;
					case (IM.NAMESPACE + "status"):
						summary.setStatus(getTTValues(fieldValue).stream().findFirst().get());
						break;
					case (RDF.NAMESPACE + "type"):
						summary.setEntityType(getTTValues(fieldValue));
						break;
					default:
				}
			}
		}
		return result;
	}

	private Set<TTIriRef> getTTValues(JsonNode fieldValue){
		Set<TTIriRef> values= new HashSet<>();
		for (Iterator<JsonNode> it = fieldValue.elements(); it.hasNext(); ) {
			JsonNode iri= it.next();
			TTIriRef iriRef= TTIriRef.iri(iri.get("@id").asText());
			if (iri.get("name")!=null)
				iriRef.setName(iri.get("name").asText());
			values.add(iriRef);
		}
		return values;
	}

	private String getTextValue(JsonNode fieldValue){
		for (Iterator<JsonNode> it = fieldValue.elements(); it.hasNext(); ) {
			JsonNode text= it.next();
			return text.asText();
		}
		return null;
	}


	/**
	 * Generic query of IM with the select statements determining the response
	 * @param queryRequest QueryRequest object
	 * @return A Json Document with property values as defined in the select statement
	 * @throws DataFormatException
	 * @throws JsonProcessingException
	 */
	public ObjectNode queryIM(QueryRequest queryRequest) throws DataFormatException, JsonProcessingException {
		this.queryRequest= queryRequest;
		if (queryRequest.getQueryIri()!=null)
			this.query= getQueryByIri(queryRequest.getQueryIri());
		else {
			this.query = queryRequest.getQuery();
		}
			if (query==null)
				throw new DataFormatException("QueryRequest must either have a queryIri or a query property with a query object");
			ObjectNode result= new OSQuery().openSearchQuery(queryRequest);
			if (result!=null)
				return result;
			else {
				validate(query);
				checkReferenceDate();
				String spq = buildSparql(query);
				return goGraphSearch(spq);
			}
	}

	private void checkReferenceDate(){
		if (queryRequest.getReferenceDate()==null){
			String now = LocalDate.now().toString();
			queryRequest.setReferenceDate(now);
		}

	}



	public boolean booleanQueryIM(String iri,Map<String,String> variables) throws DataFormatException, JsonProcessingException {
		TTEntity entity= new EntityService().getFullEntity(iri).getEntity();
		if (entity==null)
			throw new DataFormatException("Entity "+ iri+" is unknown");
		if (entity.get(IM.QUERY_DEFINITION)==null)
			throw new DataFormatException("Entity "+ iri+" is not aN ASK query");
		this.queryRequest= new QueryRequest();
		this.query= new ObjectMapper().readValue(entity.get(IM.QUERY_DEFINITION).asLiteral().getValue(),Query.class);
		checkReferenceDate();

		String spq = buildSparql(query);
		return goBooleanGraphSearch(spq);

	}

	private Query getQueryByIri(TTIriRef iri) throws DataFormatException, JsonProcessingException {
		if (iri==null)
			return null;
		TTEntity entity= new EntityService().getFullEntity(iri.getIri()).getEntity();
		if (entity.getPredicateMap()==null)
			throw new DataFormatException("Entity "+ iri.getIri()+" is unknown");
		if (entity.getPredicateMap().isEmpty())
			throw new DataFormatException("Entity "+ iri.getIri()+" is unknown");
		if (entity.get(IM.QUERY_DEFINITION)==null)
			throw new DataFormatException("Entity "+ iri.getIri()+" is not a select query");
		return new ObjectMapper().readValue(entity.get(IM.QUERY_DEFINITION).asLiteral().getValue(),Query.class);


	}




	private String getInList(List<SearchResultSummary> osResult) {
		List<String> inArray = new ArrayList<>();
		for (SearchResultSummary res : osResult) {
			inArray.add(iri(res.getIri()));
		}
		return Strings.join(inArray, ",");
	}

	private boolean goBooleanGraphSearch(String spq){
		try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
			BooleanQuery qry = conn.prepareBooleanQuery(spq);
			return qry.evaluate();
		}

	}



	private ObjectNode goGraphSearch(String spq) {
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
			try (TupleQueryResult rs= qry.evaluate()){
					while (rs.hasNext()){
						BindingSet bs= rs.next();
						bindResults(bs,result);

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

	public String buildSparql(Query query) throws DataFormatException {
		this.query = query;
		if (query.getAsk()!=null){
			return buildAskSparql(query);
		}
		else
			return buildSelectSparql(query);
	}

	private String buildAskSparql(Query query) throws DataFormatException {
		StringBuilder askQl = new StringBuilder();
		askQl.append("ASK {");
		Match match= query.getAsk();
		String entity= queryRequest.getArgument().get("this");
		if (entity==null)
			throw new DataFormatException("Query request does not contain the focus entity");
		match.setEntityId(TTIriRef.iri(entity));
		where(askQl, "entity",1, match,null);
		askQl.append("}");
		return askQl.toString();
	}


	private String buildSelectSparql(Query query) throws DataFormatException {

	StringBuilder selectQl = new StringBuilder();
			selectQl.append(getDefaultPrefixes());
			if (queryRequest.getTextSearch()!=null){
				selectQl.append("PREFIX con-inst: <http://www.ontotext.com/connectors/lucene/instance#>\n")
					.append("PREFIX con: <http://www.ontotext.com/connectors/lucene#>\n");
			}
			Select select = query.getSelect();
			selectQl.append("SELECT ");
			if (query.getResultFormat() == ResultFormat.OBJECT || select.isDistinct()) {
				selectQl.append("distinct ");
			}
			selectQl.append("?entity ");

			StringBuilder whereQl = new StringBuilder();
			whereQl.append("WHERE {");

			if (queryRequest.getTextSearch()!=null){
				textSearch(whereQl);

			}

			select(selectQl, select, whereQl, 0, "entity");
			selectQl.append("\n");

			whereQl.append("}");

			selectQl.append(whereQl).append("\n");
			orderLimit(selectQl);
			return selectQl.toString();

	}

	private void textSearch(StringBuilder whereQl) {
		String text= queryRequest.getTextSearch();
		whereQl.append("[] a con-inst:im_fts;\n")
			.append("       con:query \"label:");
		String[] words= text.split(" ");
		for (int i=0; i<words.length; i++){
			words[i]= words[i]+ ((!fuzzy) ?"*" : "~");
		}
		String searchText= String.join(" && ",words);
		whereQl.append("(").append(searchText).append(")").append("\" ;\n");
		whereQl.append("       con:entities ?entity.\n");
	}


	private void orderLimit(StringBuilder selectQl) {
		if (queryRequest.getTextSearch()!=null){
			String labelAlias= getLabelAlias(query.getSelect());
			if (labelAlias!=null)
				selectQl.append("ORDER BY DESC(").append("strstarts(lcase(?").append(labelAlias)
					.append("),\"").append(queryRequest.getTextSearch().split(" ")[0])
					.append("\")) ASC(strlen(?").append(labelAlias).append("))\n");
		}
		if (query.getSelect()!=null){
			if (query.getSelect().getOrderLimit()!=null){
				OrderLimit order= query.getSelect().getOrderLimit();
				selectQl.append("Order by ");
				if (order.getDirection()==Order.DESCENDING)
					selectQl.append("DESC(");
				else
					selectQl.append("ASC(");
				if (order.getOrderBy().getAlias()!=null)
					selectQl.append("?").append(order.getOrderBy().getAlias());
				else
					selectQl.append("?").append(order.getOrderBy().getAlias());
				selectQl.append(")");
				return;
			}
		}
		if (queryRequest.getPage()!=null) {
			if (queryRequest.getPageSize() != null) {
				selectQl.append("LIMIT ").append(queryRequest.getPageSize()).append("\n");
				if (queryRequest.getPage() > 1)
					selectQl.append("OFFSET ").append((queryRequest.getPage() - 1) * (queryRequest.getPageSize())).append("\n");
			}
		}
	}

	private String getLabelAlias(Select select) {
		if (select!=null){
			if (select.getProperty()!=null)
				for (PropertySelect prop:select.getProperty()){
					if (prop.getIri().equals(RDFS.LABEL.getIri())){
						return prop.getAlias();
					}
				}
		}
		return null;
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
		String matchSubject= subject;
		if (select.getEntityIn() != null) {
			whereEntityIn(subject, select.getEntityIn(), whereQl);
		}
		if (select.getEntityType() != null) {
			matchSubject= whereEntityType(subject, select.getEntityType(), whereQl,null);
		}
		else if (select.getEntityId() != null) {
			matchSubject= whereEntityId(subject, select.getEntityId(), whereQl,null);
		}
		if (select.getMatch()!=null){
			for (Match m:select.getMatch()) {
				where(whereQl, matchSubject, level, m,select);
			}
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
				String object= property.getAlias();
				if (object!=null)
					if (object.equals("*"))
						throw new DataFormatException("Wild card (*) not supported due to combinatorial explosion potential in graph");
				if (path==null) {
						if (object == null)
							throw new DataFormatException("Select without property or alias");
						else if (matchVarProperty.get(object) == null) {
							throw new DataFormatException("Select without property with alias that doesnt match any match clause");
						} else {
							property.setIri(matchVarProperty.get(object));
							path = property.getIri();
						}
				}
					if (object!=null)
						if (object.equals("entity"))
							return;
					if (property.getAlias()==null) {
						String local = localName(path);
						o++;
						object = local + "_" + o;
						property.setAlias(object);
					}
					if (aliases.contains(object)) {
						throw new DataFormatException("Cannot have two aliases of the same name in the same query : "
							+ property.getAlias());
					}
					else {
						selectQl.append("?").append(object).append(" ");
						String inverse = "";
						if (property.isInverseOf())
							inverse = "^";
						boolean optional= !hasFilter(property);
						if (optional)
						    whereQl.append("OPTIONAL { ");
						 whereQl.append("?").append(subject)
							  .append(" ").append(inverse).append(iri(path)).append(" ?").append(object).append(".\n");
						if (property.getSelect() != null) {
							select(selectQl, property.getSelect(), whereQl, level + 1, object);
						}
						if (optional)
							whereQl.append("}\n");
					}
	 }

	 private boolean hasFilter(PropertySelect property){
		if (property.getSelect()!=null){
			if (property.getSelect().getMatch()!=null)
				return true;
			for (PropertySelect subprop:property.getSelect().getProperty()){
				if (hasFilter(subprop))
					return true;
			}
		}
		return false;
	 }



	 private String localName(String iri){
		String del="#";
		if (!iri.contains("#"))
			del=":";
		String[] iriSplit= iri.split(del);
		return iriSplit[iriSplit.length-1];
	 }


	/**
	 * is the variable im:id or iri i.e. the id
	 * @param predicate the predicate to test
	 * @return tru of the predicate is for an iri
	 */
	public boolean isId(String predicate){
		if (predicate.equals("entity"))
			return true;
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



	private String whereEntityId(String subject, ConceptRef entityId, StringBuilder whereQl,Select select) throws DataFormatException {
		if (entityId.getIri()==null)
			entityId.setIri(resolveReference(entityId.getAlias()));
		if (select!=null)
			if (select.getEntityId()!=null)
				if (entityId.equals(select.getEntityId()))
					return subject;
		String matchSubject= subject;
		String superSubject= subject;
		if (entityId.isIncludeSubtypes()) {
			matchSubject= "match"+subject;
			superSubject= "super"+subject;
			whereQl.append(tabs).append("?").append(subject).append(" im:isA ").append("?").append(matchSubject).append(".\n");
			whereQl.append(tabs).append("?").append(matchSubject).append(" im:isA ").append("?").append(superSubject).append(".\n");
		}
			whereQl.append(tabs).append("\tfilter (").append("?").append(superSubject)
				.append("=").append(iri(entityId.getIri())).append(") \n");
		return matchSubject;
	}

	private String whereEntityType(String subject,ConceptRef entityType, StringBuilder whereQl,Select select) {
		if (select!=null)
			if (select.getEntityType()!=null)
				if (entityType.equals(select.getEntityType()))
					return subject;
		String matchSubject= subject;
		String superSubject= subject;
		if (entityType.isIncludeSubtypes()) {
			matchSubject= "match"+subject;
			superSubject= "super"+subject;
			whereQl.append(tabs).append("?").append(subject).append(" im:isA ").append("?").append(matchSubject).append(".\n");
			whereQl.append(tabs).append("?").append(matchSubject).append(" im:isA ").append("?").append(superSubject).append(".\n");
		}
		whereQl.append(tabs).append("?").append(superSubject).append(" rdf:type ").append(iri(entityType.getIri())).append(".\n");
		return matchSubject;
	}


	private void whereEntityIn(String subject,TTIriRef entityIn, StringBuilder whereQl) {
		whereQl.append(new SetToSparql().getExpansionSparql(subject, entityIn.getIri())).append("\n");
	}

	private String getPropertyPath(List<ConceptRef> path){
		StringBuilder result= new StringBuilder();
		boolean first=true;
		for (ConceptRef p:path){
			if (!first)
				result.append(" / ");
			result.append(iri(p.getIri()));
			first=false;
		}
		return result.toString();
	}



	/**
	 * Constructs a whwee clause
	 * @param whereQl the where Clause thus far
	 * @param subject the SPARQL subject passed in - always starts with ?entity in the outer where
	 * @param level the nest level for nested selects
	 * @param where the where clause of the query
	 */

	private void where(StringBuilder whereQl, String subject, int level , Match where,Select select) throws DataFormatException {
		String matchSubject=subject;
		if (where.getPathTo()!=null){
			o++;
			whereQl.append("?").append(subject).append(" ").append(getPropertyPath(where.getPathTo()))
				.append(" ").append("?").append(subject).append(o).append(".\n");
			subject= subject+o;
			matchSubject= subject;
		}
		if (where.isNotExist()) {
			whereQl.append(tabs).append(" FILTER NOT EXISTS {\n");
		}



		whereGraph(whereQl, where);
		if (subject.equals("entity"))
			if (query.isActiveOnly())
				whereQl.append("?").append(subject).append(" im:status im:Active.\n");


		if (where.getEntityInSet() != null) {
			boolean first = true;
			List<ConceptRef> entityIn = where.getEntityInSet();
			for (TTIriRef in : entityIn) {
				if (!first)
					whereQl.append(tabs).append("union\n");
				whereQl.append(tabs).append("{");
				first = false;
				String expansion = new SetToSparql().getExpansionSparql(subject, in.getIri());
				if (expansion.equals(""))
					throw new DataFormatException("Set " + in.getIri() + " does not exist, or has no definition or has no members. Should you use valueConcept?");
				whereQl.append(expansion).append("}\n");
			}
		}
		if (where.getEntityType() != null) {
			matchSubject= whereEntityType(subject,where.getEntityType(),whereQl,select);
		}
		else if (where.getEntityId()!=null) {
			matchSubject = whereEntityId(subject, where.getEntityId(), whereQl,select);
		}

		if (where.getAnd()!=null) {
			for (Match match : where.getAnd()) {
				  where(whereQl, matchSubject, level, match,select);
			}
		}

		if (where.getOr()!=null){
			boolean first=true;
			for (Match match :where.getOr()) {
				if (!first)
					whereQl.append("UNION ");
				first=false;
				whereQl.append(" {");
					where(whereQl, matchSubject, level, match,select);

				whereQl.append("}\n");
			}
		}
		if (where.getProperty()!=null){
			for (PropertyValue pv:where.getProperty()) {
				whereProperty(whereQl, matchSubject, level, where, pv);
			}
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
	private void whereProperty(StringBuilder whereQl, String subject,int level, Match where,PropertyValue pv) throws DataFormatException {
		if (pv.getPathTo()!=null){
			o++;
			whereQl.append("?").append(subject).append(" ").append(getPropertyPath(where.getPathTo()))
				.append(" ").append("?").append(subject).append(o).append(".\n");
			subject= subject+o;
		}
		if (pv.isOptional()){
				whereQl.append("OPTIONAL {");
		}
		if (pv.isNotExist()){
			whereQl.append("FILTER NOT EXISTS {");
		}
		String predicate= pv.getIri();
		String object= pv.getAlias();
		if (object==null) {
			object = nextObject();
			pv.setAlias(object);
		}


		matchVarProperty.put(object,predicate);
		String inverse="";
		if (pv.isInverseOf())
			inverse="^";
		if (pv.isIncludeSubtypes()) {
			whereQl.append(tabs).append("?").append(subject).append(" ").append(inverse).append("?p").append(object).append(" ?")
				.append(object).append(".\n");
			whereQl.append(tabs).append("?p").append(object).append(" im:isA ").append(iri(predicate)).append(".\n");
		}
		else {
			whereQl.append(tabs).append("?").append(subject).append(" ").append(inverse).append(iri(predicate)).append(" ?")
				.append(object).append(".\n");

		}
		if (pv.getIsConcept()!=null) {
			whereIsConcept(whereQl,object,pv.getIsConcept());
		}
		else if (pv.getValue()!=null){
			whereValueCompare(whereQl,object,pv.getValue());

		}
		else if (pv.getInSet()!=null){
			whereValueIn(whereQl,object,pv.getInSet());
		}
		else if (pv.getMatch()!=null){
			where(whereQl,object,level++,pv.getMatch(),null);
		}
		if (pv.isOptional()){
			whereQl.append("}\n");
		}
		if (pv.isNotExist())
			whereQl.append("}");
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
		try {
			if (value.equalsIgnoreCase("$referenceDate")) {
				return queryRequest.getReferenceDate();
			}
			else {
				value= value.replace("$","");
			if (queryRequest.getArgument().get(value)!=null) {
					return queryRequest.getArgument().get(value);
				}
				else
					throw new DataFormatException("unknown parameter variable " + value+". ");
			}
		}
		catch (Exception e) {
			throw new DataFormatException("unknown parameter variable " + value);
		}
	}


	private void whereValueIn(StringBuilder whereQl, String object, List<ConceptRef> valueIn) throws DataFormatException {
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

	private void whereIsConcept(StringBuilder whereQl, String object,List<ConceptRef> refs) throws DataFormatException {
		int conceptCount=refs.size();
		boolean superTypes= false;
		boolean subTypes= false;
		String testObject="test_"+object;
		List<String> inList= new ArrayList<>();
		for (ConceptRef ref:refs) {
			if (ref.getIri()!=null) {
				inList.add(iri(ref.getIri()));
			}
			else if (ref.getAlias()!=null){
				String refIri= resolveReference(ref.getAlias());
				if (refIri==null)
					throw new DataFormatException("Query has concept value as variable not passed into query");
				inList.add(iri(refIri));
			}
			if (ref.isIncludeSubtypes())
				subTypes= true;
			if (ref.isIncludeSupertypes())
				superTypes=true;

		}
		String in= Strings.join(inList,",");
		if (query.isActiveOnly())
			whereQl.append("?").append(object).append(" im:status im:Active.\n");
		if (subTypes) {
				whereQl.append(tabs).append("?").append(object).append(" im:isA ?")
					.append(testObject).append(".\n");
				if (conceptCount==1) {
					whereQl.append(tabs).append(" filter (?").append(testObject).append(" = ")
						.append(in).append(")\n");
				}
				else
					whereQl.append(tabs).append(" filter (?").append(testObject).append(" in (")
						.append(in).append("))\n");
		}
		else if (superTypes) {
					whereQl.append(tabs).append("?").append(object).append(" ^im:isA ?")
						.append(iri(testObject)).append(".\n");
			   if (conceptCount==1) {
				whereQl.append(tabs).append(" filter (?").append(testObject).append(" = ")
					.append(in).append(")\n");
				 }
				 else
					 whereQl.append(tabs).append(" filter (?").append(testObject).append(" in (")
					.append(in).append("))\n");
		}
		else {
			if (conceptCount==1) {
				whereQl.append(tabs).append(" filter (?").append(object).append(" = ")
					.append(in).append(")\n");
			}
			else
				whereQl.append(tabs).append(" filter (?").append(object).append(" in (")
					.append(in).append("))\n");

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
			bindSelect(bs,node,select);


		}
	}

	private void bindSelect(BindingSet bs, ObjectNode node, Select select) {
		for (PropertySelect property : select.getProperty()) {
			String var = property.getAlias();
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
						addProperty(node, alias, value);
					}
				}
				if (property.getSelect() != null){
						bindSelect(bs,node, property.getSelect());
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
			valueMap.clear();
		}
		String path=entityValue.stringValue();
		Select select = query.getSelect();
		for (PropertySelect property:select.getProperty()) {
			bindObject(bs, root, property, path, 0);
		}
	}

	private void bindObject(BindingSet bs, ObjectNode node, PropertySelect propertySelect, String path, int level) {
		String var= propertySelect.getAlias();
		String bsVar= var;
		String alias= propertySelect.getAlias();
		String property= propertySelect.getIri();
		if (alias!=null)
			bsVar=alias;
		Value value=bs.getValue(bsVar);
		if (value==null)
			return;

		if (property==null) {
			property=matchVarProperty.get(var);
			predicates.add(iri(property));
		}

		if (value.isIRI() || value.isBNode()) {
			ObjectNode subNode = valueMap.get(path+ (value.stringValue()));
			if (subNode == null) {
				subNode = mapper.createObjectNode();
				valueMap.put(path + (value.stringValue()), subNode);
				addProperty(node, resultIri(property), subNode);
				predicates.add(property);
				if (value.isIRI())
					subNode.put("@id", resultIri(value.stringValue()));
			}
			if (propertySelect.getSelect() != null) {
				level++;
				for (PropertySelect subProperty : propertySelect.getSelect().getProperty()) {
					bindObject(bs, subNode, subProperty,path+(value.stringValue()), level);
				}
			}
		} else {
			addProperty(node,resultIri(property),value.stringValue());
			predicates.add(property);
		}

	}

	private void validate(Query query) throws DataFormatException {
		if (query.getResultFormat()==null){
			query.setResultFormat(ResultFormat.OBJECT);
		}
		if (query.getAsk()!=null){
			validateMatch(query.getAsk(),null);
			return;
		}
		else 	if (query.getSelect() == null) {
			throw new DataFormatException("Query must have an ask or a select");
		}
		validateSelects(query,query.getSelect());


	}

	private void validateSelects(Query query,Select select) throws DataFormatException {
			if (select.getMatch()!=null) {
				for (Match match : select.getMatch()) {
					validateMatch(match, select);
				}
			}

		if (select.getOrderLimit()!=null)
			if (select.getOrderLimit().getOrderBy().getAlias()==null)
					throw new DataFormatException("Select order by must use  aliases");
		if (select.getProperty()!=null) {
			for (PropertySelect property : select.getProperty()) {
				if (property.getIri() == null) {
					if (property.getAlias() == null) {
						throw new DataFormatException("Missing property  in select statement");
					}
				} else {
					if (property.getAlias() == null) {
						if (query.getResultFormat() != ResultFormat.OBJECT) {
							if (property.getSelect() == null) {
								throw new DataFormatException("For a flat select you must have an alias (binding variable) for the column : (" + property.getIri() + ")" +
									" =or else use OBJECT result format");
							}

						}
					}
				}
				if (property.getSelect() != null)
					validateSelects(query, property.getSelect());

			}
		}
	}

	private void validateMatch(Match match,Select select) throws DataFormatException {
		if (match.getOr()!=null){
			for (Match or:match.getOr())
				validateMatch(or,select);
		}
		else if (match.getAnd()!=null){
			for (Match and:match.getAnd())
				validateMatch(and,select);
		}
		else 	if (match.getProperty()==null) {
			if (match.getEntityId()==null)
				throw new DataFormatException("Match clause that has no properties must have an entity id");
			if (select==null){
				throw new DataFormatException("Match clause must have properties");
			}
			if (select.getProperty()==null)
			  throw new DataFormatException("Match clause must be boolean or have at least one property in select clause");
			for (PropertySelect prop:select.getProperty()){
				if (prop.getIri()==null){
					throw new DataFormatException("Select property should have iri if the match clause has no properties");
				}
				PropertyValue pv= new PropertyValue();
				pv.setIri(prop.getIri());
				match.addProperty(pv);
			}
		}


	}


}
