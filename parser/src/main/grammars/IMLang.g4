grammar IMLang;

entity : iriLabel types annotationList predicateObjectList
    '.'
      EOF
       ;
iriLabel :
    'iri' iri
    ;
annotationList :
    (';' annotation)+
    ;
predicateObjectList
   : (';' (axiom|properties|membership|target))+
   ;


annotation:
    ((name|description|code|version) (':')? QUOTED_STRING)
    | (scheme|status)
    ;

 scheme :
    'scheme' iri
    ;
types :
     (';')? ('type') iri (',' iri)*?
     ;
version :
    'version'
    ;

axiom  :
        subclassOf
        | equivalentTo
        |subpropertyOf
        |inverseOf
        |domain
        |range
        ;
     
properties :
     'property' (':')?
    propertyRestriction (',' propertyRestriction)*?

    ;
membership :
    members (';'notmembers)*?
    ;

members :
    'member' (':')?
    classExpression? (',' classExpression)*?
    ;
notmembers  :
    'notMember' '['
        iri (',' iri)*?
     ']'
     ;
target  :
    'targetClass'
    ;


minInclusive    :'minInclusive' DOUBLE
    ;
maxInclusive    :'maxInclusive' DOUBLE
    ;
minExclusive    :'minExclusive' DOUBLE
    ;
maxExclusive :'maxExclusive' DOUBLE
    ;

status  :
    'status' iri
      ;

subclassOf : 'subClassOf' classExpression;
equivalentTo : 'equivalentTo' classExpression ;
subpropertyOf : 'subPropertyOf' iri;
inverseOf : 'inverseOf' iri;
domain : 'domain' classExpression;
range : 'range' classExpression;

classExpression :
    (classIri |existential|not)
    ((and|or) classExpression)*?
    ;
classIri :
     'class' (':')? iri
     ;

and :
    'and'
    ;
or  :
    'or'
    ;
not :
    'not' classExpression
    ;


iri : IRI

    ;
literal
    : QUOTED_STRING
    ;
existential :
    ('(')?
    roleIri ('='|some)
    classOrDataType
    (')')?
    ;
roleIri :
    'role' (':')? iri
        ;

propertyRestriction :
    propertyIri
    (exactCardinality
    |rangeCardinality
    |minCardinality
    |maxCardinality
    |some
    |only)?
    classOrDataType


     ;
some : 'some'
    ;

only : 'only'
    ;

 propertyIri:
    iri
    ;
 exactCardinality   :
    'exactly' INTEGER
    ;
 rangeCardinality :
   minCardinality maxCardinality
    ;

minCardinality :
  'min' INTEGER
  ;
maxCardinality :
  'max' INTEGER
  ;


classOrDataType :
    iri
    ;
 name : 'name'
    ;
 description : 'description'
    ;
 code : 'code'
    ;

WS
   : ([\t\r\n\u000C] | ' ') + -> skip
   ;
// lexer

fragment A:('a'|'A');
fragment B:('b'|'B');
fragment C:('c'|'C');
fragment D:('d'|'D');
fragment E:('e'|'E');
fragment F:('f'|'F');
fragment G:('g'|'G');
fragment H:('h'|'H');
fragment I:('i'|'I');
fragment J:('j'|'J');
fragment K:('k'|'K');
fragment L:('l'|'L');
fragment M:('m'|'M');
fragment N:('n'|'N');
fragment O:('o'|'O');
fragment P:('p'|'P');
fragment Q:('q'|'Q');
fragment R:('r'|'R');
fragment S:('s'|'S');
fragment T:('t'|'T');
fragment U:('u'|'U');
fragment V:('v'|'V');
fragment W:('w'|'W');
fragment X:('x'|'X');
fragment Y:('y'|'Y');
fragment Z:('z'|'Z');


EQ  : '='
    ;



DISJOINT    : D I S J O I N T W I T H
    ;



INTEGER : DIGIT+
    ;
DOUBLE : DIGIT+ ('.' DIGIT+)?
    ;

DIGIT : [0-9];



PN_CHARS_BASE
   : 'A' .. 'Z' | 'a' .. 'z' | '\u00C0' .. '\u00D6' | '\u00D8' .. '\u00F6' | '\u00F8' .. '\u02FF' | '\u0370' .. '\u037D' | '\u037F' .. '\u1FFF' | '\u200C' .. '\u200D' | '\u2070' .. '\u218F' | '\u2C00' .. '\u2FEF' | '\u3001' .. '\uD7FF' | '\uF900' .. '\uFDCF' | '\uFDF0' .. '\uFFFD'
   ;
PN_CHARS_U
   : PN_CHARS_BASE | '_'
   ;
PN_CHARS
   : PN_CHARS_U | '-' | [0-9] | '\u00B7' | [\u0300-\u036F] | [\u203F-\u2040]
   ;



IRI
   : '<' (PN_CHARS | '.' | ':' | '/' | '\\' | '#' | '@' | '%' | '&' | UCHAR)* '>'
    |((PN_CHARS)*? ':' (PN_CHARS_U | ':' | [0-9] | PLX) ((PN_CHARS | '.' | ':' | PLX)* (PN_CHARS | ':' | PLX)))
    (PIPED_STRING)?
   ;

UCHAR
   : '\\u' HEX HEX HEX HEX | '\\U' HEX HEX HEX HEX HEX HEX HEX HEX
   ;



PLX
   : PERCENT | PN_LOCAL_ESC
   ;
PERCENT
   : '%' HEX HEX
   ;


ECHAR
   : '\\' [tbnrf"'\\]
   ;


QUOTED_STRING :
   STRING_LITERAL_QUOTE|STRING_LITERAL_SINGLE_QUOTE
   ;

STRING_LITERAL_QUOTE
   : '"' (~ ["\\\r\n] | '\'' | '\\"')* '"'
   ;
STRING_LITERAL_SINGLE_QUOTE
   : '\'' (~ [\u0027\u005C\u000A\u000D] | ECHAR | UCHAR | '"')* '\''
   ;

PIPED_STRING :
    '|' (~ [\u0027\u005C\u000A\u000D] | ECHAR | UCHAR | '"')+ '|'
    ;


 PN_LOCAL_ESC
    : '\\' ('_' | '~' | '.' | '-' | '!' | '$' | '&' | '\'' | '(' | ')' | '*' | '+' | ',' | ';' | '=' | '/' | '?' | '#' | '@' | '%')
    ;
HEX
   : [0-9] | [A-F] | [a-f]
   ;

