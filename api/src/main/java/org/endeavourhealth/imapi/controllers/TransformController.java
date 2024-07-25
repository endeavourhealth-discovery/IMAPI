package org.endeavourhealth.imapi.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.endeavourhealth.imapi.logic.service.TransformService;
import org.endeavourhealth.imapi.model.iml.ModelDocument;
import org.endeavourhealth.imapi.model.iml.TransformRequest;
import org.endeavourhealth.imapi.transforms.eqd.EnquiryDocument;
import org.endeavourhealth.imapi.utility.MetricsHelper;
import org.endeavourhealth.imapi.utility.MetricsTimer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Set;

@RestController
@RequestMapping("api/transform")
@CrossOrigin(origins = "*")
@Tag(name = "TransformController")
@RequestScope
public class TransformController {
  private static final Logger LOG = LoggerFactory.getLogger(TransformController.class);
  private final TransformService transformService = new TransformService();


  @GetMapping("/public/transformeqd")
  @Operation(
    summary = "Run transform of eqd to imq",
    description = "Runs a transform from an xml eqd query document to a set of target objects"
  )
  public ModelDocument transformEqd(@RequestBody EnquiryDocument eqd) throws Exception {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Transform.TransformEqd.GET")) {
      LOG.debug("run transform");
      return new TransformService().transformEqd(eqd);
    }
  }

  @PostMapping("/public/run")
  @Operation(
    summary = "Run transform",
    description = "Runs a transform from a set of typed sources to a set of target objects defined by a transform map"
  )
  public Set<Object> run(@RequestBody TransformRequest transformRequest) throws Exception {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Transform.Run.POST")) {
      LOG.debug("run transform");
      return transformService.runTransform(transformRequest);
    }
  }
}

