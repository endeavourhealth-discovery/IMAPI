package org.endeavourhealth.imapi.transforms;

import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.endeavourhealth.imapi.dataaccess.EntityRepository;
import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.parser.imecl.IMECLBaseVisitor;
import org.endeavourhealth.imapi.parser.imecl.IMECLParser;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.SNOMED;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class ECLToIMQVisitor extends IMECLBaseVisitor<Object> {

  private List<Node> nodes = new ArrayList<>();
  private List<Where> wheres = new ArrayList<>();
  private Prefixes prefixes;

  public Query getIMQ(IMECLParser.ImeclContext ctx) {
    return getIMQ(ctx, false);
  }

  public Query getIMQ(IMECLParser.ImeclContext ctx, boolean includeNames) {
    Query query = (Query) visitImecl(ctx);
    if (includeNames) addNames();
    return query;
  }

  private void addNames() {
    Set<TTIriRef> toName = getToNameIris();

    if (!toName.isEmpty()) {
      Map<String, String> nameMap = new EntityRepository().getNameMap(toName);
      for (Node node : nodes) {
        if (node.getName() == null && node.getIri() != null)
          node.setName(nameMap.get(node.getIri()));

      }
      addWhereNames(nameMap);
    }
  }

  private void addWhereNames(Map<String, String> nameMap) {
    if (!wheres.isEmpty()) {
      for (Where where : wheres) {
        if (where.getName() == null && where.getIri() != null)
          where.setName(nameMap.get(where.getIri()));
      }
    }
  }

  private Set<TTIriRef> getToNameIris() {
    Set<TTIriRef> toName = new HashSet<>();
    for (Node node : nodes) {
      if (node.getName() == null && node.getIri() != null)
        toName.add(TTIriRef.iri(node.getIri()));

    }
    if (!wheres.isEmpty()) {
      for (Where where : wheres) {
        if (where.getName() == null && where.getIri() != null)
          toName.add(TTIriRef.iri(where.getIri()));
      }
    }
    return toName;
  }


  @Override
  public Object visitImecl(IMECLParser.ImeclContext ctx) {
    Query query = null;
    if (ctx.children != null) {
      for (ParseTree child : ctx.children) {
        Object result = visit(child);
        if (result instanceof Prefixes asPrefixes) {
          prefixes = asPrefixes;
          if (query == null)
            query = new Query();
          query.setPrefixes(prefixes);
        } else if (result instanceof Match match) {
          if (query == null)
            query = new Query();
          if (match.getBoolMatch() != null) {
            if (match.getBoolMatch() == Bool.or) {
              query.addMatch(match);
            } else
              copyMatchToQuery(match, query);
          } else
            copyMatchToQuery(match, query);
        }
      }
    }
    return query;
  }

  private void copyMatchToQuery(Match match, Query query) {
    if (match.getInstanceOf() != null) {
      query.setInstanceOf(match.getInstanceOf());
    }
    if (match.isExclude())
      query.setExclude(true);
    if (match.getMatch() != null)
      query.setMatch(match.getMatch());
    if (match.getWhere() != null)
      query.setWhere(match.getWhere());
    if (match.getBoolMatch() != null) {
      query.setBoolMatch(match.getBoolMatch());
    }
    if (match.getTypeOf() != null && match.getTypeOf().getIri().equals(IM.CONCEPT)) {
      query.setTypeOf(match.getTypeOf());
    }
  }

  @Override
  public Prefixes visitPrefixes(IMECLParser.PrefixesContext ctx) {
    Prefixes eclPrefixes = new Prefixes();
    if (ctx.children != null) {
      for (ParseTree child : ctx.children) {
        Object result = visit(child);
        if (result instanceof Prefix asPrefix) {
          eclPrefixes.add(asPrefix);
        }
      }
    }
    return eclPrefixes;

  }

  @Override
  public Object visitPrefixDecl(IMECLParser.PrefixDeclContext ctx) {
    Prefix prefix = new Prefix();
    if (ctx.children != null) {
      for (ParseTree child : ctx.children) {
        Object result = visit(child);
        if (result != null) {
          if (prefix.getPrefix() == null) {
            prefix.setPrefix(result.toString());
          } else
            prefix.setNamespace(result.toString());
        }
      }
    }
    return prefix;
  }

  @Override
  public Object visitPname(IMECLParser.PnameContext ctx) {
    return ctx.getText();
  }


  @Override
  public Object visitExpressionconstraint(IMECLParser.ExpressionconstraintContext ctx) {
    if (ctx.children != null) {
      for (ParseTree child : ctx.children) {
        Object result = visit(child);
        if (result instanceof Match match)
          return match;
      }
    }

    return null;
  }

  @Override
  public Object visitRefinedexpressionconstraint(IMECLParser.RefinedexpressionconstraintContext ctx) {
    Match match = null;
    if (ctx.children != null) {
      for (ParseTree child : ctx.children) {
        Object result = visit(child);
        if (result instanceof Match asMatch)
          match = asMatch;
        else if (result instanceof Where asWhere) {
          if (match == null) {
            match = new Match();
            match.setTypeOf(new Node().setIri(IM.CONCEPT));
          }
          match.addWhere(asWhere);
        }
      }
    }
    return match;
  }

  @Override
  public Object visitCompoundexpressionconstraint(IMECLParser.CompoundexpressionconstraintContext ctx) {
    if (ctx.children != null) {
      for (ParseTree child : ctx.children) {
        Object result = visit(child);
        if (result instanceof Match asMatch)
          return asMatch;
      }
    }
    return null;

  }

  @Override
  public Object visitConjunctionexpressionconstraint(IMECLParser.ConjunctionexpressionconstraintContext ctx) {
    Match match = new Match();
    match.setBoolMatch(Bool.and);
    if (ctx.children != null) {
      for (ParseTree child : ctx.children) {
        Object result = visit(child);
        if (result instanceof Match asMatch)
          match.addMatch(asMatch);
      }
    }
    return match;

  }

  @Override
  public Object visitDisjunctionexpressionconstraint(IMECLParser.DisjunctionexpressionconstraintContext ctx) {
    Match match = new Match();
    match.setBoolMatch(Bool.or);
    if (ctx.children != null) {
      for (ParseTree child : ctx.children) {
        Object result = visit(child);
        if (result instanceof Match asMatch)
          match.addMatch(asMatch);
      }
    }
    return match;
  }

  @Override
  public Object visitExclusionexpressionconstraint(IMECLParser.ExclusionexpressionconstraintContext ctx) {
    Match match = new Match();
    match.setBoolMatch(Bool.and);
    if (ctx.children != null) {
      for (ParseTree child : ctx.children) {
        Object result = visit(child);
        if (result instanceof Match asMatch) {
          if (match.getMatch() == null)
            match.addMatch(asMatch);
          else {
            asMatch.setExclude(true);
            match.addMatch(asMatch);
          }
        }
      }
    }
    return match;
  }


  @Override
  public Object visitSubexpressionconstraint(IMECLParser.SubexpressionconstraintContext ctx) {
    Match match = null;
    Node node = null;
    if (ctx.children != null) {
      for (ParseTree child : ctx.children) {
        Object result = visit(child);
        if (result != null) {
          if (result instanceof Match)
            return result;
          if (match == null)
            match = new Match();
          if (result instanceof Node asNode) {
            node = asNode;
            nodes.add(node);
            match.addInstanceOf(asNode);
          } else if (result instanceof TTIriRef iri) {
            if (node == null) {
              node = new Node();
              nodes.add(node);
              match.addInstanceOf(node);
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
  public Object visitEclfocusconcept(IMECLParser.EclfocusconceptContext ctx) {
    if (ctx.children != null) {
      for (ParseTree child : ctx.children) {
        Object result = visit(child);
        if (result instanceof String)
          return null;
        else
          return result;
      }
    }
    return null;
  }


  @Override
  public Object visitEclconceptreference(IMECLParser.EclconceptreferenceContext ctx) {
    TTIriRef iri = new TTIriRef();
    if (ctx.children != null) {
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
  public Object visitConceptid(IMECLParser.ConceptidContext ctx) {
    if (ctx.children != null) {
      for (ParseTree child : ctx.children) {
        Object result = visit(child);
        if (result instanceof String)
          return result;
      }
    }
    return null;
  }


  @Override
  public Object visitSctid(IMECLParser.SctidContext ctx) {
    return SNOMED.NAMESPACE + ctx.getText();
  }


  @Override
  public Object visitIri(IMECLParser.IriContext ctx) {
    String resolved = null;
    String iriRef = ctx.getText();
    if (prefixes != null) {
      int colonPos = iriRef.indexOf(":");
      if (colonPos > -1) {
        String prefix = iriRef.substring(0, colonPos);
        String namespace = prefixes.getNamespace(prefix);
        if (namespace != null)
          resolved = namespace + iriRef.substring(colonPos + 1);
      }
    }
    if (resolved == null) {
      try {
        // Creating a URL object from the given string
        new URL(iriRef);
        // If no exception is thrown, the URL is valid
        resolved = iriRef;
      } catch (MalformedURLException e) {
        throw new IllegalStateException("invalid iri syntax in : " + iriRef);
      }
    }
    return resolved;
  }


  @Override
  public Object visitErrorNode(ErrorNode node) {
    throw new IllegalStateException("invalid syntax in " + node.getText());
  }

  @Override
  public Object visitTerm(IMECLParser.TermContext ctx) {
    return ctx.getText();
  }

  @Override
  public Object visitWildcard(IMECLParser.WildcardContext ctx) {
    return "*";
  }

  @Override
  public Object visitConstraintoperator(IMECLParser.ConstraintoperatorContext ctx) {
    if (ctx.children != null) {
      for (ParseTree child : ctx.children) {
        Object result = visit(child);
        if (result instanceof Node)
          return result;
      }
    }
    return null;
  }

  @Override
  public Object visitMemberof(IMECLParser.MemberofContext ctx) {
    return new Node().setMemberOf(true);
  }

  @Override
  public Object visitDescendantof(IMECLParser.DescendantofContext ctx) {
    return new Node().setDescendantsOf(true);

  }

  @Override
  public Object visitDescendantorselfof(IMECLParser.DescendantorselfofContext ctx) {
    return new Node().setDescendantsOrSelfOf(true);
  }

  @Override
  public Object visitChildof(IMECLParser.ChildofContext ctx) {
    return new Node().setChildOf(true);
  }

  @Override
  public Object visitChildorselfof(IMECLParser.ChildorselfofContext ctx) {
    return new Node().setChildOrSelfOf(true);
  }

  @Override
  public Object visitAncestorof(IMECLParser.AncestorofContext ctx) {
    return new Node().setAncestorsOf(true);
  }

  @Override
  public Object visitAncestororselfof(IMECLParser.AncestororselfofContext ctx) {
    return new Node().setAncestorsOrSelfOf(true);
  }

  @Override
  public Object visitParentof(IMECLParser.ParentofContext ctx) {
    return new Node().setParentOf(true);
  }


  @Override
  public Object visitParentorselfof(IMECLParser.ParentorselfofContext ctx) {
    return new Node().setParentOrSelfOf(true);
  }

  @Override
  public Object visitEclrefinement(IMECLParser.EclrefinementContext ctx) {
    if (ctx.children != null) {
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
  public Object visitConjunctionrefinementset(IMECLParser.ConjunctionrefinementsetContext ctx) {
    Where where = new Where();
    where.setBoolWhere(Bool.and);
    if (ctx.children != null) {
      for (ParseTree child : ctx.children) {
        Object result = visit(child);
        if (result instanceof Where asWhere) {
          where.addWhere(asWhere);
        }
      }
    }
    return where;
  }

  @Override
  public Object visitDisjunctionrefinementset(IMECLParser.DisjunctionrefinementsetContext ctx) {
    Where where = new Where();
    where.setBoolWhere(Bool.or);
    if (ctx.children != null) {
      for (ParseTree child : ctx.children) {
        Object result = visit(child);
        if (result instanceof Where asWhere) {
          where.addWhere(asWhere);
        }
      }
    }
    return where;
  }

  @Override
  public Object visitSubrefinement(IMECLParser.SubrefinementContext ctx) {
    if (ctx.children != null) {
      for (ParseTree child : ctx.children) {
        Object result = visit(child);
        if (result instanceof Where)
          return result;
      }
    }
    return null;
  }

  @Override
  public Object visitEclattributeset(IMECLParser.EclattributesetContext ctx) {
    if (ctx.children != null) {
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
  public Object visitConjunctionattributeset(IMECLParser.ConjunctionattributesetContext ctx) {
    Where where = new Where();
    where.setBoolWhere(Bool.and);
    if (ctx.children != null) {
      for (ParseTree child : ctx.children) {
        Object result = visit(child);
        if (result instanceof Where asWhere) {
          where.addWhere(asWhere);
        }
      }
    }
    return where;
  }

  @Override
  public Object visitDisjunctionattributeset(IMECLParser.DisjunctionattributesetContext ctx) {
    Where where = new Where();
    where.setBoolWhere(Bool.or);
    if (ctx.children != null) {
      for (ParseTree child : ctx.children) {
        Object result = visit(child);
        if (result instanceof Where asWhere) {
          where.addWhere(asWhere);
        }
      }
    }
    return where;

  }

  @Override
  public Object visitSubattributeset(IMECLParser.SubattributesetContext ctx) {
    if (ctx.children != null) {
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
  public Object visitEclattributegroup(IMECLParser.EclattributegroupContext ctx) {
    Where where = new Where();
    where.setIri(IM.ROLE_GROUP);
    Match match = new Match();
    where.setMatch(match);
    if (ctx.children != null) {
      for (ParseTree child : ctx.children) {
        Object result = visit(child);
        if (result instanceof Where asWhere) {
          match.addWhere(asWhere);
        }
      }
    }
    where.setAnyRoleGroup(false);
    return where;
  }

  @Override
  public Object visitEclattribute(IMECLParser.EclattributeContext ctx) {
    Where where = null;
    boolean reverseFlag = false;
    if (ctx.children != null) {
      for (ParseTree child : ctx.children) {
        Object result = visit(child);
        if (result instanceof Boolean asBoolean && Boolean.TRUE.equals(asBoolean)) {
          reverseFlag = true;
        }

        if (result instanceof Match asMatch) {
          Node node = (asMatch).getInstanceOf().get(0);
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
            if (reverseFlag)
              where.setInverse(reverseFlag);
          } else {
            where.setIs(List.of(node));
            nodes.add(node);
          }
        }
      }
    }
    if (null != where) where.setAnyRoleGroup(true);
    return where;
  }

  @Override
  public Object visitReverseflag(IMECLParser.ReverseflagContext ctx) {
    return true;
  }
}
