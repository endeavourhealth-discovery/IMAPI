import { IM, RDF, RDFS } from "../../vocabulary"

import { Comparison } from "../sets/Comparison"

import { Helpers } from "./Helpers"
const { a, isSingular } = Helpers;

import { Clause } from "./Clause"

import * as pathMap from "./Config/PathMap.json"
import * as wordMap from "./Config/WordMap.json"

import _ from "lodash";



export class TextGenerator {



    private static showConsole = true;

  

    //goes through a hierarchical casdcade of sentences joined with a comma, e.g. entity, property, and/or, sortLimit, test 
    public static summarise(clause: any, currentPath?: string): string {


        clause = new Clause(clause)
        let { and, or, are, have, property, propertyId, valueCompare, valueFunction, valueFunctionId, comparison, valueData, valueIn0, valueNotIn0, valueConcept0, entity, test, orderById, sortLimit, count, direction, orderBy, testValueIn, sortDirection, testValueIn0, units, firstDate, secondDate } = clause;  //these are function functions that are mapped to the properties in the pathMap.json file and return a transformed string

        let sentence: any[] = [];

        //short specialised sentences
        if (test && orderById == IM.EFFECTIVE_DATE && direction == "descending" && !and && !or && TextGenerator.isEntityPersistent(clause.definition)) {
            return [have, 'unresolved', testValueIn0?.name].join(" ");
        }
        //everything else
        else if (entity) {
            sentence = [have, a(entity), entity.name]; //default sentence
            if (and && (and?.length > 0 || or?.length > 0)) {

                sentence.push("with:");
                let andOr = and?.length > 0 ? "and" : "or";
                let children = and?.length > 0 ? and : or;
                children.forEach((childClause: any, childIndex: number) => sentence = [...sentence, "--", ...TextGenerator.summariseProperty(childClause, "valueObject.and", childIndex == children.length - 1)])
            } else {
                sentence.push("with");
                sentence = [...sentence, ...TextGenerator.summariseProperty(clause.definition.valueObject, "valueObject"), ""]
            }

            // e.g. "and after sorting by ascending/descending  Property, the first # entry/ies"
            if (sortLimit) {
                let entry = wordMap['entry'][isSingular(count).toString()] || wordMap['entry']['default']; //### todo remove outer ternarny once count is added
                sentence = [...sentence, "\n", "and after sorting by", direction, orderBy.name, "the first", count, entry + ":"]

                if (test && test?.length > 0 && (test[0]?.and?.length > 0 || test[0]?.or?.length > 0)) {
                    let andOr = test[0]?.and?.length > 0 ? "and" : "or";
                    test[0][andOr].forEach((childClause: any, childIndex: number) => sentence = [...sentence, "--", ...TextGenerator.summariseProperty(childClause, `test[0].${andOr}`, childIndex == test[0][andOr].length - 1)])
                }
                else if (test && test?.length > 0) {
                    test.forEach((childClause: any) => sentence = [...sentence, TextGenerator.summarise(childClause, "test")])
                }
            }
        }
        else if (property) {
            sentence = TextGenerator.summariseProperty(clause.definition, currentPath)
        }
        sentence = sentence.filter(item => item) //removes null
        return sentence.join(" ");
    }


    public static summariseProperty(clause: any, currentPath?: string, lastItem?: boolean): any[] {

        clause = new Clause(clause)
        let { and, or, are, have, property, propertyId, valueCompare, valueFunction, valueFunctionId, comparison, valueData, valueIn0, valueNotIn0, valueConcept0, entity, test, orderById, sortLimit, count, sortDirection, testValueIn0, units, firstDate, secondDate } = clause;  //these are function functions that are mapped to the properties in the pathMap.json file and return a transformed string
        let sentence: any[] = [];


        have = currentPath && (currentPath.startsWith("valueObject") || currentPath.startsWith("test")) ? null : have;

        let commaOrSemiColon = lastItem == false ? "," : ";";

        if (propertyId == IM.IN_RESULT_SET && valueIn0?.name) {
            sentence = [are, property.name, valueIn0.name];
        }
        else if (valueIn0 || valueNotIn0) {
            let is = valueIn0 ? "is" : "is not";
            sentence = [have, a(property), property.name, "that", is, "in the value-set", valueIn0?.name + commaOrSemiColon];

        } else if (valueConcept0) {
            sentence = [have, a(property), property.name, "that is", valueConcept0?.name + commaOrSemiColon];
        } else if (valueCompare && valueFunctionId == IM.TIME_DIFFERENCE) {
            let beforeAfter = (secondDate.value == "the Reference Date") ? "before" : "after";
            units.value = isSingular(valueData) ? wordMap[units.value]['singular'] : wordMap[units.value]['plural']
            let compare = comparison;
            let value = valueData
            if (value.substring(0, 1) == "-") value = value.substring(1, value.length)
            if (Helpers.isNegative(valueData) && comparison == "more than or equal to") compare = "less than or equal to";
            if (Helpers.isNegative(valueData) && comparison == "less than or equal to") compare = "more than or equal to";

            sentence = [have, a(property), property.name, "that is", compare, value, units.value, beforeAfter, secondDate.value + commaOrSemiColon]

        } else if (valueCompare) {
            sentence = [have, a(property), property.name, "that is", comparison, valueData + commaOrSemiColon]
        }
        else if (property?.name) {
            sentence = [have, a(property), property.name + commaOrSemiColon];
        }
        return sentence;
    }


    private static isEntityPersistent(clause: any): boolean {
        clause = new Clause(clause)
        let { objectValueIn, testValueIn0 } = clause;
        let isPersistent = objectValueIn && testValueIn0 && objectValueIn.some(ref => ref['@id'] == testValueIn0['@id']);
        return isPersistent;
    }


}


