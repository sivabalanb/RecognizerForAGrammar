# RecognizerForAGrammar
A program that recognizes whether given grammar is right or wrong


// EBNF Grammar is -
//          program ::= P {declare} B {statmt} E ;
//          declare ::= ident {, ident} : V ;
//          statmt ::= assnmt | ifstmt | loop | read | output
//          assnmt ::= ident ~ exprsn ;
//          ifstmt ::= I comprsn @ {statmt} [% {statmt}] & 
//          loop ::= W comprsn L {statmt} T
//          read ::= R ident {, ident } ;
//          output ::= O ident {, ident } ;
//          comprsn ::= ( oprnd opratr oprnd )
//          exprsn ::= factor {+ factor}
//          factor ::= oprnd {* oprnd}
//          oprnd ::= integer | ident | ( exprsn )
//          opratr ::=<|=|>|!
//          ident ::= letter {char}
//          char ::= letter | digit
//          integer ::= digit {digit}
//          letter ::=X|Y|Z
//          digit ::= 0 | 1
