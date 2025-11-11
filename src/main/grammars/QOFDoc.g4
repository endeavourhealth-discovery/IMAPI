grammar QOFDoc;

expression
    : orExpression EOF
    ;

orExpression
    : andExpression (OR andExpression)*
    ;

andExpression
    : notExpression (AND notExpression)*
    ;

notExpression
    : NOT? comparisonExpression
    ;

comparisonExpression
    : 'If'? term (COMPARISON_OPERATOR term)?
    ;

term
    : IDENTIFIER
    | STRING_LITERAL
    | NUMBER
    | DATE
    | LPAREN orExpression RPAREN
    | FUNCTION_NAME LPAREN (term (COMMA term)*)? RPAREN
    | NULL
    | term OPERATOR term  // Handle arithmetic
    ;

// Lexer Rules
OR          : 'OR' | 'or';
AND         : 'AND' | 'and';
NOT         : 'NOT' | 'not';
COMMA       : ',';
LPAREN      : '(';
RPAREN      : ')';
OPERATOR    : ('+' | '–' | '-');
COMPARISON_OPERATOR
            : '=' | '!=' | '≠' |'<>' | '<' | '>' | '<=' | '>=' | 'LIKE' | 'NOT LIKE' | 'on each'
            ;
IDENTIFIER  : ('{' | '[')? [a-zA-Z_][a-zA-Z0-9_.]* ('}' | ']')?;
NUMBER      : OPERATOR? DIGIT+ ('.' DIGIT+)? (WS? UNIT)?;
UNIT        : 'years' | 'months' | '%';
DATE        : DIGIT DIGIT '/' DIGIT DIGIT '/' DIGIT DIGIT DIGIT DIGIT;
STRING_LITERAL
            : '\'' ( ~('\'' | '\\') | '\\' . )* '\'';
FUNCTION_NAME
            : 'Latest of';
NULL        : 'Null' | 'null';
WS          : [ \t\r\n]+ -> skip ;

fragment DIGIT : [0-9];
