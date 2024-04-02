package org.endeavourhealth.imapi.transforms;

import org.antlr.v4.runtime.tree.ParseTree;
import org.endeavourhealth.imapi.dataaccess.EntityRepository2;
import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.parser.ecl.ECLBaseVisitor;
import org.endeavourhealth.imapi.parser.ecl.ECLParser;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.SNOMED;

import java.util.*;

public class ECLToIMQVisitor extends ECLBaseVisitor {

	private List<Node> nodes = new ArrayList<>();
	private List<Where> wheres= new ArrayList<>();


	public Query getIMQ(ECLParser.EclContext ctx){
		return getIMQ(ctx,false);
	}

	public Query getIMQ(ECLParser.EclContext ctx,boolean includeNames){
		Query query = (Query) visitEcl(ctx);
		if (includeNames) addNames();
		return query;
	}

	private void addNames() {
		Set<TTIriRef> toName= new HashSet<>();
		for (Node node:nodes){
			if (node.getName()==null){
				toName.add(TTIriRef.iri(node.getIri()));
			}
		}
		if (!wheres.isEmpty()) {
			for (Where where : wheres) {
				if (where.getName() == null)
					toName.add(TTIriRef.iri(where.getIri()));
			}
		}

		if (!toName.isEmpty()){
			Map<String,String> nameMap= new EntityRepository2().getNameMap(toName);
			for (Node node:nodes){
				if (node.getName()==null){
					node.setName(nameMap.get(node.getIri()));
				}
			}
			if (!wheres.isEmpty()){
				for (Where where:wheres){
					if (where.getName()==null){
						where.setName(nameMap.get(where.getIri()));
					}
				}

			}
		}

	}


	@Override
	public Object visitEcl(ECLParser.EclContext ctx) {
		if (ctx.children!=null){
			for (ParseTree child:ctx.children){
				Object result= visit(child);
				if (result instanceof Match match) {
					Query query= new Query();
					if (match.getInstanceOf()!=null)
						query.setInstanceOf(match.getInstanceOf());
					if (match.isExclude())
						query.setExclude(true);
					if (match.getMatch()!=null)
						query.setMatch(match.getMatch());
					if (match.getWhere()!=null)
						query.setWhere(match.getWhere());
					if (match.getBool()!=null) {
						query.setBool(match.getBool());
					}
					return query;
				}
			}
		}
		return null;
	}

	@Override
	public Object visitExpressionconstraint(ECLParser.ExpressionconstraintContext ctx) {
		if (ctx.children!=null){
			for (ParseTree child:ctx.children){
				Object result= visit(child);
				if (result instanceof Match)
					return result;
			}
		}

		return null;
	}

	@Override
	public Object visitRefinedexpressionconstraint(ECLParser.RefinedexpressionconstraintContext ctx) {
		Match match=null;
		if (ctx.children!=null){
			for (ParseTree child:ctx.children){
				Object result= visit(child);
				if (result instanceof Match)
					match= (Match) result;
				else if (result instanceof Where) {
					assert match != null;
					match.addWhere((Where) result);
				}
			}
		}
		return match;
	}

	@Override
	public Object visitCompoundexpressionconstraint(ECLParser.CompoundexpressionconstraintContext ctx) {
		if (ctx.children!=null){
			for (ParseTree child:ctx.children){
				Object result= visit(child);
				if (result instanceof Match)
					return result;
			}
		}
		return null;

	}

	@Override
	public Object visitConjunctionexpressionconstraint(ECLParser.ConjunctionexpressionconstraintContext ctx) {
		Match match= new Match();
		match.setBool(Bool.and);
		if (ctx.children!=null){
			for (ParseTree child:ctx.children){
				Object result= visit(child);
				if (result instanceof Match)
					match.addMatch((Match) result);
			}
		}
		return match;

	}

	@Override
	public Object visitDisjunctionexpressionconstraint(ECLParser.DisjunctionexpressionconstraintContext ctx) {
		Match match= new Match();
		match.setBool(Bool.or);
		if (ctx.children!=null){
			for (ParseTree child:ctx.children){
				Object result= visit(child);
				if (result instanceof Match)
					match.addMatch((Match) result);
			}
		}
		return match;
	}

