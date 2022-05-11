package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import joptsimple.internal.Strings;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager;
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
	private DataSet query;
	private final Set<String> aliases = new HashSet<>();
	private final int nestLevel=4;
	private final Map<String, ResultNode> valueMap = new HashMap<>();
	private final Map<Value, ResultNode> entityMap = new HashMap<>();
	private final Set<String> predicates= new HashSet<>();
	private final Set<String> usedSelect= new HashSet<>();


	public Map<String, String> getVarProperty() {
		return varProperty;
	}

	public Set<String> getPredicates() {
		return predicates;
	}


	public ResultNode queryIM(DataSet query) throws DataFormatException, JsonProcessingException {
		validate(query);
		ResultNode result = new ResultNode();
		if (query.getReferenceDate()==null){
			String now= LocalDate.now().toString();
			query.setReferenceDate(now);
		}
		String spq = buildSparql(query);

		try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
			ResultNode context= new ResultNode();
				result.put("@context", context);
			if (query.isUsePrefixes()) {
				for (TTPrefix prefix : prefixes.getPrefixes())
					context.add(prefix.getPrefix(), prefix.getIri());
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

	public String buildSparql(DataSet query) throws DataFormatException {
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

		//where(whereQl, "entity", 0, query.getMatch());
		//whereSelect(whereQl,query.getSelect(),"entity",0);
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
					for (PropertyObject pob: select.getProperty())
				if (pob.getObject()!=null)
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
		if (select.getFilter()!=null){
			where(whereQl,filterSubject,level,select.getFilter());
		}
		if (select.getProperty() != null) {
			for (PropertyObject property : select.getProperty()) {
				selectProperty(property, subject, selectQl, whereQl, level);
			}
		}
	}
	private void selectProperty(PropertyObject property, String subject,
															StringBuilder selectQl,StringBuilder whereQl,int level) throws DataFormatException {
				String path = property.getIri();
				if (path==null)
					path= "*";
				if (path.equals("*")) {
					property.setBinding("*");
					fullSelect(selectQl, whereQl,level, subject);
					if (property.getObject() != null) {
						String object= nextObject();
						select(selectQl, property.getObject(), whereQl, level + 1, object);
					}
				} else {
					if (!isId(path)) {
						String local= localName(path);
						o++;
						String object = local + "_" + o;
						property.setBinding(object);
						String alias = property.getAlias();
						if (alias != null) {
							if (aliases.contains(alias))
								throw new DataFormatException("Cannot have two aliases of the same name in the same query : "
									+ property.getAlias());
							selectQl.append("(").append("?").append(object).append(" as ").append("?").append(alias).append(") ");
							aliases.add(property.getAlias());
						} else
							selectQl.append("?").append(object).append(" ");
						String inverse="";
						if (property.isInverseOf())
							inverse="^";
						whereQl.append("OPTIONAL { ").append("?").append(subject)
							.append(" ").append(inverse).append(iri(path)).append(" ?").append(object).append(".\n");
						if (property.getObject() != null)
							select(selectQl, property.getObject(), whereQl, level + 1, object);
						whereQl.append("}\n");
					}
					else {
						property.setBinding(subject);
						selectQl.append("?").append(subject).append(" ");
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
			if (predicate.substring(predicate.indexOf(":") + 1).equals("iri"))
				return true;
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

	private void where(StringBuilder whereQl, String subject, int level , Filter where) throws DataFormatException {
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

		if (where.getEntityIn() != null) {
			TTIriRef entityIn = where.getEntityIn();
			whereQl.append(new SetToSparql().getExpansionSparql(subject, entityIn.getIri())).append("\n");
		}
		if (where.getEntityType() != null) {
			ConceptRef type = where.getEntityType();
			if (type.isIncludeSubtypes()) {
				whereQl.append(tabs).append("?").append("super_" + subject).append(" rdf:type ").append(iri(where.getEntityType().getIri())).append(".\n");
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
			String andSubject=subject;
			for (Filter filter : where.getAnd()) {
				whereProperty(whereQl, andSubject, level, filter);
				andSubject= filter.getValueVar();
			}
		}
		if (where.getOptional()!=null){
			for (Filter filter :where.getOptional()) {
				whereQl.append("OPTIONAL {");
				whereProperty(whereQl, originalSubject, level, filter);
				whereQl.append("}\n");
			}
		}
		if (where.getOr()!=null){
			boolean first=true;
			for (Filter filter :where.getOr()) {
				if (!first)
					whereQl.append("UNION ");
				first=false;
				whereQl.append(" {");
				whereProperty(whereQl, subject, level, filter);
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
	private void whereProperty(StringBuilder whereQl, String subject,int level, Filter where) throws DataFormatException {
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
		if (where.getValueConcept()!=null) {
			whereValueConcept(whereQl,object,where.getValueConcept());
		}
		else if (where.getValueObject()!=null){
			level++;
			where(whereQl,object,level,where.getValueObject());
		}
		else if (where.getValueCompare()!=null){
			whereValueCompare(whereQl,object,where.getValueCompare());

		}
		else if (where.getValueIn()!=null){
			whereValueIn(whereQl,object,where.getValueIn());

		}
	}

	private void whereValueCompare(StringBuilder whereQl, String object, Compare compare) throws DataFormatException {
		if (compare.getComparison()==Comparison.STARTS_WITH){
			whereQl.append(tabs).append("  filter (strStarts(?").append(object).append(" , ")
				.append("\"").append(compare.getValue()).append("\"))\n");
		}
		else {
			String value= dataConverter(compare.getComparison(),compare.getValue());
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
				Calendar cal = DatatypeConverter.parseDateTime(value);
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
			throw new DataFormatException("Unknown comparison operator : "+ comp.toString());
	}



	private void whereGraph(StringBuilder whereQl, Filter where) {
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
													 ResultNode result) {

		if (query.getResultFormat()==ResultFormat.OBJECT) {
			bindObjects(bs,result);
		}
		else {
			ResultNode node = new ResultNode();
			result.add("entities", node);
			Select select = query.getSelect();
			for (PropertyObject property:select.getProperty()) {
				String var = property.getBinding();
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
							node.add("@id", value);
						} else if (var.equals("entity")) {
							node.add("@id", value);
						} else {
							String alias = property.getAlias();
							if (alias != null) {
								node.add(alias, value);
							} else {
								String path = property.getIri();
								if (!usedSelect.contains(path)) {
									node.add(resultIri(path), value);
									predicates.add(path);
								} else {
									node.add(var, value);
								}
							}
						}
					}
				}
			}
		}
	}

	private void bindObjects(BindingSet bs, ResultNode result) {
		Value entityValue= bs.getValue("entity");
		ResultNode root= entityMap.get(entityValue);
		if (root==null){
			root= new ResultNode();
			root.put("@id",entityValue.stringValue());
			result.add("entities",root);
			entityMap.put(entityValue,root);
		}
		Select select = query.getSelect();
		for (PropertyObject property:select.getProperty()) {
			bindObject(bs, root, property, "", "entity", 0);
		}
	}

	private void bindObject(BindingSet bs, ResultNode node, PropertyObject propertyObject, String path, String subject, int level) {
		String var= propertyObject.getBinding();
		if (var.equals("*")){
			bindAllForObject(bs,node,path,subject,level);
			return;
		}
		String bsVar= var;
		String alias= propertyObject.getAlias();
		String property= propertyObject.getIri();
		if (alias!=null)
			bsVar=alias;
		Value value=bs.getValue(bsVar);
		if (value==null)
			return;

		if (property==null) {
			varProperty.get(var);
			predicates.add(iri(property));
		}

		if (value.isIRI() || value.isBNode()) {
			ResultNode subNode = valueMap.get(path+ (value.stringValue()));
			if (subNode == null) {
				subNode = new ResultNode();
				valueMap.put(path + (value.stringValue()), subNode);
				node.add(resultIri(property),subNode);
				predicates.add(property);
				if (value.isIRI())
					subNode.add("@id", resultIri(value.stringValue()));
			}
			if (propertyObject.getObject() != null) {
				level++;
				for (PropertyObject subProperty :propertyObject.getObject().getProperty()) {
					bindObject(bs, subNode, subProperty,path+(value.stringValue()),var,level);
				}
			}
		} else {
			node.add(resultIri(property),value.stringValue());
			predicates.add(property);
		}

	}

	private void bindAllForObject(BindingSet bs, ResultNode rootNode, String path, String subject, int level) {
		ResultNode node= rootNode;
		for (int i=level; i<nestLevel;i++){
			Value prop= bs.getValue(subject+"_p"+i);
			Value ob= bs.getValue(subject+"_o"+i);
			if (prop!=null){
				String property= resultIri(prop.stringValue());
				predicates.add(iri(property));
				if (ob.isBNode()){
					ResultNode obNode= valueMap.get(path+(ob.stringValue()));
					if (obNode==null){
						obNode= new ResultNode();
						valueMap.put(path+(ob.stringValue()),obNode);
						predicates.add(property);
						node.add(property,obNode);
					}
					path = path+ (ob.stringValue());
					node= obNode;
				}
				else if (ob.isIRI()) {
					ResultNode obNode = valueMap.get(path+(ob.stringValue()));
					if (obNode == null) {
						obNode = new ResultNode();
						obNode.add("@id", resultIri(ob.stringValue()));
						valueMap.put(path+(ob.stringValue()), obNode);
						node.add(property,obNode);
						predicates.add(property);
					}
					node=obNode;
					path = path+ ob.stringValue();
				}
				else {
					node.add(property,ob.stringValue());
					predicates.add(property);

				}
			}
		}
	}

	private void bindAll(BindingSet bs, ResultNode result){
		ResultNode node= new ResultNode();
		result.add("entities",node);
		for (String binding:bs.getBindingNames()){
			if (bs.getValue(binding)!=null)
				node.add(binding,bs.getValue(binding).stringValue());
		}
	}

	private void validate(DataSet query) throws DataFormatException {
		if (query.getSelect() == null) {
			query.setSelect(new Select()
				.setDistinct(true)
				.addProperty(new PropertyObject(IM.NAMESPACE+"id")));
		}
		else {
			if (query.getSelect().getProperty()==null){
				query.getSelect().addProperty(new PropertyObject(IM.NAMESPACE+"id"));
			}
			else {
				boolean hasId= false;
				for (PropertyObject property:query.getSelect().getProperty()){
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
						.add(0,new PropertyObject(IM.NAMESPACE+"id"));
				}
			}
		}
		validateSelects(query.getSelect());


	}

	private void validateSelects(Select select) throws DataFormatException {

		for (PropertyObject property:select.getProperty()){
			if (property.getIri()==null){
				if (property.getBinding()==null) {
					throw new DataFormatException("Missing property or wild card binding in select statement");
				}
			}


		}
	}


}
