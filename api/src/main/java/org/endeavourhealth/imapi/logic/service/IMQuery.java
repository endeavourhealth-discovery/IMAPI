package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import joptsimple.internal.Strings;
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

import java.util.*;
import java.util.zip.DataFormatException;

/**
 * Methods to convert a Query object to its Sparql equivalent and return results as a json object
 */
public class IMQuery {
	private TTContext prefixes;
	private final Map<String,String> propertyVar = new HashMap<>();
	private final Map<String,String> varProperty = new HashMap<>();
	private String tabs="";
	private int o=0;
	private DataSet query;
	private final Set<String> aliases = new HashSet<>();
	private final int nestLevel=4;
	private final Map<Value, QNode> valueMap = new HashMap<>();
	private final Map<Value, QNode> entityMap = new HashMap<>();
	private final Set<String> predicates= new HashSet<>();


	public Map<String, String> getPropertyVar() {
		return propertyVar;
	}

	public Map<String, String> getVarProperty() {
		return varProperty;
	}

	public Set<String> getPredicates() {
		return predicates;
	}


	public String queryIM(DataSet query) throws DataFormatException, JsonProcessingException {
		QNode result = new QNode();
		String spq = buildSparql(query);
		try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
			QNode context= new QNode();
			if (query.isUsePrefixes()) {
				result.put("@context", context);
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
				String predlist= Strings.join(predicates,",");
				String predLookup = getDefaultPrefixes() +
					"select ?predicate ?label \nwhere {" +
					"?predicate <" + RDFS.LABEL.getIri() + "> ?label.\n" +
					"filter (?predicate in (" + predlist + "))}";
				qry= conn.prepareTupleQuery(predLookup);
				try (TupleQueryResult rs= qry.evaluate()) {
					QNode predicates = new QNode();
					result.add("predicates", predicates);
					while (rs.hasNext()) {
						BindingSet bs = rs.next();
						Value predicate = bs.getValue("predicate");
						Value label = bs.getValue("label");
						if (label != null)
							predicates.put(predicate.stringValue(), label.stringValue());
					}
				}
			}

			return result.asJson();
		}


	}


	private String resultIri(String iri){
		if (!query.isUsePrefixes())
			return iri;
		return prefixes.prefix(iri);
	}




	private void bindResults(BindingSet bs,
													 QNode result) {
		if (query.getSelect().get(0).getBinding().equals("*")){
			bindAll(bs,result);
		}
		else if (query.getResultFormat()==ResultFormat.OBJECT) {
			bindObjects(bs,result);
		}
		else {
			QNode node = new QNode();
			result.add("entities",node);
			for (Select selection : query.getSelect()) {
				String var = selection.getBinding();
				if (isId(var))
					var="entity";
				String alias = selection.getAlias();
				if (alias != null) {
					var = alias;
				}
				Value bound = bs.getValue(var);
				if (bound != null) {
						node.add(var, bound.stringValue());
				}
			}
		}
	}

	private void bindObjects(BindingSet bs, QNode result) {
		Value entityValue= bs.getValue("entity");
		QNode root= entityMap.get(entityValue);
		if (root==null){
			root= new QNode();
			root.put("@id",entityValue);
			result.add("entities",root);
			entityMap.put(entityValue,root);
		}
		for (Select select:query.getSelect()){
			bindObject(bs,root,select);
	}

}

	private void bindObject(BindingSet bs, QNode node, Select select) {
		String var= select.getBinding();
		String alias= select.getAlias();
		if (alias!=null)
			var=alias;
		Value value=bs.getValue(var);
		if (value==null)
			return;
		String property= select.getAlias();
		if (property==null) {
			property = resultIri(varProperty.get(var));
			predicates.add(iri(property));
		}

			if (value.isIRI() || value.isBNode()) {
				QNode subNode = valueMap.get(value);
				if (subNode == null) {
					subNode = new QNode();
					valueMap.put(value, subNode);
					if (value.isIRI())
						subNode.add("@id", value.stringValue());
				}
					node.add(property,subNode);
				if (select.getObject() != null) {
					for (Select subSelect : select.getObject()) {
						bindObject(bs, subNode, subSelect);
					}
				}
			} else {
				node.add(property,value.stringValue());
			}

	}

	private void bindAll(BindingSet bs, QNode result) {
		if (query.getResultFormat()==ResultFormat.OBJECT){
			Value entityValue= bs.getValue("entity");
			QNode entityNode= entityMap.get(entityValue);
			if (entityNode==null){
				entityNode= new QNode();
				entityNode.add("@id",entityValue.stringValue());
				result.add("entities",entityNode);
				entityMap.put(entityValue,entityNode);
			}
			QNode node= entityNode;
			for (int i=0; i<nestLevel;i++){
				Value prop= bs.getValue("constructp"+i);
				Value ob= bs.getValue("constructo"+i);
				if (prop!=null){
					String property= resultIri(prop.stringValue());
					predicates.add(iri(property));
					if (ob.isBNode()){
					  QNode obNode= valueMap.get(ob);
						if (obNode==null){
							obNode= new QNode();
							valueMap.put(ob,obNode);
						}
						node.add(property,obNode);
					}
					else if (ob.isIRI()) {
						QNode obNode = valueMap.get(ob);
						if (obNode == null) {
							obNode = new QNode();
							obNode.add("@id", resultIri(ob.stringValue()));
							valueMap.put(ob, obNode);
						}
						node.add(property,obNode);
					}
					else {
						node.add(property,ob.stringValue());

					}
				}
			}
		}
		else {
			QNode node= new QNode();
			result.add("entities",node);
			for (String binding:bs.getBindingNames()){
				if (bs.getValue(binding)!=null)
					node.add(binding,bs.getValue(binding).stringValue());
			}
		}
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
		sparql.append("PREFIX sh: <").append(SNOMED.NAMESPACE).append(">\n\n");
		return sparql.toString();
	}

	/**
	 * Takes an IMQ select query model and converts to SPARQL
	 * @param query Query Obiect containing select where
	 * @return String of SPARQL
	 */
	public String buildSparql(DataSet query) throws DataFormatException {
		this.query= query;
		StringBuilder whereQl= new StringBuilder();
		whereQl.append("WHERE {");
		where(whereQl, "entity", "", query.getMatch());

				StringBuilder selectQl = new StringBuilder();
				selectQl.append(getDefaultPrefixes());
			  selectQl.append("SELECT ");
				if (query.getSelect()==null){
					query.addSelect(new Select()
						.setBinding("entity"));
				}
				if (query.getResultFormat()== ResultFormat.OBJECT)
					selectQl.append("distinct ");
				if (query.getSelect()==null) {
					selectQl.append("\n");
					whereQl.append("}");
				}
				else {
					if (query.getSelect().get(0).getBinding().equals("*")) {
						fullWhereConstruct(whereQl);
						whereQl.append("}");
						fullSelect(selectQl);
					} else {
						whereQl.append("}");
					  select(selectQl, query.getSelect());
					}
				}
				selectQl.append(whereQl);
				return selectQl.toString();

	}

	private void fullSelect(StringBuilder selectQl) {
			selectQl.append("?entity");
			for (int i=0;i<nestLevel;i++){
				selectQl.append(" ?constructp").append(i).append(" ?constructo").append(i);
			}
			selectQl.append("\n");
	}



	/**
	 * Processes the dataSet clause and binds to the variables created in the where clause
	 * @param selectQl Sparql dataSet clause thus far
	 * @param selections the list of selections or subobjects to add
	 */
	private void select(StringBuilder selectQl, List<Select> selections) throws DataFormatException {
			for (Select selection : selections) {
				String var = selection.getBinding();
				String alias = selection.getAlias();
				if (alias != null) {
							if (!alias.equals(var)) {
								if (aliases.contains(alias))
									throw new DataFormatException("Cannot have two aliases of the same name in the same query : " + selection.getAlias());
								selectQl.append("(").append("?").append(var).append(" as ").append("?").append(alias).append(") ");
								aliases.add(selection.getAlias());
							} else
								selectQl.append(var.equals("*") ? "" : "?").append(var).append(" ");

						} else
							selectQl.append(var.equals("*") ? "" : "?").append(var).append(" ");
				if (selection.getObject()!=null)
					select(selectQl,selection.getObject());
				}
			selectQl.append("\n");
	}



	/**
	 * Constructs a whwee clause
	 * @param whereQl the where Clause thus far
	 * @param subject the SPARQL subject passed in - always starts with ?entity in the outer where
	 * @param path the proprerty path for nested selects
	 * @param where the where clause of the query
	 */
	private void where(StringBuilder whereQl, String subject, String path , Match where) throws DataFormatException {

		whereGraph(whereQl, where);
		if (where.isIncludeSubEntities())
			whereQl.append("{");
		if (where.getEntityType() != null) {
			whereQl.append(tabs).append("?").append(subject).append(" rdf:type ").append(iri(where.getEntityType().getIri())).append(".\n");
		}
		else if (where.getEntityId()!=null) {
			if (where.isIncludeMembers()) {
				whereQl.append(new SetToSparql().getExpansionSparql(where.getEntityId().getIri()));
			} else {
				whereQl.append(tabs).append("?").append(subject).append(" rdf:type ?any.\n");
				whereQl.append(tabs).append("\tfilter (").append("?").append(subject)
					.append("=").append(iri(where.getEntityId().getIri())).append(") ");
			}
		}
		if (where.isIncludeSubEntities()) {
			whereQl.append("}\n");
			whereQl.append("UNION {");
			if (where.getEntityId() != null) {
						whereQl.append("?").append(subject).append(" im:isA ")
							.append(iri(where.getEntityId().getIri())).append(".\n");
			} else if (where.getEntityType() != null) {
							whereQl.append("?").append(subject).append(" im:isA ?super").append(subject).append(".\n");
							whereQl.append("?super").append(subject).append(" rdf:type ").append(iri(where.getEntityType().getIri())).append(".\n");
			}
			whereQl.append("}\n");
		}


		if (where.getAnd()!=null) {
			String andSubject=subject;
			for (Match match : where.getAnd()) {
						whereProperty(whereQl, andSubject, path, match);
						andSubject= match.getValueVar();
					}
		}
		if (where.getOptional()!=null){
					for (Match match:where.getOptional()) {
						whereQl.append("OPTIONAL {");
						whereProperty(whereQl, subject, path, match);
						whereQl.append("}\n");
					}
		}
		if (where.getOr()!=null){
					whereQl.append("{");
					boolean first=true;
					for (Match match:where.getOr()) {
						if (!first)
							whereQl.append("UNION {");
						else
							whereQl.append(" {");
						first=false;
						whereProperty(whereQl, subject, path, match);
						whereQl.append("}\n");
					}
					whereQl.append("}\n");
		}
		if (where.getProperty()!=null){
					whereProperty(whereQl,subject,path,where);
		}

		if (where.getOr()!=null)
					whereQl.append("}\n");
	}



	/**
	 * is the variable im:id or iri i.e. the id
	 * @param predicate the predicate to test
	 * @return tru of the predicate is for an iri
	 */
	public boolean isId(String predicate){
		if (predicate.equals("id"))
			return true;
		if (predicate.contains(":")) {
			if (predicate.substring(predicate.indexOf(":") + 1).equals("id"))
				return true;
		}
		return predicate.equals("iri");
	}



	private void fullWhereConstruct(StringBuilder whereQl) {
		whereQl.append(tabs).append("?entity ?constructp0 ?constructo0.\n");
		for (int i=0;i<nestLevel; i++) {
			whereQl.append(tabs).append("OPTIONAL { ?constructo").append(i).append(" ?constructp").append(i+1).append(" ?constructo").append(i+1).append(".\n")
				.append(tabs).append("FILTER (isBlank(?constructo").append(i).append("))\n");
		}
		whereQl.append(tabs).append("}}}}");
		lessIndent();
	}


	/**
	 * Proecesses the where property clause, the remaining match clause including subqueries
	 * @param subject the parent subject passed to this where clause
	 * @param path the path built thus far to support nested selects
	 * @param where the where clause
	 */
	private void whereProperty(StringBuilder whereQl, String subject,String path, Match where) throws DataFormatException {
		String predicate= where.getProperty().getIri();

		String object= where.getValueVar();
		if (object==null) {
			object = nextObject();
			where.setValueVar(object);
		}


		propertyVar.put(path+predicate,object);
		varProperty.put(object,predicate);
		String inverse="";
		if (where.isInverseOf())
			inverse="^";
		if (where.isIncludeSubProperties()) {
			whereQl.append(tabs).append("?").append(subject).append(" ").append(inverse).append("?p").append(object).append(" ?")
				.append(object).append(".\n");
			whereQl.append(tabs).append("?p").append(object).append(" im:isA ").append(iri(predicate)).append(".\n");
		}
		else {
			whereQl.append(tabs).append("?").append(subject).append(" ").append(inverse).append(iri(predicate)).append(" ?")
				.append(object).append(".\n");

		}
		if (where.getValueIn()!=null){
			List<String> inList= new ArrayList<>();
			for (TTIriRef in:where.getValueIn()){
				inList.add(iri(in.getIri()));
			}
			String inString= String.join(",",inList);
			whereQl.append(tabs).append("\tfilter (?").append(object).append(" in (").append(inString).append("))\n");
		}
		else if (where.getValueObject()!=null){
			where(whereQl,object,path+predicate+" / ",where.getValueObject());
		}
		else if (where.getValueCompare()!=null){
			Compare compare= where.getValueCompare();
			whereQl.append(tabs).append("  filter (?").append(object).append(spqlOperator(compare.getComparison()))
				.append("\"").append(compare.getValue()).append("\")\n");
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

	private void whereGraph(StringBuilder whereQl, Match where) {
		if (where!=null) {
			if (where.getGraph() != null) {
				indent();
				whereQl.append("graph ").append(iri(where.getGraph().getIri())).append(" { \n");
			}
		}
	}

	private String iri(String iri){
		if (iri.startsWith("http")||iri.startsWith("urn:"))
		  return "<"+ iri+">";
		else return iri;
	}




}
