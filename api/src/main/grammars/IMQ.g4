grammar IMQ;
queryRequest : context? arguments? (query | pathQuery);
context : '@context' prefixMap ;
prefixMap : (prefix iri)+;
prefix : PN_PREFIX  IRIREF;
arguments : 'argument' argument+;
argument : parameter (value| valueIri| valueIriList| valueList);
parameter : 'parameter' STRING;
value : 'value' STRING;
valueIri : 'valueIri' IRIREF;
valueIriList : 'valueIriList' IRIREF+;
valueList : 'valueList' STRING+;
query  : 'query' queryDefinition;
queryDefinition: iriRef? description? match+ (query)*? return? orderBy? groupBy?;
iriRef: prefixedIri | iri;
prefixedIri : PN_PREFIX? ':' PN_CHARS+ ;
iri : '@id' IRIREF;
description: 'description' STRING;
match: 'match' exclude? (type | set | instance)? graph? (boolMatch match+) path? boolWhere? orderBy? ;

exclude : 'exclude';
return: ' ';

orderBy: ' ';

groupBy: ' ';

pathQuery : 'pathQuery' pathQueryDefinition;
pathQueryDefinition: ' ';




UCHAR
   : '\\u' HEX HEX HEX HEX | '\\U' HEX HEX HEX HEX HEX HEX HEX HEX
   ;

STRING
   : '"' (~ ["\\\r\n] | '\'' | '\\"')* '"'
   ;
IRIREF
   : '<' (PN_CHARS | '.' | ':' | '/' | '\\' | '#' | '@' | '%' | '&' | UCHAR)* '>'
   ;

PN_CHARS_BASE
   : 'A' .. 'Z' | 'a' .. 'z' | '\u00C0' .. '\u00D6' | '\u00D8' .. '\u00F6' | '\u00F8' .. '\u02FF' | '\u0370' .. '\u037D' | '\u037F' .. '\u1FFF' | '\u200C' .. '\u200D' | '\u2070' .. '\u218F' | '\u2C00' .. '\u2FEF' | '\u3001' .. '\uD7FF' | '\uF900' .. '\uFDCF' | '\uFDF0' .. '\uFFFD'
   ;
PN_CHARS_U
   : PN_CHARS_BASE | '_'
   ;
PN_CHARS
   : PN_CHARS_U | '-' | [0-9] | '\u00B7' | [\u0300-\u036F] | [\u203F-\u2040]
   ;
PN_LOCAL
   : (PN_CHARS_U | ':' | [0-9] | PLX) ((PN_CHARS | '.' | ':' | PLX)* (PN_CHARS | ':' | PLX))?
   ;

PN_PREFIX
   : PN_CHARS_BASE ((PN_CHARS | '.')* PN_CHARS)?
   ;

PLX
   : PERCENT | PN_LOCAL_ESC
   ;
PERCENT
   : '%' HEX HEX
   ;
HEX
   : [0-9] | [A-F] | [a-f]
   ;
PN_LOCAL_ESC
   : '\\' ('_' | '~' | '.' | '-' | '!' | '$' | '&' | '\'' | '(' | ')' | '*' | '+' | ',' | ';' | '=' | '/' | '?' | '#' | '@' | '%')
   ;


WS
   : ([\t\r\n\u000C] | ' ') + -> skip
   ;


