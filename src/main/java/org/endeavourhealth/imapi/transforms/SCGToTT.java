package org.endeavourhealth.imapi.transforms;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTNode;
import org.endeavourhealth.imapi.parser.scg.SCGLexer;
import org.endeavourhealth.imapi.parser.scg.SCGParser;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.SNOMED;

import java.util.zip.DataFormatException;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class SCGToTT {
  private TTEntity entity;
  private final SCGLexer lexer;
  private final SCGParser parser;
  private String scg;

  public SCGToTT() {
    this.lexer = new SCGLexer(null);
    this.parser = new SCGParser(null);
    this.parser.removeErrorListeners();
    this.parser.addErrorListener(new ParserErrorListener());
  }

  public TTEntity setDefinition(TTEntity entity, String scgInput) throws DataFormatException {
    this.scg = scgInput;
    this.entity = entity;
    lexer.setInputStream(CharStreams.fromString(scgInput));
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    parser.setTokenStream(tokens);
    SCGParser.ExpressionContext ctx = parser.expression();
    return convertECContext(ctx);
  }

  private TTEntity convertECContext(SCGParser.ExpressionContext ctx) throws DataFormatException {
    if (ctx.definitionstatus() != null && ctx.definitionstatus().equivalentto() != null) {
      entity.set(IM.DEFINITIONAL_STATUS.asIri(), IM.SUFFICIENTLY_DEFINED.asIri());
    }
    if (ctx.subexpression() != null)
      convertSubexpression(ctx.subexpression());
    return entity;
  }

  private void convertSubexpression(SCGParser.SubexpressionContext subexpression) throws DataFormatException {
    if (subexpression.focusconcept() != null) {
      for (SCGParser.ConceptreferenceContext concept : subexpression.focusconcept().conceptreference()) {
        entity.addObject(iri(IM.IS_A), getConRef(concept.conceptid()));
      }
    }
    if (subexpression.refinement() != null && subexpression.refinement().attributeset() != null) {
      convertAttributeSet(entity, subexpression.refinement().attributeset());
    }
  }

  private void convertAttributeSet(TTEntity node, SCGParser.AttributesetContext attributeset) throws DataFormatException {
    for (SCGParser.AttributeContext attribute : attributeset.attribute()) {
      convertAttribute(node, attribute);
    }
  }

  private void convertAttribute(TTNode node, SCGParser.AttributeContext attribute) throws DataFormatException {
    TTIriRef property = getConRef(attribute.attributename().conceptreference().conceptid());
    if (attribute.attributevalue().expressionvalue() != null) {
      TTIriRef value = getConRef(attribute.attributevalue().expressionvalue().conceptreference().conceptid());
      node.set(property, value);
    } else {
      TTNode value = new TTNode();
      node.set(property, value);
      convertSubexpression(attribute.attributevalue().expressionvalue().subexpression());
    }

  }

  private TTIriRef getConRef(SCGParser.ConceptidContext conceptId) throws DataFormatException {
    String code = conceptId.getText();
    if (code.matches("[0-9]+")) {
      if (code.contains("1000252"))
        return TTIriRef.iri(IM.NAMESPACE + code);
      else
        return TTIriRef.iri(SNOMED.NAMESPACE + code);
    } else
      throw new DataFormatException("ECL converter can only be used for snomed codes at this stage");
  }
}
