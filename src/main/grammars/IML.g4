grammar IML;

// Parser Rules
iml : definition+ assignment? prefix? default? EOF;
prefix: PREFIX OPEN_BRACE (PNAME_NS IRI)+ CLOSE_BRACE;
default: DEFAULT EQ IRI;
definition : DEFINE expression;
expression :IRI AS OPEN_BRACE matchType CLOSE_BRACE;
assignment: ASSIGN OPEN_BRACE (IDENTIFIER EQ iriRef)+ CLOSE_BRACE;
iriRef: (PNAME_NS ':' IDENTIFIER) | IRI;
matchType : MATCH type (match | booleanMatch);
type : IDENTIFIER;
match : orderBy? path ? (matchIn | (from whereClause) | whereClause) keepAs?;
path : ((ARROW|OPTIONAL_ARROW|REVERSE_ARROW) IDENTIFIER)+ ;
orderBy : (LATEST|EARLIEST| LARGEST | SMALLEST) NUMBER?;
matchIn : IN IDENTIFIER;
from  : FROM IDENTIFIER;
keepAs : AS IDENTIFIER;
booleanMatch : disjunctionMatch | conjunctionMatch;
nestedBooleanMatch : OPEN_PAREN booleanMatch CLOSE_PAREN;
disjunctionMatch :  EITHER? (match | nestedBooleanMatch ) (OR (match | nestedBooleanMatch))+;
exclusionMatch : EXCLUDE IF? (match|nestedBooleanMatch);
conjunctionMatch : AND? (match | nestedBooleanMatch) ((AND (match|nestedBooleanMatch))|exclusionMatch)+;
disjunctionWhere : EITHER? (where|nestedBooleanWhere)  (OR (where|nestedBooleanWhere))+;
conjunctionWhere : AND? (where | nestedBooleanWhere) (AND (where|nestedBooleanWhere))+;
nestedBooleanWhere : OPEN_PAREN booleanWhere CLOSE_PAREN;
booleanWhere : disjunctionWhere| conjunctionWhere;
whereClause : WHERE (booleanWhere | where);
where : property (whereRange|whereValue|whereIs);
property : IDENTIFIER |(NODEREF IDENTIFIER);
propertyOf : property OF IDENTIFIER;
whereIs : (OF|IS|IN|EQ) (concept | disjunctionConcept);
concept : IDENTIFIER;
disjunctionConcept : IDENTIFIER (',' IDENTIFIER)+;
whereRange : BETWEEN  comparisonOperator NUMBER AND comparisonOperator NUMBER units? whereRelativeTo?;
units : IDENTIFIER;
whereRelativeTo : RELATIVE TO PARAMETER;
whereValue : comparisonOperator ((NUMBER units? whereRelativeTo?) | propertyOf);
functionExpression: methodCall (whereRange|whereValue|whereIs);
methodCall
    : property '(' argumentList? ')'
    ;
argumentList
    : IDENTIFIER (',' IDENTIFIER)*
    ;
comparisonOperator : GT | GTE | LT | LTE | EQ | NEQ;
simpleExpression : (ON|IN)? IDENTIFIER;

// Lexer Rules

// Case-insensitive keywords
DEFINE : D E F I N E;
PREFIX : P R E F I X;
DEFAULT : D E F A U L T;
ASSIGN : A S S I G N;
AS : A S;
MATCH : M A T C H;
FROM : F R O M;
EITHER : E I T H E R;
OR : O R;
AND : A N D;
EXCLUDE : E X C L U D E;
IF : I F;
OF : O F;
IS : I S;
IN : I N;
ON: O N;
BETWEEN : B E T W E E N;
WITH : W I T H;
WHERE : W H E R E;
LATEST : L A T E S T;
EARLIEST : E A R L I E S T;
LARGEST : L A R G E S T;
SMALLEST : S M A L L E S T;
RELATIVE : R E L A T I V E;
TO: T O;
// Symbols
OPEN_BRACE : '{';
CLOSE_BRACE : '}';
OPEN_PAREN : '(';
CLOSE_PAREN : ')';
ARROW : '->';
OPTIONAL_ARROW : '?->';
REVERSE_ARROW : '<-';
COMMA : ',';
GT : '>';
GTE : '>=';
LT : '<';
LTE : '<=';
EQ : '=';
NEQ : '!=';
PNAME_NS : IDENTIFIER ':';
IDENTIFIER : [a-zA-Z][a-zA-Z0-9_/-]+;
NODEREF : IDENTIFIER '.';
PARAMETER : '$' IDENTIFIER;
IRI : IDENTIFIER|NODEREF|PNAME_NS  (NODEREF | IDENTIFIER|PNAME_NS| NUMBER|'//'| '\\' |'#' | '@' | '%' | '&' )+;
NUMBER : '-'? [0-9]+;
WS : [ \t\r\n]+ -> skip;
COMMENT : '//' ~[\r\n]* -> skip;
BLOCK_COMMENT : '/*' .*? '*/' -> skip;
INDENT_START  : '>>'    -> channel(HIDDEN) ;
INDENT_END    : '<<' -> channel(HIDDEN) ;
// Case-insensitive character fragments
fragment A: [aA];
fragment B: [bB];
fragment C: [cC];
fragment D: [dD];
fragment E: [eE];
fragment F: [fF];
fragment G: [gG];
fragment H: [hH];
fragment I: [iI];
fragment J: [jJ];
fragment K: [kK];
fragment L: [lL];
fragment M: [mM];
fragment N: [nN];
fragment O: [oO];
fragment P: [pP];
fragment Q: [qQ];
fragment R: [rR];
fragment S: [sS];
fragment T: [tT];
fragment U: [uU];
fragment V: [vV];
fragment W: [wW];
fragment X: [xX];
fragment Y: [yY];
fragment Z: [zZ];
