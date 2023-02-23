grammar IMQ;

queryRequest : OC prefixDecl* searchText? arguments? query CC EOF;

searchText
    : SEARCH_TEXT (STRING_LITERAL1 | STRING_LITERAL2)
    ;
arguments
        :ARGUMENTS
        OC
      argument (',' argument)*
      CC
       ;
label
    :STRING_LITERAL1
    |
    STRING_LITERAL2
    ;

string
    :STRING_LITERAL1
    |
    STRING_LITERAL2
    ;
argument: parameter  COLON (value | valueDataList | valueIriList);

parameter : PN_PROPERTY ;

valueDataList
    : value (',' value)*
    ;

valueIriList
    : iriRef (',' iriRef)*
    ;

value : string;

iriRef
    :
    '@id'
    (IRI_REF | PN_PREFIXED)
    name?

    ;



query
    : QUERY
    OC
    iriRef?
    properName?
    description?
    activeOnly?
    fromClause
    selectClause?
    query*
     CC
    ;

properName
    : 'name ' string
    ;
prefixDecl
    : PREFIX PN_PROPERTY COLON IRI_REF
    ;

selectClause
    : SELECT
    selectList
    ;
 selectList
    :select (',' select)*
    ;
select
    : OC
    (iriRef|PN_PROPERTY)
    whereClause?
    selectClause?
    CC
    ;


name
    : NAME
    ;
description
    : DESCRIPTION
    string
    ;
activeOnly
    : ACTIVE_ONLY
    ;

fromClause
    : FROM
    (from
    |
    fromBoolean
    |
    bracketFrom)

    ;

bracketFrom
    :OC
    (
    fromBoolean
    |
    from)
    OC
    ;
 fromBoolean
    :(andFrom
    |
    orFrom
    |
    notFrom)
    ;

notFrom
    :(from | bracketFrom)
    (NOT
    (from | bracketFrom))+
    ;

orFrom
    : (from | bracketFrom)
    (OR
    (from| bracketFrom))+
    ;

andFrom
    :
    (from | bracketFrom)
    (AND
    (from| bracketFrom))+
    ;

from
    :
    OC
    description?
    graph?
    reference?
    whereClause?
    CC
    ;

whereClause
    : WHERE
    subWhere
    ;

 subWhere
    :
    (where
    |
    whereBoolean
    |
    bracketWhere
    )
    ;

where
    :
     OC
    with?
     description?
    notExist?
    reference
    whereValueTest?
    (whereClause)?
    CC
    ;



notExist
    :NOTEXIST
    ;


valueLabel
    : VALUE_LABEL
    string
    ;
whereBoolean
    :
    (andWhere
    |
    orWhere
    |
    notWhere)
    ;
notWhere
    :
    (NOT
    (where | bracketWhere))+
    ;

orWhere
    : (where | bracketWhere)
    (OR
    (where| bracketWhere))+
    ;

andWhere
    :
    (where | bracketWhere)
    (AND
    (where| bracketWhere))+
    ;
bracketWhere
    :OC
    (
    whereBoolean
    |
    where)
    CC
    ;

with
    : WITH
    OC
    (where | whereBoolean)
    sortable
    CC
    ;
whereValueTest
    :(inClause | notInClause)
    | range
    | whereMeasure
    ;

inClause
    :'in'
    conceptSet
    ;

notInClause
    : 'notIn'
    conceptSet
    ;

conceptSet
    :
    ( from| bracketFrom|fromBoolean)
    ;

reference
    :
    inverseOf?
     sourceType
     ((subsumption?  (IRI_REF | PN_PREFIXED) name?)
     |
     (subsumption? variable))
     alias?
     ;

inverseOf
    : 'inverseOf'
    ;

range
    : (fromRange toRange)
    |
    fromRange
    |
    toRange
    ;

fromRange
    : FROM OC whereMeasure CC
    ;
toRange
    : TO OC whereMeasure CC
    ;

whereMeasure
    : operator
    ((string | NUMBER)
    units?)
    relativeTo?
    ;
relativeTo
    : 'relativeTo'
    PN_VARIABLE | PN_PROPERTY
    ;

operator
    : EQ | GT | LT | LTE | GTE | STARTS_WITH
    ;
units
    : PN_CHARS
    ;

sortable
    : latest| earliest| maximum| minimum
    count
    ;
latest
    : LATEST PN_PROPERTY
    ;
earliest
    : EARLIEST PN_PROPERTY
    ;
maximum
    : MAXIMUM PN_PROPERTY
    ;
minimum
    : MINIMUM PN_PROPERTY
    ;
count
    : COUNT DIGIT
    ;

graph
    : GRAPH IRI_REF
    ;



