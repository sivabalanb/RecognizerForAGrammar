import java.io.*;
import java.util.Scanner;
//--------------------------------------------
// Sivabalan Balasubramanian
// Recognizer for simple expression grammar
// 
// to run on Athena (linux) -
//    save as:  Recognizer.java
//    compile:  javac Recognizer.java
//    execute:  java Recognizer
/*    Sample INPUT 
PX:V;Y:V;BI(0<1)@&E;$
PX:V;Y:V;BW(1=1)LTE;$
PX:V;Y:V;BX~0;E;$
PX:V;Y:V;BE;$
PX:V;BE;$
PBE;$ */
//
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
//--------------------------------------------

public class Recognizer
{
  static String inputString;
  static int index = 0;
  static int errorflag = 0;

  private char token()
  { 
    return(inputString.charAt(index)); 
  }

  private void advancePtr()
  { 
    if (index < (inputString.length()-1)) 
      { index++; }
  }

  private void match(char T)
  { 
    if (T == token()) 
      { 
//        System.out.format("\n Inside Match Loop: %s" , T);
          advancePtr(); }
    else 
      {
//        System.out.format("\n Inside Match in ELSE part: %s" , T);
        error();

      } 
  }

  private void error()
  {
    System.out.println("\nError at position: " + index);
    errorflag = 1;
    advancePtr();
  }
//----------------------
// GRAMMAR Functions
  private void digit()
  { 
    if ((token() == '0') || (token() == '1')) 
      { match(token()); }
    else error(); 
  }

  private void letter()
  { 
    //System.out.print("Inside LETTER:");
    if ((token() == 'X') || (token() == 'Y') || (token() == 'Z')) 
      { match(token()); 
//        System.out.print("\nEND of LETTER block \n");
      }
    else error(); 
  }

  private void integer()
  { 
    digit(); 
    while ((token() == '0') || (token() == '1')) 
      { digit(); }
  }

  private void chara()
  { 
    if ((token() == '0') || (token() == '1')) 
      { digit(); }
    else digit();
  }

  private void ident()
  { 
   // System.out.format("\nInside IDENT \n");
    if ((token() == 'X') || (token() == 'Y') || (token() == 'Z')) 
      { letter(); }
    else chara(); 
  }

  private void opratr()
  { 
    if ((token() == '<') || (token() == '>') || (token() == '=') || (token() == '!')) 
      { match(token()); }
    else error();
  }
 
  private void oprnd()
  { if(token()=='(')
      { match('(');
        exprsn();
        match(')');
      }
    else if ((token() == '0') || (token() == '1')) 
      { digit(); }
    else 
      { letter(); }
  }


//          factor ::= oprnd {* oprnd}
 private void factor()
  {
    oprnd();
    while (token() == '*') 
    {
      match('*');
      oprnd();  
    }
  }
//          exprsn ::= factor {+ factor}
  
  private void exprsn()
  {
    factor();
    while (token() == '+')
    {
      match('+');
      factor();
    }
  }

  private void comprsn()
  {
      match('(');
      oprnd();
      opratr();
      oprnd();
      match(')');
  }

  private void output()
  {
    match('O');
    ident();
    while(token() ==',')
    {
      match(',');
      ident();
    }
    match(';');
  }
//read ::= R ident {, ident } ;
  private void read()
  {
    match('R');
    ident();
    while(token() ==',')
    {
      match(',');
      ident();
    }
    match(';');
  }

  //loop ::= W comprsn L {statmt} T

  private void loop()
  {
    match('W');
    comprsn();
    match('L');
    while((token() == 'X') || (token() == 'Y') || (token() == 'Z') || (token() == 'I') || (token() == 'R' ) || (token() == 'O') || (token() == 'W' ))
    {
      statmt();
    }
    match('T');
  }
  //ifstmt ::= I comprsn @ {statmt} [% {statmt}] &

  private void ifstmt()
  {
    match('I');
    comprsn();
    match('@');
    while((token() == 'X') || (token() == 'Y') || (token() == 'Z') || (token() == 'I') || (token() == 'R' ) || (token() == 'O') || (token() == 'W' ))
      {
          statmt();
      } 
    if (token()=='%')
      {
        match('%');
        while((token() == 'X') || (token() == 'Y') || (token() == 'Z') || (token() == 'I') || (token() == 'R' ) || (token() == 'O') || (token() == 'W' ))
        {statmt();}
      }
      match('&');
  }
  //assnmt ::= ident ~ exprsn ;
  private void assnmt()
  {
    ident();
    match('~');
    exprsn();
    match(';');
  }
  //statmt ::= assnmt | ifstmt | loop | read | output
  private void statmt()
  {
    switch(token())
      {
        case 'X':
           assnmt();
           break;
        case 'Y':
            assnmt();
            break;
        case 'Z':
            assnmt();
            break;
        case 'I':
            ifstmt();
            break;
        case 'W':
            loop();
            break;
        case 'R':
            read();
            break;
        case 'O':
            output();
            break;
            default:
        error();
      }

  }
  //declare ::= ident {, ident} : V ;
  private void declare()
  {

//    System.out.println("\nInside DECLARE \n");
    ident();
    while(token() == ',')
    {
      match(',');
      ident();
    }
    match(':');
    match('V');
    match(';');
  }
  //program ::= P {declare} B {statmt} E ;
  private void program()
  {
    match('P');
    while((token() == 'X') || (token() == 'Y') || (token() == 'Z') )
    {
//      System.out.format("\n Inside PROGRAM  \n");
      {
        declare();
      }
    }
    match('B');
    while((token() == 'X') || (token() == 'Y') || (token() == 'Z') || (token() == 'I') || (token() == 'R' ) || (token() == 'O') || (token() == 'W' ))
    {
      statmt();
    }
    match('E');
    match(';');
  }
//-----------------------------
  private void start()
  {
    program();
    match('$');

    if (errorflag == 0)
      System.out.println("\nThe expression is legal." + "\n");
    else
      System.out.println("\nErrors found." + "\n");
  }

//----------------------
  public static void main (String[] args) throws IOException
  {
    Recognizer rec = new Recognizer();

    Scanner input = new Scanner(System.in);

    System.out.print("\n" + "\nEnter an expression: ");
    inputString = input.nextLine();

    rec.start();
  }
}