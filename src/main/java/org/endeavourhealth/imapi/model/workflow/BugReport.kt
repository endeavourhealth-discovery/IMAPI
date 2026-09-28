package org.endeavourhealth.imapi.model.workflow

import org.endeavourhealth.imapi.model.workflow.bugReport.*

class BugReport(
  var product: String? = null,
  var version: String? = null,
  var module: TaskModule? = null,
  var os: OperatingSystem? = null,
  var osOther: String? = null,
  var browser: Browser? = null,
  var browserOther: String? = null,
  var severity: Severity? = null,
  var status: Status? = null,
  var error: String? = null,
  var description: String? = null,
  var reproduceSteps: String? = null,
  var expectedResult: String? = null,
  var actualResult: String? = null
) : Task()
