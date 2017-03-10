package ca.example.calculator;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.logging.log4j.core.config.Configurator;
//import org.slf4j.event.Level;
import ch.qos.logback.classic.Level;

public class Calculator {
	
	private static final Logger LOG = LoggerFactory.getLogger(Calculator.class);
	private String input;
    // *** above is the factory pattern
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		  Tokenizer tokenizer = new Tokenizer();
		  //Build the new Tokenizer regex rules for each token in Token class
		  tokenizer.add("add|sub|mult|div", Token.MATH); // math
		  tokenizer.add("let", Token.LET);
		  tokenizer.add("\\(", Token.OPEN_BRACKET); // open bracket
		  tokenizer.add("\\)", Token.CLOSE_BRACKET); // close bracket
		  tokenizer.add("[+-]+", Token.PLUSMINUS); // plus or minus
		  tokenizer.add("[0-9]+",Token.NUMBER); // integer number
		  tokenizer.add("[a-zA-Z]+", Token.VARIABLE); // variable
		  tokenizer.add(",{1}", Token.COMMA); // comma
		  
		  // Check for debug mode
		  if(args.length >= 1){
			  if(args[0].equals("-d"))
				  setLogLevel();
		  }
		  
		  // Check for input expression string "...."
		 Calculator cal = new Calculator();
		 if(args.length == 2){
			 cal.input = new String(args[2]);
		 }
		 
		 // Run the calculator
		 cal.run(tokenizer);
	}

	public Calculator() {
		LOG.debug("ENTER Calculator()");
		LOG.debug("EXIT Calculator()");
	}
	
	private void run(Tokenizer tokenizer) {
		LOG.debug("ENTER run()");
		
		final File file = new File("./input.txt");
		
		//Make a list of expressions from user input and input.txt file
		try(final FileReader fileReader = new FileReader(file);
		final BufferedReader bufferedReader = new BufferedReader(fileReader)){
			
			List<String> lines = new LinkedList<String>();
			if(input != null){
				lines.add(input);
			}
			
			String line = null;
			
			while((line = bufferedReader.readLine()) != null){
				lines.add(line);
				LOG.info("run(): Expression '{}'", line);
			    Parser parser = new Parser(tokenizer);
			    try
			    {
			       parser.parse(line);			      
			    }
			    catch (ParserException e)
			    {
			    	LOG.error(e.getMessage(), e);
			    }

			}
	
			LOG.info("Number of lines processed: {}", lines.size());
			
		} catch(Exception ex){
			LOG.error(ex.getMessage(), ex);
		}finally{
			// do something
		}
		
		LOG.debug("EXIT run()");
	}
	
	private static void setLogLevel() {
		 // if (Boolean.getBoolean("log4j.debug")) {
			  ch.qos.logback.classic.Logger rootLogger = (ch.qos.logback.classic.Logger)LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
			  rootLogger.setLevel(Level.DEBUG);
		    //Configurator.setLevel(System.getProperty("log4j.logger"), Level.DEBUG);
		//  }
		}

}
