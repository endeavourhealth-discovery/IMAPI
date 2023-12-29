package org.endeavourhealth.imapi.transforms;

import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.OWL;

import java.util.Map;

public class TTToHTML {
	private TTToHTML() {
		throw new IllegalStateException("Utility class");
	}

	public static String getExpressionText(TTNode expression) {
		StringBuilder html = new StringBuilder();
		boolean first = true;
		if (expression.get(OWL.INTERSECTION_OF) != null) {
			html.append("<p class=\"intersection\">Intersection of</p>");
			for (TTValue inter : expression.get(OWL.INTERSECTION_OF).iterator()) {
				if (inter.isIriRef()) {
					if (!first)
						html.append("<p class=\"and\" style=\"margin-left: 40px;\">and</p> ");
					first = false;
					html.append("<p class=\"name\" style=\"margin-left: 40px;\">");
					html.append(inter.asIriRef().getName());
					html.append("</p>");
				} else {
					if (!first)
						html.append("<p class=\"having\" style=\"margin-left: 40px;\">having</p> ");
					first = false;
					setRoleGroup(inter.asNode(), html,80);
				}
			}
		}
		return html.toString();
	}

	public static void setRoleGroup(TTNode roleGroup, StringBuilder html,int tab){
		for (Map.Entry<TTIriRef, TTArray> entry : roleGroup.getPredicateMap().entrySet()) {
				html.append("<p class=\"role-group\" style=\"margin-left: ").append(tab).append("px\">");
			html.append(entry.getKey().getName()).append("->");
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

