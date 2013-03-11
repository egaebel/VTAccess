package com.vtaccess.exceptions;

/**
 * Exception that gets thrown when the logging into hokiespa fails due to
 * incorrect login information
 * 
 * @author Ethan Gaebel (egaebel)
 * 
 */
public class WrongLoginException extends Exception {

    // ~Data Fields--------------------------------------------
    /**
     * the required serialVersionUID for this exception
     * 
     * auto-generated
     */
    private static final long serialVersionUID = 486289746368413764L;
    
    private static final String STANDARD_ERROR_MESSAGE = "Your login information is incorrect!";

    // ~Constructors--------------------------------------------
    public WrongLoginException() {
        
        super(STANDARD_ERROR_MESSAGE);
    }


    // ~Methods--------------------------------------------
}
