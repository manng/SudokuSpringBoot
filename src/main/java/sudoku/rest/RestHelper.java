package sudoku.rest;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.json.stream.JsonParsingException;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.xml.sax.SAXException;

import sudoku.exceptions.InvalidInputDataException;
import sudoku.exceptions.InvalidJSONFileException;
import sudoku.exceptions.UnknownXMLElementException;
import sudoku.io.Input;
import sudoku.items.Constraints;
import sudoku.solver.Solver;

/**
 * This class contains generic top-level methods which help the REST interface.  They call the top-level methods in
 * the file handling and solver packages and return the appropriate result or exception.  All the methods in this
 * class return a Response object which will be sent to the REST client.
 * 
 * @author Gary Mann
 *
 */
public class RestHelper {
	
	//set this to true if you want running results printed to the console
	private static boolean PRINT_OUTPUT_WANTED = false;

/**
 * General method for validating input data without calculating the result.  If the
 * input data are valid, the input data are returned in the body of the response
 * 
 * @param input					the input data
 * @param inputFileFormat		the format of the input data
 * @throws ResponseStatusException
 */
	public static void validateInput(String input, int inputFileFormat) throws ResponseStatusException {
		try {
			StringReader reader = new StringReader(input);
			Constraints constraints = Input.initializeConstraints(reader, inputFileFormat);
			Solver.validateInitialConstraints(constraints);
			reader.close();
		} catch (ArrayIndexOutOfBoundsException aie) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid initial constraints: " +  aie.getMessage(), aie);
		} catch (JsonParsingException jpe) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "JSON parse exception: " + jpe.getMessage(), jpe);
		} catch (IOException ioe) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "I/O exception: " + ioe.getMessage(), ioe);
		} catch (XMLStreamException xmle) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "XML parsing exception: " + xmle.getMessage(), xmle);
		} catch (InvalidJSONFileException ijfe) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid JSON exception: " + ijfe.getMessage(), ijfe);			
		} catch (UnknownXMLElementException uxle) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown XML element exception: " + uxle.getMessage(), uxle);
		} catch (InvalidInputDataException iide) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid initial constraints: " +  iide.getMessage(), iide);
		} catch (JAXBException jaxbe) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "JAXB Exception: " +  jaxbe.getMessage(), jaxbe);
		} catch (ParserConfigurationException pce) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Parser Configuration Exception: " +  pce.getMessage(), pce);
		} catch (SAXException saxe) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "SAX Exception: " +  saxe.getMessage(), saxe);
		}
	}

/**
 * General method which calculates the result from input constraints without validating the constraints.
 * 
 * @param input					the input data
 * @param inputFileFormat		the format of the input data
 * @param outputFileFormat		the format of the output data
 * @return						string to be sent to the client
 * @throws ResponseStatusException
 */
	public static String handleInputWithoutValidating (String input, 
			                                           int inputFileFormat, 
			                                           int outputFileFormat) 
	throws ResponseStatusException {
		try {
			String output = null;
			StringReader reader = new StringReader(input);
			StringWriter writer = new StringWriter();
		    Constraints constraints = Input.initializeConstraints(reader, inputFileFormat);
		    if(Solver.run(constraints, writer, outputFileFormat, PRINT_OUTPUT_WANTED)) {
		    	output = writer.toString();
			} else {
				reader.close();
				writer.close();
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Solver failed to find a solution");
			}
		    reader.close();
		    writer.close();
		    return output;
		} catch (ArrayIndexOutOfBoundsException aie) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid initial constraints: " +  aie.getMessage(), aie);
		} catch (JsonParsingException jpe) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "JSON parse exception: " + jpe.getMessage(), jpe);
		} catch (IOException ioe) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "I/O exception: " + ioe.getMessage(), ioe);
		} catch (XMLStreamException xmle) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "XML parsing exception: " + xmle.getMessage(), xmle);
		} catch (InvalidJSONFileException ijfe) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid JSON exception: " + ijfe.getMessage(), ijfe);			
		} catch (UnknownXMLElementException uxle) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown XML element exception: " + uxle.getMessage(), uxle);
		} catch (InvalidInputDataException iide) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid initial constraints: " +  iide.getMessage(), iide);
		} catch (JAXBException jaxbe) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "JAXB Exception: " +  jaxbe.getMessage(), jaxbe);
		} catch (ParserConfigurationException pce) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Parser Configuration Exception: " +  pce.getMessage(), pce);
		} catch (SAXException saxe) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "SAX Exception: " +  saxe.getMessage(), saxe);
		}
	}

/**
 * General method which takes the REST input data, calls the solver and outputs the 
 * result.
 * 
 * The Response code is 200 for a successful calculation, 500 for a calculation
 * which fails to calculate a result, or 400 if the input file is incorrectly
 * formatted.
 * 
 * @param input					the input data from the REST feed
 * @param inputFileFormat		the format of the input data
 * @param outputFileFormat		the format of the output data
 * @return						string which sends the result to the client
 * @throws ResponseStatusException
 */
	public static String handleInput(String input, 
			                           int inputFileFormat, 
			                           int outputFileFormat) 
	throws ResponseStatusException {
		try {
			String output = null;
			StringReader reader = new StringReader(input);
			StringWriter writer = new StringWriter(); 
			Constraints constraints = Input.initializeConstraints(reader, inputFileFormat);
			Solver.validateInitialConstraints(constraints);
			if (Solver.run(constraints, writer, outputFileFormat, PRINT_OUTPUT_WANTED)) {
				output = writer.toString();
			} else {
				reader.close();
				writer.close();
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Solver failed to find a solution");
			}
			reader.close();
			writer.close();
			return output;
		} catch (ArrayIndexOutOfBoundsException aie) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid initial constraints: " +  aie.getMessage(), aie);
		} catch (JsonParsingException jpe) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "JSON parse exception: " + jpe.getMessage(), jpe);
		} catch (IOException ioe) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "I/O exception: " + ioe.getMessage(), ioe);
		} catch (XMLStreamException xmle) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "XML parsing exception: " + xmle.getMessage(), xmle);
		} catch (InvalidJSONFileException ijfe) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid JSON exception: " + ijfe.getMessage(), ijfe);			
		} catch (UnknownXMLElementException uxle) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown XML element exception: " + uxle.getMessage(), uxle);
		} catch (InvalidInputDataException iide) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid initial constraints: " +  iide.getMessage(), iide);
		} catch (JAXBException jaxbe) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "JAXB Exception: " +  jaxbe.getMessage(), jaxbe);
		} catch (ParserConfigurationException pce) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Parser Configuration Exception: " +  pce.getMessage(), pce);
		} catch (SAXException saxe) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "SAX Exception: " +  saxe.getMessage(), saxe);
		}
	}

}
