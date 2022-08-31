import {Helpers, Vocabulary} from "im-library";
const {DataTypeCheckers:{isObjectHasKeys, isArrayHasLength}, TypeGuards:{isTTIriRef}} = Helpers;
const {IM,RDFS} = Vocabulary

export default class Validator {
    constructor() {
    }

    public async validate(iri: string, data: any) {
        if (iri === IM.VALIDATION_HAS_PARENT) return this.hasValidParents(data);
    }

    private hasValidParents(data: any): boolean {
        let valid = false;
        if (isObjectHasKeys(data, [RDFS.SUBCLASS_OF]) && isArrayHasLength(data[RDFS.SUBCLASS_OF]) && data[RDFS.SUBCLASS_OF].every((item: any) => isTTIriRef(item))) valid = true;
        if (isObjectHasKeys(data, [IM.IS_CONTAINED_IN]) && isArrayHasLength(data[IM.IS_CONTAINED_IN]) && data[IM.IS_CONTAINED_IN].every((item: any) => isTTIriRef(item))) valid = true;
        if (isObjectHasKeys(data, [RDFS.SUB_PROPERTY_OF]) && isArrayHasLength(data[RDFS.SUB_PROPERTY_OF]) && data[RDFS.SUB_PROPERTY_OF].every((item: any) => isTTIriRef(item))) valid = true;
        if (isObjectHasKeys(data, [IM.IS_CHILD_OF]) && isArrayHasLength(data[IM.IS_CHILD_OF]) && data[IM.IS_CHILD_OF].every((item: any) => isTTIriRef(item))) valid = true;
        return valid;
    }
}