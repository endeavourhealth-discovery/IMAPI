"use strict";
var __spreadArray = (this && this.__spreadArray) || function (to, from, pack) {
    if (pack || arguments.length === 2) for (var i = 0, l = from.length, ar; i < l; i++) {
        if (ar || !(i in from)) {
            if (!ar) ar = Array.prototype.slice.call(from, 0, i);
            ar[i] = from[i];
        }
    }
    return to.concat(ar || Array.prototype.slice.call(from));
};
exports.__esModule = true;
exports.TextGenerator = void 0;
var vocabulary_1 = require("../../vocabulary");
var Helpers_1 = require("./Helpers");
var a = Helpers_1.Helpers.a, isSingular = Helpers_1.Helpers.isSingular;
var Clause_1 = require("./Clause");
var wordMap = require("./Config/WordMap.json");
var TextGenerator = /** @class */ (function () {
    function TextGenerator() {
    }
    //goes through a hierarchical casdcade of sentences joined with a comma, e.g. entity, property, and/or, sortLimit, test 
    TextGenerator.summarise = function (clause, currentPath) {
        var _a, _b, _c, _d, _e, _f;
        clause = new Clause_1.Clause(clause);
        var and = clause.and, or = clause.or, are = clause.are, have = clause.have, property = clause.property, propertyId = clause.propertyId, valueCompare = clause.valueCompare, valueFunction = clause.valueFunction, valueFunctionId = clause.valueFunctionId, comparison = clause.comparison, valueData = clause.valueData, valueIn0 = clause.valueIn0, valueNotIn0 = clause.valueNotIn0, valueConcept0 = clause.valueConcept0, entity = clause.entity, test = clause.test, orderById = clause.orderById, sortLimit = clause.sortLimit, count = clause.count, direction = clause.direction, orderBy = clause.orderBy, testValueIn = clause.testValueIn, sortDirection = clause.sortDirection, testValueIn0 = clause.testValueIn0, units = clause.units, firstDate = clause.firstDate, secondDate = clause.secondDate; //these are function functions that are mapped to the properties in the pathMap.json file and return a transformed string
        var sentence = [];
        //short specialised sentences
        if (test && orderById == vocabulary_1.IM.EFFECTIVE_DATE && direction == "descending" && !and && !or && TextGenerator.isEntityPersistent(clause.definition)) {
            return [have, 'unresolved', testValueIn0 === null || testValueIn0 === void 0 ? void 0 : testValueIn0.name].join(" ");
        }
        //everything else
        else if (entity) {
            sentence = [have, a(entity), entity.name]; //default sentence
            if (and && ((and === null || and === void 0 ? void 0 : and.length) > 0 || (or === null || or === void 0 ? void 0 : or.length) > 0)) {
                sentence.push("with:");
                var andOr = (and === null || and === void 0 ? void 0 : and.length) > 0 ? "and" : "or";
                var children_1 = (and === null || and === void 0 ? void 0 : and.length) > 0 ? and : or;
                children_1.forEach(function (childClause, childIndex) { return sentence = __spreadArray(__spreadArray(__spreadArray([], sentence, true), ["--"], false), TextGenerator.summariseProperty(childClause, "valueObject.and", childIndex == children_1.length - 1), true); });
            }
            else {
                sentence.push("with");
                sentence = __spreadArray(__spreadArray(__spreadArray([], sentence, true), TextGenerator.summariseProperty(clause.definition.valueObject, "valueObject"), true), [""], false);
            }
            // e.g. "and after sorting by ascending/descending  Property, the first # entry/ies"
            if (sortLimit) {
                var entry = wordMap['entry'][isSingular(count).toString()] || wordMap['entry']['default']; //### todo remove outer ternarny once count is added
                sentence = __spreadArray(__spreadArray([], sentence, true), ["\n", "and after sorting by", direction, orderBy.name, "the first", count, entry + ":"], false);
                if (test && (test === null || test === void 0 ? void 0 : test.length) > 0 && (((_b = (_a = test[0]) === null || _a === void 0 ? void 0 : _a.and) === null || _b === void 0 ? void 0 : _b.length) > 0 || ((_d = (_c = test[0]) === null || _c === void 0 ? void 0 : _c.or) === null || _d === void 0 ? void 0 : _d.length) > 0)) {
                    var andOr_1 = ((_f = (_e = test[0]) === null || _e === void 0 ? void 0 : _e.and) === null || _f === void 0 ? void 0 : _f.length) > 0 ? "and" : "or";
                    test[0][andOr_1].forEach(function (childClause, childIndex) { return sentence = __spreadArray(__spreadArray(__spreadArray([], sentence, true), ["--"], false), TextGenerator.summariseProperty(childClause, "test[0]." + andOr_1, childIndex == test[0][andOr_1].length - 1), true); });
                }
                else if (test && (test === null || test === void 0 ? void 0 : test.length) > 0) {
                    test.forEach(function (childClause) { return sentence = __spreadArray(__spreadArray([], sentence, true), [TextGenerator.summarise(childClause, "test")], false); });
                }
            }
        }
        else if (property) {
            sentence = TextGenerator.summariseProperty(clause.definition, currentPath);
        }
        sentence = sentence.filter(function (item) { return item; }); //removes null
        return sentence.join(" ");
    };
    TextGenerator.summariseProperty = function (clause, currentPath, lastItem) {
        clause = new Clause_1.Clause(clause);
        var and = clause.and, or = clause.or, are = clause.are, have = clause.have, property = clause.property, propertyId = clause.propertyId, valueCompare = clause.valueCompare, valueFunction = clause.valueFunction, valueFunctionId = clause.valueFunctionId, comparison = clause.comparison, valueData = clause.valueData, valueIn0 = clause.valueIn0, valueNotIn0 = clause.valueNotIn0, valueConcept0 = clause.valueConcept0, entity = clause.entity, test = clause.test, orderById = clause.orderById, sortLimit = clause.sortLimit, count = clause.count, sortDirection = clause.sortDirection, testValueIn0 = clause.testValueIn0, units = clause.units, firstDate = clause.firstDate, secondDate = clause.secondDate; //these are function functions that are mapped to the properties in the pathMap.json file and return a transformed string
        var sentence = [];
        have = currentPath && (currentPath.startsWith("valueObject") || currentPath.startsWith("test")) ? null : have;
        var commaOrSemiColon = lastItem == false ? "," : ";";
        if (propertyId == vocabulary_1.IM.IN_RESULT_SET && (valueIn0 === null || valueIn0 === void 0 ? void 0 : valueIn0.name)) {
            sentence = [are, property.name, valueIn0.name];
        }
        else if (valueIn0 || valueNotIn0) {
            var is = valueIn0 ? "is" : "is not";
            sentence = [have, a(property), property.name, "that", is, "in the value-set", (valueIn0 === null || valueIn0 === void 0 ? void 0 : valueIn0.name) + commaOrSemiColon];
        }
        else if (valueConcept0) {
            sentence = [have, a(property), property.name, "that is", (valueConcept0 === null || valueConcept0 === void 0 ? void 0 : valueConcept0.name) + commaOrSemiColon];
        }
        else if (valueCompare && valueFunctionId == vocabulary_1.IM.TIME_DIFFERENCE) {
            var beforeAfter = (secondDate.value == "the Reference Date") ? "before" : "after";
            units.value = isSingular(valueData) ? wordMap[units.value]['singular'] : wordMap[units.value]['plural'];
            var compare = comparison;
            var value = valueData;
            if (value.substring(0, 1) == "-")
                value = value.substring(1, value.length);
            if (Helpers_1.Helpers.isNegative(valueData) && comparison == "more than or equal to")
                compare = "less than or equal to";
            if (Helpers_1.Helpers.isNegative(valueData) && comparison == "less than or equal to")
                compare = "more than or equal to";
            sentence = [have, a(property), property.name, "that is", compare, value, units.value, beforeAfter, secondDate.value + commaOrSemiColon];
        }
        else if (valueCompare) {
            sentence = [have, a(property), property.name, "that is", comparison, valueData + commaOrSemiColon];
        }
        else if (property === null || property === void 0 ? void 0 : property.name) {
            sentence = [have, a(property), property.name + commaOrSemiColon];
        }
        return sentence;
    };
    TextGenerator.isEntityPersistent = function (clause) {
        clause = new Clause_1.Clause(clause);
        var objectValueIn = clause.objectValueIn, testValueIn0 = clause.testValueIn0;
        var isPersistent = objectValueIn && testValueIn0 && objectValueIn.some(function (ref) { return ref['@id'] == testValueIn0['@id']; });
        return isPersistent;
    };
    TextGenerator.showConsole = true;
    return TextGenerator;
}());
exports.TextGenerator = TextGenerator;
