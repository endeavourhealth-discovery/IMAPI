import { IM, RDF, RDFS } from "../../vocabulary"

import { Helpers } from "./Helpers"
const { a, isSingular, html, isDateAliasCompared, href } = Helpers;

import { Clause } from "./Clause"

import * as pathMap from "./Config/PathMap.json"
import * as wordMap from "./Config/WordMap.json"

import _ from "lodash";


export class TextGenerator {



    private static showConsole = false;



    //goes through a hierarchical casdcade to match query object model against potential templates (sentence)  
    public static summarise(clause: any, currentPath?: string): any {
        TextGenerator.showConsole && console.log("### summarise() clause: ", clause, " at path: ", currentPath)


        clause = new Clause(clause)
        // let { and, or, are, has, property, propertyId, value, valueFunction, valueFunctionId, comparison, valueData, entity, test, orderById, orderLimit, count, direction, orderBy, testSetIn, sortDirection, testSetIn0, units, firstDate, secondDate } = clause;  //these are function functions that are mapped to the properties in the pathMap.json file and return a transformed string
        let {
            pathTo,
            and,
            or,
            and0PropertyId,
            and0PropertyAlias,
            and0PropertyName,
            and0PropertyInSet0,
            and0OrderById,
            and0Direction,
            and0Count,
            and1NotExist,
            and1PropertyId,
            and1PropertyAlias,
            and1PropertyName,
            and1PropertyInSet0,
            and1OrderById,
            alias,
            isConcept,
            value,
            notExist,
            property,
            id,
            inSet0,
            comparison
        } = clause;

        TextGenerator.showConsole && console.log("Clause with getters: ", clause)

        let text: any[] = [];
        // let html: string; //`<p>{{innerHTML}}</p>`


        //templates (can be abstracted away / stored elsewhere as clause.findMatchingTemplates() and it finds you a list of matching templates for each clause.
        // const entryFollowedByEntry = isTemplateMatch("im:QT_EntryFollowedByEntry", and && Array.isArray(and) && and?.length > 1 && isDateAliasCompared(and))
        const entryFollowedByEntry = and && Array.isArray(and) && and?.length > 1 && isDateAliasCompared(and)


        //cascade
        if (pathTo) { //linked datamodel entities
            if (entryFollowedByEntry) {
                TextGenerator.showConsole && console.log("### Template used: entryFollowedByEntry", entryFollowedByEntry)
                text = TextGenerator.summariseClause(clause.definition, "entryFollowedByEntry");
            } else {
                TextGenerator.showConsole && console.log("### Template used: any clause", clause.definition)

                text = TextGenerator.summariseClause(clause.definition);
            }
        }
        else if (alias) { //direct properties

            TextGenerator.showConsole && console.log("### Template used: alias ")
            text = TextGenerator.template(clause.definition);

        }



        //results
        TextGenerator.showConsole && console.log("###Text sentence array is ", text)
        let result = {
            text: text.filter(t => t && t != "").join(" "),
            // html: html,
        }
        return result;
    }


    //creates a sentence by matching translating properties in a specific order
    public static summariseClause(clause: any, templateName?: string): any {
        TextGenerator.showConsole && console.log("summariseClause: ", clause)

        let text: any[] = [];


        clause = new Clause(clause);
        const { pathTo, and, or, notExist, orderLimit, orderById, direction, count, isConcept, property, id, propertyName, inSet, notInSet, inSet0, value, comparison, valueData, functionArg1 } = clause;


        if (templateName == "entryFollowedByEntry") {

            text = [
                ...TextGenerator.template(and[0], "orderLimit"),
                ...TextGenerator.template(and[0].property[0], "of_inSet"),
                ...TextGenerator.template(and[1].property[0], "followedBy", and[1]?.notExist),
                ...TextGenerator.template(and[1], "orderLimit"),
                ...TextGenerator.template(and[1].property[0], "of_inSet"),
            ]

        } else {
            // summarise any clause in a specific order (oderLimit, then properties: concept, then value, then date, then other properties)
            // text = TextGenerator.template(and[0], "orderLimit");

            //todo: improve concept matching / create external function
            let conceptKeys = ["isConcept", "inSet", "notInSet"];
            let conceptKey = Object.keys(clause?.definition?.property[0]).find((key: string) => conceptKeys.includes(key));
            let conceptTemplate = conceptKey ? "of" + conceptKey.charAt(0).toUpperCase() + conceptKey.slice(1) : "";

            text = [
                ...TextGenerator.template(clause.definition, "orderLimit"),
                ...TextGenerator.template(clause.definition.property[0], conceptTemplate)
                // ...TextGenerator.template(and[1].property[0], "followedBy", and[1]?.notExist),
                // ...TextGenerator.template(and[1], "orderLimit"),
                // ...TextGenerator.template(and[1].property[0], "ofInSet"),
            ]

        }
        return text;

    }


