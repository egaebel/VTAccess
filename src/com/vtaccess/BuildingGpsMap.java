package com.vtaccess;

import java.util.HashMap;
import java.util.Map;
import com.vtaccess.exceptions.BuildingNotFoundException;
import com.vtaccess.schedule.Point;

/**
 * Contains a Map of Virginia Tech building GPS coordinates that is 
 * indexed by the building abbreviations of Virginia Tech. 
 * 
 * NOTE: The latitude and longitude are multiplied by 1 million so that they 
 *          can be saved with precision in an int value. Saves space. This is also
 *          how an Android GeoPoint object is saved, so values can be directly transferred
 *          between the two if desired.
 * 
 * OVERRIDING NOTE: This class will need to be overriden in the future as Virginia Tech builds
 *                  more buildings. The correct way to do this is to override setupBuildingGpsMap
 *                  and add puts for the building code and GPS point, 
 *                      i.e. put("MCB", new Point((int) (37.230311 * 1000000), (int) (-80.421771 * 1000000)));
 *                  then call the super class constructor method using super.
 *                      i.e. super();
 *                           *Building additions go here*
 *  
 * @author Ethan Gaebel (egaebel)
 *
 */
public class BuildingGpsMap {

    //~Data Fields--------------------------------------------
    /**
     * A Map with the building abbreviations as the keys
     * and the value as a Point with the latitude and longitude of the
     * building.
     * 
     * NOTE: The latitude and longitude are multiplied by 1 million so that they 
     *          can be saved with precision in an int value. Saves space. This is also
     *          how an Android GeoPoint object is saved, so values can be directly transferred
     *          between the two if desired.
     */
    protected Map<String, Point> buildings;
    
