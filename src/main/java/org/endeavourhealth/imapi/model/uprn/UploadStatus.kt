package org.endeavourhealth.imapi.model.uprn

class UploadStatusMessage {
  var status: String? = null
}

class UploadErrorMessage {
  var domain: String? = null
  var message: String? = null
  var reason: Int? = null
}

class UploadError {
  var code: String? = null
  var message: String? = null
  var request: String? = null
  var errors: List<UploadErrorMessage>? = null
}

class UploadStatus {
  var apiVersion: String? = null
  var upload: UploadStatusMessage? = null
  var error: UploadError? = null
}