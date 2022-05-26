"use strict";
exports.__esModule = true;
exports.Helpers = void 0;
var wordMap = require("./Config/WordMap.json");
var labels = require("./Config/AdditionalOntology.json");
var Helpers = /** @class */ (function () {
    function Helpers() {
    }
    //these are silenced words or excess words that need to be trimmed off
    Helpers.trimUnnecessaryText = function (inputString) {
        return inputString
            // .replace("Observation (entry type)", "")
            // .replace("Event (entry type)", "")
            .replace(" (entry type)", "");
    };
    Helpers.isSingular = function (testObject) {
        if (typeof (testObject) == "number") {
            if (testObject == 1 || testObject == -1)
                return true;
        }
        else if (typeof (testObject) == "string") {
            // #todo compare testObject against an array of strings that represent signular, all else is plural
        }
        return false;
    };
    // (case-insensitive) tests the first letter for a string against an array of letters - e.g. indefiniteArticle
    Helpers.isFirstLetterVowel = function (testString) {
        return ["a", "e", "i", "o", "u"].some(function (letter) { return letter.toLowerCase() == testString.substring(0, 1).toLowerCase(); });
    };
    // compares the value of a string (testString) against an array (of strings for comparison) - e.g. useful for valueIn
    Helpers.includes = function (testString, stringArray) {
        return stringArray.includes(testString);
    };
    Helpers.isObjectAnimate = function (testObjectName) {
        return ["person", "persons", "patient", "patients", "people"]
            .includes(testObjectName.toLowerCase()) ? true : false;
    };
    //checks the list of arguments for truthness 
    Helpers.isTrue = function () {
        var args = [];
        for (var _i = 0; _i < arguments.length; _i++) {
            args[_i] = arguments[_i];
        }
        return args.every(function (arg, index) { return arg == true; });
    };
    Helpers.hasTransformation = function (phraseType, input) {
        return wordMap[phraseType][input] == null ? false : true;
    };
    Helpers.isNegative = function (testNumber) {
        var _sign = Math.sign(testNumber);
        if (_sign == 1 || _sign == 0) {
            return false;
        }
        else {
            return true;
        }
    };
    Helpers.a = function (ref) {
        var testString = (ref === null || ref === void 0 ? void 0 : ref.name) || ref;
        if (!testString || testString == "")
            return "a/an";
        return Helpers.isFirstLetterVowel(testString) ? "an" : "a";
    };
    ;
    Helpers.pronoun = function (ref) {
        var testString = (ref === null || ref === void 0 ? void 0 : ref.name) || ref;
        if (!testString || testString == "")
            return "they/it";
        return wordMap === null || wordMap === void 0 ? void 0 : wordMap.animatePronoun[Helpers.isObjectAnimate(testString).toString()];
    };
    Helpers.getLabel = function (iri) {
        var label = labels.entities.filter(function (label) { return label['@id'] == iri; });
        return label.length > 0 ? label[0]["rdfs:label"] : null;
    };
    return Helpers;
}());
exports.Helpers = Helpers;
// export const Helpers = { "isSingular": isSingular, "firstLetterIsVowel": firstLetterIsVowel, "includes": includes, "isObjectAnimate": isObjectAnimate, "isTrue": isTrue, "hasTransformation": hasTransformation, "isNegative": isNegative, "a": a };
