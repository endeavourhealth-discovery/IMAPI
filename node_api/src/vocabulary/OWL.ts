export class OWL {
  public static NAMESPACE = "http://www.w3.org/2002/07/owl#";
  public static PREFIX = "owl";
  public static CLASS = OWL.NAMESPACE + "Class";
  public static OBJECT_PROPERTY = OWL.NAMESPACE + "ObjectProperty";
  public static SOME_VALUES_FROM = OWL.NAMESPACE + "someValuesFrom";
  public static ON_PROPERTY = OWL.NAMESPACE + "onProperty";
  public static HAS_VALUE = OWL.NAMESPACE + "hasValue";


  
  public static PREFIXED = new class {

    constructor(public superThis: any) {
      Object.keys(superThis).forEach(key => this[key] =  superThis[key].replace(superThis.NAMESPACE, superThis.PREFIX + ":"));
      return this;
    }

  }(this);
}
