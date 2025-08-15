package org.endeavourhealth.imapi.logic.service;

import org.endeavourhealth.imapi.model.imq.QueryException;
import org.endeavourhealth.imapi.model.requests.EclSearchRequest;
import org.endeavourhealth.imapi.vocabulary.Graph;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class EclModelServiceTest {
  @InjectMocks
  EclService eclService = new EclService();

  @Test
  @Disabled
  void getEcl_NotNullInferred() throws QueryException {
    String actual = eclService.getEcl(new EclSearchRequest());
    assertNotNull(actual);
  }


}
