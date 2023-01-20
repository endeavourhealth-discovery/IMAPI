package org.endeavourhealth.imapi.transforms;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.endeavourhealth.imapi.model.iml.Query;
import org.endeavourhealth.imapi.model.iml.Where;
import org.endeavourhealth.imapi.model.tripletree.TTAlias;
import org.endeavourhealth.imapi.model.tripletree.TTValue;
import org.endeavourhealth.imapi.parser.ecl.ECLBaseVisitor;
import org.endeavourhealth.imapi.parser.ecl.ECLLexer;
import org.endeavourhealth.imapi.parser.ecl.ECLParser;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.SNOMED;

import java.util.List;
import java.util.UnknownFormatConversionException;
import java.util.zip.DataFormatException;

/**
 * Converts ECL to Discovery syntax, supporting commonly used constructs
 */
public class ECLToIML extends ECLBaseVisitor<TTValue> {
    private final ECLLexer lexer;
    private final ECLParser parser;
    private String ecl;
    private Query query;
    public static final String ROLE_GROUP = IM.ROLE_GROUP.getIri();


    public ECLToIML() {
        this.lexer = new ECLLexer(null);
        this.parser = new ECLParser(null);
        this.parser.removeErrorListeners();
        this.parser.addErrorListener(new ParserErrorListener());
        this.query = new Query();
    }

    /**
     * Converts an ECL string into IM Query definition class. Assumes active and inactive concepts are requested.
     * <p>To include only active concepts use method with boolean activeOnly= true</p>
     *
     * @param ecl String compliant with ECL
     * @return Class conforming to IM Query model JSON-LD when serialized.
     * @throws DataFormatException for invalid ECL.
     */
    public Query getQueryFromECL(String ecl) throws DataFormatException {
        return getClassExpression(ecl, false);
    }

    /**
     * Converts an ECL string into IM Query definition class.
     *
     * @param ecl        String compliant with ECL
     * @param activeOnly boolean true if limited to active concepts
     * @return Class conforming to IM Query model JSON-LD when serialized.
     * @throws DataFormatException for invalid ECL.
     */
    public Query getClassExpression(String ecl, boolean activeOnly) throws DataFormatException {
        this.ecl = ecl;
        lexer.setInputStream(CharStreams.fromString(ecl));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        parser.setTokenStream(tokens);
        try {
            ECLParser.EclContext eclCtx = parser.ecl();

            query = new Query();
            Where where = convertECContext(eclCtx);
            Where flatWhere = flattenWhere(where);
            query.setWhere(flatWhere);
            if (activeOnly)
                query.setActiveOnly(true);

            return query;
        } catch (UnknownFormatConversionException e) {
            throw new DataFormatException(e.getMessage());
        }

    }

    private Where flattenWhere(Where where) {
        Where flatWhere = new Where();

        if (where.getOr() == null)
            return where;

        for (Where or : where.getOr()) {
            if (or.getFrom() == null
                || or.getAnd() != null
                || or.getOr() != null
                || or.getPathTo() != null
                || or.getNotExist() != null)
                return where;

            for (TTAlias from : or.getFrom()) {
                flatWhere.addFrom(from);
            }
        }

        if (where.getNotExist() != null)
            flatWhere.setNotExist(where.getNotExist());

        return flatWhere;
    }

    private Where convertECContext(ECLParser.EclContext ctx) throws DataFormatException {
        return convertECContext(ctx.expressionconstraint());
    }

