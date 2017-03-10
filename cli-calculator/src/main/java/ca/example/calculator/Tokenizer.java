package ca.example.calculator;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Tokenizer {

	private static final Logger LOG = LoggerFactory.getLogger(Tokenizer.class);
	
	 // Inner class to maintain the regex for each token 
	  private class TokenInfo {
		    public final Pattern regex;
		    public final int token;

		    public TokenInfo(Pattern regex, int token) {
		      super();
		      this.regex = regex;
		      this.token = token;
		    }
	  }
	  
	  // Maintain a regex list given by the caller add(String regex, int token) 
	  private LinkedList<TokenInfo> tokenInfos;
	  private LinkedList<Token> tokens;

	  public Tokenizer() {
		  tokenInfos = new LinkedList<TokenInfo>();
		  tokens = new LinkedList<Token>();
	}
	  
	  // Compile the pattern list
	  public void add(String regex, int token) {
		  tokenInfos.add(
		  new TokenInfo(
		  Pattern.compile("^(\\s?"+regex+")"), token));
		}
		
	  public void tokenize(String str) throws ParserException {
		  
		  String s = new String(str);
		  tokens.clear();
		  String tok;
		  /*for (TokenInfo info : tokenInfos) {
			  LOG.debug(info.regex.pattern());
		  }*/
		  
		  while (!s.equals("")) {
			    boolean match = false;
			    //Look up in the TokenInfos list to find a matching token
			    for (TokenInfo info : tokenInfos) {
			        Matcher m = info.regex.matcher(s);
			        // If match is found and is not a COMMA (,), add it to Tokens list 
			        if (m.find()) {
			          match = true;
			          tok = m.group().trim();
			          LOG.debug("Token match found '{}' ", m.group());
			          if(info.token != Token.COMMA){			          
			        	  tokens.add(new Token(info.token, tok));
			          }
			          s = m.replaceFirst("");
			          break;
			        }
			      }
			    // Remove leading white space between tokens 
			    s = s.trim();
			    LOG.debug("tokenize(): next string '{}'", s);
			    if (!match) throw new ParserException(
			            "Unexpected character in input: "+s);
		}
	}

public LinkedList<Token> getTokens() {
	  return tokens;
	}

}
