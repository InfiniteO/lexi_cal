package ca.example.calculator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Parser {

	private static final Logger LOG = LoggerFactory.getLogger(Parser.class);
	private LinkedList<Token> tokens;
	private Tokenizer tokenizer;
	private Integer result = 0;
	  
	  Parser(Tokenizer tokenizer){
		  this.tokenizer = tokenizer;
	  }
	  
	  public void parse(String expression) throws ParserException {
		//LOG.debug("Parser calling tokenizer on string input");
	    tokenizer.tokenize(expression);
	    LinkedList<Token> tokens = tokenizer.getTokens();
	    this.parseTokens(tokens);
	  }
	  
	  private void parseTokens(List<Token> tokens) throws ParserException {
		this.tokens = (LinkedList<Token>) tokens;

	    //Evaluate the tokenized expression
	    result = expression();
	    LOG.info("parse(): expression result: {}", result);
	  }
	 
	  
	  private Integer expression() throws ParserException {
	      
	        // Stack for numbers: values, Stack for symbols: syms, Map for variables: vars
	        Stack<Integer> values = new Stack<Integer>();
	        Stack<Token> syms = new Stack<Token>();
	        Map<String, Integer> vars = new LinkedHashMap<String, Integer>();
	        Token tok;
	        Integer value;
	        Token sym;
	        
	        //Parse the tokens and evaluate
	        for (int i = 0; i < tokens.size(); i++) {
	        	tok = tokens.get(i);
	        	LOG.debug("expression(): '{}'", tok.sequence);
	        	
	        	switch (tok.token) {
	        	//Unary +/- before Integer only
	        	case Token.PLUSMINUS:
	        		if(tokens.get(i+1).token == Token.NUMBER) {
		        		values.push(Integer.parseInt( tok.sequence + tokens.get(i+1).sequence));
		        		i++;      	
	        		}
	        		else {
	        			throw new ParserException("Unhandled sequence  '" + tok.sequence + "' found");
	        		}
	        	
	        		break;
	        		
	        	// Plain number literal expression, push it in values stack
	        	case Token.NUMBER:
	        		values.push(Integer.parseInt(tok.sequence));
	        		// Found a name - value pair, put it in the vars LinkedHashMap
	        		if(syms.peek().token == Token.VARIABLE)
	        		{
	        			vars.put(syms.pop().sequence, values.pop());
	        			
	        		}
	        			break;
	      
	        	// Closing bracket -> pop and evaluate until opening bracket symbol
	        	case Token.CLOSE_BRACKET:
	        			        			
	        		// push the result of sub-expression between brackets into values stack
	        			sym = syms.pop();
		        		if(sym.token != Token.OPEN_BRACKET) {
		        			throw new ParserException("Missing pair of open bracket,  '" + sym.sequence + "' found instead");
		        		}
		        		
		        		if(syms.peek().token == Token.LET) {
		        			LOG.debug("expression(): 'LET(' popped");
		        			syms.pop();
		        			  for (String key : vars.keySet()) {
		        				    LOG.debug("expression(): removing name-value '{}'-'{}'",key, vars.get(key));
		        				    vars.remove(key);
		        				    break;
		        				  }
		        			value = values.peek();
		        		}
		        		else {
		        			value = applySym(syms.pop(), values.pop(), values.pop());
		        			values.push(value);
		        		}
		        		LOG.debug("expression(): sub-expression evaluated {}", value);
		        	
		        	if(!syms.isEmpty() && (syms.peek().token == Token.VARIABLE)) {

	        				// Name - expression pair found, add it to vars LinkedHashMap after evaluation
	        				value = values.pop();
	        				sym = syms.pop();
	        				vars.put(sym.sequence, value);
	        				LOG.debug("expression(): name - value expression {}-{}", sym.sequence, value);
	        				
	        		}
	        			break;
	        	
	        	// Push the operation tokens on syms stack
	        	case Token.MATH:
	        	case Token.OPEN_BRACKET:
	        			//LOG.debug("expression(): case {}", tok.sequence);
	        			syms.push(tok);
	        			break;
	        			
	         	case Token.LET:
        			//LOG.debug("expression(): case {}", tok.sequence);
        			syms.push(tok);
	         		break;
	         		
	        	// LET expression where variable is used, assign value to variable and push it in values stack
	        	case Token.VARIABLE:

	        		if(vars.containsKey(tok.sequence)){
	        			values.push(vars.get(tok.sequence));
	        		}
	        		else{
	        			syms.push(tok);
	        		}
	        			break;

	        	default:
	        		throw new ParserException("expression(): Invalid token  '" + tok.token + "' found");
	        	
	        	}
	        }
	        
	        return values.pop();
	 
	  }
	   public static int applySym(Token op, Integer b, Integer a)
	    {
		   LOG.debug("applySym(); token {}, operator1: {}, operator2: {}", op.sequence, a, b);
	        switch (op.sequence)
	        {
	        case "add":
	            return a + b;
	        case "sub":
	            return a - b;
	        case "mult":
	            return a * b;
	        case "div":
	            if (b == 0)
	                throw new
	                UnsupportedOperationException("Cannot divide by zero");
	            return a / b;
	        }
	        return 0;
	    }
	 
}
