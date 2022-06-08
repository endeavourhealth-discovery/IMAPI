import {MysqlService} from "../services/mysql.service";
import {GraphdbService, iri} from "../services/graphdb.service";
import {Sql} from "../model/sql/Sql";
import {Table} from "../model/sql/Table";
import {Join} from "../model/sql/Join";
import {SimpleCondition} from "../model/sql/SimpleCondition";
import {ConditionList} from "../model/sql/ConditionList";
import {Condition} from '../model/sql/Condition';
import {TTIriRef} from '../model/tripletree/TTIriRef';
import {IM, RDF, SHACL} from '../vocabulary';
import {Query, Match, Select} from 'im-library/dist/types/models/modules/AutoGen';

export default class QueryRunner {
  private mysql: MysqlService;
  private graph: GraphdbService;
  private sql: Sql;

  constructor() {
    this.mysql = new MysqlService();
    this.graph = new GraphdbService();
  }

  public async generateSQL(queryIri: string): Promise<string> {
    try {
      const definition: Query = await this.getDefinition(queryIri);

      await this.generateSql(definition);

      return this.sql.toCreate();
    } catch (e) {
      console.error("***** ERROR!!");
      console.log(e);
      return 'Error generating SQL';
    }
  }

  public async runQuery(queryIri: string): Promise<any[]> {
    try {
      const definition: Query = await this.getDefinition(queryIri);

      await this.generateSql(definition);
/*      console.log("===== SQL ========================================================")
      console.log(JSON.stringify(this.sql, null, 2));
      console.log("==================================================================")*/

      console.log("DROPPING: " + this.sql.toDrop());
      await this.mysql.execute(this.sql.toDrop());

      // const refDate = new Date().toISOString().replace("T", " ").replace("Z", "");
      const refDate = '2002-09-06 00:00:00';                                    // TODO: Specific date valid for test data

      let sqlString: string = this.sql.toCreate();
      sqlString = sqlString.replace(/\$ReferenceDate/g, refDate);
      sqlString = sqlString.replace(/\$referenceDate/g, '"' + refDate + '"');   // TODO: Case within query definitions!?
      console.log("CREATING: " + sqlString);
      await this.mysql.execute(sqlString);

      console.log("SELECTING: " + this.sql.toSelect());
      const result = await this.mysql.execute(this.sql.toSelect() + " LIMIT 5");

      console.log(result.length + " rows");

      for(const r of result) {
        console.log(r);
      }

      return result;

    } catch (e) {
      console.error("***** ERROR!!");
      console.log(e);
      return [];
    }
  }

   public async getDefinition(queryIri: string): Promise<Query> {
     console.log("Loading " + queryIri);
     const rs = await this.graph.execute(
       "SELECT * WHERE { ?s ?p ?def }",
       {
         s: iri(queryIri),
         p: iri("http://endhealth.info/im#definition")
       });

     if (rs.length != 1)
       throw new Error("Unable to load definition");

     const def = JSON.parse(rs[0].def.value);

     console.log("===== DEFINITION =================================================")
     console.log(JSON.stringify(def, null, 2));
     console.log("==================================================================")

     return def;
   }

  private async generateSql(query: Query) {
    this.sql = new Sql(query['@id']);
    this.sql.table = this.sql.getTable(query.select.entityType["@id"], "m");

    await this.processSelect(query.select);
  }

  private async processSelect(select: Select) {
    await this.processMatch(this.sql.table, this.sql.conditions, select.match);
  }

  private async processMatch(table: Table, conditions: Condition[], match: Match) {
    if (match.isConcept) {
      await this.processIsConcept(table, conditions, match);
    } else if (match.match) {
      await this.processSubMatch(table, match.property["@id"], match.match);
    } else if (match.inSet) {
      await this.processInSet(table, match);
    } else if (match.entityInValueSet) {
      await this.processEntityInValueSet(table, match, match.entityInValueSet);
    } else if (match.value) {
      await this.processValueCompare(table, conditions, match);
    } else if (match.notExist) {
      await this.processNotExist(table, conditions, match);
    } else if (match.and || match.or) {
      // Globally handled (below)
    } else {
      console.error("Unknown/no filter type\n" + JSON.stringify(match, null, 2));
    }

/*    if (match.valueVar)
      this.sql.fields.push(await this.getSubject(table, match) + " AS " + match.valueVar);*/

    if (match.and)
      await this.processBoolean(table, conditions, match.and, "AND");

    if (match.or)
      await this.processBoolean(table, conditions, match.or, "OR");
  }

