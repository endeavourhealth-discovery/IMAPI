package org.endeavourhealth.imapi.transforms;

import org.endeavourhealth.imapi.model.sets.DataSet;
import org.endeavourhealth.imapi.model.sets.Match;
import org.endeavourhealth.imapi.model.sets.Select;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.*;

import java.util.*;
import java.util.zip.DataFormatException;

/**
 * Methods to convert a Query object to its Sparql equivalent
 */
public class IMToSparql {
	private TTContext prefixes;
	private final Map<String,String> propertyVars = new HashMap<>();
	private final Map<String,String> aliasMap= new HashMap<>();
	private String tabs="";
	private int o=0;
	private DataSet query;
	private java.util.Set aliases = new HashSet<>();



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
	public String convert(DataSet query) throws DataFormatException {
		this.query= query;
		StringBuilder selectQl= new StringBuilder();
		selectQl.append(getDefaultPrefixes());
		selectQl.append("SELECT ");
		StringBuilder whereQl= new StringBuilder();
		whereQl.append("WHERE {");
		where(whereQl, "?entity", "", query.getMatch());
		whereQl.append("}");
		select(selectQl,"?entity",query);
		selectQl.append(whereQl.toString());
		return selectQl.toString();
	}

	/**
	 * Processes the dataSet clause and binds to the variables created in the where clause
	 * @param selectQl Sparql dataSet clause thus far
	 * @param dataSet the dataSet property value being selected
	 */
	private void select(StringBuilder selectQl, String subject, DataSet dataSet) throws DataFormatException {
		if (dataSet.getSelect()!=null) {
			for (Select selection : dataSet.getSelect()) {
				String var = selection.getVar();
				if (isId(var))
					selectQl.append(subject).append(" ");
				else {
					if (selection.getObject() != null) {
						select(selectQl, var, selection.getObject());
					} else {
						String alias = selection.getAlias();
						if (alias != null) {
							if (!alias.equals(var)) {
								if (aliases.contains(alias))
									throw new DataFormatException("Cannot have two aliases of the same name in the same query : " + selection.getAlias());
								selectQl.append("(").append("?").append(var).append(" as ").append("?").append(alias).append(") ");
								aliases.add(selection.getAlias());
								aliasMap.put(var, alias);
							}
							else
								selectQl.append("?").append(var).append(" ");

						} else
							selectQl.append("?").append(var).append(" ");

					}
				}
			}
			selectQl.append("\n");
		}
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
		if (where.getOr()!=null)
			whereQl.append("{");
		if (where.getEntityType() != null) {
			whereQl.append(tabs).append(subject).append(" rdf:type ").append(iri(where.getEntityType().getIri())).append(".\n");
		}
		if (where.getEntityId()!=null)
			if (!where.isIncludeMembers())
				whereQl.append(tabs).append("\tfilter (").append(subject).append("=").append(iri(where.getEntityId().getIri())).append(") \n");

		if (where.getAnd()!=null) {
			for (Match match : where.getAnd()) {
				whereProperty(whereQl, subject, "", match);
			}
		}
		if (where.getMay()!=null){
			for (Match match:where.getMay()) {
				whereQl.append("OPTIONAL {");
				whereProperty(whereQl, subject, "", match);
				whereQl.append("}\n");
			}
		}
		if (where.getOr()!=null){
			for (Match match:where.getOr()) {
				whereQl.append("UNION {");
				whereProperty(whereQl, subject, "", match);
				whereQl.append("}\n");
			}
		}

			if (where.isIncludeMembers()) {
				TTIriRef superClass= where.getEntityId();
				if (superClass==null)
					superClass= where.getEntityType();
				if (where.getEntityId() != null)
					throw new DataFormatException("Include members only supports specific entity ids ");
				whereQl.append(new SetToSparql().getExpansionSparql(superClass.getIri()));
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

		propertyVars.put(predicate,object);
		String inverse="";
		if (where.isInverseOf())
			inverse="^";
		whereQl.append(tabs).append(subject).append(" ").append(inverse).append(iri(predicate)).append(" ?")
			.append(object).append(".\n");
		if (where.getValueIn()!=null){
			List<String> inList= new ArrayList<>();
			for (TTIriRef in:where.getValueIn()){
				inList.add(iri(in.getIri()));
			}
			String inString= String.join(",",inList);
			whereQl.append(tabs).append("\tfilter (?").append(object).append(" in (").append(inString).append("))\n");
		}
		else if (where.getValueObject()!=null){
			where(whereQl,"?"+ object,path+predicate+" / ",where.getValueObject());
		}
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

	public Map<String, String> getPropertyVars() {
		return propertyVars;
	}

	public Map<String, String> getAliasMap() {
		return aliasMap;
	}
}
