import {Table} from './Table';
import {Join} from './Join';
import {ConditionList} from './ConditionList';
import {SimpleCondition} from './SimpleCondition';


export class Sql extends Join {
  public fields: string[] = [];
  public table: Table;

  public toString(): string {
    let result = "SELECT m." + this.table.fields.pk;

    for(let f=0; f<this.fields.length; f++) {
      result += ", " + this.fields[f];
    }

    result += "\nFROM " + this.table.name + " " + this.table.alias;

    for(let j=0; j<this.joins.length; j++) {
      const join:Join = this.joins[j];
      result += "\nJOIN " + join.table.name + " " + join.table.alias + " ON " + join.on;

      result += this.getConditions(join, "AND ");
    }

    result += this.getConditions(this, "WHERE ");

    return result;
  }

  private getConditions(conditionList: ConditionList, initial: string = ''): string {
    if (!conditionList || conditionList.conditions.length == 0)
      return '';

    let result: string = "\n" + initial;

    if (conditionList && conditionList.conditions.length > 0) {
      for(let c=0; c<conditionList.conditions.length; c++) {
        if (c>0)
          result += "\n" + conditionList.operator + " ";

        if ((conditionList.conditions[c] as ConditionList).operator) {
          const cl: ConditionList = conditionList.conditions[c] as ConditionList;
          result += "(" + this.getConditions(cl) + ")";
        } else {
          const cond: SimpleCondition = conditionList.conditions[c] as SimpleCondition;
          result += cond.subject + " " + cond.predicate + " " + cond.object;
        }
      }
    }

    return result;
  }
}