sourceType :
    (TYPE | SET | INSTANCE| VAR)
    ;


subsumption
    : descendantorselfof
    | descendantof
    | ancestorOf
    |ancestorAndDescendantOf
    ;

ancestorAndDescendantOf: '>><<';

ancestorOf: '>>';

descendantof: '<';

descendantorselfof: '<<';

variable
    :
    PN_VARIABLE
    ;

alias
    : ALIAS COLON PN_PROPERTY
    ;



// LEXER

SEARCH_TEXT
    :'searchText'
    ;
ARGUMENTS
    : 'arguments'
    ;

QUERY
    :'query'
    ;
FROM
    :'from'
    ;
GRAPH
    :'graph'
    ;
WHERE
    :'where'
    ;
NOTEXIST
    : 'notExist'
    ;
WITH
    : 'with'
    ;
SELECT
    : 'select'
    ;
EARLIEST
    :'earliest'
    ;
LATEST
    :'latest'
    ;
MAXIMUM
    :'maximum'
    ;
MINIMUM
    :'minimum'
    ;
COUNT
    :'count'
    ;

SOURCE_TYPE
    :'sourceType'
    ;
PREFIX
    :'prefix' |'PREFIX'
    ;

COMMENT
    :  '/*'  ( ~('\u0022' | '\u005C' | '\u000A' | '\u000D'))* '*/'
    ;
DESCRIPTION
    : 'description'
    ;

NAME
    : '|'  ( ~('\u0022' | '\u005C' | '\u000A' | '\u000D'))* '|'
    ;
ALIAS
    :'alias'
    ;

ACTIVE_ONLY
    :'activeOnly'
    ;
IN
    :'in'
    ;
TYPE
    :'@type'
    ;
SET
    :'@set'
    ;
INSTANCE
    :'@id'
    ;
EQ
    :'='
    ;
GT
    :'>'
    ;
LT
    :'<'
    ;
GTE
    :'>='
    ;
LTE
    :'<='
    ;
STARTS_WITH
    :'startsWith'
    ;
AND
    :'and' | 'AND'
    ;
OR  :'or' | 'OR'
    ;
NOT :'not' | 'NOT'
    ;
TO
    :'to'
    ;
OC
    :'{'
    ;
CC
    :'}'
    ;
OSB : '['
    ;
CSB  : ']'
    ;
OB  : '('
    ;
CB  : ')'
    ;
COLON
    :':'
    ;



UCHAR
   : '\\u' HEX HEX HEX HEX | '\\U' HEX HEX HEX HEX HEX HEX HEX HEX
   ;

HEX
   : [0-9] | [A-F] | [a-f]
   ;

IRI_REF
   : ('http:'|'https:') (PN_CHARS | '.' | ':' | '/' | '\\' | '#' | '@' | '%' | '&' | UCHAR)*
   ;

STRING_LITERAL1
    :  '\''  ( ~('\u0022' | '\u005C' | '\u000A' | '\u000D'))* '\''
    ;

STRING_LITERAL2
    : '"'  ( ~('\u0022' | '\u005C' | '\u000A' | '\u000D'))* '"'
    ;


PN_CHARS_U
    : PN_CHARS_BASE | '_'
    ;

COMMA
    : ','
    ;
WS
   : ([\t\r\n\u000C] | ' '| COMMENT) + -> skip
   ;

PN_PROPERTY
    :
    ('a'..'z' | 'A'.. 'Z')
    (PN_CHARS | DIGIT)*
    ;

PN_VARIABLE
    :'$'
    (PN_CHARS| DIGIT)+
    ;

PN_PREFIXED
    : PN_PROPERTY? COLON ((DIGIT)+ | PN_PROPERTY)
    ;

PN_CHARS_BASE
    : 'A'..'Z'
    | 'a'..'z'
    | '\u00C0'..'\u00D6'
    | '\u00D8'..'\u00F6'
    | '\u00F8'..'\u02FF'
    | '\u0370'..'\u037D'
    | '\u037F'..'\u1FFF'
    | '\u200C'..'\u200D'
    | '\u2070'..'\u218F'
    | '\u2C00'..'\u2FEF'
    | '\u3001'..'\uD7FF'
    | '\uF900'..'\uFDCF'
    | '\uFDF0'..'\uFFFD'
    ;

PN_CHARS
    : PN_CHARS_U
    | '-'
    | DIGIT
    /*| '\u00B7'
    | '\u0300'..'\u036F'
    | '\u203F'..'\u2040'*/
    ;


DIGIT
    : '0'..'9'
    ;

NUMBER
    : '-'? DIGIT+ '.'? DIGIT+
    ;



NOTIN : 'notIn';
VALUE_LABEL : 'valueLabel';

VAR : '@var'
    ;
