"use strict";
exports.__esModule = true;
exports.SHACL = void 0;
var SHACL = /** @class */ (function () {
    function SHACL() {
    }
    var _a;
    _a = SHACL;
    SHACL.NAMESPACE = "http://www.w3.org/ns/shacl#";
    SHACL.PREFIX = "shacl";
    SHACL.PROPERTY = SHACL.NAMESPACE + "property";
    SHACL.PATH = SHACL.NAMESPACE + "path";
    SHACL.CLASS = SHACL.NAMESPACE + "class";
    SHACL.DATATYPE = SHACL.NAMESPACE + "datatype";
    SHACL.MINCOUNT = SHACL.NAMESPACE + "minCount";
    SHACL.MAXCOUNT = SHACL.NAMESPACE + "maxCount";
    SHACL.NODESHAPE = SHACL.NAMESPACE + "NodeShape";
    SHACL.PREFIXED = new /** @class */ (function () {
        function class_1(superThis) {
            var _this = this;
            this.superThis = superThis;
            Object.keys(superThis).forEach(function (key) { return _this[key] = superThis[key].replace(superThis.NAMESPACE, superThis.PREFIX + ":"); });
            return this;
        }
        return class_1;
    }())(_a);
    return SHACL;
}());
exports.SHACL = SHACL;