	@Override
	public Object visitExclusionexpressionconstraint(ECLParser.ExclusionexpressionconstraintContext ctx) {
		Match match= new Match();
		match.setBool(Bool.and);
		if (ctx.children!=null){
			for (ParseTree child:ctx.children){
				Object result= visit(child);
				if (result instanceof Match) {
					if (match.getMatch()==null)
						match.addMatch((Match) result);
					else {
						((Match) result).setExclude(true);
						match.addMatch((Match) result);
					}
				}
			}
		}
		return match;
	}


	@Override
	public Object visitSubexpressionconstraint(ECLParser.SubexpressionconstraintContext ctx) {
		Match match=null;
		Node node=null;
		if (ctx.children!=null){
			for (ParseTree child:ctx.children) {
				Object result = visit(child);
				if (result != null) {
					if (result instanceof Match)
						return result;
					if (match==null)
						match= new Match();
					if (result instanceof Node) {
						node= (Node) result;
						nodes.add(node);
						if (node.isMemberOf()){
							match.addIs(node);
						}
						else
							match.setInstanceOf((Node) result);
					}
					else if (result instanceof TTIriRef iri) {
						if (node==null){
							node= new Node();
							nodes.add(node);
							match.setInstanceOf(node);
						}
						if (iri.getIri() != null)
							node.setIri(iri.getIri());
						if (iri.getName() != null)
							node.setName(iri.getName());
					}
				}
			}
			}
		return match;
	}

	@Override
	public Object visitEclfocusconcept(ECLParser.EclfocusconceptContext ctx) {
		if (ctx.children!=null){
			for (ParseTree child:ctx.children){
				Object result= visit(child);
				if (result instanceof String)
						return null;
					else
						return result;
				}
			}
		return null;
	}



	@Override
	public Object visitEclconceptreference(ECLParser.EclconceptreferenceContext ctx) {
		TTIriRef iri= new TTIriRef();
		if (ctx.children!=null) {
			for (ParseTree child : ctx.children) {
				Object result = visit(child);
					if (result instanceof String) {
						if (result.toString().equals("*"))
							return null;
					else if (iri.getIri() == null)
						iri.setIri(result.toString());
					else
						iri.setName(result.toString());
				}
			}
		}
		return iri;
	}

	@Override
	public Object visitConceptid(ECLParser.ConceptidContext ctx) {
		return SNOMED.NAMESPACE+ctx.getText();
	}

	@Override
	public Object visitTerm(ECLParser.TermContext ctx) {
		return ctx.getText();
	}

	@Override
	public Object visitWildcard(ECLParser.WildcardContext ctx) {
		return "*";
	}

	@Override
	public Object visitConstraintoperator(ECLParser.ConstraintoperatorContext ctx) {
		if (ctx.children!=null){
			for (ParseTree child:ctx.children){
				Object result= visit(child);
				if (result instanceof Node)
					return result;
			}
		}
		return null;
	}

	@Override
	public Object visitMemberof(ECLParser.MemberofContext ctx) {
		return new Node().setMemberOf(true);
	}

	@Override
	public Object visitDescendantof(ECLParser.DescendantofContext ctx) {
		return new Node().setDescendantsOf(true);

	}

	@Override
	public Object visitDescendantorselfof(ECLParser.DescendantorselfofContext ctx) {
		return new Node().setDescendantsOrSelfOf(true);
	}

	@Override
	public Object visitChildof(ECLParser.ChildofContext ctx) {
		return new Node().setChildOf(true);
	}

	@Override
	public Object visitChildorselfof(ECLParser.ChildorselfofContext ctx) {
		return new Node().setChildOrSelfOf(true);
	}

	@Override
	public Object visitAncestorof(ECLParser.AncestorofContext ctx) {
		return new Node().setAncestorsOf(true);
	}

	@Override
	public Object visitAncestororselfof(ECLParser.AncestororselfofContext ctx) {
		return new Node().setAncestorsOrSelfOf(true);
	}

	@Override
	public Object visitParentof(ECLParser.ParentofContext ctx) {
		return new Node().setParentOf(true);
	}

	@Override
	public Object visitParentorselfof(ECLParser.ParentorselfofContext ctx) {
		return new Node().setParentOrSelfOf(true);
	}

