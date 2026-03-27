package org.endeavourhealth.imapi.queryengine;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.endeavourhealth.imapi.cache.TimedCache;
import org.endeavourhealth.imapi.dataaccess.EntityRepository;
import org.endeavourhealth.imapi.logic.reasoner.LogicOptimizer;
import org.endeavourhealth.imapi.logic.service.IriCollector;
import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.transforms.Context;
import org.endeavourhealth.imapi.utility.Pluraliser;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.NAMESPACE;
import org.endeavourhealth.imapi.vocabulary.RDF;
import org.endeavourhealth.imapi.vocabulary.RDFS;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;
import static org.endeavourhealth.imapi.vocabulary.VocabUtils.asHashSet;

public class QueryDescriptor {
  private static final TimedCache<String, String> queryCache = new TimedCache<>("queryCache", 120, 5, 10);
  @Getter
  private EntityRepository repo = new EntityRepository();
  @Getter
  private Map<String, TTEntity> iriContext;
  private StringBuilder shortDescription = new StringBuilder();
  private DisplayMode displayMode;

  public static String describeOrderBy(OrderLimit orderBy) {
    String orderDisplay = "";
    for (OrderDirection property : orderBy.getProperty()) {
      String field = property.getIri();
      if (field.toLowerCase().contains("date")) {
        if (property.getDirection() == Order.descending) orderDisplay = "latest ";
        else orderDisplay = "earliest ";
      } else {
        if (property.getDirection() == Order.descending) orderDisplay = "maximum ";
        else orderDisplay = "minimum ";
      }
    }
    if (orderBy.getLimit() > 1) {
      if (orderBy.getLimit() < 100)
        orderDisplay = orderDisplay + " " + orderBy.getLimit();
      else orderDisplay = orderDisplay + " several ";
    }
    orderBy.setDescription(orderDisplay);

    return orderDisplay;
  }

  private static boolean isValidDate(String dateStr) {
    String[] patterns = {
      "yyyy-MM-dd",
      "dd/MM/yyyy",
      "MM/dd/yyyy",
      "yyyyMMdd"
    };
    for (String pattern : patterns) {
      try {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDate.parse(dateStr, formatter);
        return true;
      } catch (DateTimeParseException ignored) {
      }
    }
    return false;
  }

  public static String getShortName(String name) {
    if (name == null) return null;
    int length = name.length();
    StringBuilder startShort = new StringBuilder(name.substring(0, Math.min(length, 50)));
    if (length > 50) {
      boolean bracket = false;
      for (int i = 50; i < Math.min(length, 70); i++) {
        Character c = name.charAt(i);
        if (c == '(') bracket = true;
        if (c == ')') bracket = false;
        if (c == ' ') {
          if (!bracket) return startShort + "...";
          else startShort.append(c);
        } else startShort.append(c);
      }
      return startShort.toString() + "...";
    } else return startShort.toString();
  }

  public Query describeQuery(String queryIri, DisplayMode displayMode) throws JsonProcessingException, QueryException {
    TTEntity queryEntity = repo.getEntityPredicates(queryIri, asHashSet(RDFS.LABEL, IM.DEFINITION)).getEntity();
    if (queryEntity.get(iri(IM.DEFINITION)) == null) return null;
    Query query = queryEntity.get(iri(IM.DEFINITION)).asLiteral().objectValue(Query.class);
    if (query.getIri() == null)
      query.setIri(queryIri);
    query = describeQuery(query, displayMode);
    queryCache.put(queryIri, new ObjectMapper().writeValueAsString(query));
    return query;
  }

  public Match describeSingleMatch(Match match) throws QueryException {
    setIriNames(match);
    describeMatch(match);
    return match;
  }

  public Query describeQuery(Query query, DisplayMode displayMode) throws QueryException, JsonProcessingException {
    this.displayMode = displayMode;
    setIriNames(query);
    if (iriContext == null || iriContext.isEmpty())
      return query;
    if (query.getUuid() == null) query.setUuid(UUID.randomUUID().toString());
    if (displayMode == DisplayMode.RULES && query.getRule() == null) {
      new LogicOptimizer().getRulesFromLogic(query);
    } else if (displayMode == DisplayMode.LOGICAL && query.getRule() != null) {
      new LogicOptimizer().resolveLogic(query, DisplayMode.LOGICAL);
    }
    describeMatch(query);
    if (query.getGroupBy() != null) {
      describeGroupBys(query.getGroupBy());
    }

    return query;
  }

