import {DataSet} from './DataSet';
import {TTIriRef} from '../tripletree/TTIriRef';
import {ConceptRef} from './ConceptRef';
import {PropertyObject} from './PropertyObject';
import {Filter} from './Filter';

export class Select {
  public name: string;
  public sum: boolean;
  public average: boolean;
  public max: boolean;
  public count: boolean;
  public property: PropertyObject[];
  public filter: Filter;
  public distinct: boolean;
  public entityType: ConceptRef;
  public entityId: ConceptRef;
  public entityIn: TTIriRef;
  public groupBy: string[];
}
