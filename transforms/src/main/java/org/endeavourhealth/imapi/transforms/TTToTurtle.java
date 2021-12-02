package org.endeavourhealth.imapi.transforms;

import org.endeavourhealth.imapi.model.tripletree.*;

import org.endeavourhealth.imapi.vocabulary.RDF;

import java.util.ArrayList;
import java.util.List;
import org.endeavourhealth.imapi.query.Query;
import org.endeavourhealth.imapi.vocabulary.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Transforms a document or entity in the Triple tree node based form to Turtle.
 * The default serializations of TT Classes is JSON-LD. Turtle provides a more easily readable format
 */


public class TTToTurtle {
	private TTContext context;
	private StringBuilder turtle;
	private int level;
	private Map<String,String> prefixes= new HashMap<>();

	public TTToTurtle(){
	}


	/**
	 * Transforms a document to Turtle format.
	 * @param document the document in Triple Tree class format
	 * @return a string of turtle, with prefixed IRIs and tabs
	 */
	public String transformDocument(TTDocument document){
		this.context= document.getContext();
		turtle= new StringBuilder();
		nl();
		for (TTEntity entity:document.getEntities())
			appendEntity(entity);
		turtle.append("\n");

		insertPrefixes();
		return turtle.toString();
	}

	public String transformEntity(TTEntity entity, TTContext context){
		turtle = new StringBuilder();
		this.context = context;
		appendEntity(entity);
		insertPrefixes();
		return turtle.toString();
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

	private void insertPrefixes() {
		for (Map.Entry<String, String> entry : prefixes.entrySet()) {
			turtle.insert(0, "@prefix " + entry.getValue() + ": <" + entry.getKey() + "> .\n");

		}
	}

	public String transformEntity (TTEntity entity){
		return transformEntity(entity,entity.getContext());
	}


	private  void appendEntity(TTEntity entity){
		level=0;
		nl();
		append(getShort(entity.getIri()));
		if (entity.getPredicateMap()!=null){
			level=level+3;
			nl();
			setPredicateObjects(entity);
			append(" .");
			level=level-3;
		}

	}

	private void setPredicateObjects(TTNode node) {
		int nodeCount = 1;
		Map<TTIriRef, TTArray> predicateObjectList = node.getPredicateMap();
		if (predicateObjectList != null) {
			for (Map.Entry<TTIriRef, TTArray> entry : predicateObjectList.entrySet()) {
				TTIriRef predicate = entry.getKey();
				outputPredicateObject(predicate, entry.getValue(), nodeCount);
				nodeCount++;
					}
			}
	}

	private void outputPredicateObject(TTIriRef predicate,TTArray object,int nodeCount) {
        if (nodeCount > 1) {
            append(";");
            nl();
        }
        String pred = getShort(predicate.getIri()) + " ";
        append(pred);
        int olevel=level;
        setObject(object);
        level = olevel;
    }

	private void outputPredicateObject(TTIriRef predicate,TTValue object,int nodeCount) {
		if (nodeCount > 1) {
			append(";");
			nl();
		}
		String pred = getShort(predicate.getIri()) + " ";
		append(pred);
		int olevel=level;
		setObject(object);
		level = olevel;
	}



    private void setObject(TTArray value){
        int firstIn=1;
        if (value.size()>1){
            level=level+6;
            nl();
        }
        for (TTValue entry:value.iterator()){
            if (firstIn>1){
                append(" , ");
                nl();
            }
            firstIn++;
            setObject(entry);
        }
    }

	private void setObject(TTValue value){
			if (value.isIriRef())
				append(getShort(value.asIriRef().getIri()));
			else if(value.isLiteral()){
				if (value.asLiteral().getType()==null)
				 append("\""+ value.asLiteral().getValue().replace("\"","")+"\"");
				else {
					append("\""+value.asLiteral().getValue().replace("\"","")+"\"^^"+ getShort(value.asLiteral().getType().getIri()));

				}
			} else{ ;
				append("[");
				setPredicateObjects(value.asNode());
				append("]");

			}
	}



	private StringBuilder append(String aString){
		turtle.append(aString);
		return turtle;
	}

	private String getShort(String iri) {
		if (iri.contains("#")) {
			int lnPos = iri.indexOf("#") + 1;
			String ns = iri.substring(0, lnPos);
			String ln = iri.substring(lnPos);
			String prefix=getPrefix(ns);
			if (prefix!=null)
				return prefix+ ":" + ln;
			else
				return iri;
		} else
			return iri;
	}


	private String getPrefix(String ns) {
		if (prefixes.get(ns)!=null)
			return prefixes.get(ns);
		String prefix=context.getPrefix(ns);
		if (prefix!=null){
			prefixes.put(ns,prefix);
			return prefix;
		} else
			return null;
	}



}
