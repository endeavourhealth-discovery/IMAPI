package org.endeavourhealth.imapi.transforms;

import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.query.Filter;
import org.endeavourhealth.imapi.query.Path;
import org.endeavourhealth.imapi.query.Query;
import org.endeavourhealth.imapi.query.QueryTriple;
import org.endeavourhealth.imapi.vocabulary.IMQ;

import java.util.HashMap;
import java.util.Map;

public class TTToSPARQL {
	private TTContext context;
	private Map<String,String> prefixes= new HashMap<>();
	int tabs;
	private StringBuilder spq;


	/**
	 * Returns the query definition on SPARQL format
	 * @param context standard prefix namespace list;
	 * @param includePrefixes whether prefixes are included in the result;
	 * @return String of SPARQL
	 */
	public String transform(Query query, TTContext context, boolean includePrefixes){
		this.context=context;
		spq= new StringBuilder();
		if (includePrefixes)
			nl();
		convertSelect(query);
		convertWhere(query);
		return spq.toString();
	}
	private void nl(){
		spq.append("\n");
		for (int i=0;i<tabs;i++)
			spq.append(" ");
	}

	private void convertSelect(Query query){
		if (query.get(IMQ.SELECT)!=null){
			spq.append("SELECT ");
			for (TTValue entry:query.get(IMQ.SELECT).asArray().getElements()){
				TTNode select= entry.asNode();
				if (select.get(IMQ.AS)!=null){
					if (select.get(IMQ.EXPRESSION)!=null)
						spq.append("("+select.get(IMQ.EXPRESSION).asLiteral().getValue()+
							" AS ?"+select.get(IMQ.AS).asLiteral().getValue()+") ");
					else
						spq.append("(?"+select.get(IMQ.VAR).asLiteral().getValue()+" ?"+
							select.get(IMQ.AS)+")");
				} else if (select.get(IMQ.VAR)!=null)
					spq.append("?"+ select.get(IMQ.VAR).asLiteral().getValue());

			}
			nl();
		}
	}

	private void convertWhere(Query query) {
		if (query.get(IMQ.WHERE) != null) {
			TTNode where = query.get(IMQ.WHERE).asNode();
			spq.append("WHERE");
			tabs=tabs+2;
			nl();
			spq.append("{ ");
			tabs = tabs + 2;
			if (where.get(IMQ.UNION) != null) {
				spq.append("{ ");
				tabs = tabs + 2;
			}
			convertClause(where);
			if (where.get(IMQ.UNION)!=null){
				for (TTValue union:where.get(IMQ.UNION).asArray().getElements()){
					tabs=tabs-4;
					nl();
					spq.append("UNION");
					tabs=tabs+2;
					nl();
					spq.append("{ ");
					tabs=tabs+2;
					convertClause(union.asNode());
				}
				spq.append("}");
			}

			}

	}

	private void convertFilter(TTNode clause) {
		for (TTValue entry:clause.get(IMQ.FILTER).asArray().getElements()) {
			Filter filter = new Filter(entry.asNode());
			nl();
			spq.append("FILTER (");
			TTIriRef filterType = filter.getType();
			if (filter.get(IMQ.VAR) != null)
				spq.append("?" + filter.get(IMQ.VAR).asLiteral().getValue());
			if (filterType.equals(IMQ.IN_LIST)) {
				spq.append(" IN ");
				convertList(filter.get(IMQ.EXPRESSION).asArray());
			}
			spq.append(")");
		}
	}
	private void convertList(TTArray list){
		spq.append("(");
		boolean first=true;
		for (TTValue entry:list.getElements()){
			if (!first)
				spq.append(" , ");
			first=false;
			if (entry.isIriRef())
				spq.append(getPrefix(entry.asIriRef().getIri()));
		}
		spq.append(")");

	}
	private void pad(int no){
		for (int i=0;i<no;i++)
			spq.append(" ");
	}


