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
import org.endeavourhealth.imapi.vocabulary.Namespace;
import org.endeavourhealth.imapi.vocabulary.RDF;
import org.endeavourhealth.imapi.vocabulary.RDFS;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;
import static org.endeavourhealth.imapi.vocabulary.VocabUtils.asHashSet;

public class QuerySummariser {
  private final EntityRepository repo = new EntityRepository();
  private final Map<String, String> nodeRefToLabel = new HashMap<>();
  @Getter
  private Map<String, TTEntity> iriContext;
  private StringBuilder shortDescription = new StringBuilder();
  private StringBuilder summary = new StringBuilder();
  private int level;








  public String summariseQuery(Match query) throws QueryException, JsonProcessingException {
    new QueryDescriptor().describeSingleMatch(query);
    summariseMatch(query,0,"");
    return summary.toString();
  }






  public void summariseMatch(Match match,Integer index,String bool) {
    int subIndex=-1;
    if (match.getTypeOf() != null) {
      summary.append(match.getTypeOf().getName()).append(" with ");
      indent();
    }
    if (!bool.isEmpty() &&!bool.equals("union") &&!bool.equals("step") &&index>0){
      summary.append(bool).append(" ");
    }
    if (bool.equals("or")&&index==0) {
      summary.append("either ");
    }
    if (match.getNodeRef()!=null){
      summary.append("From ").append(match.getNodeRef());
      indent();
    }

    if (match.getOrderBy() != null) {
      summary.append(match.getOrderBy().getDescription());
    }



    if (match.getIs() != null) {
      summariseIs(match.getIs());
    }

    if (match.getOr() != null) {
      level++;
      for (Match subMatch : match.getOr()) {
        subIndex++;
        indent();
        summariseMatch(subMatch,subIndex,"or");
      }
      level--;
    }
    if (match.getAnd() != null) {
      level++;
      for (Match subMatch : match.getAnd()) {
        subIndex++;
        indent();
        summariseMatch(subMatch,subIndex,"and");
      }
      level--;
    }
    if (match.getUnion() != null) {
      summary.append("Combine results of :");
      level++;
      for (Match subMatch : match.getUnion()) {
        subIndex++;
        indent();
        summariseMatch(subMatch,subIndex,"union");
      }
      level--;
    }
    if (match.getStep() != null) {
      if (!bool.equals("union"))
        summary.append(bool).append(" ");
      else summary.append("combined with ");
      level++;
      for (Match subMatch : match.getStep()) {
        subIndex++;
        indent();
        summary.append(match.getNodeRef()!=null ?"from "+match.getNodeRef()+" test" :subIndex>0 ?"with ": "");
        summariseMatch(subMatch,subIndex,"step");
      }
      level--;
    }
    if (match.getPath() != null) {
      for (Path path : match.getPath()) {
        summarisePath(path);
      }
    }

    if (match.getWhere() != null) {
      summary.append(" with ");
      summariseWhere(match.getWhere(),0,"");
    }
    if (match.getNode() != null){
      summary.append("as (").append(match.getNode()).append(")");
    }
  }

  private void summarisePath(Path path) {
    summary.append(path.getName());
    if (path.getPath() != null) {
      for (Path subPath : path.getPath()) {
        summary.append("->");
        summarisePath(subPath);
      }
    }
  }




  private void summariseWheres(List<Where> wheres,Integer index,String bool) {
    int subIndex=-1;
    for (Where where : wheres) {
      subIndex++;
      summariseWhere(where,subIndex,bool);
    }
  }


  private void summariseIs(List<Node> inSets) {
    for (Node set : inSets) {
      if (set.getDescription()!=null) {
        summary.append(set.getDescription()).append(" ");
      }
     else summary.append(set.getName()).append(" ");
      if (set.getMatch()!=null) {
        summariseMatch(set.getMatch(),0,"");
      }
    }
  }



  private void summariseWhere(Where where,Integer index, String bool) {
    if (!bool.isEmpty()) {
      if (index>0) summary.append(bool).append(" ");
      else if (bool.equals("or")) summary.append("either ");
    }
    if (where.getName()!=null){
      summary.append(where.getName()).append(" ");
    }
    if (where.getAnd() != null) {
      summariseWheres(where.getAnd(),index,"and");
    }
    if (where.getOr() != null) {
      summariseWheres(where.getOr(),index,"or");
    }
    if (where.getDescription()!=null) {
      summary.append(where.getDescription()).append(" ");
    }
    else if (where.getIs() != null) {
      summariseIs(where.getIs());
    }
    else if (where.getValueLabel()!=null) {
      summary.append(where.getValueLabel()).append(" ");
    }


    if (where.getRelativeTo()!=null) {
      summariseRelativeTo(where);
    }

  }



  private void summariseRelativeTo(Where where) {
    if (where.getRelativeTo() == null) return;
    summariseRelation(where.getRelativeTo());
  }

  private void summariseRelation(RelativeTo relativeTo) {
     if (relativeTo.getName()!=null) {
       summary.append(relativeTo.getName()).append(" ");
     }
     if (relativeTo.getParameterName()!=null) {
       summary.append(relativeTo.getParameterName()).append(" ");
     }
     if (relativeTo.getNodeRef()!=null){
       summary.append(relativeTo.getNodeRef()).append(" ");
     }
  }





  private void indent(){
    summary.append("\n"+ "                         ".substring(0, Math.min(level,20)));
  }
}




