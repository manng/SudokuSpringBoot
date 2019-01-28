package sudoku.rest.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import sudoku.rest.RestHelper;
import sudoku.util.FileFormat;

/**
 * This is the REST server class.  It receives REST POST calls from the client, sends the 
 * data to the solver which calculates the result, and send the results back to the client.
 * 
 * This class only contains method which can be directly annotated with REST calls (always
 * POST in this application).  All its methods use the RestHelper class to call the solver.
 * The main role of this class is to determine the file format from the Produces and
 * Consumes annotations.
 * 
 * @author Gary Mann
 *
 */
@RestController
@CrossOrigin
@RequestMapping("/sudoku")
public class SudokuController {

/**
 * This feed validates the JSON input data without calculating the result. 
 * 
 * @param input		the JSON input data
 * @return          empty string if no error found
 */
@RequestMapping(path="/validate", 
		        method=RequestMethod.POST,
		        consumes="application/json",
		        produces="application/json") 
	public String validateJsonInput(@RequestBody String input) {
		RestHelper.validateInput(input, FileFormat.JSON);
	    return "";
	}

/**
 * This feed validates XML input data without calculating the result.
 * 
 * @param input		the XML input data
 * @return          empty string if no error found
 */
@RequestMapping(path="/validate", 
                method=RequestMethod.POST,
                consumes= {"application/xml", "text/xml"},
                produces= {"application/xml", "text/xml"}) 
	 public String validateXMLInput(@RequestBody String input) {
		RestHelper.validateInput(input, FileFormat.XML);
	 	return "";
	}

/**
 * This feed validates plain text input data without calculating the result. 
 * 
 * @param input		the plain text input data
 * @return          empty string if no error found
 */
@RequestMapping(path="/validate", 
		        method=RequestMethod.POST,
		        consumes= "text/plain",
		        produces= "text/plain") 
	public String validateTextInput(@RequestBody String input) {
	  	RestHelper.validateInput(input, FileFormat.TEXT);
	 	return "";
	}

/**
 * This feed handles JSON data without validating the initial constraints.
 * 
 * @param input		the input data in JSON format
 * @return			string to be sent to the client
 */
@RequestMapping(path="/handleWithoutValidating", 
		        method=RequestMethod.POST,
		        consumes="application/json",
		        produces="application/json") 
	public String handleJsonInputWithoutValidation(@RequestBody String input)  {
	  	return RestHelper.handleInputWithoutValidating(input, FileFormat.JSON, FileFormat.JSON);
	}

/**
 * This feed handles XML input data without validating the initial constraints.
 * 
 * @param input		input data in XML format
 * @return			string to be sent to the client
 */
@RequestMapping(path="/handleWithoutValidating", 
		        method=RequestMethod.POST,
		        consumes= {"application/xml", "text/xml"},
		        produces= {"application/xml", "text/xml"}) 
	public String handleXmlInputWithoutValidation(@RequestBody String input) {
	   	return RestHelper.handleInputWithoutValidating(input, FileFormat.XML, FileFormat.XML);
	}
	   
/**
 * This feed handles plain text input without validating the initial constraints.
 * 
 * @param input			input data in plain text format
 * @return				string to be sent to the client
 */
@RequestMapping(path="/handleWithoutValidating", 
		        method=RequestMethod.POST,
		        consumes= "text/plain",
		        produces= "text/plain") 
	public String handleTextInputWithoutValidation(@RequestBody String input) {
	   	return RestHelper.handleInputWithoutValidating(input, FileFormat.TEXT, FileFormat.TEXT);
	}
	
/**
 * The feed which handles JSON input from the REST POST requests
 * 
 * @param input		the input data in JSON format
 * @return			string to be sent to the client
 */
@RequestMapping(method=RequestMethod.POST,
		        consumes="application/json",
		        produces="application/json") 
	public String handleJsonInput(@RequestBody String input)  {
		return RestHelper.handleInput(input, FileFormat.JSON, FileFormat.JSON);
	}

/**
 * The feed which handles XML input from the REST POST requests
 * 
 * @param input		the input data in XML format
 * @return			string to be sent to the client
 */
@RequestMapping(method=RequestMethod.POST,
		        consumes= {"application/xml", "text/xml"},
		        produces= {"application/xml", "text/xml"}) 
	public String getXMLInput(@RequestBody String input)  {
		return RestHelper.handleInput(input, FileFormat.XML, FileFormat.XML);
	}

/**
 * The feed which handles plain text input from the REST POST requests
 *  
 * @param input		the input data in plain text format
 * @return			string to be sent to the client
 */
@RequestMapping(method=RequestMethod.POST,
		        consumes= "text/plain",
		        produces= "text/plain") 
@ResponseBody
	public String getTextInput(@RequestBody String input)  {
		return RestHelper.handleInput(input, FileFormat.TEXT, FileFormat.TEXT);
	}

}
