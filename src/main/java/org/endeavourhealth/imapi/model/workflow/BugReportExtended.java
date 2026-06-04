package org.endeavourhealth.imapi.model.workflow;

import lombok.Getter;
import org.endeavourhealth.imapi.model.tripletree.TTIriRefExtended;
import org.endeavourhealth.interfacemanager.model.*;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

@Getter
public class BugReportExtended extends BugReport implements Task {

  public BugReportExtended(TTIriRefExtended id, TaskType type, String createdBy, String assignedTo, TaskState state, LocalDateTime dateCreated, List<TaskHistory> history, String hostUrl, String product, String version, TaskModule module, OperatingSystem os, Browser browser, Severity severity, Status status, String error, String description, String reproduceSteps, String expectedResult, String actualResult) {
    this.id(id).createdBy(createdBy).type(type).state(state).assignedTo(assignedTo).dateCreated(OffsetDateTime.from(dateCreated)).history(history).hostUrl(hostUrl).product(product).version(version).module(module).os(os).browser(browser).severity(severity).status(status).error(error).description(description).reproduceSteps(reproduceSteps).expectedResult(expectedResult).actualResult(actualResult);
    this.product(product).version(version).module(module).os(os).browser(browser).severity(severity).status(status).error(error).description(description).reproduceSteps(reproduceSteps).expectedResult(expectedResult).actualResult(actualResult);
  }

  public BugReportExtended() {
  }

  @Override
  public BugReportExtended product(String product) {
    this.setProduct(product);
    return this;
  }

  @Override
  public BugReportExtended version(String version) {
    this.setVersion(version);
    return this;
  }

  @Override
  public BugReportExtended module(TaskModule module) {
    this.setModule(module);
    return this;
  }

  @Override
  public BugReportExtended os(OperatingSystem os) {
    this.setOs(os);
    return this;
  }

  @Override
  public BugReportExtended browser(Browser browser) {
    this.setBrowser(browser);
    return this;
  }

  @Override
  public BugReportExtended severity(Severity severity) {
    this.setSeverity(severity);
    return this;
  }

  @Override
  public BugReportExtended status(Status status) {
    this.setStatus(status);
    return this;
  }

  @Override
  public BugReportExtended error(String error) {
    this.setError(error);
    return this;
  }

  @Override
  public BugReportExtended description(String description) {
    this.setDescription(description);
    return this;
  }

  @Override
  public BugReportExtended reproduceSteps(String reproduceSteps) {
    this.setReproduceSteps(reproduceSteps);
    return this;
  }

  @Override
  public BugReportExtended expectedResult(String expectedResult) {
    this.setExpectedResult(expectedResult);
    return this;
  }

  @Override
  public BugReportExtended actualResult(String actualResult) {
    this.setActualResult(actualResult);
    return this;
  }

  @Override
  public BugReportExtended osOther(String osOther) {
    this.setOsOther(osOther);
    return this;
  }

  @Override
  public BugReportExtended browserOther(String browserOther) {
    this.setBrowserOther(browserOther);
    return this;
  }

  @Override
  public BugReportExtended history(List<TaskHistory> history) {
    this.setHistory(history);
    return this;
  }
}
