package org.endeavourhealth.imapi.model.uprn

import com.fasterxml.jackson.annotation.JsonProperty

class UprnSearchResponse {
    @get:JsonProperty("Address_format")
    var Address_format: String? = null
    @get:JsonProperty("Postcode_quality")
    var Postcode_quality: String? = null
    @get:JsonProperty("Matched")
    var Matched: Boolean? = null
    @get:JsonProperty("BestMatch")
    var BestMatch: BestMatch? = null
}

class BestMatch {
    @get:JsonProperty("UPRN")
    var UPRN: String? = null
    @get:JsonProperty("Qualifier")
    var Qualifier: String? = null
    @get:JsonProperty("LogicalStatus")
    var LogicalStatus: String? = null
    @get:JsonProperty("Classification")
    var Classification: String? = null
    @get:JsonProperty("ClassTerm")
    var ClassTerm: String? = null
    @get:JsonProperty("Algorithm")
    var Algorithm: String? = null
    @get:JsonProperty("ABPAddress")
    var ABPAddress: Address? = null
    @get:JsonProperty("Match_pattern")
    var Match_pattern: MatchPattern? = null
}