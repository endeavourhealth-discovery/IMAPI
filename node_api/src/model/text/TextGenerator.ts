import { IM, RDF, RDFS } from "../../vocabulary"

import { Comparison } from "../sets/Comparison"

import { Helpers } from "./Helpers"
const { a } = Helpers;

import { Clause } from "./Clause"


import _ from "lodash";

export class TextGenerator {




    public static summarise(clause: any): string {


        // const isPath = jsonPath => _.get(clauseDefinition, jsonPath) != undefined;
        // const path = jsonPath => _.get(clauseDefinition, jsonPath);

        clause = new Clause(clause);

        let property = clause.path('property.rdfs:label').value;
        let comparison = Comparison[clause.path('valueCompare.comparison').value];
        let comparator = clause.path('valueCompare.valueData').value;
        let valueIn = clause.path('valueIn[0].rdfs:label').value;
        let entity = clause.path('valueObject.entityType.rdfs:label').value;
        let test = clause.path('valueObject.sortLimit.test').value;
        let testValueIn = clause.path('valueObject.sortLimit.test[0].valueIn[0].name').value;
        // let testValueIn = clause.path('valueObject.sortLimit.test.valueIn[0].rdfs:label').value;



        let sentence: any[] = [];


        if (test) {
            sentence = ["had", a(testValueIn), testValueIn];

        } else if (clause.path("valueObject.entityType").exists) {
            sentence = ["had", a(entity), entity, "with a", property, "that is", valueIn,];

        } else if (clause.path("valueIn").exists) {
            sentence = ["had", a(property), property, "that is", valueIn];

        } else if (clause.path("valueCompare").exists) {
            sentence = ["had", a(property), property, "that is", comparator, comparison];

        } else if (clause.path("property").exists) {
            sentence = ["had", a(property), property, "that exists"];

        }

        return sentence.join(" "); 
    }

}