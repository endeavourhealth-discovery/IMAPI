import TTEntity from '../model/tripletree/TTEntity';
import { OntologyUtils } from '../helpers'
import { Keyspaces } from 'aws-sdk';

export default class ManipulationUtils {

    /**
    * A callback function for Array.filter() that filters out duplicate items in an array e.g. use as Array.filter(onlyUnique);
    * @name onlyUnique
    **/
    public static onlyUnique(element: any, index: any, array: any): boolean {
        return array.indexOf(element) === index;
    }

    public static isTTIriRef(element: any): boolean {
        const keys = Object.keys(element);
        const excludedKeys = ["and", "or", "property"]; 
        const isExcludedKeysPresent = !keys.some(key => excludedKeys.includes(key));
        return keys.includes("@id") && isExcludedKeysPresent;
    }


    public static excludedPaths(clause: any): boolean {
        const excludedPathProperties = ["test", "valueObject"]
        return !excludedPathProperties.some(path => clause.path.includes(path));
    }

    public static entitiesFromPredicates(queryResult): any {
        let entity;
        const visitedIris = new Set();
        const entities: any[] = [];

        // populates response with queryResults
        queryResult.forEach((item, index) => {
            const iri = item.iri.value;

            if (!visitedIris.has(iri)) {
                visitedIris.size > 0 ? entities.push(entity) : null;
                visitedIris.add(iri);
                entity = new TTEntity(iri);
            }

            const predicate = item.predicate.value || item?.predicate;
            const object = item?.object?.value || item?.object;
            const predicateLabel = item?.predicateLabel;
            const objectLabel = item?.objectLabel;
            const prefixedPredicate = OntologyUtils.toPrefix(predicate)

            if (Array.isArray(entity[prefixedPredicate])) {
                entity[prefixedPredicate].push(
                    {
                        "@id": object,
                        "name": objectLabel
                    }
                );
            } else {
                entity[prefixedPredicate] = object;
            }


            if (index == queryResult.length - 1) {
                entities.push(entity)
            }
        })

        return entities;


    }

}