/* tslint:disable */
/* eslint-disable */
// Generated using typescript-generator version 2.36.1070 on 2022-05-30 13:21:18.

export interface Alias extends TTIriRef {
    alias: string;
}

export interface Argument {
    parameter: string;
    valueData: any;
    valueVariable: string;
}

export interface Compare {
    comparison: Comparison;
    valueData: string;
    valueVariable: string;
    valueSelect: string;
}

export interface ConceptRef extends TTIriRef {
    includeSubtypes: boolean;
    includeSupertypes: boolean;
}

export interface Function {
    id: TTIriRef;
    name: string;
    argument: Argument[];
}

export interface Heading extends TTIriRef {
    description: string;
    var: string;
}

export interface Match extends Heading {
    notExist: boolean;
    entityType: ConceptRef;
    entityId: TTIriRef;
    includeSubEntities: boolean;
    property: ConceptRef;
    isConcept: ConceptRef[];
    inRange: Range;
    value: Compare;
    function: Function;
    within: Within;
    valueVar: string;
    match: Match;
    and: Match[];
    or: Match[];
    orderLimit: OrderLimit;
    optional: Match[];
    graph: TTIriRef;
    entityInValueSet: TTIriRef;
    inSet: TTIriRef[];
    notInSet: TTIriRef[];
    isNotConcept: ConceptRef[];
    entityVar: string;
    propertyVar: string;
    inverseOf: boolean;
    index: boolean;
}

export interface OrderLimit {
    orderBy: Alias;
    direction: Order;
    count: number;
    test: Match[];
}

export interface PropertySelect extends TTIriRef {
    inverseOf: boolean;
    alias: string;
    binding: string;
    function: Function;
    select: Select;
}

export interface Query extends Heading {
    mainEntity: TTIriRef;
    resultFormat: ResultFormat;
    activeOnly: boolean;
    referenceDate: string;
    select: Select;
    groupBy: Select[];
    graph: TTIriRef;
    subselect: Select[];
    usePrefixes: boolean;
    page: number;
    pageSize: number;
}

export interface Range {
    from: Compare;
    to: Compare;
}

export interface Select {
    name: string;
    distinct: boolean;
    sum: boolean;
    average: boolean;
    max: boolean;
    entityType: ConceptRef;
    entityId: ConceptRef;
    entityIn: TTIriRef;
    property: PropertySelect[];
    match: Match;
    count: boolean;
    groupBy: string[];
    orderLimit: OrderLimit;
}

export interface SetDocument {
    id: TTIriRef;
    profile: Query[];
    dataSet: Query[];
}

export interface SetFactory {
}

export interface Summariser {
}

export interface Within {
    range: Range;
    compare: Compare;
    of: string;
    function: Function;
    targetFilter: Match;
}

export interface TTIriRef extends TTValue, Serializable {
    name: string;
    "@id": string;
}

export interface TTValue extends Serializable {
    order: number;
}

export interface Serializable {
}

export type Comparison = "EQUAL" | "LESS_THAN" | "LESS_THAN_OR_EQUAL" | "GREATER_THAN" | "GREATER_THAN_OR_EQUAL" | "MEMBER_OF" | "NOT_EQUAL" | "STARTS_WITH" | "LIKE";

export type Operator = "AND" | "OR" | "NOT";

export type Order = "ASCENDING" | "DESCENDING";

export type ResultFormat = "OBJECT" | "RELATIONAL";
