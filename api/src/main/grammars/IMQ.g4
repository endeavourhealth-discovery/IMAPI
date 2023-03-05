grammar IMQ;

queryRequest : prefixes? searchText? arguments? query EOF;

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
prefixes
    : PREFIXES
     OC
     prefixed
     (',' prefixed)*
     CC
     ;
prefixed
    :
     PN_PROPERTY COLON IRI_REF
    ;
selectClause
    :SELECT
    OC
    (select (',' select)*)
    CC
    ;

select
    :
    (iriRef|PN_PROPERTY)
    (whereClause)?
    (OC
    (select (',' select)*)
    CC)?
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
    :FROM
    (from|booleanFrom)
    ;


 booleanFrom
    :
    (andFrom
    |
    orFrom
    |
    notFrom
    )
    ;

notFrom
    :
    NOT
    (from)
    ;

orFrom
    : (from)
    (OR
    (from))+
    ;

andFrom
    :
    (from)
    (AND
    (from))+
    ;

from
    :OC
    description?
    graph?
    (reference
    |
    (booleanFrom)
    )?
    whereClause?
    CC
    ;



whereClause
    : WHERE
    (where|booleanWhere)
    ;


where
    :OC
    description?
    (
    (reference whereClause)
    |
    (reference with whereClause)
    |
    (reference whereValueTest)
    |
    (booleanWhere)
    )
    CC
    ;


booleanWhere
    :
    (andWhere
    |
    orWhere
    |
    notWhere)
    ;
notWhere
    :
    NOT
    where
    ;

orWhere
    : (where)
    (OR
    (where))+
    ;

andWhere
    :
    (where)
    (AND
    (where))+
    ;

with
    :
    WITH
    OC
    whereClause
    sortable?
    CC
    ;
whereValueTest
    :
    (
    (inClause | notInClause)
    |
    range
    |
    whereMeasure
    )
    valueLabel?
    ;

 valueLabel
    : ('valueLabel'
      string)
    ;

inClause
    :'in'
    (reference (',' reference)*)
    ;

notInClause
    : 'notIn'
      (reference (',' reference)*)
    ;


reference
    :
    inverseOf?
     sourceType?
     (
     ((IRI_REF | PN_PREFIXED) name?)
     |
     (variable)
     )
     alias?
     ;

inverseOf
    : 'inverseOf'
    ;

range
    :RANGE
    OC
    (fromRange toRange)
    |
    (fromRange)
    |
    (toRange)
    CC
    ;

fromRange
    : FROM whereMeasure
    ;
toRange
    : TO whereMeasure
    ;

whereMeasure
    :
    operator
    (string | number)
    units?
    relativeTo?
    ;
number
    : SIGNED
    |
    INTEGER
    |
    FLOAT
    ;
relativeTo
    : 'relativeTo'
    (PN_VARIABLE | PN_PROPERTY)
    ;

operator
    : EQ | GT | LT | LTE | GTE | STARTS_WITH
    ;
units
    : PN_PROPERTY
    ;

sortable
    :
    (iriRef| PN_PROPERTY)
    direction
    count
    ;

direction
    : ASCENDING|DESCENDING
    ;
count
    : (COUNT INTEGER)
    ;

graph
    : GRAPH IRI_REF
    ;

sourceType
    : type
    |
    set
    |
    var
    |
    descendantorselfof
    | descendantof
    | ancestorOf
    |ancestorAndDescendantOf
    ;
type
    :'@'
    ;
set
    :'^'
    ;
var
    : '$'
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

SIGNED
    : ('-' | '+')
    (INTEGER | FLOAT)
    ;


FLOAT
    :
    (DIGIT+ '.' DIGIT+)
    ;
INTEGER
    : DIGIT+
    ;

DIGIT
    : '0'..'9'
    ;


SEARCH_TEXT
    :'searchText'
    ;
ARGUMENTS
    : 'arguments'
    ;

RANGE
    : 'range'
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
ASCENDING
    :'ascending'
    ;
DESCENDING
    :'descending'
    ;
COUNT
    :'count'
    ;

SOURCE_TYPE
    :'sourceType'
    ;
PREFIXES
    :'prefixes' |'PREFIXES'
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





IRI_REF
   : ('http:'|'https:'|'urn:') (PN_CHARS | '.' | ':' | '/' | '\\' | '#' | '@' | '%' | '&' | DIGIT)*
   ;

STRING_LITERAL1
    :  '\''  ( ~('\u0022' | '\u005C' | '\u000A' | '\u000D'))* '\''
    ;

STRING_LITERAL2
    : '"'  ( ~('\u0022' | '\u005C' | '\u000A' | '\u000D'))* '"'
    ;


PN_CHARS_U
    : PN_CHARS_BASE | '_'|'-'
    ;

COMMA
    : ','
    ;
WS
   : ([\t\r\n\u000C] | ' '| COMMENT) + -> skip
   ;
DELIM
    :([\t\r\n\u000C] | ' ')
    ;
PN_PREFIXED
    :
    PN_PROPERTY? COLON ((DIGIT)+ | PN_PROPERTY)
    ;
PN_PROPERTY
    :
    ('a'..'z'|'A'..'Z')
    (PN_CHARS|DIGIT)*
    ;

PN_VARIABLE
    :'$'
    PN_PROPERTY
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
    |
    DIGIT
    ;




NOTIN : 'notIn';


VAR : '@var'
    ;


