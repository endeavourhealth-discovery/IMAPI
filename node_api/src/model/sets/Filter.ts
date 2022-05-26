import {SortLimit} from './SortLimit';
import {TTIriRef} from '../tripletree/TTIriRef';
import {ConceptRef} from './ConceptRef';
import {Compare} from './Compare';
import {Within} from './Within';
import {Heading} from './Heading';
import {Function} from './Function';

export class Filter extends Heading {
  public and: Filter[];
  public or: Filter[];
  public optional: Filter[];
  public sortLimit: SortLimit;
  public graph: TTIriRef;
  public entityType: ConceptRef;
  public entityId: TTIriRef;
  public entityIn: TTIriRef;
  public subsetOf: TTIriRef[];

  public property: ConceptRef;
  public valueCompare: Compare;
  public valueIn: TTIriRef[];
  public valueNotIn: TTIriRef[];
  public valueConcept: ConceptRef[];
  public valueNotConcept: ConceptRef[];
  public valueSuperTypeOf: TTIriRef[];
  public valueRange: Range;
  public entityVar: string;
  public propertyVar: string;
  public valueVar: string;
  public valueObject: Filter;
  public function: Function;
  public valueWithin: Within;
  public inverseOf: boolean;
  public notExist: boolean;
  public isIndex: boolean;
  public includeSubEntities: boolean;
}