    private Where convertECContext(ECLParser.ExpressionconstraintContext ctx) throws DataFormatException {
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

    private boolean isWildCard(ECLParser.RefinedexpressionconstraintContext refined) {
        return refined.subexpressionconstraint() != null && refined.subexpressionconstraint().eclfocusconcept() != null && refined.subexpressionconstraint().eclfocusconcept().wildcard() != null;
    }

    private Where convertRefined(ECLParser.RefinedexpressionconstraintContext refined) throws DataFormatException {
        Where mainWhere = getMainWhere(refined);

        ECLParser.EclrefinementContext refinement = refined.eclrefinement();
        ECLParser.SubrefinementContext subref = refinement.subrefinement();

        Where subWhere = getSubWhere(mainWhere, refinement);

        convertRefinedAttributes(subref, subWhere);
        convertRefinedConjunction(mainWhere, refinement, subref);
        convertRefinedDisjunction(mainWhere, refinement, subref);
        return mainWhere;
    }

    private void convertRefinedAttributes(ECLParser.SubrefinementContext subref, Where subWhere) throws DataFormatException {
        if (subref.eclattributeset() != null) {
            convertAttributeSet(subWhere, subref.eclattributeset(), false);
        } else if (subref.eclattributegroup() != null) {
            subWhere.setPathTo(IM.ROLE_GROUP.getIri());
            convertAttributeGroup(subWhere, subref.eclattributegroup());
        } else
            throw new UnknownFormatConversionException("ECL attribute format not supported " + ecl);
    }

    private void convertRefinedConjunction(Where mainWhere, ECLParser.EclrefinementContext refinement, ECLParser.SubrefinementContext subref) throws DataFormatException {
        if (refinement.conjunctionrefinementset() != null) {
            for (ECLParser.SubrefinementContext subAndRef : refinement.conjunctionrefinementset().subrefinement()) {
                Where pv = new Where();
                mainWhere.addAnd(pv);
                if (subref.eclattributeset() != null) {
                    convertAttributeSet(pv, subAndRef.eclattributeset(), false);
                } else if (subref.eclattributegroup() != null) {
                    pv.setPathTo(IM.ROLE_GROUP.getIri());
                    convertAttributeGroup(pv, subAndRef.eclattributegroup());
                }
            }
        }
    }

    private void convertRefinedDisjunction(Where mainWhere, ECLParser.EclrefinementContext refinement, ECLParser.SubrefinementContext subref) throws DataFormatException {
        if (refinement.disjunctionrefinementset() != null) {
            for (ECLParser.SubrefinementContext subOrRef : refinement.disjunctionrefinementset().subrefinement()) {
                Where pv = new Where();
                mainWhere.addOr(pv);
                if (subref.eclattributeset() != null) {
                    convertAttributeSet(pv, subOrRef.eclattributeset(), false);
                } else if (subref.eclattributegroup() != null) {
                    pv.setPathTo(IM.ROLE_GROUP.getIri());
                    convertAttributeGroup(pv, subOrRef.eclattributegroup());
                }
            }
        }
    }

    private static Where getSubWhere(Where mainWhere, ECLParser.EclrefinementContext refinement) {
        Where subWhere;
        if (refinement.conjunctionrefinementset() != null) {
            subWhere = new Where();
            mainWhere.addAnd(subWhere);
        } else if (refinement.disjunctionrefinementset() != null) {
            subWhere = new Where();
            mainWhere.addOr(subWhere);
        } else subWhere = mainWhere;
        return subWhere;
    }

    private Where getMainWhere(ECLParser.RefinedexpressionconstraintContext refined) throws DataFormatException {
        Where mainWhere;
        if (!isWildCard(refined)) {
            if (refined.subexpressionconstraint().expressionconstraint() != null) {
                mainWhere = convertECContext(refined.subexpressionconstraint().expressionconstraint());
            } else {
                mainWhere = convertSubECContext(refined.subexpressionconstraint());
            }
        } else
            mainWhere = new Where();
        return mainWhere;
    }

    private void convertAttributeSet(Where where, ECLParser.EclattributesetContext eclAtSet,
                                     boolean alreadyGrouped) throws DataFormatException {
        if (eclAtSet.conjunctionattributeset() == null && eclAtSet.disjunctionattributeset() == null) {
            if (!alreadyGrouped)
                where.setPathTo(IM.ROLE_GROUP.getIri());
            convertAttribute(where, eclAtSet.subattributeset().eclattribute());
        } else {
            if (eclAtSet.subattributeset().eclattribute() == null) {
                throw new DataFormatException("nested attribute sets not supported");
            }
            if (eclAtSet.conjunctionattributeset() != null) {
                convertAndAttributeSet(where, eclAtSet, alreadyGrouped);
            } else {
                convertOrAttributeSet(where, eclAtSet, alreadyGrouped);
            }
        }

    }

    private void convertAndAttributeSet(Where where,
                                        ECLParser.EclattributesetContext eclAtSet,
                                        boolean alreadyGrouped) throws DataFormatException {
        if (eclAtSet.subattributeset() != null) {
            Where and = new Where();
            if (!alreadyGrouped)
                and.setPathTo(IM.ROLE_GROUP.getIri());
            where.addAnd(and);
            convertAttribute(and, eclAtSet.subattributeset().eclattribute());
        }

        for (ECLParser.SubattributesetContext subAt : eclAtSet.conjunctionattributeset().subattributeset()) {
            Where and = new Where();
            if (!alreadyGrouped)
                and.setPathTo(IM.ROLE_GROUP.getIri());
            where.addAnd(and);
            convertAttribute(and, subAt.eclattribute());
        }
    }

    private void convertOrAttributeSet(Where where,
                                       ECLParser.EclattributesetContext eclAtSet,
                                       boolean alreadyGrouped) throws DataFormatException {
        if (eclAtSet.subattributeset() != null) {
            Where or = new Where();
            if (!alreadyGrouped)
                or.setPathTo(IM.ROLE_GROUP.getIri());
            where.addOr(or);
            convertAttribute(or, eclAtSet.subattributeset().eclattribute());
        }

        for (ECLParser.SubattributesetContext subAt : eclAtSet.disjunctionattributeset().subattributeset()) {
            Where or = new Where();
            if (!alreadyGrouped)
                or.setPathTo(IM.ROLE_GROUP.getIri());
            where.addOr(or);
            convertAttribute(or, subAt.eclattribute());
        }
    }


    private Where convertConjunction(ECLParser.ConjunctionexpressionconstraintContext eclAnd) throws DataFormatException {
        Where where = new Where();
        for (ECLParser.SubexpressionconstraintContext eclInter : eclAnd.subexpressionconstraint()) {
            where.addAnd(convertSubECContext(eclInter));
        }
        return where;
    }

    private Where convertExclusion(ECLParser.ExclusionexpressionconstraintContext eclExc) throws DataFormatException {
        Where mainWhere;
        mainWhere = convertSubECContext(eclExc.subexpressionconstraint().get(0));
        ECLParser.SubexpressionconstraintContext eclMinus = eclExc.subexpressionconstraint().get(1);
        mainWhere.setNotExist(convertSubECContext(eclExc.
            subexpressionconstraint().get(1)));
        return mainWhere;
    }

    private Where convertDisjunction(ECLParser.DisjunctionexpressionconstraintContext eclOr) throws DataFormatException {
        Where where = new Where();
        for (ECLParser.SubexpressionconstraintContext eclUnion : eclOr.subexpressionconstraint()) {
            where.addOr(convertSubECContext(eclUnion));
        }
        return where;
    }

    private void convertAttribute(Where where, ECLParser.EclattributeContext attecl) throws DataFormatException {

        ECLParser.EclconceptreferenceContext eclRef = attecl.eclattributename().subexpressionconstraint().eclfocusconcept().eclconceptreference();
        ECLParser.ConstraintoperatorContext entail = attecl.eclattributename().subexpressionconstraint().constraintoperator();
        TTAlias property = getConRef(eclRef, entail);
        if (attecl.expressioncomparisonoperator() != null) {
            if (attecl.expressioncomparisonoperator().EQUALS() != null) {
                if (attecl.subexpressionconstraint().eclfocusconcept() != null) {
                    where.setProperty(property);
                    where.setIs(getConRef(attecl
                            .subexpressionconstraint().eclfocusconcept().eclconceptreference(),
                        attecl.subexpressionconstraint().constraintoperator()));
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


    private TTAlias getConRef(ECLParser.EclconceptreferenceContext eclRef, ECLParser.ConstraintoperatorContext entail) throws DataFormatException {
        TTAlias conRef = new TTAlias();
        String name = null;
        if (eclRef.term() != null)
            name = eclRef.term().getText();
        ECLParser.ConceptidContext conceptId = eclRef.conceptid();
        String code = conceptId.getText();
        if (code.matches("[0-9]+")) {
            if (code.contains("1000252"))
                conRef.setIri(IM.NAMESPACE + code);
            else
                conRef.setIri(SNOMED.NAMESPACE + code);
        } else
            throw new DataFormatException("ECL converter can only be used for snomed codes at this stage");
        if (entail != null) {
            if (entail.descendantorselfof() != null)
                conRef.setIncludeSubtypes(true);
            else if (entail.descendantof() != null) {
                conRef.setExcludeSelf(true);
                conRef.setIncludeSubtypes(true);
            }
        }
        if (name != null)
            conRef.setName(name);
        return conRef;

    }


    private void convertAttributeGroup(Where group,
                                       ECLParser.EclattributegroupContext eclGroup) throws DataFormatException {
        if (eclGroup.eclattributeset() != null) {
            convertAttributeSet(group, eclGroup.eclattributeset(), true);
        } else
            throw new DataFormatException("Unable to cope with this type of attribute group : " + ecl);
    }

    private Where convertSubECContext(ECLParser.SubexpressionconstraintContext eclSub) throws DataFormatException {

        if (eclSub.expressionconstraint() != null) {
            return convertECContext(eclSub.expressionconstraint());
        } else {
            if (eclSub.eclfocusconcept() != null) {
                Where where = new Where();
                pairPropertyValue(where, eclSub);
                return where;
            } else {
                throw new UnknownFormatConversionException("Unrecognised ECL subexpression constraint " + ecl);

            }
        }
    }

    private void pairPropertyValue(Where where, ECLParser.SubexpressionconstraintContext eclSub) {
        boolean includeSubs = false;
        boolean excludeSelf = false;
        if (eclSub.constraintoperator() != null) {
            if (eclSub.constraintoperator().descendantorselfof() != null)
                includeSubs = true;
            else if (eclSub.constraintoperator().descendantof() != null) {
                includeSubs = true;
                excludeSelf = true;
            }
        }
        pairPropertyValue(where, eclSub, includeSubs, excludeSelf);


    }

    private void pairPropertyValue(Where where, ECLParser.SubexpressionconstraintContext eclSub, boolean includeSubs, boolean excludeSelf) {
        String concept = eclSub.eclfocusconcept().eclconceptreference().conceptid().getText();
        String name = null;
        if (eclSub.eclfocusconcept().eclconceptreference().term() != null) {
            name = eclSub.eclfocusconcept().eclconceptreference().term().getText();
        }
        String conceptIri;
        if (concept.matches("[0-9]+")) {
            conceptIri = concept.contains("1000252") ? IM.NAMESPACE + concept : SNOMED.NAMESPACE + concept;
        } else
            conceptIri = concept;
        if (excludeSelf) {
            TTAlias from = new TTAlias();
            from
                .setIri(conceptIri).setIncludeSubtypes(true).setExcludeSelf(true);
            if (name != null)
                from.setName(name);
            where.addFrom(from);
        } else if (includeSubs) {
            TTAlias from = new TTAlias();
            from
                .setIri(conceptIri).setIncludeSubtypes(true);
            if (name != null)
                from.setName(name);
            where.addFrom(from);

        } else {
            TTAlias from = new TTAlias();
            from
                .setIri(conceptIri).setIncludeSubtypes(false);
            if (name != null)
                from.setName(name);
            where.addFrom(from);
        }

    }
}
