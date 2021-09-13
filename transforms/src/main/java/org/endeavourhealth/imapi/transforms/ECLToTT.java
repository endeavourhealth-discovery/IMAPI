package org.endeavourhealth.imapi.transforms;


import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.parser.ecl.ECLBaseVisitor;
import org.endeavourhealth.imapi.parser.ecl.ECLLexer;
import org.endeavourhealth.imapi.parser.ecl.ECLParser;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.OWL;
import org.endeavourhealth.imapi.vocabulary.RDF;

import java.util.UnknownFormatConversionException;

/**
 * Converts ECL to Discovery syntax, supporting commonly used constructs
 */
public class ECLToTT extends ECLBaseVisitor<TTValue> {
   private TTEntity entity;
   private final ECLLexer lexer;
   private final ECLParser parser;
   private String ecl;
   public static final String SN = "sn:";
   public static final String ROLE_GROUP = "sn:609096000";

   public ECLToTT() {
      this.lexer = new ECLLexer(null);
      this.parser = new ECLParser(null);
      this.parser.removeErrorListeners();
      this.parser.addErrorListener(new ECLErrorListener());
   }

   public TTValue getClassExpression(String ecl) {
      this.ecl = ecl;
      lexer.setInputStream(CharStreams.fromString(ecl));
      CommonTokenStream tokens = new CommonTokenStream(lexer);
      parser.setTokenStream(tokens);
      ECLParser.ExpressionconstraintContext eclCtx = parser.expressionconstraint();
      return visitExpressionconstraint(eclCtx);
   }

   @Override
   public TTValue visitExpressionconstraint(ECLParser.ExpressionconstraintContext ctx) {
      return convertECContext(ctx);
   }

   private TTValue convertECContext(ECLParser.ExpressionconstraintContext ctx) {
      if (ctx.subexpressionconstraint() != null) {
         return convertSubECContext(ctx.subexpressionconstraint());
      } else if (ctx.compoundexpressionconstraint() != null) {
         if (ctx.compoundexpressionconstraint().disjunctionexpressionconstraint() != null) {
            return convertDisjunction(ctx.compoundexpressionconstraint().disjunctionexpressionconstraint());

         } else if (ctx.compoundexpressionconstraint().exclusionexpressionconstraint() != null) {
            return convertExclusion(ctx.compoundexpressionconstraint().exclusionexpressionconstraint());

         } else if (ctx.compoundexpressionconstraint().conjunctionexpressionconstraint() != null) {
            return convertConjunction(ctx.compoundexpressionconstraint().conjunctionexpressionconstraint());

         } else {
            throw new UnknownFormatConversionException("Unknown ECL format " + ecl);
         }

      } else if (ctx.refinedexpressionconstraint() != null) {
         return convertRefined(ctx.refinedexpressionconstraint());
      } else {
         throw new UnknownFormatConversionException(("unknown ECL layout " + ecl));
      }
   }

   private TTValue convertRefined(ECLParser.RefinedexpressionconstraintContext refined) {
      TTNode exp;
      if (refined.subexpressionconstraint().expressionconstraint() != null) {
         exp = convertECContext(refined.subexpressionconstraint().expressionconstraint()).asNode();
      } else {
         exp = new TTNode();
         exp.set(OWL.INTERSECTIONOF,convertSubECContext(refined.subexpressionconstraint()));
      }
      ECLParser.EclrefinementContext refinement = refined.eclrefinement();
      ECLParser.SubrefinementContext subref = refinement.subrefinement();
      if (subref.eclattributeset() != null) {
         convertAttributeSet(exp, subref.eclattributeset());
      } else if (subref.eclattributegroup() != null) {
         convertAttributeGroup(exp, subref.eclattributegroup());
      } else
         throw new UnknownFormatConversionException("ECL attribute format not supported " + ecl);
      if (refinement.conjunctionrefinementset() != null) {
         for (ECLParser.SubrefinementContext subref2 : refinement.conjunctionrefinementset().subrefinement()) {
            convertAttributeGroup(exp, subref2.eclattributegroup());
         }
         return exp;

      } else
         return exp;
   }

   private TTValue convertAttributeSet(TTNode exp, ECLParser.EclattributesetContext eclAtSet) {
      if (eclAtSet.subattributeset() != null) {
         if (eclAtSet.subattributeset().eclattribute() != null) {
            return convertAttribute(eclAtSet.subattributeset().eclattribute());
         } else if (eclAtSet.conjunctionattributeset() != null) {
               convertAndAttributeSet(exp, eclAtSet.conjunctionattributeset());
               return exp;
            }
      }
      throw new UnknownFormatConversionException("ECL Attribute format not supoorted " + ecl);
   }

