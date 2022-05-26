import {TTIriRef} from '../tripletree/TTIriRef';
import {Select} from './Select';

export class PropertyObject extends TTIriRef {
  public binding: string;
  public alias: string;
  public object: Select;
  public inverseOf: boolean;
  public function: Function;
}
