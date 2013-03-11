package com.vtaccess.exceptions;

/**
 * Exception noting that a building is not present in the BuildingGpsMap and needs to be added!
 * 
 * @author Ethan Gaebel (egaebel)
 *
 */
public class BuildingNotFoundException extends Exception {

    /**
     * Serial number for this Exception.
     */
    private static final long serialVersionUID = 2180421775996319152L;
    
    /**
     * Constructor that takes a string that will be used to further identify the error cause.
     * 
     * Most likely to display the building name that was not found.
     */
    public BuildingNotFoundException(String string) {
        
        super(string);
    }
}
