export class SHACL {
  public static NAMESPACE = "http://www.w3.org/ns/shacl#";
  public static PREFIX = "shacl";
  public static PROPERTY = SHACL.NAMESPACE + "property";
  public static PATH = SHACL.NAMESPACE + "path";
  public static FUNCTION = SHACL.NAMESPACE + "Function";
  public static CLASS = SHACL.NAMESPACE + "class";
  public static DATATYPE = SHACL.NAMESPACE + "datatype";
  public static MINCOUNT = SHACL.NAMESPACE + "minCount";
  public static MAXCOUNT = SHACL.NAMESPACE + "maxCount";
  public static NODESHAPE = SHACL.NAMESPACE + "NodeShape";

  public static PREFIXED = new class {

    constructor(public superThis: any) {
      Object.keys(superThis).forEach(key => this[key] =  superThis[key].replace(superThis.NAMESPACE, superThis.PREFIX + ":"));
      return this;
    }

  }(this);
}
