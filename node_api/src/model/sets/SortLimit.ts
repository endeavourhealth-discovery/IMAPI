import {TTIriRef} from '../tripletree/TTIriRef';
import {Order} from './Order';
import {Filter} from './Filter';

export class SortLimit {
  public orderBy: TTIriRef;
  public count: number;
  public direction: Order;
  public must: Filter[];
}
