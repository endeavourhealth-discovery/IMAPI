import {Table} from './Table';
import {Join} from './Join';
import {ConditionList} from './ConditionList';

export class Sql {
  public select: string[] = [];
  public from: Table;
  public joins: Join[] = [];
  public where: ConditionList = new ConditionList();
}
