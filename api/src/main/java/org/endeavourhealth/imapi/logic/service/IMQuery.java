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
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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


	public Map<String, String> getVarProperty() {
		return varProperty;
	}

	public Set<String> getPredicates() {
		return predicates;
	}


	public ResultNode queryIM(DataSet query) throws DataFormatException, JsonProcessingException {
		ResultNode result = new ResultNode();
		if (query.getReferenceDate()==null){
			String now= LocalDate.now().toString();
			query.setReferenceDate(now);
		}
		String spq = buildSparql(query);

		try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
			ResultNode context= new ResultNode();
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
					ResultNode predicates = new ResultNode();
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

			return result;
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
			for (Select selection : query.getSelect()) {
				String var = selection.getBinding();
				if (var.equals("*")) {
					bindAll(bs, result);
				} else {
					if (isId(var))
						var = "entity";
					String alias = selection.getAlias();
					if (alias != null) {
						var = alias;
					}
					Value bound = bs.getValue(var);
					if (bound != null) {
						node.add(var.equals("entity")? "@id": var, bound.stringValue());
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
			root.put("@id",entityValue);
			result.add("entities",root);
			entityMap.put(entityValue,root);
		}
		for (Select select:query.getSelect()){
			bindObject(bs,root,select,"","entity",0);
	}

}

	private void bindObject(BindingSet bs, ResultNode node, Select select, String path, String subject, int level) {
		String var= select.getBinding();
		if (var.equals("*")){
			bindAllForObject(bs,node,path,subject,level);
			return;
		}
		String bsVar= var;
		String alias= select.getAlias();
		if (alias!=null)
			bsVar=alias;
		Value value=bs.getValue(bsVar);
		if (value==null)
			return;
		String property= select.getAlias();
		if (property==null) {
			property = resultIri(varProperty.get(var));
			predicates.add(iri(property));
		}

			if (value.isIRI() || value.isBNode()) {
				ResultNode subNode = valueMap.get(path+ (value.stringValue()));
				if (subNode == null) {
					subNode = new ResultNode();
					valueMap.put(path + (value.stringValue()), subNode);
					node.add(property,subNode);
					if (value.isIRI())
						subNode.add("@id", value.stringValue());
				}
				if (select.getObject() != null) {
					level++;
					for (Select subSelect : select.getObject()) {
						bindObject(bs, subNode, subSelect,path+(value.stringValue()),var,level);
					}
				}
			} else {
				node.add(property,value.stringValue());
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
						}
						node=obNode;
						path = path+ ob.stringValue();
					}
					else {
						node.add(property,ob.stringValue());

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
	 * Takes an IMQ select query model and converts to SPARQL
	 * @param query Query Obiect containing select where
	 * @return String of SPARQL
	 */
	public String buildSparql(DataSet query) throws DataFormatException {
		this.query = query;
		if (query.getResultFormat()==null){
			query.setResultFormat(inferResultFormat());
		}
		StringBuilder whereQl = new StringBuilder();
		whereQl.append("WHERE {");
		where(whereQl, "entity", 0, query.getMatch());
		whereSelect(whereQl,query.getSelect(),"entity",0);
		whereQl.append("}");

		StringBuilder selectQl = new StringBuilder();
		selectQl.append(getDefaultPrefixes());
		selectQl.append("SELECT ");
		if (query.getSelect() == null) {
			query.setDistinct(true);
			query.addSelect(new Select()
				.setBinding("entity"));
		}

		if (query.getResultFormat() == ResultFormat.OBJECT) {
			selectQl.append("distinct ");
		}
		else if (query.isDistinct())
			selectQl.append("distinct ");
		selectQl.append("?entity ");
		select(selectQl, query.getSelect(),0,"entity");
		selectQl.append(whereQl);
		return selectQl.toString();
	}

	private ResultFormat inferResultFormat() {
		if (query.getSelect()!=null){
			for (Select select: query.getSelect())
				if (select.getObject()!=null)
					return ResultFormat.OBJECT;
		}
		return ResultFormat.RELATIONAL;
	}

	private void whereSelect(StringBuilder whereQl, List<Select> selects,String subject,int level) throws DataFormatException {
		if (selects==null)
			return;
		for (Select select:selects){
			String binding= select.getBinding();
			if (binding.equals("*")){
				wildCard(whereQl,subject,level);

			}
			else if (!isId(binding))
			  if (varProperty.get(binding)==null){
				 throw new DataFormatException("binding variable ("+ binding+") is not in a match clause as a value variable.  Selections must be matched by binding variables to match clause value var");
			}
			if (select.getObject()!=null){
				whereSelect(whereQl,select.getObject(),select.getBinding(),level+1);
			}
		}
	}

	private void fullSelect(StringBuilder selectQl, int level,String subject) {
		selectQl.append(" ?").append(subject).append("_p").append(level)
			.append(" ?").append(subject).append("_o").append(level);
			for (int i=level+1;i<nestLevel;i++){
				selectQl.append(" ?").append(subject).append("_p").append(i)
					.append(" ?").append(subject).append("_o").append(i);
			}
			selectQl.append("\n");
	}



	/**
	 * Processes the dataSet clause and binds to the variables created in the where clause
	 * @param selectQl Sparql dataSet clause thus far
	 * @param selections the list of selections or subobjects to add
	 */
	private void select(StringBuilder selectQl, List<Select> selections,int level,String subject) throws DataFormatException {
			for (Select selection : selections) {
				String var = selection.getBinding();
				if (var.equals("*")) {
					fullSelect(selectQl, level,subject);
				}
				else {
					if (!isId(var)|(!subject.equals("entity"))) {
						String alias = selection.getAlias();
						if (alias != null) {
							if (!alias.equals(var)) {
								if (aliases.contains(alias))
									throw new DataFormatException("Cannot have two aliases of the same name in the same query : " + selection.getAlias());
								selectQl.append("(").append("?").append(var).append(" as ").append("?").append(alias).append(") ");
								aliases.add(selection.getAlias());
							} else
								selectQl.append("?").append(var).append(" ");

						} else
							selectQl.append("?").append(var).append(" ");
						if (selection.getObject() != null) {
							level++;
							select(selectQl, selection.getObject(), level, selection.getBinding());
						}
					}
				}
			}
		selectQl.append("\n");
	}



	/**
	 * Constructs a whwee clause
	 * @param whereQl the where Clause thus far
	 * @param subject the SPARQL subject passed in - always starts with ?entity in the outer where
	 * @param level the nest level for nested selects
	 * @param where the where clause of the query
	 */
	private void where(StringBuilder whereQl, String subject, int level , Match where) throws DataFormatException {
		String originalSubject= subject;
		if (where.isNotExist()){
			whereQl.append(tabs).append(" FILTER NOT EXISTS {\n");
		}

		whereGraph(whereQl, where);
		if (subject.equals("entity"))
			if (query.isActiveOnly())
				whereQl.append("?").append(subject).append(" im:status im:Active.\n");
		if (where.isIncludeSubEntities()){
			o++;
			whereQl.append(tabs).append("?").append(subject)
				.append(" im:isA ?")
				.append("super_").append(subject).append(o).append(".\n");
			subject="super_"+subject+o;
		}

		if (where.getEntityIn()!=null){
			TTIriRef entityIn= where.getEntityIn();
				whereQl.append(new SetToSparql().getExpansionSparql(subject,entityIn.getIri())).append("\n");
		}
		if (where.getEntityType() != null) {
			ConceptRef type= where.getEntityType();
			if (type.isIncludeSubtypes()){
				whereQl.append(tabs).append("?").append("super_"+subject).append(" rdf:type ").append(iri(where.getEntityType().getIri())).append(".\n");
				whereQl.append(tabs).append("?").append(subject).append(" im:isA ").append("?").append(subject).append(".\n");
			}
			else if (type.isIncludeSupertypes()){
				whereQl.append(tabs).append("?").append("sub_"+subject).append(" rdf:type ").append(iri(where.getEntityType().getIri())).append(".\n");
				whereQl.append(tabs).append("?").append(subject).append(" ^im:isA ").append("?").append(subject).append(".\n");
			}
			else
				whereQl.append(tabs).append("?").append(subject).append(" rdf:type ").append(iri(where.getEntityType().getIri())).append(".\n");
			} else if (where.getEntityId() != null) {
					whereQl.append(tabs).append("?").append(subject).append(" rdf:type ?any.\n");
					whereQl.append(tabs).append("\tfilter (").append("?").append(subject)
						.append("=").append(iri(where.getEntityId().getIri())).append(") ");

			}

		if (where.getAnd()!=null) {
			String andSubject=subject;
			for (Match match : where.getAnd()) {
						whereProperty(whereQl, andSubject, level, match);
						andSubject= match.getValueVar();
					}
		}
		if (where.getOptional()!=null){
					for (Match match:where.getOptional()) {
						whereQl.append("OPTIONAL {");
						whereProperty(whereQl, originalSubject, level, match);
						whereQl.append("}\n");
					}
		}
		if (where.getOr()!=null){
					boolean first=true;
					for (Match match:where.getOr()) {
						if (!first)
							whereQl.append("UNION ");
						first=false;
						whereQl.append(" {");
						whereProperty(whereQl, subject, level, match);
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



	private void wildCard(StringBuilder whereQl,String subject, int level) {
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
		lessIndent();
	}




	/**
	 * Proecesses the where property clause, the remaining match clause including subqueries
	 * @param whereQl the string builder sparql
	 * @param subject the parent subject passed to this where clause
	 * @param level the nested level built thus far to support nested selects
	 * @param where the where clause
	 */
	private void whereProperty(StringBuilder whereQl, String subject,int level, Match where) throws DataFormatException {
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

	/**
	 * validates a data set definition against
	 * @param query
	 */
	public void validateQuery(DataSet query){

	}




}
