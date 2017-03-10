package ca.example.calculator;

public class ParserException extends Exception {


	private static final long serialVersionUID = 322468815439357588L;
	private Token token = null;
	
	public ParserException(String string) {
		super(string);
	}

	public ParserException(String string, Token token)
	  {
	    super(string);
	    this.token = token;
	  }
	
	  public String getMessage()
	  {
	    String msg = super.getMessage();
	    if (token != null)
	    {
	      msg = msg.replace("%s", token.sequence);
	    }
	    return msg;
	  }
}
