grammar HDSL;

query : profile | dataset;
profile : entityType booleanClause|matchMatchClause;
dataset:;
entityType :
    'entityType' iri;
iri: id name?;
id : IRIREF;
name : String;
clause : (name |  iri | description)*;
description : String;
booleanClause: 'bool' clause operator (booleanClause | matchClause)+;
operator: 'and' | 'or' | 'not';
matchMatchClause: 'match' matchClause;
matchClause :
     pathTo?
     entityType?
     property?
     valueCompare?
     (valueIn+)?
	 (valueNotIn+)?
	  valueRange?
	 valueFunction?
	 valueVar?
	 test?
	 function?
	 notExist
	 ;

pathTo : IRIREF;
property : IRIREF;




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


IRIREF
   :  (PN_CHARS | '.' | ':' | '/' | '\\' | '#' | '@' | '%' | '&' | UCHAR)*
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


WS
   : ([\t\r\n\u000C] | ' ') + -> skip
   ;



