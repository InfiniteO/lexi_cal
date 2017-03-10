package ca.example.calculator;


public class Token
{
  public static final int PLUSMINUS = 1;
  public static final int MATH = 2;
  public static final int LET = 3;
  public static final int OPEN_BRACKET = 4;
  public static final int CLOSE_BRACKET = 5;
  public static final int NUMBER = 6;
  public static final int VARIABLE = 7;
  public static final int COMMA = 8;


  public final int token;
  public final String sequence;

  public Token(int token, String sequence)
  {
    super();
    this.token = token;
    this.sequence = sequence;
  }
}