    public static template(clause: any, templateName?: string, notExist?: boolean, isPlural?: boolean): any {
        TextGenerator.showConsole && console.log("###summariseProperty() - template", templateName, " - Clause ", clause)


        let text: any[] = [];


        const propertyClause = new Clause(clause) as any;
        const { orderLimit, orderById, direction, count, isConcept, property, id, propertyName, inSet, notInSet, inSet0, value, comparison, valueData, functionArg1 } = propertyClause;


        //properted of linked entities
        if (templateName == "of_inSet" || templateName == "inSet") {
            TextGenerator.showConsole && console.log("### TemplateName inSet")

            let isOf = templateName == "of_inSet" ? "of" : "";
            let names = inSet?.length > 1 ? `${inSet[0].name} or ${inSet[1].name}` : inSet[0].name;
            text = [isOf, names];

        } else if (templateName == "of_IsConcept" || templateName == "isConcept") {
            TextGenerator.showConsole && console.log("### TemplateName isConcept")

            let isOf = templateName == "of_IsConcept" ? "of" : "";
            let names = isConcept?.length > 1 ? `${isConcept[0].name} or ${isConcept[1].name}` : isConcept[0].name;
            text = [isOf, names];

        }
        else if (templateName == "followedBy") {
            TextGenerator.showConsole && console.log("### TemplateName followedBy")
            let is = notExist || notInSet ? "is not" : "is";
            text = [is, "followed by"];

            //direct properties of main entity
        } else if (templateName == "orderLimit") {

            //any/latest/earliest/highest/lowest
            let anyLatestHighest;
            if (orderById == IM.EFFECTIVE_DATE && direction == "descending") {
                anyLatestHighest = "latest";
            } else if (orderById == IM.EFFECTIVE_DATE && direction == "ascending") {
                anyLatestHighest = "earliest";
            } else {
                anyLatestHighest = "any";
            }

            //latest [quantity] entry/entries. If quantity = 1 it is silenced 
            let quantity = count > 1 ? count : "";
            let entry = count > 1 ? "entries" : "entry";

            text = [anyLatestHighest, quantity, entry];


        } else if (isConcept) {
            TextGenerator.showConsole && console.log("### Template isConcept")

            let is = notExist ? "is not" : "is";
            let andSubtypes = isConcept[0].includeSubtypes ? "(or its subtypes)" : "";
            text = [propertyName, is, isConcept[0].name, andSubtypes];


        } else if (inSet || notInSet) {
            TextGenerator.showConsole && console.log("### Template inSet")

            let is = notExist || notInSet ? "is not" : "is";
            text = [propertyName, is, inSet[0].name]


        } else if (value) {
            TextGenerator.showConsole && console.log("### Template value")

            let is = notExist ? "is not" : "is";
            let units = isSingular(valueData) ? wordMap[functionArg1]['singular'] : wordMap[functionArg1]['plural']
            text = [propertyName, is, comparison, valueData, units]
        }

        return text

    }



    // }




    // if (Array.isArray(clause) && clause.length > 0) {
    //     clause.forEach((property: any) => {




    //     })
    // }


}