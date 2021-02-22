package org.endeavourhealth.converters;

import org.antlr.v4.runtime.ParserRuleContext;
import org.endeavourhealth.imapi.model.Concept;
import org.endeavourhealth.informationmanager.parser.IMLangBaseListener;
import org.endeavourhealth.informationmanager.parser.IMLangParser.CodeContext;
import org.endeavourhealth.informationmanager.parser.IMLangParser.DescriptionContext;
import org.endeavourhealth.informationmanager.parser.IMLangParser.IdentifierIriContext;
import org.endeavourhealth.informationmanager.parser.IMLangParser.NameContext;

public class IMLangListener extends IMLangBaseListener {

    Concept concept = new Concept();

    @Override
    public void enterEveryRule(ParserRuleContext ctx) { // see gramBaseListener for allowed functions
        System.out.println("rule entered: " + ctx.getText()); // code that executes per rule
    }

    @Override
    public void enterIdentifierIri(IdentifierIriContext ctx) {
        concept.setIri(ctx.getText());
    }

    @Override
    public void enterName(NameContext ctx) {
        concept.setName(ctx.getText().replace("Name ", "").replaceAll("\"", ""));
    }

    @Override
    public void enterDescription(DescriptionContext ctx) {
        concept.setDescription(ctx.getText().replace("description ", "").replaceAll("\"", ""));
    }

    @Override
    public void enterCode(CodeContext ctx) {
        concept.setCode(ctx.getText().replace("code", "").replaceAll("\"", ""));
    }

    public Concept getConcept() {
        return concept;
    }

    public void setConcept(Concept concept) {
        this.concept = concept;
    }
}
