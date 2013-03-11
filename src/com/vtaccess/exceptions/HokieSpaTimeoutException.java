package com.vtaccess.exceptions;

/**
 * Exception that is thrown by an object that requires an active session with HokieSpa. 
 * This exception indicates that HokieSpa session that was thought to be active, has timed out and
 * is no longer active, AND that attempts to establish a new session failed.
 * 
 * @author Ethan Gaebel (egaebel)
 *
 */
public class HokieSpaTimeoutException extends Exception {

    //~Constants----------------------------------------------
    /**
     * 
     */
    private static final long serialVersionUID = -7964026067660372334L;
    
    private static final String STANDARD_ERROR_MESSAGE = "Your HokieSpa login session timed out! Re-login!";


    //~Data Fields--------------------------------------------


    //~Constructors--------------------------------------------
    public HokieSpaTimeoutException() {
        
        super(STANDARD_ERROR_MESSAGE);
    }

    //~Methods-------------------------------------------------
}
