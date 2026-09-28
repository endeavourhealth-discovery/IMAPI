package org.endeavourhealth.imapi.model.requests

import org.endeavourhealth.imapi.model.tripletree.TTDocument
import org.endeavourhealth.imapi.vocabulary.NAMESPACE

class FileDocumentRequest {
  var document: TTDocument? = null
  var insertNamespace: NAMESPACE? = null
}
