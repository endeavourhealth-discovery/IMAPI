"use strict";
exports.__esModule = true;
exports.Clause = exports.TTIriRef = void 0;
var lodash_1 = require("lodash");
var wordMap = require("./Config/WordMap.json");
var pathMap = require("./Config/PathMap.json");
var Helpers_1 = require("./Helpers");
var TTIriRef = /** @class */ (function () {
    function TTIriRef(obj) {
        if (obj && obj["@id"] && (obj === null || obj === void 0 ? void 0 : obj.name)) {
            this["@id"] = obj["@id"] ? obj["@id"] : null;
            this.name = obj === null || obj === void 0 ? void 0 : obj.name;
        }
    }
    return TTIriRef;
}());
exports.TTIriRef = TTIriRef;
var Clause = /** @class */ (function () {
    function Clause(definition) {
        var _this = this;
        this.definition = definition;
        //maps all key-value pairs in pathMap.json to functions (key) which return a transfored string (value mapped against wordMap.json)
        Object.keys(pathMap).forEach(function (key) {
            Object.defineProperty(_this, key, {
                get: function () { return this.get(pathMap[key]); }
            });
        });
        return this;
    }
    //ensures all properties return labels from WordMap.json (if applicable) or labels from AdditionalOntology.json where Iris match  (if applicable)
    Clause.prototype.get = function (path) {
        var _a;
        var originalObject = lodash_1._.cloneDeep(lodash_1._.get(this.definition, path));
        var originalValue = (originalObject === null || originalObject === void 0 ? void 0 : originalObject.name) || (originalObject === null || originalObject === void 0 ? void 0 : originalObject.value) || originalObject;
        if (originalValue == "Unknown code set")
            originalValue = Helpers_1.Helpers.getLabel(originalObject['@id']) || originalValue;
        if (typeof (originalValue) == "string")
            originalValue = Helpers_1.Helpers.trimUnnecessaryText(originalValue);
        var mappedValue;
        if (originalValue)
            mappedValue = ((_a = wordMap.name.find(function (obj) { return obj.originalValue == originalValue; })) === null || _a === void 0 ? void 0 : _a.mappedValue) || originalValue;
        var mappedObject = lodash_1._.cloneDeep(originalObject); //if you want to replace .name property of IriRef in original Definition remove _.cloneDeep
        if (mappedObject === null || mappedObject === void 0 ? void 0 : mappedObject.name)
            mappedObject.name = mappedValue;
        else if (mappedObject === null || mappedObject === void 0 ? void 0 : mappedObject.value)
            mappedObject.value = mappedValue;
        else
            mappedObject = mappedValue;
        return mappedValue ? mappedObject : originalObject;
    };
    Object.defineProperty(Clause.prototype, "exists", {
        //special getters that are not defined in PathMap.json
        get: function () {
            var _a, _b, _c;
            var exists = ((_a = this === null || this === void 0 ? void 0 : this.definition) === null || _a === void 0 ? void 0 : _a.notExist) != true && ((_c = (_b = this === null || this === void 0 ? void 0 : this.definition) === null || _b === void 0 ? void 0 : _b.valueObject) === null || _c === void 0 ? void 0 : _c.notExist) != true;
            return exists;
        },
        enumerable: false,
        configurable: true
    });
    Object.defineProperty(Clause.prototype, "are", {
        get: function () {
            return wordMap["are"][this.exists.toString()];
        },
        enumerable: false,
        configurable: true
    });
    Object.defineProperty(Clause.prototype, "have", {
        get: function () {
            return wordMap["have"][this.exists.toString()];
        },
        enumerable: false,
        configurable: true
    });
    return Clause;
}());
exports.Clause = Clause;
