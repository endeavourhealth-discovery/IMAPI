grammar IMQ;
imq:
  definition+
  prefix*
  ;

definition:
  DEFINE
  (variableDefinition
  |
  definedAs)
;

 variableDefinition:
   VARIABLE EQ iri
   ;
 definedAs :
  VARIABLE AS query
  ;

prefix: PREFIX
VARIABLE EQ IRI_REF
;
setLabel: VARIABLE
;
iri: (IRI_REF| PNAME_LN)
;



 type: VARIABLE
 ;

 query :
 OB
 type
 cte
 (with cte)*
 CB
 ;
 cte:
  (with
  |
  (ordered?
  from
  where?
  booleanClause*)?)
  booleanCte*
 ;
 booleanCte:
 (and | or |exclude)
 cte
 ;
 exclude : EXCLUDE
 ;
 booleanClause:
  (and| or| not)
  (resultSet|where | query | propertyTest)
;
with: WITH
  (VARIABLE AS OB cte CB)
  |
  VARIABLE
 ;
and: AND | '&';
or: OR;
not: NOT;

from : FROM set
 ;
set : VARIABLE
 ;

where : WHERE (propertyTest | function | inResultSet)
;
inResultSet: IN resultSet
;
resultSet: name
;

name : VARIABLE
;
propertyTest:
  property
  (valueCompare | is)
 ;

valueCompare:
  operator
  ((value units?)
  |
  parameter
  |
  property)
;
parameter : PARAMETER
;

is : IS VARIABLE
;

function :PARAMETER
;
property : VARIABLE
 ('.' VARIABLE)*

;
operator : EQ | GT |GTE |LT|LTE
;
value : NUMERIC
;
units :  VARIABLE
;
ordered :(LATEST | EARLIEST)
INTEGER?
;
EXCLUDE: [eE] [xX] [cC] [lL] [uU] [dD] [eE]
;
IF : [iI] [fF]
;

LATEST: [lL] [aA] [tT] [eE] [sS] [tT]
;
EARLIEST : [eE] [aA] [rR] [lL] [iI] [eE] [sS] [tT]
;

WHERE: [wW] [hH] [eE] [rR] [eE]
;

PREFIX : [pP] [rR] [eE] [fF] [iI] [xX]
;
CREATE: [cC] [rR] [eE] [aA] [tT] [eE]
;
VARIABLES: [vV] [aA] [rR] [iI] [aA] [bB] [lL] [eE] [sS]
;
DEFINE: [dD] [eE] [fF] [iI] [nN] [eE]
;


NUMERIC
    :   SIGN? DIGITS? '.' DIGITS (EXPONENT)?
    |   INTEGER
    ;
INTEGER : SIGN? DIGITS
;
AND: [aA] [nN] [dD]
 ;
OR: [oO] [rR]
 ;
NOT: [nN] [oO] [tT]
 ;
WITH: [wW] [iI] [tT] [hH]
 ;
FROM: [fF] [rR] [oM] [mM]
 ;
OB: '('
 ;
 CB : ')'
 ;
IS: [iI] [sS]
 ;
 IN: [iI] [nN]
  ;
 AS : [aA] [sS]
 ;
 COHORT : [cC] [oO] [Hh] [oO] [rR] [tT]
 ;

 EQ: '='
 ;
 GT : '>'
 ;
 GTE : '>='
 ;
 LT : '<'
 ;
 LTE : '<='
 ;

VARIABLE: [a-zA-Z] [a-zA-Z0-9_$]*
  ;
 PARAMETER
   : [$] [a-zA-Z_]+
   ;

VALUE
  : [a-zA-Z][a-zA-Z0-9+.-]* '://' ~[ \t\r\n]+
  ;

WS
  : [ \t\r\n\u000C]+ -> skip
  ;


IRI_REF
    : '<' (~[<>"{}|^`\u0000-\u0020])+'>'
    ;

PNAME_LN
    : PNAME_NS PN_LOCAL
    ;

// Prefix alone: prefix:
PNAME_NS
    : PN_PREFIX? ':'
    ;

// ---- fragments ----
fragment PN_PREFIX
    : PN_CHARS_BASE ((PN_CHARS | '.')* PN_CHARS)?
    ;

fragment PN_LOCAL
    : (PN_CHARS_U | ':' | [0-9] | PLX)
      ((PN_CHARS | '.' | ':' | PLX)* (PN_CHARS | ':' | PLX))?
    ;

fragment PN_CHARS_U
    : PN_CHARS_BASE | '_'
    ;

fragment PN_CHARS
    : PN_CHARS_U | '-' | [0-9]
    ;

fragment PN_CHARS_BASE
    : [A-Z] | [a-z]
    ;

fragment PLX
    : PERCENT | PN_LOCAL_ESC
    ;

fragment PERCENT
    : '%' HEX HEX
    ;

fragment HEX
    : [0-9] | [A-F] | [a-f]
    ;

fragment PN_LOCAL_ESC
    : '\\' ( '_' | '~' | '.' | '-' | '!' | '$' | '&' | '\''
           | '(' | ')' | '*' | '+' | ',' | ';' | '='
           | '/' | '?' | '#' | '@' | '%' )
    ;

  fragment SIGN
      :   [+-]
      ;

  fragment DIGITS
      :   [0-9]+
      ;

  fragment EXPONENT
      :   [eE] SIGN? DIGITS
      ;
