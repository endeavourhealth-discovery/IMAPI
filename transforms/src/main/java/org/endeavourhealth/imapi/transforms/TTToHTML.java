package org.endeavourhealth.imapi.transforms;

import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.OWL;

import java.util.Map;
import java.util.zip.DataFormatException;

public class TTToHTML {

	public String getExpressionText(TTNode expression) {
		StringBuilder html = new StringBuilder();
		boolean first = true;
		if (expression.get(OWL.INTERSECTIONOF) != null) {
			for (TTValue inter : expression.get(OWL.INTERSECTIONOF).asArray().getElements()) {
				if (inter.isIriRef()) {
					if (!first)
						html.append(" and ");
					first = false;
					html.append(inter.asIriRef().getName());
				} else {
					if (!first)
						html.append("<br> having <br> ");
					first = false;
					setRoleGroup(inter.asNode(), html,40);
				}
			}
		}
		return html.toString();
	}

	public void setRoleGroup(TTNode roleGroup, StringBuilder html,int tab){
		boolean firstProp= true;
		for (Map.Entry<TTIriRef, TTValue> entry : roleGroup.getPredicateMap().entrySet()) {
				html.append("<p style=\"margin-left: "+tab+"px\">");
			firstProp=false;
			html.append(entry.getKey().getName()+ "->");
			if (entry.getValue().isIriRef()){
				html.append(entry.getValue().asIriRef().getName());
				html.append("</p>");
			} else if (entry.getValue().isNode()){
				  html.append("(");
				  setRoleGroup(entry.getValue().asNode(),html, tab+20);
				  html.append(")");
			}

			}
		}
	}

