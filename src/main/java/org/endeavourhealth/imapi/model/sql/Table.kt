package org.endeavourhealth.imapi.model.sql;

class Table(
  var dataModels: List<String>? = ArrayList(),
  var table: String = "",
  var primaryKey: String = "",
  var condition: String? = "",
  var dataModel: String = "",
  var fields: HashMap<String, Field> = HashMap(),
  var relationships: HashMap<String, Relationship> = HashMap()
)
