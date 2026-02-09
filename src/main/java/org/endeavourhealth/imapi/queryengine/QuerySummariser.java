package org.endeavourhealth.imapi.queryengine;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.endeavourhealth.imapi.cache.TimedCache;
import org.endeavourhealth.imapi.dataaccess.EntityRepository;
import org.endeavourhealth.imapi.logic.reasoner.LogicOptimizer;
import org.endeavourhealth.imapi.logic.service.IriCollector;
import org.endeavourhealth.imapi.logic.service.QueryDescriptor;
import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.transforms.Context;
import org.endeavourhealth.imapi.utility.Pluraliser;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.Namespace;
import org.endeavourhealth.imapi.vocabulary.RDF;
import org.endeavourhealth.imapi.vocabulary.RDFS;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

import static org.endeavourhealth.imapi.logic.service.QueryDescriptor.getShortName;
import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;
import static org.endeavourhealth.imapi.vocabulary.VocabUtils.asHashSet;

public class QuerySummariser {
  private static final TimedCache<String, String> queryCache = new TimedCache<>("queryCache", 120, 5, 10);
  @Getter
  private final Map<String, String> nodeRefToLabel = new HashMap<>();
  private final StringBuilder text = new StringBuilder();
  private int indent = 0;


 private String tabs(){
   StringBuilder tabs = new StringBuilder();
   for (int i = 0; i < indent; i++) {
     tabs.append("  ");
   }
   return tabs.toString();
 }

  public String summariseMatch(Match match){
    describeMatch(match);
    return text.toString();
  }




  private void describeFunction(FunctionClause function) {
    text.append(function.getName()).append(" (");;
    if (function.getArgument() != null) {
      int i=0;
      for (Argument argument : function.getArgument()) {
        i++;
        if (i>1) text.append(", ");
        if (argument.getValuePath()!=null){
          describePath(argument.getValuePath());
        }
        if (argument.getValueIri() != null)
          text.append(getTermInContext(argument.getValueIri().getName(), Context.PATH));
        if (argument.getValueIriList() != null) {
          for (TTIriRef valueIri : argument.getValueIriList()) {
            text.append(getTermInContext(valueIri.getName(),Context.PATH));
          }
        }
      }
      text.append(")");
    }

  }





  public String getTermInContext(String source, Context... contexts) {
    if (source.isEmpty()) return "";
    StringBuilder term = new StringBuilder();
    term.append(source);
    for (Context context : contexts) {
      if (context == Context.PLURAL) {
            term = new StringBuilder(Pluraliser.pluralise(term.toString()));
          }
      if (context == Context.LOWERCASE) {
        term = new StringBuilder(term.toString().toLowerCase());
      }
    }

    return term.toString();
  }





  public void describeMatch(Match match) {
   text.append(tabs()).append("{\n");
   indent++;

    if (match.getOrderBy() != null) {
      describeOrderBy(match.getOrderBy());
    }
    if (match.getTypeOf() != null) {
      text.append(getTermInContext(match.getTypeOf().getName(), Context.PLURAL));
    }
    if (match.getIs() != null) {
      describeIs(match.getIs());
    }

    if (match.getRule() != null) {
      for (Match subMatch : match.getRule()) {
        describeMatch(subMatch);
      }
    }

    if (match.getPath() != null) {
      for (Path path : match.getPath()) {
        describePath(path);
      }
    }

    if (match.getWhere() != null) {
      text.append("\n").append(tabs()).append("where ").append("{\n");
      indent++;
      describeWhere(match.getWhere());
      text.append("\n");
      indent--;
      text.append(tabs()).append("}");
    }
    indent--;
    text.append("\n").append(tabs()).append("}");

  }