  private void describeGroupBys(List<GroupBy> groupBys) {
    for (GroupBy groupBy : groupBys) {
      if (groupBy.getIri() != null) groupBy.setName(getTermInContext(groupBy.getIri()));
    }
  }

  private void describeReturn(Return prop) {
    if (prop.getIri() != null) prop.setName(getTermInContext(prop.getIri(), Context.PATH));
    if (prop.getAs() == null) prop.setAs(prop.getName());
    if (prop.getFunction() != null) {
      describeFunction(prop.getFunction());
    }
  }

  private void describeFunction(FunctionClause function) {
    if (function.getIri() != null) function.setName(getTermInContext(function.getIri(), Context.PATH));
    if (function.getArgument() != null) {
      for (Argument argument : function.getArgument()) {
        if (argument.getValuePath() != null) {
          describePath(argument.getValuePath());
        }
        if (argument.getValueIri() != null)
          argument.getValueIri().setName(getTermInContext(argument.getValueIri().getIri(), Context.PATH));
        if (argument.getValueIriList() != null) {
          for (TTIriRef valueIri : argument.getValueIriList()) {
            valueIri.setName(getTermInContext(valueIri.getIri(), Context.PATH));
          }
        }
      }
    }

  }

  private void setIriNames(Match match) throws QueryException {
    Set<String> iriSet = IriCollector.collectIris(match);
    try {
      iriContext = repo.getEntitiesWithPredicates(iriSet, asHashSet(IM.PREPOSITION, IM.CODE, RDF.TYPE, IM.DISPLAY_LABEL));
    } catch (Exception e) {
      throw new QueryException(e.getMessage() + " Query content error found by query Descriptor", e);
    }
  }

  private void setIriNames(Query query) throws QueryException {
    Set<String> iriSet = IriCollector.collectIris(query);
    if (!iriSet.isEmpty()) {
      try {
        iriContext = repo.getEntitiesWithPredicates(iriSet, asHashSet(IM.PREPOSITION, RDF.TYPE, IM.CODE, RDF.TYPE, IM.DISPLAY_LABEL, IM.ALTERNATIVE_CODE));
      } catch (Exception e) {
        throw new QueryException(e.getMessage() + " Query content error found by query Descriptor", e);
      }
    }
  }

  public String getTermInContext(String source, Context... contexts) {
    if (source.isEmpty()) return "";
    StringBuilder term = new StringBuilder(source);
    TTEntity entity = iriContext.get(source);
    if (entity != null) {
      term = new StringBuilder(entity.getName());
    }
    for (Context context : contexts) {
      if (context == Context.PLURAL) {
        if (entity != null) {
          if (entity.get(iri(NAMESPACE.IM + "plural")) == null) {
            term = new StringBuilder(Pluraliser.pluralise(term.toString()));
          } else {
            term = new StringBuilder(entity.get(iri(NAMESPACE.IM + "plural")).asLiteral().getValue());
          }
        } else term = new StringBuilder(Pluraliser.pluralise(term.toString()));
      }
      if (context == Context.LOWERCASE) {
        term = new StringBuilder(term.toString().toLowerCase());
      }
    }

    return term.toString();
  }

  public String getTermInContext(Node node, Context... context) {
    if (node.getParameter() != null) {
      return node.getParameter();
    }
    if (node.getIri() != null) {
      return getTermInContext(node.getIri(), context);
    } else if (node.getNode() != null) {
      return node.getNode();
    } else if (node.getNodeRef() != null) {
      return node.getNodeRef();
    } else return "";
  }

  public String getTermInContext(Where node, Context... context) {
    if (node.getParameter() != null) {
      return node.getParameter();
    }
    if (node.getIri() != null) {
      return getTermInContext(node.getIri(), context);
    } else if (node.getPropertyVariable() != null) {
      return node.getPropertyVariable();
    } else if (node.getNode() != null) {
      return node.getNode();
    } else if (node.getNodeRef() != null) {
      return node.getNodeRef();
    } else return "";
  }

  private void nestReturns(Match match) {
    if (match.getReturn() == null) return;
  }

