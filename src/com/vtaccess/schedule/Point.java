package com.vtaccess.schedule;

/**
 * Represents a point on the x,y plane, has x and y int objects, and getters and setters
 * 
 * also has toString and equals methods for printing and testing purposes
 * 
 * @author ethan
 */
public class Point {

    //~DataFields-------------------------------------------------------------
    /**
     * x coordinate of the point
     */
    public int x;
    
    /**
     * y coordinate of the point
     */
    public int y;

    //~Constructors-----------------------------------------------------------
    /**
     * default constructor that doesnt initialize anything
     */
    public Point() {
        
    }
    
    /**
     * initializes the x,y cords to passed in values
     */
    public Point (int newX, int newY) {
        
        setX(newX);
        setY(newY);
    }
    
    //~Methods----------------------------------------------------------------
    /**
     * @param x the x to set
     */
    public void setX(int x) {

        this.x = x;
    }

    /**
     * @return the x
     */
    public int getX() {

        return x;
    }

    /**
     * @param y the y to set
     */
    public void setY(int y) {

        this.y = y;
    }

    /**
     * @return the y
     */
    public int getY() {

        return y;
    }
    
    /**
     * determines if one point object is equal to another
     * @param thing the point object to compare this one to
     * @return value true if equal, false otherwise
     */
    @Override
    public boolean equals(Object thing) {
        
        boolean value = false;
        
        if (thing != null) {
            
            if (thing instanceof Point) {
                
                if (((Point) thing).getX() == this.x && ((Point) thing).getY() == this.y) {
                   
                    value = true;
                }
            }
        }
        
        return value;
    }
    
    /**
     * returns the x,y cords in the format (x, y)
     * 
     * @return the xy cords in this format -- (x, y)
     */
    @Override
    public String toString() {
        
        return x + " " + y;
    }
}