  private async processIsConcept(table: Table, conditions: Condition[], match: Match) {
    // Direct comparison to a concept (list)

    const concept: Join = new Join();
    concept.table = this.sql.getTable("http://endhealth.info/im#concept", "t" + this.sql.joins.length);
    if (match.isConcept.length == 1) {
      const im1Id: string = await this.getIM1Id(match.isConcept[0]['@id']);
      concept.on = await this.getField(concept.table, "iri") + " = '" + im1Id + "'";
    } else {
      concept.on = await this.getField(concept.table, "iri") + " IN (" + JSON.stringify(match.isConcept) + ")";
    }
    this.sql.joins.push(concept);

    const tct: Join = new Join();
    tct.table = this.sql.getTable("http://endhealth.info/im#conceptTct", "t" + this.sql.joins.length);
    tct.on = await this.getField(tct.table, "target") + " = " + await this.getField(concept.table, "dbid") + " AND " + await this.getField(tct.table, "property") + " = 11"; // TODO: Correct property!
    this.sql.joins.push(tct);

    const c = new SimpleCondition();
    conditions.push(c);
    c.subject = await this.getSubject(table, match);
    c.predicate = " = ";
    c.object = await this.getField(tct.table, "source");

  }

  private async processEntityInValueSet(table: Table, match: Match, setIri: TTIriRef) {
    if (match.property['@id'] == "http://endhealth.info/im#inResultSet") {
      const join: Join = new Join();
      join.table = this.sql.getTable(setIri['@id'], "t" + this.sql.joins.length);
      join.on = await this.getField(table, "pk") + " = " + await this.getField(join.table, "pk");

      this.sql.joins.push(join);
    } else if (match.property['@id'] == "http://endhealth.info/im#concept") {
      let vs: Join = new Join();
      vs.table = this.sql.getTable("http://endhealth.info/im#ValueSet", "t" + this.sql.joins.length);
      vs.on = await this.getField(vs.table, "iri") + " = '" + setIri['@id'] + "'";
      this.sql.joins.push(vs);

      let vsm: Join = new Join();
      vsm.table = this.sql.getTable("http://endhealth.info/im#ValueSetMember", "t" + this.sql.joins.length);
      vsm.on = await this.getField(vsm.table, "value_set") + " = " + await this.getField(vs.table, "pk") + " AND " + await this.getField(table, "http://endhealth.info/im#concept") + " = " + await this.getField(vsm.table, "member");
      this.sql.joins.push(vsm);
    } else if (match.property['@id'] == "http://endhealth.info/im#code") {
      let vs: Join = new Join();
      vs.table = this.sql.getTable("http://endhealth.info/im#ValueSet", "t" + this.sql.joins.length);
      vs.on = await this.getField(vs.table, "iri") + " = '" + setIri['@id'] + "'";
      this.sql.joins.push(vs);

      let vsm: Join = new Join();
      vsm.table = this.sql.getTable("http://endhealth.info/im#ValueSetMember", "t" + this.sql.joins.length);
      vsm.on = await this.getField(vsm.table, "value_set") + " = " + await this.getField(vs.table, "pk") + " AND " + await this.getField(table, "http://endhealth.info/im#concept") + " = " + await this.getField(vsm.table, "member");
      this.sql.joins.push(vsm);

    } else
      throw new Error("Unknown 'Value In' predicate [" + match.property['@id'] + "]");

  }

  private async processSubMatch(parentTable: Table, propertyId: string, match: Match) {
    const join: Join = this.sql.getJoin(parentTable, propertyId, match.entityType["@id"], "t" + this.sql.joins.length);
    this.sql.joins.push(join);

    await this.processMatch(join.table, join.conditions, match);
  }

  private async processInSet(parentTable: Table, match: Match) {
    for (const set of match.inSet) {
      await this.processEntityInValueSet(parentTable, match, set);
    }
  }

