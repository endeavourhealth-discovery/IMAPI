"use strict";
exports.__esModule = true;
exports.OWL = void 0;
var OWL = /** @class */ (function () {
    function OWL() {
    }
    var _a;
    _a = OWL;
    OWL.NAMESPACE = "http://www.w3.org/2002/07/owl#";
    OWL.PREFIX = "owl";
    OWL.CLASS = OWL.NAMESPACE + "Class";
    OWL.OBJECT_PROPERTY = OWL.NAMESPACE + "ObjectProperty";
    OWL.SOME_VALUES_FROM = OWL.NAMESPACE + "someValuesFrom";
    OWL.ON_PROPERTY = OWL.NAMESPACE + "onProperty";
    OWL.HAS_VALUE = OWL.NAMESPACE + "hasValue";
    OWL.PREFIXED = new /** @class */ (function () {
        function class_1(superThis) {
            var _this = this;
            this.superThis = superThis;
            Object.keys(superThis).forEach(function (key) { return _this[key] = superThis[key].replace(superThis.NAMESPACE, superThis.PREFIX + ":"); });
            return this;
        }
        return class_1;
    }())(_a);
    return OWL;
}());
exports.OWL = OWL;
