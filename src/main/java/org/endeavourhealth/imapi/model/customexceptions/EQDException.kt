package org.endeavourhealth.imapi.model.customexceptions


class EQDException : Exception {
    constructor(errorMessage: String?, ex: Throwable?) : super(errorMessage, ex)

    constructor(errorMessage: String?) : super(errorMessage)
}