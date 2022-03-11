grammar HQL;

process : id? (query | update| delete | create) ;
query : (query|members) return;
members : id+;
update : ;
delete : ;
create : ;
query : 'query' entityType booleanClause|matchClause;
dataset:;
entityType :
    'entityType' iriRef;
id : IRIREF;
name : String;
clause : (name |  iriRef | description)*;
description : String;
booleanClause: 'bool' clause operator (booleanClause | matchClause)+;
operator: 'and' | 'or' | 'not';
matchClause: 'match' matchAndFilter;
matchAndFilter :
     pathTo?
     entityType?
     property?
	 ;

pathTo : IRIREF;
property : IRIREF;

iriRef
    : IRI_REF
    | prefixedName
    ;

prefixedName
    : PNAME_LN
    | PNAME_NS
    ;


PNAME_NS
    : PN_PREFIX? ':'
    ;

PNAME_LN
    : PNAME_NS PN_LOCAL
    ;

PN_PREFIX
    : PN_CHARS_BASE ((PN_CHARS|'.')* PN_CHARS)?
    ;


PN_LOCAL
    : ( PN_CHARS_U | DIGIT ) ((PN_CHARS|'.')* PN_CHARS)?
    ;



String
   : STRING_LITERAL_QUOTE | STRING_LITERAL_SINGLE_QUOTE | STRING_LITERAL_LONG_SINGLE_QUOTE | STRING_LITERAL_LONG_QUOTE
   ;


STRING_LITERAL_LONG_SINGLE_QUOTE
   : '\'\'\'' (('\'' | '\'\'')? ([^'\\] | ECHAR | UCHAR | '"'))* '\'\'\''
   ;
STRING_LITERAL_LONG_QUOTE
   : '"""' (('"' | '""')? (~ ["\\] | ECHAR | UCHAR | '\''))* '"""'
   ;


STRING_LITERAL_QUOTE
   : '"' (~ ["\\\r\n] | '\'' | '\\"')* '"'
   ;
STRING_LITERAL_SINGLE_QUOTE
   : '\'' (~ [\u0027\u005C\u000A\u000D] | ECHAR | UCHAR | '"')* '\''
   ;


IRI_REF
    : '<' ( ~('<' | '>' | '"' | '{' | '}' | '|' | '^' | '\\' | '`') | (PN_CHARS))* '>'
    ;


ECHAR
   : '\\' [tbnrf"'\\]
   ;
UCHAR
   : '\\u' HEX HEX HEX HEX | '\\U' HEX HEX HEX HEX HEX HEX HEX HEX
   ;
HEX
   : [0-9] | [A-F] | [a-f]
   ;

 PN_CHARS_BASE
    : 'A' .. 'Z' | 'a' .. 'z' | '\u00C0' .. '\u00D6' | '\u00D8' .. '\u00F6' | '\u00F8' .. '\u02FF' | '\u0370' .. '\u037D' | '\u037F' .. '\u1FFF' | '\u200C' .. '\u200D' | '\u2070' .. '\u218F' | '\u2C00' .. '\u2FEF' | '\u3001' .. '\uD7FF' | '\uF900' .. '\uFDCF' | '\uFDF0' .. '\uFFFD'
    ;
 PN_CHARS_U
    : PN_CHARS_BASE | '_'
    ;
 PN_CHARS
    : PN_CHARS_U | '-' | [0-9] | '\u00B7' | [\u0300-\u036F] | [\u203F-\u2040];


fragment
DIGIT
    : '0'..'9'
    ;

WS
   : ([\t\r\n\u000C] | ' ') + -> skip
   ;



