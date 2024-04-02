package org.endeavourhealth.imapi.transforms;

import org.endeavourhealth.imapi.model.tripletree.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TTToNQuad {
	private List<String> quads;
	private static int blank;
	String graph;

	public List<String> transformEntity(TTEntity entity,String graph){
		quads= new ArrayList<>();
		this.graph=graph;
		appendEntity(entity);
		return quads;
	}

	private  void appendEntity(TTEntity entity){
		String subject= "<"+entity.getIri()+"> ";
		if (entity.getPredicateMap()!=null){
			setPredicateObjects(subject,entity);
		}

	}

	private void setPredicateObjects(String subject, TTNode node) {
		Map<TTIriRef, TTArray> predicateObjectList = node.getPredicateMap();
		if (predicateObjectList != null) {
			for (Map.Entry<TTIriRef, TTArray> entry : predicateObjectList.entrySet()) {
				String predicate = "<"+entry.getKey().getIri()+"> ";
				TTArray value= entry.getValue();
				if ((value!=null) && (!value.isEmpty())) {
					for (TTValue val:value.getElements()) {
						setObject(subject, predicate, val);
					}
				}
			}
		}
	}

	private void setObject(String subject,String predicate, TTValue value){
		if (value.isIriRef())
			quads.add(subject+ predicate + "<"+ value.asIriRef().getIri()+"> <"+graph+">.");
		else if(value.isLiteral()){
			String data= value.asLiteral().getValue();
			data= data.replace("\\","\\\\").replace("\n","\\n").replace("\r","\\r").replace("\"","\\\"");
			if (value.asLiteral().getType()==null)
				quads.add(subject+ predicate+ "\""+ data+"\" <"+graph+">.");
			else {
				quads.add(subject+predicate+ "\""+data+"\"^^<"+ value.asLiteral().getType().getIri()+"> <"+graph+">.");

			}
		} else {
			blank++;
			String blankNode= "_:b"+blank;
			quads.add(subject+ predicate+ blankNode+" <"+graph+">.");
			setPredicateObjects(blankNode,value.asNode());

		}
	}
}
