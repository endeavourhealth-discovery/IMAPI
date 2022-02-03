package org.endeavourhealth.imapi.logic.service;

import org.endeavourhealth.imapi.dataaccess.InstanceRepository;
import org.endeavourhealth.imapi.dataaccess.entity.Tpl;
import org.endeavourhealth.imapi.model.dto.InstanceDTO;
import org.endeavourhealth.imapi.model.report.SimpleCount;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Component
public class InstanceService {

    private final InstanceRepository instanceRepository = new InstanceRepository();

    public InstanceDTO getInstancePredicates(String iri, Set<String> predicates) {
        if(iri==null)
            return null;
        TTEntity ttEntity = new TTEntity(iri);
        InstanceDTO result = new InstanceDTO();

        List<Tpl> triples = instanceRepository.getTriplesRecursive(iri, predicates);

        List<TTIriRef> pre = new ArrayList<>();

        // Reconstruct
        HashMap<Integer, TTNode> nodeMap = new HashMap<>();

        for (Tpl triple : triples) {
            TTValue v = Tpl.getValue(nodeMap, triple);
            pre.add(triple.getPredicate());

            addTripleValueToEntity(ttEntity, nodeMap, triple, v);
        }
        result.setEntity(ttEntity);
        result.setPredicates(pre);
        return result;
    }

    private void addTripleValueToEntity(TTEntity ttEntity, HashMap<Integer, TTNode> nodeMap, Tpl triple, TTValue v) {
        if (triple.getParent() == null) {
            if (triple.isFunctional()) {
                ttEntity.set(triple.getPredicate(), v);
            } else {
                ttEntity.addObject(triple.getPredicate(), v);
            }
        } else {
            TTNode n = nodeMap.get(triple.getParent());
            if (n == null)
                throw new IllegalStateException("Unknown parent node!");
            if (triple.isFunctional()) {
                n.set(triple.getPredicate(), v);
            } else {
                n.addObject(triple.getPredicate(), v);
            }
        }
    }

    public List<TTIriRef> search(String request, Set<String> types) {
        return instanceRepository.search(request, types);
    }

    public List<SimpleCount> typesCount() {
        return instanceRepository.getTypesCount();
    }
}
