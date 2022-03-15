package org.endeavourhealth.imapi.transforms;

import org.endeavourhealth.imapi.model.tripletree.*;

import java.util.HashMap;
import java.util.Map;

/**
	* Transforms an entity in the Triple tree node based form to Spqarql triples.
 * It does not include line feeds.
	* The default serializations of TT Classes is JSON-LD. Turtle provides a more easily readable format
	*/

public class TTToSPARQL {

		private StringBuilder turtle;
		private int level;
		private Map<String,String> prefixes= new HashMap<>();


		public String transformEntity(TTEntity entity){
			turtle = new StringBuilder();
			appendEntity(entity);
			append("\n");
			return turtle.toString();
		}



		private void nl() {
			turtle.append(" ");
		}




		private  void appendEntity(TTEntity entity){
			level=0;
			nl();
			append("<"+entity.getIri()+"> ");
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
					TTArray value= entry.getValue();
					if (value!=null)
						if (!value.isEmpty()) {
							outputPredicateObject(predicate, entry.getValue(), nodeCount);
							nodeCount++;
						}
				}
			}
		}

		private void outputPredicateObject(TTIriRef predicate,TTArray object,int nodeCount) {
			if (nodeCount > 1) {
				append(";");
				nl();
			}
			String pred = "<" +predicate.getIri() + "> ";
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
				append("<"+ value.asIriRef().getIri()+">");
			else if(value.isLiteral()){
				String data= value.asLiteral().getValue();
				data=data.replace("\\","\\\\");
				data= data.replace("\n","\\n").replace("\r","\\r").replace("\"","");
				if (value.asLiteral().getType()==null)
					append("\""+ data+"\"");
				else {
					append("\""+data+"\"^^<"+ value.asLiteral().getType().getIri()+">");

				}
			} else {
				append("[");
				setPredicateObjects(value.asNode());
				append("]");

			}
		}



		private StringBuilder append(String aString){
			turtle.append(aString);
			return turtle;
		}




	}
