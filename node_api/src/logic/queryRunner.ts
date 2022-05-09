import {MysqlService} from "../services/mysql.service";
import {GraphdbService, iri} from "../services/graphdb.service";
import {getField, getJoin, getTable} from "./dataModelMap";
import {DataSet} from "../model/sets/DataSet";
import {Match} from "../model/sets/Match";
import {Sql} from "../model/sql/Sql";
import {Table} from "../model/sql/Table";
import {Join} from "../model/sql/Join";
import {SimpleCondition} from "../model/sql/SimpleCondition";
import {ConditionList} from "../model/sql/ConditionList";
import {TTIri} from '../model/tripletree/TTIri';
import {Condition} from '../model/sql/Condition';
import {Argument} from '../model/sets/Argument';

export default class QueryRunner {
  private mysql: MysqlService;
  private graph: GraphdbService;
  private sql: Sql;

  constructor() {
    this.mysql = new MysqlService();
    this.graph = new GraphdbService();
  }

  public async runQuery(queryIri: string): Promise<any> {
    try {
      const definition: DataSet = await this.getDefinition(queryIri);

/*      console.log("===== DEFINITION =================================================")
      console.log(JSON.stringify(definition.match, null, 2));
      console.log("==================================================================")*/

      this.generateSql(definition.match);
/*      console.log("===== SQL ========================================================")
      console.log(JSON.stringify(this.sql, null, 2));
      console.log("==================================================================")*/

      console.log(this.sql.toString());


      // return await this.mysql.test();
    } catch (e) {
      console.error("***** ERROR!!");
      console.log(e);
      return {};
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

    return JSON.parse(rs[0].def);
  }




  private generateSql(match: Match): void {
    this.sql = new Sql();
    this.sql.table = getTable(match.entityType["@id"], "m");

    this.processMatch(this.sql.table, this.sql.conditions, match);
  }

  private processMatch(table: Table, conditions: Condition[], match: Match) {
    if (match.valueObject) {
      this.processValueObject(table, match.property["@id"], match.valueObject);
    } else if (match.subsetOf) {
      for (const s of match.subsetOf)
        this.processSubsetOf(table, s);
    } else if (match.valueIn) {
      this.processValueIn(table, conditions, match);
    } else if (match.valueCompare) {
      this.processValueCompare(table, conditions, match);
    } else if (match.notExist) {
      this.processNotExist(table, conditions, match);
    } else if (match.valueVar || match.and || match.or) {
      // Globally handled (below)
    } else {
      console.error("Unknown/no match type\n" + JSON.stringify(match, null, 2));
    }

    if (match.valueVar)
      this.sql.fields.push(this.getSubject(table, match) + " AS " + match.valueVar);

    if (match.and)
      this.getMatches(table, conditions, match.and, "AND");

    if (match.or)
      this.getMatches(table, conditions, match.or, "OR");
  }

  private processValueIn(table: Table, conditions: Condition[], match: Match) {
    const c = new SimpleCondition();
    conditions.push(c);
    c.subject = getField(table, match.property['@id'])

    // TODO: Need to translate from IRIs to (v1) DBIDs
    c.predicate = 'IN'
    c.object = '(';
    match.valueIn.forEach(v => {
      c.object += '\n"' + v['@id'] + '"';
    });
    c.object += ')';
  }

  private processValueObject(parentTable: Table, propertyId: string, match: Match) {
    const join: Join = getJoin(parentTable, propertyId, match.entityType["@id"], "t" + this.sql.joins.length);
    this.sql.joins.push(join);

    this.processMatch(join.table, join.conditions, match);
  }

  private processSubsetOf(parentTable: Table, queryIri: TTIri) {
    const join: Join = new Join();
    join.table = getTable(queryIri['@id'], "t" + this.sql.joins.length);
    join.on = getField(parentTable, "pk") + " = " + getField(join.table, "pk");

    this.sql.joins.push(join);
  }

  private processValueCompare(table: Table, conditions: Condition[], match: Match) {
    const c = new SimpleCondition();
    conditions.push(c);
    c.subject = this.getSubject(table, match);
    c.predicate = this.getComparison(match.valueCompare.comparison);
    c.object = match.valueCompare.valueData;
  }

  private processNotExist(table: Table, conditions: Condition[], match: Match) {
    const c = new SimpleCondition();
    conditions.push(c);
    c.subject = this.getSubject(table, match);
    c.predicate = 'IS'
    c.object = 'NULL';
  }

  private getMatches(table: Table, conditions: Condition[], matches:Match[], operator: string) {
    const result: ConditionList = new ConditionList();
    conditions.push(result);
    result.operator = operator;

    for(const match of matches) {
      this.processMatch(table, result.conditions, match);
    }

    return result;
  }


  private getSubject(table: Table, match: Match): string {
    if (match.function)
      return this.getFunction(table, match);
    else
      return getField(table, match.property['@id']);
  }

  private getFunction(table: Table, match: Match): string {
    const fn = match.function.id['@id'];

    if (fn === "http://endhealth.info/im#AgeFunction") {
      return "TIMESTAMPDIFF("
        + this.getArgument(match.function.argument, "units") + ", "
        + getField(table, match.property['@id']) + ", "
        + this.getArgument(match.function.argument, "referenceDate") + ")";
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
