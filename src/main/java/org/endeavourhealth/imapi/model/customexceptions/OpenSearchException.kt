package org.endeavourhealth.imapi.model.customexceptions

class OpenSearchException : Exception {
    constructor(errorMessage: String?, err: Throwable?) : super(errorMessage, err)

    constructor(errorMessage: String?) : super(errorMessage)
}