  private void describePath(Path path) {
   text.append(getTermInContext(path.getName(), Context.PLURAL));
    if (path.getPath() != null) {
      text.append("->");
      for (Path subPath : path.getPath()) {
        describePath(subPath);
      }
    }
    else text.append(" ");
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
        .stream().map(prop -> getTermInContext(prop.getName(), Context.PLURAL).split(" \\(")[0])
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

  private void describeWheres(List<Where> wheres,String operator) {
   int i=0;
    for (Where where : wheres) {
      i++;
      text.append(tabs());
      if (i>1) text.append(operator).append(" ");
      if (where.getAnd()!=null||where.getOr()!=null)
        text.append("(");
      describeWhere(where);
      if (where.getAnd()!=null||where.getOr()!=null)
        text.append(")");
      text.append("\n");
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
      String label = getTermInContext(set.getName());
      text.append(qualifier).append(" ").append(label).append("\n").append(tabs()).append("  ");
      }
  }


  public void describeOrderBy(OrderLimit orderBy) {
    text.append(QueryDescriptor.describeOrderBy(orderBy)).append(" ");
  }

  private void describeWhere(Where where) {
   where.setUuid(null);
   where.setNodeRef(null);

    if (where.getAnd() != null) {

      describeWheres(where.getAnd(),"and");

    }
    if (where.getOr() != null) {
      describeWheres(where.getOr(),"or");
    } else if (where.getAnd() == null) {
      text.append(getTermInContext(where.getName(), Context.PROPERTY)).append(" ");
      if (where.getRange() != null) {
        describeRangeWhere(where);
      }
      if (where.getValue() != null || where.getOperator() != null) {
        describeValueWhere(where);
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
      } catch (DateTimeParseException ignored) {}
    }
    return false;
  }

  private void describeValue(Assignable assignable, Operator operator, boolean date, String value, TTIriRef unit, boolean relativeTo, boolean isRange) {
    String qualifier = null;
    boolean inclusive = false;
    boolean past = false;
    assignable.setValueLabel("");
    if (value != null) if (value.startsWith("-")) past = true;
    String relativity = null;
    if (null != operator) switch (operator) {
      case gt:
        if (date) {
          if (!isRange) {
            if (value != null) {
              if (value.equals("0")) {
                qualifier = "after ";
              } else {
                if (isValidDate(value))
                  qualifier=" after";
                else qualifier = "is within ";
                if (past && relativeTo) relativity = " before the ";
                if (!past && relativeTo) relativity = " of the ";
              }
            } else qualifier = "after";
          }
        } else {
          if (!isRange) qualifier = "greater than ";
          if (relativeTo && value != null)
            relativity = " relative to the ";
        }
        break;
      case gte:
        inclusive = true;
        if (date) {
          if (!isRange) {
            if (value != null) {
              if (value.equals("0")) {
                qualifier = " on or after ";
              } else {
                if (isValidDate(value))
                  qualifier=" on or after";
                else qualifier = "is within ";
                if (past && relativeTo) relativity = " before the ";
                if (!past && relativeTo) relativity = " of the ";
              }
            } else qualifier = "on or after";
            relativity = " the ";
          }
          if (past && relativeTo) relativity = " before ";
        } else {
          if (!isRange) {
            qualifier = "equal to or more than ";
            if (relativeTo && value != null)
              relativity = " relative to the ";
          }
        }
        break;
      case lt:
        if (date) {
          if (!isRange) {
            qualifier = "before ";
            if (value != null) {
              if (value.equals("0")) {
                qualifier = "before ";
              } else {
                if (isValidDate(value))
                  qualifier = "before ";
                else qualifier = "is within ";
                if (past && relativeTo) relativity = " before the ";
                if (!past && relativeTo) relativity = " of the ";
              }
            }
            if (relativeTo) relativity = " the ";
          }
          if (past && relativeTo) relativity = " before the ";
        } else {
          if (!isRange) {
            qualifier = "under ";
            if (relativeTo && value != null)
              relativity = " relative to the ";
          }
        }
        break;
      case lte:
        inclusive = true;
        if (date) {
          if (!isRange) {
            qualifier = "on or before ";
            if (value != null) {
              if (value.equals("0")) {
                qualifier = "on or before ";
              } else {
                if (isValidDate(value))
                  qualifier = " on or before ";
                else qualifier = "is within ";
                if (past && relativeTo) relativity = " before the ";
                if (!past && relativeTo) relativity = " of the ";
              }
            }
          }
          if (past && relativeTo) relativity = " before the ";
        } else {
          if (!isRange) {
            qualifier = "equal to or less than ";
            if (relativeTo && value != null)
              relativity = " relative to the ";
          }
        }
        break;
      case contains:
        qualifier = "contains ";
        break;
      case start:
        qualifier = "starts with ";
        break;
      case eq:
        if (date) {
          if (!isRange) {
            if (assignable.getQualifier()==null) {
              qualifier = " on ";
              if (relativeTo) relativity = " the ";
            } else {
              qualifier = " is ";
            }
          }
        }
        else  qualifier=" =";
        break;
    }
    if (qualifier != null) {
      assignable.setDescription(qualifier);
      text.append(qualifier);
    }
    if (value != null) {
      if (!date || !value.equals("0")) {
        if (assignable.getQualifier()!=null){
          if (value.startsWith("-")) {
            assignable.setValueLabel(value.replace("-", "") + " before ");
          }
          else {
            assignable.setValueLabel(value+ (value.equals("0") ?"": "after "));
          }
        }
        else {
          assignable.setValueLabel(value.replace("-", ""));
        }
        if (unit != null) {
          assignable.setValueLabel(assignable.getValueLabel() + " " + getTermInContext(unit.getName(), Context.VALUE));
        }
      }
    }
    if (inclusive && qualifier == null) {
      if (assignable.getValueLabel() != null) {
        assignable.setValueLabel(assignable.getValueLabel() + " (inc.)");
      }
    }
    if (relativity != null) assignable.setValueLabel(assignable.getValueLabel() + relativity);
    text.append(assignable.getValueLabel());
  }


  private void describeFrom(Where where, Value from) {
    String qualifier;
    boolean inclusive = false;
    boolean past = false;
    Operator operator = from.getOperator();
    String value = from.getValue();
    if (value != null) if (value.startsWith("-")) past = true;
    qualifier = "is between ";
    if (null != operator) if (operator == Operator.gte) {
      inclusive = true;
    }
    if (value != null) {
      qualifier = qualifier + value.replace("-", "");
    }
    if (from.getUnits() != null) {
      qualifier = qualifier + " " + getTermInContext(from.getUnits().getName(), Context.PLURAL);
    }
    if (inclusive) {
      qualifier = qualifier + " (inc.)";
    }
    if (past) qualifier = qualifier + " before";
    where.setDescription(qualifier);

  }

  private void describeTo(Where where, Value to) {
    String qualifier = null;
    boolean inclusive = false;
    Operator operator = to.getOperator();
    String value = to.getValue();
    boolean date = false;
    if (where.getIri() != null) {
      date = where.getIri().toLowerCase().contains("date");
    }
    if (null != operator) switch (operator) {
      case gt:
        qualifier = "and ";
        break;
      case gte:
        inclusive = true;
        qualifier = "and ";
        break;
      case lt:
        if (date) {
          qualifier = "and before ";
        } else qualifier = "below ";
        break;
      case lte:
        inclusive = true;
        if (date) {
          qualifier = "and ";
        } else qualifier = "below ";
        break;
      case eq:
        if (date) {
          qualifier = "";
        } else qualifier = "between ";
        break;
    }
    if (value != null) {
      qualifier = qualifier + value.replace("-", "");
    }
    if (to.getUnits() != null) {
      qualifier = qualifier + " " + getTermInContext(to.getUnits().getName(), Context.PLURAL);
    }
    if (inclusive) {
      qualifier = qualifier + " (inc.)";
    }

    if (value != null) {
      if (date) {
        if (!value.contains("-")) {
          qualifier = qualifier + " after ";
        } else qualifier = qualifier + " before ";
      }
    }

    where.setDescription(where.getDescription() + " and " + qualifier);
    text.append(where.getDescription()).append(" and ").append(qualifier).append(" ");
  }


  private void describeValueWhere(Where where) {
    boolean date = false;
    if (where.getIri() != null) date = where.getIri().toLowerCase().contains("date");
    Operator operator = where.getOperator();
    describeValue(where, operator, date, where.getValue(), where.getUnits(), where.getRelativeTo() != null, false);
    describeRelativeTo(where);
  }

  private void describeRangeWhere(Where where) {
    describeFrom(where, where.getRange().getFrom());
    describeTo(where, where.getRange().getTo());
    describeRelativeTo(where);
  }

  private void describeRelativeTo(Where where) {
    if (where.getRelativeTo() == null) return;
    describeRelation(where.getRelativeTo());
  }

  private void describeRelation(RelativeTo relativeTo) {
    if (relativeTo.getNodeRef() != null) {
      if (nodeRefToLabel.get(relativeTo.getNodeRef()) != null) {
        text.append(nodeRefToLabel.get(relativeTo.getNodeRef()));
      } text.append(relativeTo.setTargetLabel("the above"));
    }
    if (relativeTo.getParameter() != null) {
      text.append(relativeTo.getParameterName());
    }
  }


  private void describeWhereIs(Where where) {
   text.append("is ");
   if (where.getIs().size()>1){
     text.append("  one of(");
   }
   for (Node set : where.getIs()) {
      String modifier = set.isExclude() ? " but not: " : " ";
      set.setDescription(modifier);
    }
    for (int i = 0; i < where.getIs().size(); i++) {
      if (i > 0) text.append(", or ");
      Node set = where.getIs().get(i);
      text.append(set.getDescription() != null ? set.getDescription() + " " : "").append(getShortName(set.getName()));
    }
    if (where.getIs().size()>1){
      text.append(")");
    }
  }
}




