"use strict";
exports.__esModule = true;
exports.RDFS = void 0;
var RDFS = /** @class */ (function () {
    function RDFS() {
    }
    var _a;
    _a = RDFS;
    RDFS.NAMESPACE = "http://www.w3.org/2000/01/rdf-schema#";
    RDFS.PREFIX = "rdfs";
    RDFS.LABEL = RDFS.NAMESPACE + "label";
    RDFS.COMMENT = RDFS.NAMESPACE + "comment";
    RDFS.SUBCLASS = RDFS.NAMESPACE + "subClassOf";
    RDFS.PREFIXED = new /** @class */ (function () {
        function class_1(superThis) {
            var _this = this;
            this.superThis = superThis;
            Object.keys(superThis).forEach(function (key) { return _this[key] = superThis[key].replace(superThis.NAMESPACE, superThis.PREFIX + ":"); });
            return this;
        }
        return class_1;
    }())(_a);
    return RDFS;
}());
exports.RDFS = RDFS;
