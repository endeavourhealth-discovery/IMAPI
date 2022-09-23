package org.endeavourhealth.imapi.transforms;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.endeavourhealth.imapi.model.iml.Query;
import org.endeavourhealth.imapi.model.iml.Where;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.parser.ecl.ECLBaseVisitor;
import org.endeavourhealth.imapi.parser.ecl.ECLLexer;
import org.endeavourhealth.imapi.parser.ecl.ECLParser;
import org.endeavourhealth.imapi.transforms.ECLErrorListener;
import org.endeavourhealth.imapi.vocabulary.*;

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
	private Where mainOr;
	public static final String ROLE_GROUP = IM.ROLE_GROUP.getIri();



	public ECLToIML() {
		this.lexer = new ECLLexer(null);
		this.parser = new ECLParser(null);
		this.parser.removeErrorListeners();
		this.parser.addErrorListener(new ECLErrorListener());
		this.query= new Query();
	}

	public Query getClassExpression(String ecl) throws DataFormatException {
		this.ecl = ecl;
		lexer.setInputStream(CharStreams.fromString(ecl));
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		parser.setTokenStream(tokens);
		ECLParser.ExpressionconstraintContext eclCtx = parser.expressionconstraint();
		query= new Query();
		Where where= convertECContext(eclCtx);
		Where flatWhere= flattenWhere(where);
		query.setWhere(flatWhere);

		return query;
	}

	private Where flattenWhere(Where where) {
		Where flatWhere= new Where();
		if (where.getOr()!=null){
			for (Where or:where.getOr()) {
				if (or.getIs() != null)
					if (or.getAnd() == null) {
						if (or.getOr() == null) {
							if (or.getNotExist() == null) {
								if (or.getProperty().equals(IM.IS_A)) {
									flatWhere.setProperty(IM.IS_A);
									flatWhere.addIn(or.getIs());
								}
							} else {
								flatWhere.addOr(or);
							}
						} else {
							flatWhere.addOr(or);
						}
					}
				else
					flatWhere.addOr(or);
			}
		}
		else
			flatWhere= where;
		return flatWhere;
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
	private boolean isWildCard(ECLParser.RefinedexpressionconstraintContext refined){
		return refined.subexpressionconstraint() != null && refined.subexpressionconstraint().eclfocusconcept() != null && refined.subexpressionconstraint().eclfocusconcept().wildcard() != null;
	}

	private Where convertRefined(ECLParser.RefinedexpressionconstraintContext refined) throws DataFormatException {
		Where mainWhere= new Where();
		if (!isWildCard(refined)){
			if (refined.subexpressionconstraint().expressionconstraint() != null) {
				mainWhere.addAnd(convertECContext(refined.subexpressionconstraint().expressionconstraint()));
			} else {
				mainWhere.addAnd(convertSubECContext(refined.subexpressionconstraint()));
			}
		}
		else {
			mainWhere.and(a-> a
			 .setProperty(IM.IS_A)
			.setIs(new TTAlias(IM.CONCEPT).setIncludeSubtypes(true)));
		}

		ECLParser.EclrefinementContext refinement = refined.eclrefinement();
		ECLParser.SubrefinementContext subref = refinement.subrefinement();
		if (subref.eclattributeset() != null) {
			Where pv= new Where();
			mainWhere.addAnd(pv);
			pv.setPath(IM.ROLE_GROUP.getIri());
			convertAttributeSet(pv,subref.eclattributeset());
		}
		else if (subref.eclattributegroup() != null) {
			Where pv= new Where();
			mainWhere.addAnd(pv);
			pv.setPath(IM.ROLE_GROUP.getIri());
			convertAttributeGroup(pv, subref.eclattributegroup());
		}
		else
			throw new UnknownFormatConversionException("ECL attribute format not supported " + ecl);
		if (refinement.conjunctionrefinementset() != null) {
			Where pv= new Where();
			mainWhere.addAnd(pv);
			pv.setPath(IM.ROLE_GROUP.getIri());
			for (ECLParser.SubrefinementContext subref2 : refinement.conjunctionrefinementset().subrefinement()) {
				Where refinedGroup= new Where();
				pv.addAnd(refinedGroup);
				convertAttributeGroup(refinedGroup, subref2.eclattributegroup());
			}

		}
		return mainWhere;
	}

	private Where convertAttributeSet(Where where,ECLParser.EclattributesetContext eclAtSet) throws DataFormatException {
		if (eclAtSet.subattributeset() != null) {
			if (eclAtSet.subattributeset().eclattribute() != null) {
				Where attWhere= new Where();
				if(eclAtSet.conjunctionattributeset()!=null) {
					where.addAnd(attWhere);
				}
				else
					where.setWhere(attWhere);
				convertAttribute(attWhere, eclAtSet.subattributeset().eclattribute());
				}
			}
			if (eclAtSet.conjunctionattributeset() != null) {
				convertAndAttributeSet(where, eclAtSet.conjunctionattributeset());
			}
			return where;
	}

	private Where convertAndAttributeSet(Where where, ECLParser
		.ConjunctionattributesetContext eclAtAnd) throws DataFormatException {
		for (ECLParser.SubattributesetContext subAt : eclAtAnd.subattributeset()) {
			Where and= new Where();
			where.addAnd(and);
			convertAttribute(and,subAt.eclattribute());
		}
		return where;
	}


	private Where convertConjunction(ECLParser.ConjunctionexpressionconstraintContext eclAnd) throws DataFormatException {
		Where where= new Where();
		for (ECLParser.SubexpressionconstraintContext eclInter : eclAnd.subexpressionconstraint()) {
			where.addAnd(convertSubECContext(eclInter));
		}
		return where;
	}

	private Where convertExclusion(ECLParser.ExclusionexpressionconstraintContext eclExc) throws DataFormatException {
		Where where= new Where();
		where.addAnd(convertSubECContext(eclExc.subexpressionconstraint().get(0)));
		where.setNotExist(convertSubECContext(eclExc.
			subexpressionconstraint().get(1)));
		return where;
	}

	private Where convertDisjunction(ECLParser.DisjunctionexpressionconstraintContext eclOr) throws DataFormatException {
		Where where= new Where();
		for (ECLParser.SubexpressionconstraintContext eclUnion : eclOr.subexpressionconstraint()) {
			where.addOr(convertSubECContext(eclUnion));
		}
		return where;
	}

	private Where convertAttribute(Where where ,ECLParser.EclattributeContext attecl) throws DataFormatException {

		ECLParser.ConceptidContext conceptId= attecl.eclattributename().subexpressionconstraint().eclfocusconcept().eclconceptreference().conceptid();
		ECLParser.ConstraintoperatorContext entail= attecl.eclattributename().subexpressionconstraint().constraintoperator();
		TTAlias property=getConRef(conceptId,entail);
		if (attecl.expressioncomparisonoperator() != null) {
			if (attecl.expressioncomparisonoperator().EQUALS() != null) {
				if (attecl.subexpressionconstraint().eclfocusconcept() != null) {
					where.setProperty(property);
					where.setIs(getConRef(attecl
						.subexpressionconstraint().eclfocusconcept().eclconceptreference().conceptid(),
						attecl.subexpressionconstraint().constraintoperator()));
					return where;
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


	private TTAlias getConRef(ECLParser.ConceptidContext conceptId,ECLParser.ConstraintoperatorContext entail ) throws DataFormatException {
		TTAlias conRef= new TTAlias();
		String code=conceptId.getText();
		if (code.matches("[0-9]+")) {
			if (code.contains("1000252"))
				conRef.setIri(IM.NAMESPACE + code);
			else
				conRef.setIri(SNOMED.NAMESPACE + code);
		} else
			throw new DataFormatException("ECL converter can only be used for snomed codes at this stage");
		if (entail.descendantorselfof()!=null)
			conRef.setIncludeSubtypes(true);
		else if (entail.descendantof()!=null){
			conRef.setExcludeSelf(true);
			conRef.setIncludeSubtypes(true);
		}
		return conRef;

	}


	private Where convertAttributeGroup(Where group,
																				ECLParser.EclattributegroupContext eclGroup) throws DataFormatException {
		if (eclGroup.eclattributeset()!=null) {
			convertAttributeSet(group, eclGroup.eclattributeset());
			return group;
		}
		else
			throw new DataFormatException("Unable to cope with this type of attribute group : "+ ecl);
	}

	private Where convertSubECContext(ECLParser.SubexpressionconstraintContext eclSub) throws DataFormatException {

		if (eclSub.expressionconstraint() != null) {
			return convertECContext(eclSub.expressionconstraint());
		} else {
			if (eclSub.eclfocusconcept() != null) {
				Where where = new Where();
				pairPropertyValue(where,eclSub);
					return where;
			} else {
				throw new UnknownFormatConversionException("Unrecognised ECL subexpression constraint " + ecl);

			}
		}
	}

	private void pairPropertyValue(Where where, ECLParser.SubexpressionconstraintContext eclSub) {
		String concept= eclSub.eclfocusconcept().eclconceptreference().conceptid().getText();
		String conceptIri;
		if (concept.matches("[0-9]+")) {
			conceptIri = concept.contains("1000252") ? IM.NAMESPACE + concept : SNOMED.NAMESPACE + concept;
		}
		else
			conceptIri= concept;
		if (eclSub.constraintoperator().descendantorselfof()!=null) {
			where.setProperty(IM.IS_A)
				.setIs(TTAlias.iri(conceptIri));
		}
		else if (eclSub.constraintoperator().descendantof()!=null){
			where.setProperty(IM.IS_A)
				.setIs(TTAlias.iri(conceptIri).setExcludeSelf(true));
		}
		else {
			where.setProperty(IM.id)
				.setIs(TTAlias.iri(conceptIri));
		}

	}
}
