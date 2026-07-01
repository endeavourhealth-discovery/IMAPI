package org.endeavourhealth.imapi.model.extensions

import org.endeavourhealth.interfacemanager.model.SearchTermCode

fun SearchTermCode.compareTo(other: SearchTermCode): Int =
  compareBy<SearchTermCode, String?>(
    nullsLast<String>(),
    { it.status?.iri }
  )
    .thenComparator { a, b ->
      nullsLast<String>().compare(
        a.term?.takeIf { it.isNotEmpty() },
        b.term?.takeIf { it.isNotEmpty() }
      )
    }
    .compare(this, other)