    //~Constructors--------------------------------------------
    /**
     * Assigns the building abbreviations with Points and adds them to the Map
     * 
     */
    public BuildingGpsMap() {
        
        buildings = new HashMap<String, Point>();
        
        buildings.put("MCB", new Point((int) (37.230311 * 1000000), (int) (-80.421771 * 1000000)));
        buildings.put("DER", new Point((int) (37.229064 * 1000000), (int) (-80.425504 * 1000000)));
        buildings.put("HAHN N", new Point((int) (37.228552 * 1000000), (int) (-80.426685 * 1000000)));
        buildings.put("HAHN S", new Point((int) (37.227817 * 1000000), (int) (-80.425686 * 1000000)));
        buildings.put("TORG", new Point((int) (37.229833 * 1000000), (int) (-80.420398 * 1000000)));
        
        buildings.put("RAND", new Point((int) (37.230499 * 1000000), (int) (-80.42338 * 1000000)));
        buildings.put("HOLD", new Point((int) (37.230132 * 1000000), (int) (-80.422069 * 1000000)));
        buildings.put("NOR", new Point((int) (37.229483 * 1000000), (int) (-80.423196 * 1000000)));
        
        buildings.put("HAN", new Point((int) (37.230098 * 1000000), (int) (-80.424044 * 1000000)));
        buildings.put("WHIT", new Point((int) (37.230696 * 1000000), (int) (-80.424719 * 1000000)));
        buildings.put("PAT", new Point((int) (37.229235 * 1000000), (int) (-80.422145 * 1000000)));
        buildings.put("CO", new Point((int) (37.230004 * 1000000), (int) (-80.424943 * 1000000)));
        
        buildings.put("DAV", new Point((int) (37.227031 * 1000000), (int) (-80.425093 * 1000000)));
        buildings.put("ROB", new Point((int) (37.22821 * 1000000), (int) (-80.425115 * 1000000)));
        buildings.put("PAM", new Point((int) (37.228543 * 1000000), (int) (-80.424203 * 1000000)));
        
        buildings.put("SURGE", new Point((int) (37.232985 * 1000000), (int) (-80.423183 * 1000000)));
        buildings.put("MAJWM", new Point((int) (37.227894 * 1000000), (int) (-80.424342 * 1000000)));
        buildings.put("SQUIR", new Point((int) (37.22956 * 1000000), (int) (-80.417497 * 1000000)));
        
        buildings.put("WLH", new Point((int) (37.230905 * 1000000), (int) (-80.422256 * 1000000)));
        buildings.put("EMPOR", new Point((int) (37.228552 * 1000000), (int) (-80.422972 * 1000000)));
        
        buildings.put("WAL", new Point((int) (37.22296 * 1000000), (int) (-80.424187 * 1000000)));
        buildings.put("LITRV", new Point((int) (37.221606 * 1000000), (int) (-80.423771 * 1000000)));
        buildings.put("ENGEL", new Point((int) (37.223793 * 1000000), (int) (-80.423249 * 1000000)));
        buildings.put("GYM", new Point((int) (37.223793 * 1000000), (int) (-80.423249 * 1000000)));
        
        buildings.put("FEM", new Point((int) (37.231344 * 1000000), (int) (-80.421242 * 1000000)));
        buildings.put("AGNEW", new Point((int) (37.224754 * 1000000), (int) (-80.424187 * 1000000)));
        buildings.put("BURCH", new Point((int) (37.229615 * 1000000), (int) (-80.424193 * 1000000)));
        buildings.put("BUR", new Point((int) (37.228991 * 1000000), (int) (-80.423657 * 1000000)));
        
        buildings.put("DURHM", new Point((int) (37.231757 * 1000000), (int) (-80.423705 * 1000000)));
        buildings.put("HEND", new Point((int) (37.230599 * 1000000), (int) (-80.416681 * 1000000)));
        buildings.put("HUTCH", new Point((int) (37.225531 * 1000000), (int) (-80.423164 * 1000000)));
        buildings.put("JCH", new Point((int) (37.223868 * 1000000), (int) (-80.422744 * 1000000)));
        
        buildings.put("LANE", new Point((int) (37.230832 * 1000000), (int) (-80.419677 * 1000000)));
        buildings.put("LATH", new Point((int) (37.224575 * 1000000), (int) (-80.422443 * 1000000)));
        buildings.put("LIBR", new Point((int) (37.228819 * 1000000), (int) (-80.419606 * 1000000)));
        buildings.put("LIBR", new Point((int) (37.2306 * 1000000), (int) (-80.420874 * 1000000)));
        
        buildings.put("PRICE", new Point((int) (37.22579 * 1000000), (int) (-80.424117 * 1000000)));
        buildings.put("SANDY", new Point((int) (37.225814 * 1000000), (int) (-80.42348 * 1000000)));
        buildings.put("SAUND", new Point((int) (37.224875 * 1000000), (int) (-80.424483 * 1000000)));
        buildings.put("SEITZ", new Point((int) (37.22456 * 1000000), (int) (-80.423546 * 1000000)));
        
        buildings.put("SHANK", new Point((int) (37.231551 * 1000000), (int) (-80.420117 * 1000000)));
        buildings.put("SHULTZ", new Point((int) (37.231801 * 1000000), (int) (-80.418714 * 1000000)));
        buildings.put("SMYTH", new Point((int) (37.224932 * 1000000), (int) (-80.422911 * 1000000)));
        buildings.put("SQUIR", new Point((int) (37.229455 * 1000000), (int) (-80.417589 * 1000000)));
        
        buildings.put("WMS", new Point((int) (37.227824 * 1000000), (int) (-80.424234 * 1000000)));
    }

    //~Methods--------------------------------------------   
    /**
     * Gets the GPS coordinates in a Point object that correspond to the building with the
     * passed in key. If the key is not found, throws a BuildingNotFoundException.
     * 
     * NOTE: The latitude and longitude are multiplied by 1 million so that they 
     *          can be saved with precision in an int value. Saves space. This is also
     *          how an Android GeoPoint object is saved, so values can be directly transferred
     *          between the two if desired.
     * 
     * @param key the building abbreviation to get GPS data for.
     * @return Point object containing latitude and longitude.
     * 
     * @throws BuildingNotFoundException thrown when the string of the building is either invalid, or is missing from the Map. 
     *          If the latter is the case follow the instructions in the class definition Javadoc.
     *          As of 02/2013 the building map SHOULD be comprehensive.
     */
    public Point get(String key) throws BuildingNotFoundException {
        
        Point point = buildings.get(key);
        
        if (point != null) {
            
            return point;
        }
        else {
            
            throw new BuildingNotFoundException("The building of name: " + key + " is either invalid, or needs to be added");
        }
    }
}