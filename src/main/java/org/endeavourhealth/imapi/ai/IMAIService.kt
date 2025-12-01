package org.endeavourhealth.imapi.ai;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.logic.service.DataModelService;
import org.endeavourhealth.imapi.logic.service.SearchService;
import org.endeavourhealth.imapi.model.DataModelProperty;
import org.endeavourhealth.imapi.model.customexceptions.OpenSearchException;
import org.endeavourhealth.imapi.model.iml.Page;
import org.endeavourhealth.imapi.model.imq.Node;
import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.model.imq.QueryException;
import org.endeavourhealth.imapi.model.imq.Where;
import org.endeavourhealth.imapi.model.requests.QueryRequest;
import org.endeavourhealth.imapi.model.responses.SearchResponse;
import org.endeavourhealth.imapi.model.search.SearchResultSummary;
import org.endeavourhealth.imapi.model.sql.MappingParser;
import org.endeavourhealth.imapi.model.sql.TableMap;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.Namespace;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class IMAIService {
  private final ObjectMapper om = new ObjectMapper();
  private final DataModelService dataModelService = new DataModelService();
  private final SearchService searchService = new SearchService();

  @Tool(description = "Get an entity IRI for an entity (concept, data model, property, query, set or SNOMED code) given its name")
  public String getIriForEntity(String text) throws OpenSearchException, JsonProcessingException, QueryException {
    Where w = new Where();
    w.addAnd(new Where()
      .setIri(IM.HAS_STATUS)
      .setIs(List.of(
        new Node().setIri(IM.ACTIVE.toString())))
    );
    w.addAnd(new Where()
      .setIri(IM.HAS_SCHEME)
      .setIs(List.of(
        new Node().setIri(Namespace.IM.toString()),
        new Node().setIri(Namespace.SNOMED.toString()),
        new Node().setIri(Namespace.SMARTLIFE.toString()))
      )
    );

    Query qry = new Query();
    qry.setWhere(w);

    QueryRequest qr = new QueryRequest();
    qr.setTextSearch(text);
    qr.setPage(new Page().setPageNumber(1).setPageSize(1));
    qr.setQuery(qry);
    SearchResponse searchResponse = searchService.queryIMSearch(qr);

    if (searchResponse != null) {
      List<SearchResultSummary> entities = searchResponse.getEntities();
      if (entities != null && !entities.isEmpty())
        return om.writeValueAsString(entities.getFirst());
    }
    return "";
  }
  @Tool(description = "Get the properties for a data model")
  public List<DataModelProperty> getHealthDataModelProperties(String modelIri) {
    return dataModelService.getDataModelProperties(modelIri);
  }

  @Tool(description = "Get the IM Data Model to SQL map")
  public TableMap getDataModelMap() throws IOException {
    return new MappingParser().parse("IMQtoMYSQL.json");
  }
}