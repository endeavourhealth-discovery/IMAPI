package org.endeavourhealth.imapi.transforms;

import org.endeavourhealth.imapi.dataaccess.EntityTripleRepository;
import org.endeavourhealth.imapi.model.query.Match;
import org.endeavourhealth.imapi.model.query.Query;
import org.endeavourhealth.imapi.model.query.Selection;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.*;

import java.util.*;
import java.util.zip.DataFormatException;

/**
 * Methods to convert a Query object to its Sparql equivalent
 */
public class IMQToSparql {
	private TTContext prefixes;
	private final Map<String,String> propertyMap = new HashMap<>();
	private final Map<String,String> aliasMap= new HashMap<>();
	private String tabs="";
	private int o=0;
	private Query query;
	private Set<String> aliases = new HashSet<>();



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
		sparql.append("PREFIX sn: <"+ SNOMED.NAMESPACE+">\n\n");
		return sparql.toString();
	}

	/**
	 * Takes an IMQ select query model and converts to SPARQL
	 * @param query Query Obiect containing select where
	 * @return String of SPARQL
	 */
	public String convert(Query query) throws DataFormatException {
		this.query= query;
		StringBuilder selectQl= new StringBuilder();
		selectQl.append(getDefaultPrefixes());
		selectQl.append("SELECT ");
		StringBuilder whereQl= new StringBuilder();
		whereQl.append("WHERE {");
		where(whereQl,"?entity","",query.getWhere());
		whereQl.append("}");
		select(selectQl,"","?entity",query.getSelect());
		selectQl.append(whereQl.toString());
		return selectQl.toString();
	}

	/**
	 * Processes the select clause and binds to the variables created in the where clause
	 * @param selectQl Sparql select clause thus far
	 * @param path path for nested
	 * @param select the list of selections
	 */
	private void select(StringBuilder selectQl,String path, String subject,List<Selection> select) throws DataFormatException {
		for (Selection selection:select){
				String property= selection.getProperty().getIri();
				if (isId(property))
					selectQl.append(subject).append(" ");
				else {
					String var = propertyMap.get(path+property);
					if (selection.getSelect() != null) {
						select(selectQl, path + property + "/", var, selection.getSelect());
					}
					else {
					String alias = selection.getAlias();
					if (alias != null) {
						if (aliases.contains(alias))
							throw new DataFormatException("Cannot have two aliases of the same name in the same query : " + selection.getAlias());
						selectQl.append("(").append("?").append(var).append(" as ").append("?").append(alias).append(") ");
						aliases.add(selection.getAlias());
						aliasMap.put(var, alias);
					} else
						selectQl.append("?").append(var).append(" ");

					}
				}
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
	private void where(StringBuilder whereQl,String subject,String path ,Match where) throws DataFormatException {

		whereGraph(whereQl,where);
		if (where.getEntityType()!=null){
			whereQl.append(tabs).append(subject).append(" rdf:type ").append(iri(where.getEntityType().getIri())).append(".\n");
		}
		else if (where.getEntityId()!=null){
			if (!where.isIncludeMembers())
				whereQl.append(tabs).append("\tfilter (").append(subject).append("=").append(iri(where.getEntityId().getIri())).append(") \n");
		}
		if (where.getProperty()!=null){
			whereProperty(whereQl,subject,path,where);

		}
		whereSelect(whereQl,query.getSelect(), "?entity","");
		if (where.getGraph()!=null) {
			lessIndent();
			whereQl.append("}");
		}
		if (where.isIncludeMembers()){
			if (where.getEntityId()==null)
				throw new DataFormatException("Include members only supports specific entity ids ");
			whereQl.append(new SetToSparql().getExpansionSparql(where.getEntityId().getIri()));
		}
	}



	/**
	 * Checks the select clause for properties not used in the where clause and adds them as optional.
	 * @param whereQl  the Sparql where clause thus far
	 * @param select the list of selections from the select clause
	 * @param subject the Sparql subject passed in - ?entity at the outer level
	 * @param path the property path from nested objects
	 */
	private void whereSelect(StringBuilder whereQl,List<Selection> select,
													 String subject,String path) {
		for (Selection  selection:select){
				String predicate= selection.getProperty().getIri();
				String inverse="";
				if (selection.isInverseOf())
					inverse="^";
				if (!isId(predicate)) {
					if (propertyMap.get(path+predicate) == null) {
						String object = nextObject(path,predicate);
						whereQl.append(tabs).append(" OPTIONAL {").append(subject).append(" ").append(inverse).append(iri(predicate)).append(" ").append(object);
						if (selection.getSelect()!=null){
							whereQl.append(".\n");
							path=path+predicate+"/";
							whereSelect(whereQl,selection.getSelect(),object,path);
						}
						whereQl.append("}\n");
					}
				}
		}
	}

	/**
	 * is the variable im:id or iri i.e. the id
	 * @param predicate the predicate to test
	 * @return tru of the predicate is for an iri
	 */
	public static boolean isId(String predicate){
		if (predicate.equals("id"))
			return true;
		if (predicate.contains(":")) {
			if (predicate.substring(predicate.indexOf(":") + 1).equals("id"))
				return true;
		}
		return predicate.equals("iri");
	}


	/**
	 * Proecesses the where property clause, the remaining match clause including subqueries
	 * @param subject the parent subject passed to this where clause
	 * @param path the path built thus far to support nested selects
	 * @param where the where clause
	 */
	private void whereProperty(StringBuilder whereQl, String subject,String path, Match where) {
		String predicate= where.getProperty().getIri();
		String object= nextObject(path,predicate);
		String inverse="";
		if (where.isInverseOf())
			inverse="^";
		whereQl.append(tabs).append(subject).append(" ").append(inverse).append(iri(predicate)).append(" ")
			.append(object).append(".\n");
		if (where.getValueIn()!=null){
			List<String> inList= new ArrayList<>();
			for (TTIriRef in:where.getValueIn()){
				inList.add(iri(in.getIri()));
			}
			String inString= String.join(",",inList);
			whereQl.append(tabs).append("\tfilter (").append(object).append(" in (").append(inString).append("))\n");
		}
	}


	private String nextObject(String path,String predicate){
		o++;
		String debugObject="";
		if (!path.equals(""))
		   debugObject= path.replaceAll("[^a-zA-Z0-9:]","").replace(":","_")
		+ "_";
		debugObject=debugObject+ (predicate.replaceAll(
			"[^a-zA-Z0-9:]", "").replace(":","_"))+o;
		propertyMap.put(path+predicate,debugObject);
		return ("?"+debugObject);
	}

	private void indent(){
		tabs=tabs+"\t";
	}
	private void lessIndent(){
		if (!tabs.equals(""))
		tabs=tabs.substring(0,tabs.length()-1);
	}

	private void whereGraph(StringBuilder whereQl,Match where) {
		if (where.getGraph()!=null) {
			indent();
			whereQl.append("graph ").append(iri(where.getGraph().getIri())).append(" { \n");
		}
	}

	private String iri(String iri){
		if (iri.startsWith("http")||iri.startsWith("urn:"))
		  return "<"+ iri+">";
		else return iri;
	}

	public Map<String, String> getPropertyMap() {
		return propertyMap;
	}

	public Map<String, String> getAliasMap() {
		return aliasMap;
	}
}
