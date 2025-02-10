package org.endeavourhealth.imapi.logic.service;

import org.endeavourhealth.imapi.model.dto.GraphDto;
import org.endeavourhealth.imapi.model.tripletree.TTArray;
import org.endeavourhealth.imapi.model.tripletree.TTBundle;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.OWL;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

@Component
public class GraphDtoService {
  private static final Logger LOG = LoggerFactory.getLogger(GraphDtoService.class);
  private EntityService entityService = new EntityService();
  private DataModelService dataModelService = new DataModelService();

  public GraphDto getGraphData(String iri) {
    if(null == iri || iri.isEmpty()) return new GraphDto();

    TTEntity entity = entityService.getBundle(iri, Set.of(RDFS.SUBCLASS_OF, RDFS.LABEL)).getEntity();

    GraphDto graphData = new GraphDto().setKey("0").setIri(entity.getIri()).setName(entity.getName());
    GraphDto graphParents = new GraphDto().setKey("0_0").setName("Is a");
    GraphDto graphChildren = new GraphDto().setKey("0_1").setName("Subtypes");

    TTBundle axioms = entityService.getBundle(iri, new HashSet<>(Arrays.asList(RDFS.SUBCLASS_OF, IM.ROLE_GROUP)));
    List<GraphDto> axiomGraph = new ArrayList<>();

    List<GraphDto> dataModelProps = dataModelService.getDataModelProperties(iri).stream().map(prop -> new GraphDto(prop.getProperty().getIri(), prop.getProperty().getName(), prop.getType() != null ? prop.getType().getIri() : "", prop.getType() != null ? prop.getType().getName() : "", prop.getInheritedFrom() != null ? prop.getInheritedFrom().getIri() : "", prop.getInheritedFrom() != null ? prop.getInheritedFrom().getName() : "")).toList();

    List<GraphDto> isas = getEntityDefinedParents(entity);

    List<GraphDto> subtypes = getDefinitionSubTypes(iri).stream().map(subtype -> new GraphDto().setName(subtype.getName()).setIri(subtype.getIri())).toList();

    GraphDto axiom = new GraphDto().setKey("0_2").setName("Axioms");
    GraphDto axiomWrapper = getWrapper(axiomGraph, "0_2_0");
    addWrapper(axiom, axiomWrapper, "0_2_0");

    GraphDto dataModel = new GraphDto().setKey("0_3").setName("Data model properties");

    GraphDto dataModelDirect = new GraphDto().setKey("0_3_0").setName("Direct");
    GraphDto dataModelDirectWrapper = getWrapper(dataModelProps, "0_3_0_0");
    addWrapper(dataModelDirect, dataModelDirectWrapper, "0_3_0_0");

    GraphDto dataModelInherited = new GraphDto().setKey("0_3_1").setName("Inherited");
    GraphDto dataModelInheritedWrapper = getDataModelInheritedWrapper(dataModelProps);
    addWrapper(dataModelInherited, dataModelInheritedWrapper, "0_3_1_0");

    if (!dataModelDirectWrapper.getLeafNodes().isEmpty()) {
      dataModel.getChildren().add(dataModelDirect);
    }
    if (!dataModelInheritedWrapper.getLeafNodes().isEmpty()) {
      dataModel.getChildren().add(dataModelInherited);
    }

    GraphDto childrenWrapper = new GraphDto().setKey("0_1_0").setType(!subtypes.isEmpty() ? GraphDto.GraphType.SUBTYPE : GraphDto.GraphType.NONE);
    childrenWrapper.getLeafNodes().addAll(subtypes);

    GraphDto parentsWrapper = new GraphDto().setKey("0_0_0").setType(!isas.isEmpty() ? GraphDto.GraphType.ISA : GraphDto.GraphType.NONE);
    parentsWrapper.getLeafNodes().addAll(isas);

    graphParents.getChildren().add(parentsWrapper);
    graphChildren.getChildren().add(childrenWrapper);

    graphData.getChildren().add(graphParents);
    graphData.getChildren().add(graphChildren);
    if (!(axiomWrapper.getLeafNodes().isEmpty())) {
      graphData.getChildren().add(axiom);
    }
    if (!(dataModelDirectWrapper.getLeafNodes().isEmpty() && dataModelInheritedWrapper.getLeafNodes().isEmpty())) {
      graphData.getChildren().add(dataModel);
    }
    return graphData;
  }

  private GraphDto getWrapper(List<GraphDto> props, String key) {
    GraphDto wrapper = new GraphDto().setKey(key).setType(GraphDto.GraphType.PROPERTIES);
    wrapper.getLeafNodes().addAll(props.stream().filter(prop -> prop.getInheritedFromIri() == null).toList());
    return wrapper;
  }

  private GraphDto getDataModelInheritedWrapper(List<GraphDto> dataModelProps) {
    GraphDto dataModelInheritedWrapper = new GraphDto().setKey("0_3_1_0").setType(GraphDto.GraphType.PROPERTIES);
    dataModelInheritedWrapper.getLeafNodes().addAll(dataModelProps.stream().filter(prop -> prop.getInheritedFromIri() != null).toList());
    return dataModelInheritedWrapper;
  }

  private void addWrapper(GraphDto dto, GraphDto wrapper, String key) {
    dto.getChildren().add(wrapper.getLeafNodes().isEmpty() ? new GraphDto().setKey(key).setType(GraphDto.GraphType.NONE) : wrapper);
  }

  private List<GraphDto> getEntityDefinedParents(TTEntity entity) {
    TTArray parent = entity.get(iri(RDFS.SUBCLASS_OF));
    if (parent == null) return Collections.emptyList();
    List<GraphDto> result = new ArrayList<>();
    parent.getElements().forEach(item -> {
      if (!OWL.THING.equals(item.asIriRef().getIri()))
        result.add(new GraphDto().setIri(item.asIriRef().getIri()).setName(item.asIriRef().getName()).setPropertyType(iri(RDFS.SUBCLASS_OF).getName()));
    });

    return result;
  }

  private List<TTIriRef> getDefinitionSubTypes(String iri) {
    return entityService.getChildren(iri, null, null, null, false).stream().map(t -> new TTIriRef(t.getIri(), t.getName())).toList();
  }
}
