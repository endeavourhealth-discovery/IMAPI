import { IM, RDF, RDFS } from "../vocabulary"
import { Comparison } from "../model/sets/Comparison";


import _ from "lodash";

export default class QueryUtils {


    private static a = (testString: string) => ["a", "e", "i", "o", "u"].some((letter: string) => letter.toLowerCase() == testString.substring(0, 1).toLowerCase()) ? "an" : "a";

    public static summariseClause(clauseDefinition: any): string {


        const isPath = jsonPath => _.get(clauseDefinition, jsonPath) != undefined;
        const path = jsonPath => _.get(clauseDefinition, jsonPath);

        const a = QueryUtils.a;
        const property = () => path('property.rdfs:label');
        const comparison = () => path('valueCompare.comparison');
        const comparator = () => path('valueCompare.valueData');
        const valueIn = () => path('valueIn[0].rdfs:label');


        let sentence: any[] = [];


        if (isPath("valueObject.sortLimit.test")) {

        } else if (isPath("valueObject.entityType")) {
        } else if (isPath("valueIn")) {
            sentence = [a(property()), property(), "that is", valueIn()];
        } else if (isPath("valueCompare")) {
            sentence = [a(property()), property(), "that is", comparator(), Comparison[comparison()]];
        } else if (isPath("property")) {
            sentence = [a(property()), property(), "that exists"];
        }

        return sentence.join(" ");

    }

}