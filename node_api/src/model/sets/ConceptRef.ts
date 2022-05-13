import {TTIriRef} from '../tripletree/TTIriRef';

export class ConceptRef extends TTIriRef {
  public includeSubtypes: boolean;
  public includeSupertypes: boolean;
}
