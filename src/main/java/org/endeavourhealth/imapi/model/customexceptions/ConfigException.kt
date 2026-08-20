package org.endeavourhealth.imapi.model.customexceptions

class ConfigException : Exception {
    constructor(message: String?) : super(message)

    constructor(message: String?, exception: Throwable?) : super(message, exception)
}
