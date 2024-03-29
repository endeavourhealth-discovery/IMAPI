grammar GraphQL;

//https://spec.graphql.org/June2018/#sec-Language.Document
document: definition+;

definition:
	executableDefinition
	| typeSystemDefinition
	| typeSystemExtension;

//https://spec.graphql.org/June2018/#ExecutableDefinition
executableDefinition: operationDefinition | fragmentDefinition;

//https://spec.graphql.org/June2018/#sec-Language.Operations
operationDefinition:
	operationType name? variableDefinitions? directives? selectionSet
	| selectionSet
	;

operationType: 'query' | 'mutation' | 'subscription';

//https://spec.graphql.org/June2018/#sec-Selection-Sets
selectionSet: '{' selection+ '}';

selection: field
    | fragmentSpread
    | inlineFragment
    ;
//https://spec.graphql.org/June2018/#sec-Language.Fields
field: alias? name arguments? directives? selectionSet?;

//https://spec.graphql.org/June2018/#sec-Language.Arguments
arguments: '(' argument+ ')';
argument: name ':' value;

//https://spec.graphql.org/June2018/#sec-Field-Alias
alias: name ':';

//https://spec.graphql.org/June2018/#sec-Language.Fragments
fragmentSpread: '...' fragmentName directives?;
fragmentDefinition:
	'fragment' fragmentName typeCondition directives? selectionSet;
fragmentName: name; // except on

//https://spec.graphql.org/June2018/#sec-Type-Conditions
typeCondition: 'on' namedType;

//https://spec.graphql.org/June2018/#sec-Inline-Fragments
inlineFragment:	'...' typeCondition? directives? selectionSet;

//https://spec.graphql.org/June2018/#sec-Input-Values
value:
	 variable
	| intValue
	| floatValue
	| stringValue
	| booleanValue
	| nullValue
	| enumValue
	| listValue
	| objectValue
   ;

//https://spec.graphql.org/June2018/#sec-Int-Value
intValue: INT;

//https://spec.graphql.org/June2018/#sec-Float-Value
floatValue: FLOAT;

//https://spec.graphql.org/June2018/#sec-Boolean-Value
booleanValue
	:	'true'
	|	'false'
	;

//https://spec.graphql.org/June2018/#sec-String-Value
stringValue : STRING | BLOCK_STRING;

//https://spec.graphql.org/June2018/#sec-Null-Value
nullValue: 'null';

//https://spec.graphql.org/June2018/#sec-Enum-Value
enumValue: name; //{ not (nullValue | booleanValue) };

//https://spec.graphql.org/June2018/#sec-List-Value
listValue: '[' ']'
    | '[' value+ ']'
    ;

//https://spec.graphql.org/June2018/#sec-Input-Object-Values
objectValue: '{' objectField* '}';

objectField: name ':' value;

//https://spec.graphql.org/June2018/#sec-Language.Variables
variable: '$' name;
variableDefinitions: '(' variableDefinition+ ')';
variableDefinition: variable ':' type_ defaultValue?;
defaultValue: '=' value;

//https://spec.graphql.org/June2018/#sec-Type-References
type_: namedType '!'?
    | listType '!'?
    ;

namedType: name;
listType: '[' type_ ']';


//https://spec.graphql.org/June2018/#sec-Language.Directives
directives:  directive+;
directive: '@' name arguments?;

// https://graphql.github.io/graphql-spec/June2018/#TypeSystemDefinition
typeSystemDefinition: schemaDefinition
	| typeDefinition
	| directiveDefinition
	;

//https://spec.graphql.org/June2018/#TypeSystemExtension
typeSystemExtension: schemaExtension
    | typeExtension
    ;

// https://graphql.github.io/graphql-spec/June2018/#sec-Schema
schemaDefinition:
	 'schema' directives? '{' rootOperationTypeDefinition+ '}';

rootOperationTypeDefinition: operationType ':' namedType;

//https://spec.graphql.org/June2018/#sec-Schema-Extension
schemaExtension:
    'extend' 'schema' directives? '{' operationTypeDefinition+ '}'
    | 'extend' 'schema' directives
    ;

//https://spec.graphql.org/June2018/#OperationTypeDefinition
operationTypeDefinition: operationType ':' namedType;


//https://spec.graphql.org/June2018/#sec-Descriptions
description: stringValue;

//https://spec.graphql.org/June2018/#sec-Types
typeDefinition:
	scalarTypeDefinition
	| objectTypeDefinition
	| interfaceTypeDefinition
	| unionTypeDefinition
	| enumTypeDefinition
	| inputObjectTypeDefinition;

//https://spec.graphql.org/June2018/#sec-Type-Extensions
typeExtension : scalarTypeExtension
    | objectTypeExtension
    | interfaceTypeExtension
    | unionTypeExtension
    | enumTypeExtension
    | inputObjectTypeExtension
    ;

//https://spec.graphql.org/June2018/#sec-Scalars
scalarTypeDefinition: description? 'scalar' name directives?;

//https://spec.graphql.org/June2018/#sec-Scalar-Extensions
scalarTypeExtension:  'extend' 'scalar' name directives;

// https://graphql.github.io/graphql-spec/June2018/#sec-Objects
objectTypeDefinition :
    description?   'type' name implementsInterfaces?  directives? fieldsDefinition?;

implementsInterfaces: 'implements' '&'? namedType
    | implementsInterfaces '&' namedType
    ;


