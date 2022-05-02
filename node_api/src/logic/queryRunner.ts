import {MysqlService} from '../services/mysql.service';
import {GraphdbService, iri} from '../services/graphdb.service';
import {dataModelMap} from './dataModelMap';
import {DataSet} from '../model/sets/DataSet';
import {Match} from '../model/sets/Match';
import {Sql} from '../model/sql/Sql';
import {Table} from '../model/sql/Table';
import {Join} from '../model/sql/Join';
import {TTIriRef} from '../model/tripletree/TTIriRef';
import {Condition} from '../model/sql/Condition';

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

      console.log("===== DEFINITION =================================================")
      console.log(JSON.stringify(definition.match, null, 2));
      console.log("==================================================================")

      this.generateSql(definition.match);
      console.log("===== SQL ========================================================")
      console.log(JSON.stringify(this.sql, null, 2));
      console.log("==================================================================")

      console.log(this.sqlToString());


      return await this.mysql.test();
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

    this.sql.from = this.getTable(match.entityType['@id']);

    this.processMatch(match);
  }

  private processMatch(match: Match) {
    const fk = match.property['@id'];     // TODO: Seems wrong place for FK?
    if (match.valueObject) {
      this.processValueObject(this.sql.from, match.valueObject, fk);
    } else
      throw "Unknown/missing match type";
  }

  private processValueObject(parent: Table, valObj: Match, fk: string) {
    let join: Join = new Join();
    join.table = this.getTable(valObj.entityType['@id']);
    join.conditions.push({
      subject: this.getField(join.table, fk),
      predicate: "=",
      object: parent.alias + "." + parent.pk,
    } as Condition);

    this.sql.joins.push(join);

    if (valObj.and) {
      this.processConditions(join, valObj.and, 'AND');
    }
/*
    if (valObj.or) {
      this.processConditions(sql, join, valObj.or, 'OR');
    }*/
  }

  private processConditions(join: Join, tests: Match[], operator: string) {
    if (tests.length) {
      tests.forEach(test => this.processCondition(join, test, operator));
    }
  }

  private processCondition(join: Join, test: Match, operator: string) {
    let condition: Condition = new Condition();
    join.conditions.push(condition);
    join.operator = operator;
    condition.subject = this.getField(join.table, test.property['@id']);

    this.getTest(join, condition, test);
  }

  private getTest(join: Join, condition: Condition, test: Match) {
    if (test.valueIn) {
      condition.predicate = "IN";
      condition.object = "(";
      for (let i=0; i < test.valueIn.length; i++) {
        condition.object += (i>0 ? ", " : "") + test.valueIn[i]['@id'];
      }
      condition.object += ")";
    } else if (test.valueCompare) {
      condition.predicate = JSON.stringify(test.valueCompare.comparison);
      condition.object = test.valueCompare.valueData;
    } else if (test.notExist) {
      condition.predicate = "IS";
      condition.object = "NULL";
    } else {
      console.error("Unknown test!");
    }
  }

  private getTable(entityTypeId: string): Table {
    if (!entityTypeId)
      throw "No entity type provided";

    if (!dataModelMap[entityTypeId])
      throw "Entity [" + entityTypeId + "] does not exist in map";

    const table = JSON.parse(JSON.stringify(dataModelMap[entityTypeId]));
    table.alias = 't' + this.sql.joins.length;

    return table;
  }

  private getField(table: Table, fieldId: string): string {
    if (!table.fields[fieldId])
      throw "Table [" + table.name + "] does not contain field [" + fieldId + "]";

    return table.alias + "." + table.fields[fieldId];
  }

  private sqlToString(): string {
    let result = "SELECT m." + this.sql.from.pk;

    if (this.sql.select.length > 0) {
      this.sql.select.forEach(f => result += ", " + f);
    }

    result += "\nFROM " + this.sql.from.name + " m";
    if (this.sql.joins && this.sql.joins.length > 0) {
      this.sql.joins.forEach((j: Join) => {
        console.log(JSON.stringify(j.table))
        result += "\nJOIN " + j.table.name + " " + j.table.alias + " ";
        for (let i=0; i<j.conditions.length; i++) {
          let c : Condition = j.conditions[i];
          result += "\n\t" + (i==0 ? "ON" : j.operator) + " " + c.subject + " " + c.predicate + " " + c.object;
        }
      });
    }

    if (this.sql.where && this.sql.where.conditions.length > 0) {
      for (let i = 0; i < this.sql.where.conditions.length; i++) {
        const c: Condition = this.sql.where.conditions[i];

        result += "\n" + (i === 0 ? "WHERE" : this.sql.where.operator) + " " + c.subject + " " + c.predicate + " " + c.object;
      }
    }

    return result;
  }
}
