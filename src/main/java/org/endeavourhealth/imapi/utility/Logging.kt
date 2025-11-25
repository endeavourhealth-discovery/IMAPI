package org.endeavourhealth.imapi.utility

import org.slf4j.Logger
import org.slf4j.LoggerFactory

fun <T: Any> logger(forClass: Class<T>): Logger {
  return LoggerFactory.getLogger(forClass)
}
