"use strict";
exports.__esModule = true;
exports.RDF = void 0;
var RDF = /** @class */ (function () {
    function RDF() {
    }
    var _a;
    _a = RDF;
    RDF.NAMESPACE = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
    RDF.PREFIX = "rdf";
    RDF.TYPE = RDF.NAMESPACE + "type";
    RDF.PROPERTY = RDF.NAMESPACE + "Property";
    RDF.PREFIXED = new /** @class */ (function () {
        function class_1(superThis) {
            var _this = this;
            this.superThis = superThis;
            Object.keys(superThis).forEach(function (key) { return _this[key] = superThis[key].replace(superThis.NAMESPACE, superThis.PREFIX + ":"); });
            return this;
        }
        return class_1;
    }())(_a);
    return RDF;
}());
exports.RDF = RDF;