	private void convertClause(TTNode clause) {
		if (clause.get(IMQ.TRIPLE) != null) {
			TTValue commonSubject = null;
			for (TTValue entry : clause.get(IMQ.TRIPLE).asArray().getElements()) {
				QueryTriple triple = new QueryTriple(entry.asNode());
				TTValue entity = triple.get(IMQ.ENTITY);
				if (commonSubject != null) {
					if (sameValue(entity,commonSubject)) {
						spq.append(";");
						nl();
					} else {
						spq.append(".");
						nl();
					}
				}

				if (entity.isLiteral())
					if (sameValue(entity,commonSubject))
						pad(entity.asLiteral().getValue().length()+2);
					else
						spq.append("?" + entity.asLiteral().getValue() + " ");
				else
					if (sameValue(entity,commonSubject))
						pad(getPrefix(entity.asIriRef().getIri()).length()+1);
					else
   					spq.append(getPrefix(entity.asIriRef().getIri()) + " ");
				commonSubject = entity;
				Path path = new Path(triple.get(IMQ.PROPERTY).asNode());
				TTIriRef pathType= path.getPathType();
				TTValue pathValue= path.getPath();
				if (pathType.equals(IMQ.VAR))
					spq.append("?"+ pathValue.asLiteral().getValue());
				else if (pathType.equals(IMQ.IRI))
					spq.append(getPrefix(pathValue.asIriRef().getIri()));
				else if (pathType.equals(IMQ.SEQUENCE))
					buildSeqence(pathValue.asArray(),"/");
				else if (pathType.equals(IMQ.ALTERNATIVE))
					buildSeqence(pathValue.asArray(),"|");
				else if (pathType.equals(IMQ.INVERSE))
					spq.append("^"+getPrefix(pathValue.asIriRef().getIri()));
				else if (pathType.equals(IMQ.ONE_OR_MORE))
					spq.append(getPrefix(pathValue.asIriRef().getIri()+"*"));
				spq.append(" ");
				TTValue value = triple.get(IMQ.VALUE);
				if (value.isLiteral())
					spq.append("?"+value.asLiteral().getValue());
				else
					spq.append(getPrefix(value.asIriRef().getIri()));
			}
			spq.append(".");
			if (clause.get(IMQ.FILTER)!=null){
				convertFilter(clause.asNode());
			}
			if (clause.get(IMQ.MINUS)!=null){
				nl();
				spq.append("MINUS");
				tabs=tabs+2;
				nl();
				spq.append("{ ");
				tabs=tabs+2;
				convertClause(clause.get(IMQ.MINUS).asNode());
				tabs=tabs-4;
			}
			spq.append("}");
		}
	}
	private boolean sameValue(TTValue one,TTValue two){
	if (one instanceof TTIriRef) {
		if (two instanceof TTIriRef)
			return ((TTIriRef) one).equals((TTIriRef) two);
		else
			return false;
	}
	if (one instanceof TTLiteral) {
		if (two instanceof TTLiteral)
			return ((TTLiteral) one).getValue().equals(((TTLiteral) two).getValue());
		else
			return false;
	}
	else return one.equals(two);
	}

	private void buildSeqence(TTArray pathValue, String delim) {
		boolean first=true;
		for (TTValue entry:pathValue.getElements()){
			if (!first)
				spq.append(" "+delim+" ");
			first=false;
			if (delim.equals("/"))
			spq.append(getPrefix(entry.asIriRef().getIri()));
		}
	}

	public String getPrefix(String iri) {
		String prefixForm= context.prefix(iri);
		if (prefixForm.startsWith("http")) {
			prefixForm = "<" + prefixForm + ">";
		}
		else
			insertPrefix(prefixForm.split(":")[0]);
		return prefixForm;
	}
	private void insertPrefix(String prefix) {
		if (prefixes.get(prefix) == null) {
			String iri = context.getNamespace(prefix);
			prefixes.put(prefix, iri);
			spq.insert(0, "PREFIX " + prefix + ": <"+iri+">\n");

		}
	}

}