	@Override
	public Object visitEclrefinement(ECLParser.EclrefinementContext ctx) {
		if (ctx.children!=null) {
			for (ParseTree child : ctx.children) {
				Object result = visit(child);
				if (result instanceof Where) {
					return result;
				}
			}
		}
		return null;
	}

	@Override
	public Object visitConjunctionrefinementset(ECLParser.ConjunctionrefinementsetContext ctx) {
		Where where = new Where();
		where.setBool(Bool.and);
		if (ctx.children!=null) {
			for (ParseTree child : ctx.children) {
				Object result = visit(child);
				if (result instanceof Where) {
					where.addWhere((Where) result);
				}
			}
		}
		return where;
	}

	@Override
	public Object visitDisjunctionrefinementset(ECLParser.DisjunctionrefinementsetContext ctx) {
		Where where = new Where();
		where.setBool(Bool.or);
		if (ctx.children!=null) {
			for (ParseTree child : ctx.children) {
				Object result = visit(child);
				if (result instanceof Where) {
					where.addWhere((Where) result);
				}
			}
		}
		return where;
	}

	@Override
	public Object visitSubrefinement(ECLParser.SubrefinementContext ctx) {
		if (ctx.children!=null){
			for (ParseTree child:ctx.children){
				Object result= visit(child);
				if (result instanceof Where)
					return result;
			}
		}
		return null;
	}

	@Override
	public Object visitEclattributeset(ECLParser.EclattributesetContext ctx) {
		if (ctx.children!=null) {
			for (ParseTree child : ctx.children) {
				Object result = visit(child);
				if (result instanceof Where) {
					return result;
				}
			}
		}
		return null;
	}

	@Override
	public Object visitConjunctionattributeset(ECLParser.ConjunctionattributesetContext ctx) {
		Where where= new Where();
		where.setBool(Bool.and);
		if (ctx.children!=null) {
			for (ParseTree child : ctx.children) {
				Object result = visit(child);
				if (result instanceof Where) {
					where.addWhere((Where) result);
				}
			}
		}
		return where;
	}

	@Override
	public Object visitDisjunctionattributeset(ECLParser.DisjunctionattributesetContext ctx) {
		Where where= new Where();
		where.setBool(Bool.or);
		if (ctx.children!=null) {
			for (ParseTree child : ctx.children) {
				Object result = visit(child);
				if (result instanceof Where) {
					where.addWhere((Where) result);
				}
			}
		}
		return where;

	}

	@Override
	public Object visitSubattributeset(ECLParser.SubattributesetContext ctx) {
		if (ctx.children!=null) {
			for (ParseTree child : ctx.children) {
				Object result = visit(child);
				if (result instanceof Where) {
					return result;
				}
			}
		}
		return null;
	}

	@Override
	public Object visitEclattributegroup(ECLParser.EclattributegroupContext ctx) {
		Where where= new Where();
		where.setIri(IM.ROLE_GROUP);
		Match match = new Match();
		where.setMatch(match);
		if (ctx.children!=null) {
			for (ParseTree child : ctx.children) {
				Object result = visit(child);
				if (result instanceof Where) {
					match.addWhere((Where) result);
				}
			}
		}
		where.setAnyRoleGroup(false);
		return where;
	}

	@Override
	public Object visitEclattribute(ECLParser.EclattributeContext ctx) {
		Where where = null;
		if (ctx.children != null) {
			for (ParseTree child : ctx.children) {
				Object result = visit(child);
				if (result instanceof Match) {
					Node node = ((Match) result).getInstanceOf();
					if (where == null) {
						where = new Where();
						wheres.add(where);
						where.setIri(node.getIri());
						where.setName(node.getName());
						where.setDescendantsOf(node.isDescendantsOf());
						where.setDescendantsOrSelfOf(node.isDescendantsOrSelfOf());
						where.setChildOf(node.isChildOf());
						where.setChildOrSelfOf(node.isChildOrSelfOf());
						where.setParentOf(node.isParentOf());
						where.setParentOrSelfOf(node.isParentOrSelfOf());
					}
					else {
						where.setIs(List.of(node));
						nodes.add(node);
					}
				}
			}
		}
		where.setAnyRoleGroup(true);
		return where;
	}


}