  public void describeMatch(Match match) {
    if (match.getUuid() == null) match.setUuid(UUID.randomUUID().toString());


    if (match.getReturn() != null) {
      for (Return prop : match.getReturn()) {
        describeReturn(prop);
      }
    }
    if (match.getOrderBy() != null) {
      String orderDisplay = describeOrderBy(match.getOrderBy());
      if (!orderDisplay.equals("")) shortDescription.append(orderDisplay);
    }
    if (match.getName() == null && match.getDescription() != null) {
      match.setName(match.getDescription());
    }


    if (match.getTypeOf() != null) {
      match.getTypeOf().setName(getTermInContext(match.getTypeOf(), Context.PLURAL));
    }
    if (match.getIs() != null) {
      describeIs(match.getIs());
    }
    if (match.getIs() != null) {
      for (Node node : match.getIs()) {
        if (node.getIri() != null)
          node.setName(getTermInContext(node.getIri(), Context.MATCH));
      }
    }

    if (match.getRule() != null) {
      for (Match subMatch : match.getRule()) {
        describeMatch(subMatch);
      }
    }
    if (match.getOr() != null) {
      for (Match subMatch : match.getOr()) {
        describeMatch(subMatch);
      }
    }
    if (match.getAnd() != null) {
      for (Match subMatch : match.getAnd()) {
        describeMatch(subMatch);
      }
    }

    if (match.getPath() != null) {
      for (Path path : match.getPath()) {
        describePath(path);
      }
    }

    if (match.getWhere() != null) {
      describeWhere(match.getWhere(), match);
    }

    if (match.getThen() != null) {
      describeWhere(match.getThen(), match);
    }
  }

  private void describePath(Path path) {
    if (path.getIri() != null) {
      path.setName(getTermInContext(path.getIri(), Context.PROPERTY));
    }
    if (path.getTypeOf() != null) {
      path.getTypeOf().setName(getTermInContext(path.getTypeOf(), Context.SINGLE));
    }
    if (path.getPath() != null) {
      for (Path subPath : path.getPath()) {
        describePath(subPath);
      }
    }
  }

  private String getPreface(Match match) {
    StringBuilder preface = new StringBuilder();
    preface.append("Select the ");
    addReturnText(match, preface);
    return preface.toString();
  }

  private void addReturnText(Match match, StringBuilder preface) {
    if (match.getOrderBy() != null) {
      preface.append(match.getOrderBy().getDescription()).append(" ");
    }
    if (match.getReturn() != null)
      preface.append(match.getReturn()
        .stream().map(prop -> getTermInContext(prop.getIri(), Context.PLURAL).split(" \\(")[0])
        .collect(Collectors.joining(", ")));
  }

  private String getUnionHeader(Match match) {
    StringBuilder header = new StringBuilder();
    header.append("With the ");
    addReturnText(match, header);
    header.append(" ");
    header.append("from one of the following:");
    return header.toString();
  }

  private void describeWheres(List<Where> wheres, Match match) {
    for (Where where : wheres) {
      describeWhere(where, match);
    }
  }

  private void describeIs(List<Node> inSets) {
    for (Node set : inSets) {
      String qualifier = "";
      if (set.isExclude()) {
        qualifier = "but not ";
      }
      if (set.isMemberOf()) {
        qualifier = qualifier + "in ";
      } else qualifier = qualifier + "is a";
      String label = getTermInContext(set);
      set.setName(label);
      set.setDescription(qualifier);
      if (set.getMatch() != null) {
        describeMatch(set.getMatch());
      }
    }
  }

  private void describeWhere(Where where, Match match) {
    if (where.getUuid() == null) where.setUuid(UUID.randomUUID().toString());
    if (where.getAnd() != null) {
      describeWheres(where.getAnd(), match);
    }
    if (where.getQualifier() != null) {
      where.getQualifier().setName(getTermInContext(where.getQualifier().getIri(), Context.SINGLE));
    }
    if (where.getOr() != null) {
      describeWheres(where.getOr(), match);
    } else if (where.getAnd() == null) {
      where.setName(getTermInContext(where, Context.PROPERTY));
      if (where.getRange() != null) {
        describeRangeWhere(where, match);
      }
      if (where.getValue() != null || where.getOperator() != null) {
        describeValueWhere(where, match);
      }
      if (where.getIs() != null) {
        describeWhereIs(where);
      }
      if (where.getIsNull()) {
        where.setValueLabel("is not recorded");
      }
      if (where.getIsNotNull()) {
        where.setValueLabel("is recorded");
      }
    }
  }

