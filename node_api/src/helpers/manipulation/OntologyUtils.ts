import {Namespaces, Prefixes} from '../../model/constants';


export default class OntologyUtils {



    public static toPrefixedPredicate(iri: string): string {
        const namespace = iri.split("#")[0] + "#";
        const predicate = iri.split("#")[1];
        const prefix = Prefixes[namespace];
        
        return `${prefix}:${predicate}`;
    }


}