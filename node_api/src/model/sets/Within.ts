import {Compare} from './Compare';
import {Range} from './Range';
import {Function} from './Function';
import {Filter} from './Filter';

export class Within {
  public range: Range;
  public compare: Compare;
  public of: string;
  public function: Function;
  public targetFilter: Filter;
}
