import {ConditionList} from './ConditionList';

export class Condition {
  public subject: string;
  public predicate: string;
  public object: string;
  public subConditions: ConditionList;
}
