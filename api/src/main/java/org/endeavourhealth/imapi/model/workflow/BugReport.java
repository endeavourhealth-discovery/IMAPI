package org.endeavourhealth.imapi.model.workflow;

import lombok.Getter;
import org.endeavourhealth.imapi.model.workflow.bugReport.*;

@Getter
public class BugReport extends Task {
    private String product;
    private String version;
    private TaskModule module;
    private OperatingSystem os;
    private Browser browser;
    private Severity severity;
    private Status status;
    private String error;
    private String description;
    private String reproduceSteps;
    private String expectedResult;
    private String actualResult;

    public BugReport(String product, String version, TaskModule module, OperatingSystem os, Browser browser, Severity severity, Status status, String error, String description, String reproduceSteps, String expectedResult, String actualResult) {
        this.product = product;
        this.version = version;
        this.module = module;
        this.os = os;
        this.browser = browser;
        this.severity = severity;
        this.status = status;
        this.error = error;
        this.description = description;
        this.reproduceSteps = reproduceSteps;
        this.expectedResult = expectedResult;
        this.actualResult = actualResult;
    }

    public BugReport() {}

    public BugReport setProduct(String product) {
        this.product = product;
        return this;
    }

    public BugReport setVersion(String version) {
        this.version = version;
        return this;
    }

    public BugReport setModule(TaskModule module) {
        this.module = module;
        return this;
    }

    public BugReport setOs(OperatingSystem os) {
        this.os = os;
        return this;
    }

    public BugReport setBrowser(Browser browser) {
        this.browser = browser;
        return this;
    }

    public BugReport setSeverity(Severity severity) {
        this.severity = severity;
        return this;
    }

    public BugReport setStatus(Status status) {
        this.status = status;
        return this;
    }

    public BugReport setError(String error) {
        this.error = error;
        return this;
    }

    public BugReport setDescription(String description) {
        this.description = description;
        return this;
    }

    public BugReport setReproduceSteps(String reproduceSteps) {
        this.reproduceSteps = reproduceSteps;
        return this;
    }

    public BugReport setExpectedResult(String expectedResult) {
        this.expectedResult = expectedResult;
        return this;
    }

    public BugReport setActualResult(String actualResult) {
        this.actualResult = actualResult;
        return this;
    }
}
