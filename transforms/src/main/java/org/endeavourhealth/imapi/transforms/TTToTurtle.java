package org.endeavourhealth.imapi.transforms;

import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.RDF;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Transforms a document or entity in the Triple tree node based form to Turtle.
 * The default serializations of TT Classes is JSON-LD. Turtle provides a more easily readable format
 */

public class TTToTurtle {
	private TTContext context = new TTContext();
	private StringBuilder turtle;
	private StringBuilder prefixes;
	private int level;
	private List<String> entityPrefixes = new ArrayList<>();


	/**
	 * Transforms a document to Turtle format.
	 * @param document the document in Triple Tree class format
	 * @return a string of turtle, with prefixed IRIs and tabs
	 */
	public String transformDocument(TTDocument document){
		this.context= document.getContext();
		turtle= new StringBuilder();
		setPrefixes();
		for (TTEntity entity:document.getEntities())
			appendEntity(entity);
		return turtle.toString();
	}

	public String transformEntity(TTEntity entity, TTContext context){
		StringBuilder result = new StringBuilder();
		this.context = context;
		turtle= new StringBuilder();
		prefixes = new StringBuilder();
		appendEntity(entity);
		setEntityPrefixes();
		result.append(prefixes)
				.append("\n")
		        .append(turtle);
		return result.toString();
	}

	private void nl(){
		turtle.append("\n");
		if (level>0) {
			StringBuilder indent = new StringBuilder();
			for(int i=0; i<level;i++){
				indent.append(" ");
			}
			turtle.append(indent);
		}
	}

	private void setEntityPrefixes() {
		if (context!=null)
			if (context.getPrefixes()!=null)
				for (TTPrefix prefix:context.getPrefixes()){
					if(entityPrefixes.contains(prefix.getIri())){
						prefixes.append("@prefix ")
								.append(prefix.getPrefix()+": ")
								.append("<"+prefix.getIri()+"> .")
						        .append("\n");
					}
				}
	}

	private void setPrefixes() {
		if (context!=null)
			if (context.getPrefixes()!=null)
				for (TTPrefix prefix:context.getPrefixes()){
					turtle.append("@prefix ")
					.append(prefix.getPrefix()+": ")
					.append("<"+prefix.getIri()+"> .");
					nl();
				}
		nl();
	}

	private void appendEntity(TTEntity entity){
		level=0;
		setIriandType(entity);
		if (entity.getPredicateMap()!=null){
			level=level+5;
			append(";");
			nl();
			setPredicateObjects(entity);
			append(" .");
			level=level-5;
			nl();
			nl();
		}

	}

	private void setPredicateObjects(TTNode node) {
		Map<TTIriRef, TTValue> predicateObjectList = node.getPredicateMap();
		if (predicateObjectList!=null) {
			int first = 1;
			for (Map.Entry<TTIriRef, TTValue> entry : predicateObjectList.entrySet()) {
				TTIriRef predicate = entry.getKey();
				if (!predicate.equals(RDF.TYPE)) {
					if (first > 1) {
						append(";");
						nl();
					}
					first++;
					append(getShort(predicate.getIri()) + " ");
					TTValue object = entry.getValue();
					setObject(object);
				}
			}
		}
	}

	private void setObject(TTValue value){
			if (value.isIriRef())
				append(getShort(value.asIriRef().getIri()));
			else if(value.isLiteral()){
				if (value.asLiteral().getType()==null)
					append("\""+ value.asLiteral().getValue()+"\"");
				else {
						append("\""+value.asLiteral().getValue()+"\"^^"+ getShort(value.asLiteral().getType().getIri()));
				}
			} else if (value.isList()){
				append("(");
				level=level+5;
				nl();
				int firstIn=1;
				for (TTValue entry:value.asArray().getElements()){
					if (firstIn>1){
						append(" ");
						nl();
					}
					firstIn++;
					setObject(entry);
				}

				nl();
				append(")");
				level=level-5;
			} else{ ;
				append("[");
				level=level+1;
				setPredicateObjects(value.asNode());
				append("]");
				level=level-1;

			}
	}

	private void setIriandType(TTEntity entity) {
		append(getShort(entity.getIri()));
		append(" a ");
		int first=1;
		if(entity.getType()!=null){
			for (TTValue type:entity.getType().getElements()){
				if (first>1)
					append(" , ");
				append(getShort(type.asIriRef().getIri()));
				first++;
			}
		}

	}

	private StringBuilder append(String aString){
		turtle.append(aString);
		return turtle;
	}

	private String getShort(String iri) {
		if (iri.contains("#")) {
			int lnPos = iri.indexOf("#") + 1;
			String ns= iri.substring(0,lnPos);
			String ln= iri.substring(lnPos);
			if(!entityPrefixes.contains(ns))
			    entityPrefixes.add(ns);
			return getPrefix(ns)+ ln;
		}
		return iri;
	}

	public TTToTurtle addPrefix(TTPrefix directive) {
		addPrefix(directive.getIri(), directive.getPrefix());
		return this;
	}
	public String getPrefix(String iri) {
			return context.prefix(iri);
	}

	public TTToTurtle addPrefix(String iri, String prefix) {
		context.add(iri, prefix);
		return this;
	}

	public TTContext getContext() {
		return context;
	}

	public TTToTurtle setContext(TTContext context) {
		this.context = context;
		return this;
	}
}
