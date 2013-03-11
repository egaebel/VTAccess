package com.vtaccess.exceptions;

/**
 * Exception that is thrown when an invalid area is inputted by the user.
 * 
 * Strings identifying areas must be either: 01->07, 1->7, or 1W. Otherwise
 * this exception is thrown.
 * 
 * @author Ethan Gaebel (egaebel)
 *
 */
public class InvalidAreaException extends Exception {

    //~Constants----------------------------------------------

    private static final long serialVersionUID = 8127768223563281963L;
    
    private static final String STANDARD_ERROR_MESSAGE = "The value you inputted as an 'Area' is invalid! You entered: ";

    //~Data Fields--------------------------------------------


    //~Constructors--------------------------------------------
    public InvalidAreaException(String areaNumber) {
        
        super(STANDARD_ERROR_MESSAGE + areaNumber);
    }


    //~Methods-------------------------------------------------
}
