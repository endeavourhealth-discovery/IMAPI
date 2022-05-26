import {MysqlService} from "../services/mysql.service";
import {GraphdbService, iri} from "../services/graphdb.service";
import {DataSet} from "../model/sets/DataSet";
import {Sql} from "../model/sql/Sql";
import {Table} from "../model/sql/Table";
import {Join} from "../model/sql/Join";
import {SimpleCondition} from "../model/sql/SimpleCondition";
import {ConditionList} from "../model/sql/ConditionList";
import {Condition} from '../model/sql/Condition';
import {Argument} from '../model/sets/Argument';
import {Filter} from '../model/sets/Filter';
import {Select} from '../model/sets/Select';
import {TTIriRef} from '../model/tripletree/TTIriRef';

export default class QueryRunner {
  private mysql: MysqlService;
  private graph: GraphdbService;
  private sql: Sql;

  constructor() {
    this.mysql = new MysqlService();
    this.graph = new GraphdbService();
  }

  public async runQuery(queryIri: string): Promise<any[]> {
    try {
      const definition: DataSet = await this.getDefinition(queryIri);

      this.generateSql(definition);
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
      const result = await this.mysql.execute(this.sql.toSelect());

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

   public async getDefinition(queryIri: string): Promise<DataSet> {
    console.log("Loading " + queryIri);
    const rs = await this.graph.execute(
      "SELECT * WHERE { ?s ?p ?def } LIMIT 1",
      {
        s: iri(queryIri),
        p: iri("http://endhealth.info/im#definition")
      });

    if (rs.length != 1)
      return { } as DataSet;

     console.log("===== DEFINITION =================================================")
     console.log(JSON.stringify(rs[0].def, null, 2));
     console.log("==================================================================")

    return rs[0].def;
  }

  private generateSql(dataset: DataSet): void {
    this.sql = new Sql(dataset['@id']);
    this.sql.table = this.sql.getTable(dataset.select.entityType["@id"], "m");

    this.processSelect(dataset.select);
  }

  private processSelect(select: Select) {
    this.processFilter(this.sql.table, this.sql.conditions, select.filter);
  }

  private processFilter(table: Table, conditions: Condition[], filter: Filter) {
    if (filter.valueConcept) {
      this.processValueConcept(table, conditions, filter);
    } else if (filter.valueObject) {
      this.processValueObject(table, filter.property["@id"], filter.valueObject);
    } else if (filter.subsetOf) {
      for (const s of filter.subsetOf)
        this.processSubsetOf(table, s);
    } else if (filter.valueIn) {
      this.processValueIn(table, conditions, filter);
    } else if (filter.valueCompare) {
      this.processValueCompare(table, conditions, filter);
    } else if (filter.notExist) {
      this.processNotExist(table, conditions, filter);
    } else if (filter.valueVar || filter.and || filter.or) {
      // Globally handled (below)
    } else {
      console.error("Unknown/no filter type\n" + JSON.stringify(filter, null, 2));
    }

    if (filter.valueVar)
      this.sql.fields.push(this.getSubject(table, filter) + " AS " + filter.valueVar);

    if (filter.and)
      this.getFilters(table, conditions, filter.and, "AND");

    if (filter.or)
      this.getFilters(table, conditions, filter.or, "OR");
  }

  private processValueConcept(table: Table, conditions: Condition[], filter: Filter) {
    // Direct comparison to a concept (list)

    const join: Join = new Join();
    join.table = this.sql.getTable("http://endhealth.info/im#conceptTct", "t" + this.sql.joins.length);
    if (filter.valueConcept.length == 1) {
      join.on = this.sql.getField(join.table, "iri") + " = '" + filter.valueConcept[0]['@id'] + "'";
    } else {
      join.on = this.sql.getField(join.table, "iri") + " IN (" + JSON.stringify(filter.valueConcept) + ")";
    }
    this.sql.joins.splice(this.sql.joins.length-1, 0, join);

    const c = new SimpleCondition();
    conditions.push(c);
    c.subject = this.getSubject(table, filter);
    c.predicate = " = ";
    c.object = this.sql.getField(join.table, "child");

  }

  private processValueIn(table: Table, conditions: Condition[], filter: Filter) {
    if (filter.property['@id'] == "http://endhealth.info/im#inResultSet") {
      const join: Join = new Join();
      join.table = this.sql.getTable(filter.valueIn[0]['@id'], "t" + this.sql.joins.length);
      join.on = this.sql.getField(table, "pk") + " = " + this.sql.getField(join.table, "pk");

      this.sql.joins.push(join);
    } else if (filter.property['@id'] == "http://endhealth.info/im#concept") {
      let vs: Join = new Join();
      vs.table = this.sql.getTable("http://endhealth.info/im#ValueSet", "t" + this.sql.joins.length);
      vs.on = this.sql.getField(vs.table, "iri") + " = '" + filter.valueIn[0]['@id'] + "'";
      this.sql.joins.push(vs);

      let vsm: Join = new Join();
      vsm.table = this.sql.getTable("http://endhealth.info/im#ValueSetMember", "t" + this.sql.joins.length);
      vsm.on = this.sql.getField(vsm.table, "value_set") + " = " + this.sql.getField(vs.table, "pk") + " AND " + this.sql.getField(table, "http://endhealth.info/im#concept") + " = " + this.sql.getField(vsm.table, "member");
      this.sql.joins.push(vsm);
    } else if (filter.property['@id'] == "http://endhealth.info/im#code") {
      let vs: Join = new Join();
      vs.table = this.sql.getTable("http://endhealth.info/im#ValueSet", "t" + this.sql.joins.length);
      vs.on = this.sql.getField(vs.table, "iri") + " = '" + filter.valueIn[0]['@id'] + "'";
      this.sql.joins.push(vs);

      let vsm: Join = new Join();
      vsm.table = this.sql.getTable("http://endhealth.info/im#ValueSetMember", "t" + this.sql.joins.length);
      vsm.on = this.sql.getField(vsm.table, "value_set") + " = " + this.sql.getField(vs.table, "pk") + " AND " + this.sql.getField(table, "http://endhealth.info/im#concept") + " = " + this.sql.getField(vsm.table, "member");
      this.sql.joins.push(vsm);

    } else
      throw "Unknown 'Value In' predicate [" + filter.property['@id'] + "]";

  }

  private processValueObject(parentTable: Table, propertyId: string, filter: Filter) {
    const join: Join = this.sql.getJoin(parentTable, propertyId, filter.entityType["@id"], "t" + this.sql.joins.length);
    this.sql.joins.push(join);

    this.processFilter(join.table, join.conditions, filter);
  }

  private processSubsetOf(parentTable: Table, queryIri: TTIriRef) {
    const join: Join = new Join();
    join.table = this.sql.getTable(queryIri['@id'], "t" + this.sql.joins.length);
    join.on = this.sql.getField(parentTable, "pk") + " = " + this.sql.getField(join.table, "pk");

    this.sql.joins.push(join);
  }

  private processValueCompare(table: Table, conditions: Condition[], filter: Filter) {
    const c = new SimpleCondition();
    conditions.push(c);
    c.subject = this.getSubject(table, filter);
    c.predicate = this.getComparison(filter.valueCompare.comparison);
    c.object = "'" + filter.valueCompare.valueData + "'";
  }

  private processNotExist(table: Table, conditions: Condition[], filter: Filter) {
    const c = new SimpleCondition();
    conditions.push(c);
    c.subject = this.getSubject(table, filter);
    c.predicate = 'IS'
    c.object = 'NULL';
  }

  private getFilters(table: Table, conditions: Condition[], filters:Filter[], operator: string) {
    const result: ConditionList = new ConditionList();
    result.operator = operator;

    for(const filter of filters) {
      this.processFilter(table, result.conditions, filter);
    }

    if (result.conditions.length == 0) {
      console.log("================== FILTERS =======================");
      console.log(JSON.stringify(filters, null, 2));
      console.log("================== CONDITIONS =======================");
      console.log(JSON.stringify(result, null, 2));
      console.error("No filters found!");
    } else {
      conditions.push(result);
    }

    return result;
  }


  private getSubject(table: Table, filter: Filter): string {
    if (filter.function)
      return this.getFunction(table, filter);
    else
      return this.sql.getField(table, filter.property['@id']);
  }

  private getFunction(table: Table, filter: Filter): string {
    const fn = filter.function.id['@id'];

    if (fn === "http://endhealth.info/im#AgeFunction") {
      return "TIMESTAMPDIFF("
        + this.getArgument(filter.function.argument, "units") + ", "
        + this.sql.getField(table, filter.property['@id']) + ", "
        + this.getArgument(filter.function.argument, "referenceDate") + ")";
    } else if (fn === "http://endhealth.info/im#TimeDifference") {
      return "TIMESTAMPDIFF("
        + this.getArgument(filter.function.argument, "units") + ", "
        + this.getArgument(filter.function.argument, "firstDate") + ", "
        + this.getArgument(filter.function.argument, "secondDate") + ")";
    } else {
      throw "Unknown function [" + fn + "]";
    }
  }

  private getArgument(args: Argument[], name: string): any {
    for(const a of args) {
      if (a.parameter === name)
        return a.value;
    }

    throw "Unknown argument [" + name + "]";
  }

  private getComparison(c: string) {
    switch (c) {
      case "EQUAL": return "=";
      case "GREATER_THAN": return ">";
      case "GREATER_THAN_OR_EQUAL": return ">=";
      case "LESS_THAN": return "<";
      case "LESS_THAN_OR_EQUAL": return "<=";
      case "NOT_EQUAL": return "<>";
      case "MEMBER_OF": throw "Cannot compare \"Member of\"";
      default: throw "Unknown comparator [" + c + "]";
    }
  }
}
