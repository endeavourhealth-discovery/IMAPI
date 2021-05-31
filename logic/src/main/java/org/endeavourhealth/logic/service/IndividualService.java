package org.endeavourhealth.logic.service;

import org.endeavourhealth.dataaccess.entity.TplInsData;
import org.endeavourhealth.dataaccess.entity.TplInsObject;
import org.endeavourhealth.dataaccess.repository.InstanceTplDataRepository;
import org.endeavourhealth.dataaccess.repository.InstanceTplRepository;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.endeavourhealth.imapi.model.tripletree.TTLiteral.literal;

@Component
@Qualifier("IndividualService")
public class IndividualService {
    private static final Logger LOG = LoggerFactory.getLogger(IndividualService.class);

    InstanceTplRepository instanceTplRepository = new InstanceTplRepository();
    InstanceTplDataRepository instanceTplDataRepository = new InstanceTplDataRepository();

    public TTConcept getIndividual(String iri) throws Exception {
        LOG.debug("getIndividual");

        TTConcept result = new TTConcept().setIri(iri);

        List<TplInsObject> objects = instanceTplRepository.findAllBySubjectIri(iri);

        Map<Integer, TTNode> nodes = new HashMap<>();

        for (TplInsObject o : objects) {
            if (o.getBnode() == null) {
                if (o.getObject() != null) {
                    if (result.has(o.getPredicate()))
                        result.addObject(o.getPredicate(), o.getObject());
                    else
                        result.set(o.getPredicate(), o.getObject());
                } else {
                    TTNode bnode = new TTNode();
                    if (result.has(o.getPredicate()))
                        result.addObject(o.getPredicate(), bnode);
                    else
                        result.set(o.getPredicate(), bnode);

                    nodes.put(o.getDbid(), bnode);
                }
            } else {
                TTNode bnode = nodes.get(o.getBnode());
                if (bnode.has(o.getPredicate()))
                    bnode.addObject(o.getPredicate(), o.getObject());
                else
                    bnode.set(o.getPredicate(), o.getObject());
            }
        }

        List<TplInsData> data = instanceTplDataRepository.findAllBySubjectIri(iri);
        for (TplInsData d : data) {
            if (d.getBnode() == null) {
                if (result.has(d.getPredicate()))
                    result.addObject(d.getPredicate(), literal(d.getLiteral(), d.getDataType()));
                else
                    result.set(d.getPredicate(), literal(d.getLiteral(), d.getDataType()));
            } else {
                TTNode bnode = nodes.get(d.getBnode());
                if (bnode.has(d.getPredicate()))
                    bnode.addObject(d.getPredicate(), literal(d.getLiteral(), d.getDataType()));
                else
                    bnode.set(d.getPredicate(), literal(d.getLiteral(), d.getDataType()));
            }
        }

        return result;
    }
}
