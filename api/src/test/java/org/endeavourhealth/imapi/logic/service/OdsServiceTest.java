package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.model.ods.OdsResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OdsServiceTest {

    // @Test
    void getOrganisationData() throws JsonProcessingException {
        OdsResponse response = new OdsService().getOrganisationData("8D536");
        System.out.println(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(response));
    }

    // @Test
    void getRoles() throws JsonProcessingException {
        OdsResponse response = new OdsService().getRoleData();
        System.out.println(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(response));
    }}
