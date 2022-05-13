import {TTIriRef} from '../tripletree/TTIriRef';
import {Select} from './Select';
import {Heading} from './Heading';

export class DataSet extends Heading {
  public graph: TTIriRef;
  public select: Select;
  public subselect: Select[];
  public groupBy: Select[];
  public resultFormat: any;
  public usePrefixes: boolean;
  public referenceDate: string;
  public activeOnly: boolean;

}