  private void describeValueWhere(Where where, Match match) {
    if (where.getUuid() == null) where.setUuid(UUID.randomUUID().toString());
    if (where.getIri() != null) {
      where.setName(getTermInContext(where.getIri(), Context.PROPERTY));
    }
    if (where.getQualifier() != null) {
      where.getQualifier().setName(getTermInContext(where.getQualifier().getIri(), Context.PLURAL));
    }
    describeAssignable(where);
  }

  public void describeAssignable(Assignable assignable) {
    if (assignable.getCompare() != null) {
      describeCompare(assignable.getCompare());
    }

  }

  private void describeCompare(Compare compare) {
    describeValueSource(compare.getLeft());
    describeValueSource(compare.getRight());
    if (compare.getUnits() != null) {
      compare.getUnits().setName(getTermInContext(compare.getUnits().getIri(), Context.PLURAL));
    }
  }

  private void describeValueSource(ValueSource source) {
    if (source.getIri() != null) {
      source.setName(getTermInContext(source.getIri(), Context.PROPERTY));
    }
    if (source.getParameter() != null) {
      if (source.getParameter().toLowerCase().contains("searchdate"))
        source.setName("search date");
      else if (source.getParameter().toLowerCase().contains("achievementdate"))
        source.setName("achievement date");
      else source.setName(source.getParameter());
    }
  }

  private void describeRangeWhere(Where where, Match match) {
    describeValueWhere(where, match);
    describeAssignable(where.getRange().getFrom());
    describeAssignable(where.getRange().getTo());
  }

  private void describeWhereIs(Where where) {
    for (Node set : where.getIs()) {
      if (set.getIri() != null) {
        if (iriContext.get(set.getIri()) != null) {
          TTEntity nodeEntity = (iriContext.get(set.getIri()));
          set.setCode(nodeEntity.getCode());
          String modifier = set.isExclude() ? " but not: " : " ";
          set.setDescription(nodeEntity.getName() + modifier);
          set.setQualifier(modifier);
        }
      }
      if (set.getName() == null) {
        String value = getTermInContext(set);
        set.setName(value);
      }
    }
    StringBuilder valueLabel = new StringBuilder();
    for (int i = 0; i < where.getIs().size(); i++) {
      if (i > 1) {
        valueLabel.append(" ...+ more");
        break;
      } else {
        if (i > 0) valueLabel.append(", or ");
      }
      Node set = where.getIs().get(i);
      valueLabel.append(set.getDescription() != null ? set.getDescription() + " " : "").append(getShortName(set.getName()));
    }
    where.setValueLabel(valueLabel.toString());
    if (where.getShortLabel() != null)
      shortDescription.append(where.getShortLabel()).append(" ");
    else shortDescription.append(where.getValueLabel()).append(" ");
  }

  public void generateUUIDs(Where where) {
    if (where.getUuid() == null) {
      where.setUuid(UUID.randomUUID().toString());
    }
    for (List<Where> wheres : Arrays.asList(where.getOr(), where.getAnd())) {
      if (wheres != null) {
        for (Where subWhere : wheres) {
          generateUUIDs(subWhere);
        }
      }
    }
  }

  public String getDescriptions(Match match) {
    if (match.getDescription() != null) return match.getDescription();
    StringBuilder description = new StringBuilder();
    String operators = "or,and,not";
    int opIndex = -1;
    for (List<Match> matches : Arrays.asList(match.getOr(), match.getAnd())) {
      opIndex++;
      if (matches != null) {
        for (Match subMatch : matches) {
          if (subMatch.getDescription() != null) {
            if (description.isEmpty()) description.append(subMatch.getDescription());
            else
              description.append(", ").append(operators.split(",")[opIndex]).append(" ").append(subMatch.getDescription());
          }
        }
      }
    }
    return description.toString();
  }

  public String getShortDescription(Match match) throws QueryException {
    shortDescription = new StringBuilder();
    setIriNames(match);
    if (match.getOrderBy() != null) {
      String orderDisplay = describeOrderBy(match.getOrderBy());
      if (!orderDisplay.isEmpty()) shortDescription.append(orderDisplay).append(" ");
    }
    if (match.getWhere() != null) {
      describeWhere(match.getWhere(), match);
    }
    return shortDescription.toString();
  }
}