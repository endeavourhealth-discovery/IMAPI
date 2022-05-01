import {Table} from './Table';
import {ConditionList} from './ConditionList';
import {SimpleCondition} from './SimpleCondition';
import {getField} from '../../logic/dataModelMap';

export class Join extends ConditionList {
  public table: Table;
  public on: string;
  public joins: Join[] = [];
}
