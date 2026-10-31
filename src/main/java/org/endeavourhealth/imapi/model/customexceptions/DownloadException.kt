package org.endeavourhealth.imapi.model.customexceptions

class DownloadException : Exception {
    constructor(errorMessage: String?, err: Throwable?) : super(errorMessage, err)

    constructor(errorMessage: String?) : super(errorMessage)
}
