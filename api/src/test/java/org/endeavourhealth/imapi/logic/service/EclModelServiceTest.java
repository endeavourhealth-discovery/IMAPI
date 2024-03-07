package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.model.imq.QueryException;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.zip.DataFormatException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
public class EclModelServiceTest {
    @InjectMocks
    EclService eclService;

    @Test
    @Ignore
    void getEcl_NotNullInferred() throws QueryException, JsonProcessingException {
       String actual = eclService.getEcl(new Query());
        assertNotNull(actual);
    }

    private Query getQuery() throws JsonProcessingException {
        String query=
          "\n" +
            "\t{\n" +
            "\t\t \"match\": [\n" +
            "\t\t\t{\n" +
            "\t\t\t\t \"instanceOf\": {\n" +
            "\t\t\t\t\t \"descendantsOrSelfOf\": true,\n" +
            "\t\t\t\t\t \"@id\": \"http://snomed.info/sct#763158003\"\n" +
            "\t\t\t\t},\n" +
            "\t\t\t\t \"property\": [\n" +
            "\t\t\t\t\t{\n" +
            "\t\t\t\t\t\t \"@id\": \"http://snomed.info/sct#127489000\",\n" +
            "\t\t\t\t\t\t \"descendantsOrSelfOf\": true,\n" +
            "\t\t\t\t\t\t \"is\": [\n" +
            "\t\t\t\t\t\t\t{\n" +
            "\t\t\t\t\t\t\t\t \"descendantsOrSelfOf\": true,\n" +
            "\t\t\t\t\t\t\t\t \"@id\": \"http://snomed.info/sct#1119343008\"\n" +
            "\t\t\t\t\t\t\t}\n" +
            "\t\t\t\t\t\t],\n" +
            "\t\t\t\t\t\t \"anyRoleGroup\": true\n" +
            "\t\t\t\t\t}\n" +
            "\t\t\t\t]\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t\t \"instanceOf\": {\n" +
            "\t\t\t\t\t \"descendantsOrSelfOf\": true,\n" +
            "\t\t\t\t\t \"@id\": \"http://snomed.info/sct#39330711000001103\"\n" +
            "\t\t\t\t}\n" +
            "\t\t\t}\n" +
            "\t\t],\n" +
            "\t\t \"bool\": \"or\"\n" +
            "\t}\n" +
            "]";
        return new ObjectMapper().readValue(query,Query.class);
    }
}
