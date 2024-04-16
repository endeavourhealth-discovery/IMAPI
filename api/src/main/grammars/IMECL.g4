grammar IMECL;
import ECL;
imecl :
   prefixes? ws expressionconstraint EOF
   ;

prefixes :
    (ws prefixDecl)+
    ;

prefixDecl:
   (CAP_P|P) (CAP_R|R) (CAP_E|E) (CAP_F|F) (CAP_I| I) (CAP_X | X)
   ws
   pname? COLON ws iri ws
   ;
pname
    : (alpha|digit)+
    ;
iri :
    ( digit| alpha| SLASH | HASH | PERIOD | DASH | DOLLAR | PERCENT | UNDERSCORE | COLON )+
    ;
conceptid :
    (sctid | iri) // enable iris as well as snomed digits
 ;


 //override ecl defaults
 eclrefinement : (subrefinement | conjunctionrefinementset | disjunctionrefinementset);
 conjunctionrefinementset : subrefinement (ws conjunction ws subrefinement)+;
 disjunctionrefinementset : subrefinement (ws disjunction ws subrefinement)+;
 eclattributeset : (subattributeset | conjunctionattributeset | disjunctionattributeset)?;
 conjunctionattributeset : subattributeset (ws conjunction ws subattributeset)+;
 disjunctionattributeset : subattributeset (ws disjunction ws subattributeset)+;




