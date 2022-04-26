import {Condition} from './Condition';

export class ConditionList {
  public operator: string;
  public conditions: Condition[] = [];
  public subConditions: ConditionList;
}