fieldsDefinition: '{'  fieldDefinition+ '}';
fieldDefinition: description? name  argumentsDefinition? ':' type_  directives? ;

//https://spec.graphql.org/June2018/#sec-Field-Arguments
argumentsDefinition: '(' inputValueDefinition+ ')';
inputValueDefinition:  description? name ':' type_ defaultValue? directives?;

//https://spec.graphql.org/June2018/#sec-Object-Extensions
objectTypeExtension:
    'extend' 'type' name implementsInterfaces? directives? fieldsDefinition
    | 'extend' 'type' name implementsInterfaces? directives
    | 'extend' 'type' name implementsInterfaces
    ;

//https://spec.graphql.org/June2018/#sec-Interfaces
interfaceTypeDefinition: description? 'interface' name directives? fieldsDefinition?;

//https://spec.graphql.org/June2018/#sec-Interface-Extensions
interfaceTypeExtension:  'extend' 'interface' name directives? fieldsDefinition
    | 'extend' 'interface' name directives
    ;

// https://graphql.github.io/graphql-spec/June2018/#sec-Unions
unionTypeDefinition:  description? 'union' name directives? unionMemberTypes?;
unionMemberTypes: '=' '|'?  namedType ('|'namedType)* ;

//https://spec.graphql.org/June2018/#sec-Union-Extensions
unionTypeExtension : 'extend' 'union' name directives? unionMemberTypes
    | 'extend' 'union' name directives
    ;

//https://spec.graphql.org/June2018/#sec-Enums
enumTypeDefinition:  description? 'enum' name directives? enumValuesDefinition?;
enumValuesDefinition: '{' enumValueDefinition+  '}';
enumValueDefinition: description? enumValue  directives?;

//https://spec.graphql.org/June2018/#sec-Enum-Extensions
enumTypeExtension: 'extend' 'enum' name directives? enumValuesDefinition
    | 'extend' 'enum' name directives
    ;

//https://spec.graphql.org/June2018/#sec-Input-Objects
inputObjectTypeDefinition: description? 'input' name directives? inputFieldsDefinition?;
inputFieldsDefinition: '{' inputValueDefinition+ '}';

//https://spec.graphql.org/June2018/#sec-Input-Object-Extensions
inputObjectTypeExtension:  'extend' 'input' name directives? inputFieldsDefinition
    | 'extend' 'input' name directives
    ;

//https://spec.graphql.org/June2018/#sec-Type-System.Directives
directiveDefinition: description? 'directive' '@' name argumentsDefinition? 'on' directiveLocations;
directiveLocations: directiveLocation ('|' directiveLocation)*;
directiveLocation: executableDirectiveLocation | typeSystemDirectiveLocation;

executableDirectiveLocation:
     'QUERY'
    | 'MUTATION'
    | 'SUBSCRIPTION'
    | 'FIELD'
    | 'FRAGMENT_DEFINITION'
    | 'FRAGMENT_SPREAD'
    | 'INLINE_FRAGMENT'
    ;

typeSystemDirectiveLocation:
     'SCHEMA'
    | 'SCALAR'
    | 'OBJECT'
    | 'FIELD_DEFINITION'
    | 'ARGUMENT_DEFINITION'
    | 'INTERFACE'
    | 'UNION'
    | 'ENUM'
    | 'ENUM_VALUE'
    | 'INPUT_OBJECT'
    | 'INPUT_FIELD_DEFINITION'
    ;

name: NAME;

//Start lexer
NAME: [_A-Za-z] [_0-9A-Za-z]*;

fragment CHARACTER: ( ESC | ~ ["\\]);
STRING: '"' CHARACTER* '"';
BLOCK_STRING
    :   '"""' .*? '"""'
    ;
ID: STRING;
fragment ESC: '\\' ( ["\\/bfnrt] | UNICODE);

fragment UNICODE: 'u' HEX HEX HEX HEX;

fragment HEX: [0-9a-fA-F];

fragment NONZERO_DIGIT: [1-9];
fragment DIGIT: [0-9];
fragment FRACTIONAL_PART: '.' DIGIT+;
fragment EXPONENTIAL_PART: EXPONENT_INDICATOR SIGN? DIGIT+;
fragment EXPONENT_INDICATOR: [eE];
fragment SIGN: [+-];
fragment NEGATIVE_SIGN: '-';

FLOAT: INT FRACTIONAL_PART
    | INT EXPONENTIAL_PART
    | INT FRACTIONAL_PART EXPONENTIAL_PART
    ;

INT: NEGATIVE_SIGN? '0'
    | NEGATIVE_SIGN? NONZERO_DIGIT DIGIT*
    ;

PUNCTUATOR: '!'
    | '$'
    | '(' | ')'
    | '...'
    | ':'
    | '='
    | '@'
    | '[' | ']'
    | '{' | '}'
    | '|'
    ;

// no leading zeros

fragment EXP: [Ee] [+\-]? INT;

// \- since - means "range" inside [...]

WS: [ \t\n\r]+ -> skip;
COMMA: ',' -> skip;
LineComment
    :   '#' ~[\r\n]*
        -> skip
    ;

UNICODE_BOM: (UTF8_BOM
    | UTF16_BOM
    | UTF32_BOM
    ) -> skip
    ;

UTF8_BOM: '\uEFBBBF';
UTF16_BOM: '\uFEFF';
UTF32_BOM: '\u0000FEFF';