  private async processValueCompare(table: Table, conditions: Condition[], match: Match) {
    const c = new SimpleCondition();
    conditions.push(c);
    c.subject = await this.getSubject(table, match);
    c.predicate = this.getComparison(match.value.comparison);
    c.object = "'" + match.value.valueData + "'";
  }

  private async processNotExist(table: Table, conditions: Condition[], match: Match) {
    const c = new SimpleCondition();
    conditions.push(c);
    c.subject = await this.getSubject(table, match);
    c.predicate = 'IS'
    c.object = 'NULL';
  }

  private async processBoolean(table: Table, conditions: Condition[], matches:Match[], operator: string) {
    const result: ConditionList = new ConditionList();
    result.operator = operator;

    for(const filter of matches) {
      await this.processMatch(table, result.conditions, filter);
    }

    if (result.conditions.length == 0) {
      console.log("================== FILTERS =======================");
      console.log(JSON.stringify(matches, null, 2));
      console.log("================== CONDITIONS =======================");
      console.log(JSON.stringify(result, null, 2));
      console.error("No filters found!");
    } else {
      conditions.push(result);
    }

    return result;
  }


  private async getSubject(table: Table, match: Match) {

    if (match.property) {
      const propType = await this.getPropertyType(table.id, match.property['@id']);
      if (propType.function.value === "true") {
        return this.getFunction(table, match);
      } else {
        return this.getField(table, match.property['@id']);
      }
    } else {
      throw new Error("Function has no property");
    }
  }

  private async getFunction(table: Table, match: Match) {
    console.log('!!!!PROPERTY [' + table.id + '][' + match.property['@id'] + '] IS A FUNCTION!!!!')
    const fn = match.property['@id'];

    if (fn === 'http://endhealth.info/im#age') {
      return 'TIMESTAMPDIFF('
        + await this.getArgument(table, match, 'units') + ', '
        + await this.getField(table, 'http://endhealth.info/im#dateOfBirth') + ', '
        + '$referenceDate)';
    } else if (fn === 'http://endhealth.info/im#TimeDifference') {
      return 'TIMESTAMPDIFF('
        + await this.getArgument(table, match, 'units') + ', '
        + await this.getArgument(table, match, 'firstDate') + ', '
        + await this.getArgument(table, match, 'secondDate') + ')';
    } else if (fn === 'http://endhealth.info/im#gpPatientType') {
      const subSelectTable = this.sql.getTable('http://endhealth.info/im#GPRegistration', 'sub')
      return '(SELECT '
        + await this.getField(subSelectTable, 'http://endhealth.info/im#patientType')
        + ' FROM ' + subSelectTable.name + ' AS ' + subSelectTable.alias
        + ' WHERE ' + await this.getField(subSelectTable, 'http://endhealth.info/im#isSubjectOf') + ' = ' + await this.getField(table, 'pk')
        + ' AND ' + await this.getField(subSelectTable, 'http://endhealth.info/im#organisation') + ' = ' + await this.getField(table, 'http://endhealth.info/im#organisation')
        + ' AND ' + await this.getField(subSelectTable, 'http://endhealth.info/im#effectiveDate') + ' <= $referenceDate '
        + ' AND ( ' + await this.getField(subSelectTable, 'http://endhealth.info/im#endDate') + ' > $referenceDate '
        + ' OR ' + await this.getField(subSelectTable, 'http://endhealth.info/im#endDate') + ' IS NULL )'
        + ')';
    } else if (fn === 'http://endhealth.info/im#gpRegistrationStatus') {
      const subSelectTable = this.sql.getTable('http://endhealth.info/im#GPRegistration', 'sub')
      return '(SELECT '
        + await this.getField(subSelectTable, 'http://endhealth.info/im#patientType')
        + ' FROM ' + subSelectTable.name + ' AS ' + subSelectTable.alias
        + ' WHERE ' + await this.getField(subSelectTable, 'http://endhealth.info/im#isSubjectOf') + ' = ' + await this.getField(table, 'pk')
        + ' AND ' + await this.getField(subSelectTable, 'http://endhealth.info/im#organisation') + ' = ' + await this.getField(table, 'http://endhealth.info/im#organisation')
        + ' AND ' + await this.getField(subSelectTable, 'http://endhealth.info/im#effectiveDate') + ' <= $referenceDate '
        + ' AND ( ' + await this.getField(subSelectTable, 'http://endhealth.info/im#endDate') + ' > $referenceDate '
        + ' OR ' + await this.getField(subSelectTable, 'http://endhealth.info/im#endDate') + ' IS NULL )'
        + ')';
    } else if (fn === 'http://endhealth.info/im#gpGMSRegistrationDate') {
      const subSelectTable = this.sql.getTable('http://endhealth.info/im#GPRegistration', 'sub')
      return '(SELECT '
        + await this.getField(subSelectTable, 'http://endhealth.info/im#effectiveDate')
        + ' FROM ' + subSelectTable.name + ' AS ' + subSelectTable.alias
        + ' WHERE ' + await this.getField(subSelectTable, 'http://endhealth.info/im#isSubjectOf') + ' = ' + await this.getField(table, 'pk')
        + ' AND ' + await this.getField(subSelectTable, 'http://endhealth.info/im#organisation') + ' = ' + await this.getField(table, 'http://endhealth.info/im#organisation')
        + ' AND ' + await this.getField(subSelectTable, 'http://endhealth.info/im#effectiveDate') + ' <= $referenceDate '
        + ' AND ( ' + await this.getField(subSelectTable, 'http://endhealth.info/im#endDate') + ' > $referenceDate '
        + ' OR ' + await this.getField(subSelectTable, 'http://endhealth.info/im#endDate') + ' IS NULL )'
        + ')';
    } else {
      throw new Error('Unknown function [' + fn + ']');
    }
  }

