import {Compare} from './Compare';
import {Match} from './Match';
import {Range} from './Range';
import {Function} from './Function';

export class Within {
  public range: Range;
  public compare: Compare;
  public of: string;
  public function: Function;
  public targetMatch: Match;
}