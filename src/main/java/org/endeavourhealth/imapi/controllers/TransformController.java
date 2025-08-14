package org.endeavourhealth.imapi.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.endeavourhealth.imapi.logic.service.RequestObjectService;
import org.endeavourhealth.imapi.logic.service.TransformService;
import org.endeavourhealth.imapi.model.requests.TransformRequest;
import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.imapi.transforms.eqd.EnquiryDocument;
import org.endeavourhealth.imapi.utility.MetricsHelper;
import org.endeavourhealth.imapi.utility.MetricsTimer;
import org.endeavourhealth.imapi.vocabulary.Graph;
import org.endeavourhealth.imapi.vocabulary.Namespace;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("api/transform")
@CrossOrigin(origins = "*")
@Tag(name = "TransformController")
@RequestScope
@Slf4j
public class TransformController {
  private final TransformService transformService = new TransformService();
  private final RequestObjectService requestObjectService = new RequestObjectService();

  @GetMapping("/public/transformeqd")
  @Operation(
    summary = "Run transform of eqd to imq",
    description = "Runs a transform from an xml eqd query document to a set of target objects"
  )
  public TTDocument transformEqd(
    HttpServletRequest request,
    @RequestBody EnquiryDocument eqd
  ) throws Exception {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Transform.TransformEqd.GET")) {
      log.debug("transformEqd");
      List<Graph> graphs = requestObjectService.getUserGraphs(request);
      return new TransformService().transformEqd(eqd, Namespace.IM, graphs);
    }
  }

  @PostMapping("/public/run")
  @Operation(
    summary = "Run transform",
    description = "Runs a transform from a set of typed sources to a set of target objects defined by a transform map"
  )
  public Set<Object> run(
    HttpServletRequest request,
    @RequestBody TransformRequest transformRequest
  ) throws Exception {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Transform.Run.POST")) {
      log.debug("run transform");
      List<Graph> graphs = requestObjectService.getUserGraphs(request);
      return transformService.runTransform(transformRequest, graphs);
    }
  }
}

