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
argument: parameter  COLON (value | iriRef |valueDataList | valueIriList);

parameter : PN_PROPERTY ;

valueDataList
    : OB value (',' value)* CB
    ;

valueIriList
    : OB iriRef (',' iriRef)* CB
    ;

value : string;

iriRef
    :
    '@id'
    (IRI_REF | PN_PREFIXED | PN_PROPERTY)
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
    : SELECT selection
    ;
selection
    : (selectionList| select)
    ;

selectionList
    : OSB select (',' select)* CSB
    ;
select
    : OC
    (iriRef|PN_PROPERTY)
    whereClause?
    OC
    selectClause?
    CC
    ;


name
    : NAME
    ;
description
    : string
    ;
activeOnly
    : ACTIVE_ONLY
    ;

fromClause
    : FROM
    OC
    (from
    |
    fromWhere
    |
    fromBoolean
    |
    bracketFrom)
    CC
    ;

fromWhere
    :( (from
    |
    bracketFrom)
    whereClause)
    |
    whereClause
    ;
bracketFrom
    :OB
    (fromWhere
    |
    fromBoolean
    |
    from)
    CB
    ;
 fromBoolean
    :(andFrom
    |
    orFrom
    |
    notFrom)
    ;

notFrom
    :(from | bracketFrom| fromWhere)
    (NOT
    (from | bracketFrom|fromWhere))+
    ;

orFrom
    : (from | bracketFrom| fromWhere)
    (OR
    (from| bracketFrom|fromWhere))+
    ;

andFrom
    :
    (from | bracketFrom| fromWhere)
    (AND
    (from| bracketFrom|fromWhere))+
    ;

from
    :
    description?
    graph?
    reference
    ;

whereClause
    : WHERE
    subWhere
    ;

 subWhere
    :
    OC
    (where
    |
    whereWith
    |
    whereValue
    |
    whereBoolean
    |
    whereWhere
    |
    bracketWhere
    )
    CC
    ;

whereWith
    :
    with
    (where | whereValue | whereBoolean)
    ;
where
    : description?
    notExist?
    reference
    ;
whereWhere
    : where
    subWhere
    ;


notExist
    :NOTEXIST
    ;
whereValue
    : where
    valueLabel?
    whereValueTest
    ;

valueLabel
    : VALUE_LABEL
    string
    ;
whereBoolean
    :
    where?
    (andWhere
    |
    orWhere
    |
    notWhere)
    ;
notWhere
    :
    (NOT
    (where | bracketWhere|whereValue|whereWhere))+
    ;

orWhere
    : (where | bracketWhere| whereValue|whereWhere)
    (OR
    (where| bracketWhere| whereValue|whereWhere))+
    ;

andWhere
    :
    (where | bracketWhere|whereValue|whereWhere)
    (AND
    (where| bracketWhere|whereValue|whereWhere))+
    ;
bracketWhere
    :OB
    (whereValue
    |
    whereBoolean
    |
    where)
    CB
    ;

with
    : WITH
    OC
    (where | whereBoolean| whereValue|whereWhere)
    sortable
    CC
    ;
whereValueTest
    :(in | notin)
    | range
    | whereMeasure
    ;

in
    : 'in'
    ( from| fromWhere| fromBoolean|bracketFrom)

    ;

notin
    : 'notIn'
    ( from| fromWhere| fromBoolean|bracketFrom)
    ;

reference
    :
     sourceType
     ((subsumption?  (IRI_REF | PN_PREFIXED | PN_PROPERTY) name?)
     |
     (subsumption? variable))
     alias?
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
    (TYPE | SET | INSTANCE)
    ;



subsumption
    : descendantorselfof
    | descendantof
    | ancestorOf
    ;

ancestorOf: '>>';

descendantof: '<';

descendantorselfof: '<<';

variable
    : PN_VARIABLE
    ;

alias
    : ALIAS COLON string
    ;



// LEXER

SEARCH_TEXT
    :'searchText'
    ;
ARGUMENTS
    : 'arguments'
    ;
ID
    : 'id'
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

DESCRIPTION
    :  '/*'  ( ~('\u0022' | '\u005C' | '\u000A' | '\u000D'))* '*/'
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
   : ([\t\r\n\u000C] | ' ') + -> skip
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
    : PN_PROPERTY? COLON PN_PROPERTY
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
