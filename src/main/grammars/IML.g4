grammar IML;

// Parser Rules
iml : definition EOF;

definition : DEFINE IDENTIFIER AS OPEN_BRACE matchStatement CLOSE_BRACE;


matchStatement : MATCH entity
                 fromStatement?
                 (booleanExpression |expression)*;

entity : VARIABLE? IDENTIFIER;

fromStatement : FROM IDENTIFIER;

booleanExpression : disjunction | conjunction | exclusion;

nestedBooleanExpression : OPEN_PAREN booleanExpression CLOSE_PAREN;

disjunction : EITHER whereStatement (OR whereStatement)+;

conjunction : (AND (whereStatement | nestedBooleanExpression | expression))+;

whereStatement : pathSegment* WHERE expression;

property : IDENTIFIER '.' IDENTIFIER;

expression : nestedBooleanExpression
           | functionExpression
           | conceptExpression
           | simpleExpression;

exclusion : (EXCLUDE IF expression)+;


pathSegment : (ARROW|OPTIONAL_ARROW) IDENTIFIER)+ ;

conceptExpression : property (OF|IS|IN|EQ) IDENTIFIER;

rangeExpression : IDENTIFIER range;

range : BETWEEN  comparisonOperator NUMBER AND comparisonOperator NUMBER;

valueCompare : comparisonOperator NUMBER AND comparisonOperator NUMBER;

functionExpression: methodCall (range|valueCompare);

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


// Symbols
OPEN_BRACE : '{';
CLOSE_BRACE : '}';
OPEN_PAREN : '(';
CLOSE_PAREN : ')';
ARROW : '->';
OPTIONAL_ARROW : '?->';
COMMA : ',';
GT : '>';
GTE : '>=';
LT : '<';
LTE : '<=';
EQ : '=';
NEQ : '!=';

VARIABLE : IDENTIFIER ':';
IDENTIFIER : [a-zA-Z][a-zA-Z0-9_]+;
NUMBER : [0-9]+;
WS : [ \t\r\n]+ -> skip;
COMMENT : '//' ~[\r\n]* -> skip;
BLOCK_COMMENT : '/*' .*? '*/' -> skip;

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
