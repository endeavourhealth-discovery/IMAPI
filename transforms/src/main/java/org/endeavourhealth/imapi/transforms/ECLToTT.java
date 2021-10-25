package org.endeavourhealth.imapi.transforms;


import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.parser.ecl.ECLBaseVisitor;
import org.endeavourhealth.imapi.parser.ecl.ECLLexer;
import org.endeavourhealth.imapi.parser.ecl.ECLParser;
import org.endeavourhealth.imapi.vocabulary.*;

import java.util.UnknownFormatConversionException;
import java.util.zip.DataFormatException;

/**
 * Converts ECL to Discovery syntax, supporting commonly used constructs
 */
public class ECLToTT extends ECLBaseVisitor<TTValue> {
   private TTEntity entity;
   private final ECLLexer lexer;
   private final ECLParser parser;
   private String ecl;
   public static final String ROLE_GROUP = "sn:609096000";

   public ECLToTT() {
      this.lexer = new ECLLexer(null);
      this.parser = new ECLParser(null);
      this.parser.removeErrorListeners();
      this.parser.addErrorListener(new ECLErrorListener());
   }

   public TTValue getClassExpression(String ecl) throws DataFormatException {
      this.ecl = ecl;
      lexer.setInputStream(CharStreams.fromString(ecl));
      CommonTokenStream tokens = new CommonTokenStream(lexer);
      parser.setTokenStream(tokens);
      ECLParser.ExpressionconstraintContext eclCtx = parser.expressionconstraint();
      return convertECContext(eclCtx);
   }



   private TTValue convertECContext(ECLParser.ExpressionconstraintContext ctx) throws DataFormatException {
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

   private TTValue convertRefined(ECLParser.RefinedexpressionconstraintContext refined) throws DataFormatException {
      TTNode exp;
      if (refined.subexpressionconstraint().expressionconstraint() != null) {
         exp = convertECContext(refined.subexpressionconstraint().expressionconstraint()).asNode();
      } else {
         exp = new TTNode();
         exp.set(RDFS.SUBCLASSOF,new TTArray().add(convertSubECContext(refined.subexpressionconstraint())));
      }
      ECLParser.EclrefinementContext refinement = refined.eclrefinement();
      ECLParser.SubrefinementContext subref = refinement.subrefinement();
      if (subref.eclattributeset() != null) {
         exp.set(IM.PROPERTY_GROUP,new TTArray());
         TTNode group= new TTNode();
         exp.get(IM.PROPERTY_GROUP).asArray().add(group);
         convertAttributeSet(group, subref.eclattributeset());
      } else if (subref.eclattributegroup() != null) {
         exp.set(IM.PROPERTY_GROUP,new TTArray());
         TTNode group= new TTNode();
         exp.get(IM.PROPERTY_GROUP).asArray().add(group);
         convertAttributeGroup(group, subref.eclattributegroup());
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

   private TTValue convertAttributeSet(TTNode exp, ECLParser.EclattributesetContext eclAtSet) throws DataFormatException {
      if (eclAtSet.subattributeset() != null) {
         if (eclAtSet.subattributeset().eclattribute() != null) {
            return convertAttribute(exp,eclAtSet.subattributeset().eclattribute());
         } else if (eclAtSet.conjunctionattributeset() != null) {
               convertAndAttributeSet(exp, eclAtSet.conjunctionattributeset());
               return exp;
            }
      }
      throw new UnknownFormatConversionException("ECL Attribute format not supoorted " + ecl);
   }

   private TTValue convertAndAttributeSet(TTNode group, ECLParser
       .ConjunctionattributesetContext eclAtAnd) throws DataFormatException {
      for (ECLParser.SubattributesetContext subAt : eclAtAnd.subattributeset()) {
         convertAttribute(group,subAt.eclattribute());
      }
      return group;
   }


   private TTValue convertConjunction(ECLParser.ConjunctionexpressionconstraintContext eclAnd) throws DataFormatException {
      TTNode exp = new TTNode();
      TTArray inters= new TTArray();
      exp.set(SHACL.AND,inters);
      for (ECLParser.SubexpressionconstraintContext eclInter : eclAnd.subexpressionconstraint()) {
         inters.add(convertSubECContext(eclInter));
      }
      return exp;
   }

   private TTValue convertExclusion(ECLParser.ExclusionexpressionconstraintContext eclExc) throws DataFormatException {
      TTNode exp = new TTNode();
      TTArray ands = new TTArray();
      exp.set(SHACL.AND,ands);
      ands.add(convertSubECContext(eclExc.subexpressionconstraint().get(0)));
      TTNode not= new TTNode();
      ands.add(not);
      not.set(SHACL.NOT,convertSubECContext(eclExc.
              subexpressionconstraint().get(1)));
      return exp;
   }

   private TTValue convertDisjunction(ECLParser.DisjunctionexpressionconstraintContext eclOr) throws DataFormatException {
      TTNode exp = new TTNode();
      TTArray unions= new TTArray();
      exp.set(SHACL.OR,unions);
      for (ECLParser.SubexpressionconstraintContext eclUnion : eclOr.subexpressionconstraint()) {
         unions.add(convertSubECContext(eclUnion));
      }
      return exp;
   }

   private TTValue convertAttribute(TTNode exp,ECLParser.EclattributeContext attecl) throws DataFormatException {
      if (exp.get(SHACL.PROPERTY)==null)
         exp.set(SHACL.PROPERTY,new TTArray());
      TTNode pv = new TTNode();
      exp.get(SHACL.PROPERTY).asArray().add(pv);
      pv.set(SHACL.PATH,getConRef(attecl.eclattributename()
          .subexpressionconstraint()
          .eclfocusconcept()
          .eclconceptreference()
          .conceptid()));
      if (attecl.expressioncomparisonoperator() != null) {
         if (attecl.expressioncomparisonoperator().EQUALS() != null) {
            if (attecl.subexpressionconstraint().eclfocusconcept() != null) {
               pv.set(SHACL.CLASS,getConRef(attecl
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


   private TTIriRef getConRef(ECLParser.ConceptidContext conceptId) throws DataFormatException {
      String code=conceptId.getText();
      if (code.matches("[0-9]+")) {
         if (code.contains("1000252"))
            return TTIriRef.iri(IM.NAMESPACE + code);
         else
            return TTIriRef.iri(SNOMED.NAMESPACE + code);
      } else
         throw new DataFormatException("ECL converter can only be used for snomed codes at this stage");
   }

   private TTValue convertAttributeGroup(TTNode group,
                                                 ECLParser.EclattributegroupContext eclGroup) throws DataFormatException {
      if (eclGroup.eclattributeset()!=null) {
         convertAttributeSet(group, eclGroup.eclattributeset());
         return group;
      }
      else
         throw new DataFormatException("Unable to cope with this type of attribute group : "+ ecl);
   }

   private TTValue convertSubECContext(ECLParser.SubexpressionconstraintContext eclSub) throws DataFormatException {

      if (eclSub.expressionconstraint() != null) {
         return convertECContext(eclSub.expressionconstraint());
      } else {
         if (eclSub.eclfocusconcept() != null) {
            ECLParser.ConstraintoperatorContext entail = eclSub.constraintoperator();
            if (entail == null) {
               TTNode exp = new TTNode();
               exp.set(IM.CODE, getConRef(eclSub.eclfocusconcept()
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
