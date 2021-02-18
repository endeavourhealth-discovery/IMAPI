package org.endeavourhealth.converters;

import org.endeavourhealth.informationmanager.parser.IMLangBaseVisitor;
import org.endeavourhealth.informationmanager.parser.IMLangParser.IriContext;
import org.endeavourhealth.informationmanager.parser.IMLangParser.NameContext;
import org.endeavourhealth.informationmanager.parser.IMLangParser.TypeContext;

public class ImLangVisitor extends IMLangBaseVisitor<Object> {
	
	@Override
	public String visitIri(IriContext ctx) {
		// TODO Auto-generated method stub
		return "rule visit: " + ctx.getText();
	}
	
	@Override
	public String visitType(TypeContext ctx) {
		return "rule visit: " + ctx.getText();
	}
	
	@Override
	public String visitName(NameContext ctx) {
		// TODO Auto-generated method stub
		return "rule visit: " + ctx.getText();
	}

}