  private async getArgument(table: Table, match: Match, name: string) {
    for(const a of match.function.argument) {
      if (a.parameter === name) {
        if (a.valueVariable == '$this') {
          return this.getField(table, match.property['@id'])
        } else {
          return a.valueData;
        }
      }
    }

    throw new Error("Unknown argument [" + name + "]");
  }

  private getComparison(c: string) {
    switch (c) {
      case "EQUAL": return "=";
      case "GREATER_THAN": return ">";
      case "GREATER_THAN_OR_EQUAL": return ">=";
      case "LESS_THAN": return "<";
      case "LESS_THAN_OR_EQUAL": return "<=";
      case "NOT_EQUAL": return "<>";
      case "MEMBER_OF": throw new Error("Cannot compare \"Member of\"");
      default: throw new Error("Unknown comparator [" + c + "]");
    }
  }

  private async getIM1Id(entity: string): Promise<string> {
    // TODO: Temporary hard coded IM1 maps
    if (entity == "http://endhealth.info/im#2751000252106")
      return 'FHIR_RT_R';

    const rs = await this.graph.execute(
      "SELECT ?id WHERE { ?iri ?im1Id ?id }",
      {
        iri: iri(entity),
        im1Id: iri(IM.IM1ID)
      });

    if (rs.length != 1)
      throw new Error("Unable to get IM1 ID for entity [" + entity + "]");

    return rs[0].id;
  }

  private async getField(table: Table, fieldId: string){
    return this.sql.getField(table, fieldId);
  }

  private async getPropertyType(entity: string, property: string): Promise<any> {
    if (!property)
      throw new Error("No property!!!");

    if (!property.startsWith("http"))
      return { };

    const spql =
      "SELECT ?function ?type ?class\n" +
      "WHERE {\n" +
      "    ?s  " + iri(SHACL.PROPERTY) + " ?bn .\n" +
      "    ?bn " + iri(SHACL.PATH) + " ?p .\n" +
      "    OPTIONAL { BIND(EXISTS {?p " + iri(RDF.TYPE) + " " + iri(SHACL.FUNCTION) + "} AS ?function)  }\n" +
      "    OPTIONAL { ?bn " + iri(SHACL.DATATYPE) + " ?type  }\n" +
      "    OPTIONAL { ?bn " + iri(SHACL.CLASS) + " ?class  }\n" +
      "}";

    const rs = await this.graph.execute(
      spql,
      {
        s: iri(entity),
        p: iri(property),
      });

    if (rs.length != 1) {
      console.log("Unable to get type of property [" + property + "] on entity [" + entity + "]");
      return { };
    }

    return rs[0];
  }
}