   private TTValue convertAndAttributeSet(TTNode exp, ECLParser
       .ConjunctionattributesetContext eclAtAnd) {
      TTArray inters= new TTArray();
      exp.set(OWL.INTERSECTIONOF,inters);
      for (ECLParser.SubattributesetContext subAt : eclAtAnd.subattributeset()) {
         inters.add(convertAttribute(subAt.eclattribute()));
      }
      return exp;
   }


   private TTValue convertConjunction(ECLParser.ConjunctionexpressionconstraintContext eclAnd) {
      TTNode exp = new TTNode();
      TTArray inters= new TTArray();
      exp.set(OWL.INTERSECTIONOF,inters);
      for (ECLParser.SubexpressionconstraintContext eclInter : eclAnd.subexpressionconstraint()) {
         inters.add(convertSubECContext(eclInter));
      }
      return exp;
   }

   private TTValue convertExclusion(ECLParser.ExclusionexpressionconstraintContext eclExc) {
      TTNode exp = new TTNode();
      TTArray ands = new TTArray();
      exp.set(OWL.INTERSECTIONOF,ands);
      ands.add(convertSubECContext(eclExc.subexpressionconstraint().get(0)));
      ands.add(new TTNode()
          .set(OWL.COMPLEMENTOF,convertSubECContext(eclExc.
              subexpressionconstraint().get(1))));
      return exp;
   }

   private TTValue convertDisjunction(ECLParser.DisjunctionexpressionconstraintContext eclOr) {
      TTNode exp = new TTNode();
      TTArray unions= new TTArray();
      exp.set(OWL.UNIONOF,unions);
      for (ECLParser.SubexpressionconstraintContext eclUnion : eclOr.subexpressionconstraint()) {
         unions.add(convertSubECContext(eclUnion));
      }
      return exp;
   }

   private TTValue convertAttribute(ECLParser.EclattributeContext attecl) {
      TTNode pv = new TTNode();
      pv.set(OWL.ONPROPERTY,getConRef(attecl.eclattributename()
          .subexpressionconstraint()
          .eclfocusconcept()
          .eclconceptreference()
          .conceptid()));
      if (attecl.expressioncomparisonoperator() != null) {
         if (attecl.expressioncomparisonoperator().EQUALS() != null) {
            if (attecl.subexpressionconstraint().eclfocusconcept() != null) {
               pv.set(OWL.ONCLASS,getConRef(attecl
                   .subexpressionconstraint().eclfocusconcept().eclconceptreference().conceptid()));
               return pv;
            } else {
               throw new UnknownFormatConversionException("multi nested ECL not yest supported " + ecl);
            }
         } else {
            throw new UnknownFormatConversionException("unknown comparison type operator " + ecl);
         }
      } else {
         throw new UnknownFormatConversionException("unrecognised comparison operator " + ecl);
      }
   }


   private TTIriRef getConRef(ECLParser.ConceptidContext conceptId) {

      TTIriRef conRef = new TTIriRef(SN + conceptId.getText());
      return conRef;
   }

   private TTValue convertAttributeGroup(TTNode exp,
                                                 ECLParser.EclattributegroupContext eclGroup) {

      TTArray roleExp = new TTArray();
      exp.set(OWL.INTERSECTIONOF,roleExp);
      TTNode roleGroup = new TTNode();
      roleGroup.set(RDF.TYPE,OWL.RESTRICTION);
      roleGroup.set(OWL.ONPROPERTY, IM.ROLE_GROUP);
      TTNode subGroup = new TTNode();
      roleGroup.set(OWL.ONCLASS,subGroup);
      convertAttributeSet(subGroup, eclGroup.eclattributeset());
      return exp;
   }

   private TTValue convertSubECContext(ECLParser.SubexpressionconstraintContext eclSub) {

      if (eclSub.expressionconstraint() != null) {
         return convertECContext(eclSub.expressionconstraint());
      } else {
         if (eclSub.eclfocusconcept() != null) {
            ECLParser.ConstraintoperatorContext entail = eclSub.constraintoperator();
            if (entail == null) {
               TTNode exp = new TTNode();
               exp.set(OWL.ONEOF, getConRef(eclSub.eclfocusconcept()
                   .eclconceptreference().conceptid()));
               return exp;
            }
            else {
               TTValue exp = getConRef(eclSub.eclfocusconcept()
                   .eclconceptreference().conceptid());
               return exp;
            }
         } else {
            throw new UnknownFormatConversionException("Unrecognised ECL subexpressionconstraint " + ecl);

         }
      }
   }
}
