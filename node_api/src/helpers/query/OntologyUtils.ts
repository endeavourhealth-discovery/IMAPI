import {Namespaces, Prefixes} from "../../model/constants"


export default class OntologyUtils {


    public static toPrefix(iri: string): string {
        const namespace = iri.split("#")[0] + "#";
        const predicate = iri.split("#")[1];
        const prefix = (<any>Prefixes)[namespace];
        
        return `${prefix}:${predicate}`;
    }


}
