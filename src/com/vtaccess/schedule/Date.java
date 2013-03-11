package com.vtaccess.schedule;

/**
 * Date object which holds both String and int representations of a month, day
 * and year. Basic wrapper class for a date of format
 * 
 * MM/DD/YYYY
 * 
 * @author Ethan Gaebel (egaebel)
 * 
 */
public class Date {
    
    // ~Data Fields--------------------------------------------
    /**
     * Int of the month.
     */
    protected int month;
    /**
     * Int of the day.
     */
    protected int day;
    /**
     * Int of the year.
     */
    protected int year;
    /**
     * String of the month.
     */
    protected String strMonth;
    /**
     * String of the day.
     */
    protected String strDay;
    /**
     * String of the year.
     */
    protected String strYear;

    // ~Constructors--------------------------------------------
    /**
     * Takes a string representing a date in the format of (MM/DD/YYYY) and sets
     * all the variables in this Date object to the values obtained from the
     * passed string.
     * 
     * @param date
     *            the date in the format mm/dd/yyyy
     */
    public Date(String date) {

        String[] dateBlocks = date.split("/");

        strMonth = dateBlocks[0];
        strDay = dateBlocks[1];
        strYear = dateBlocks[2];

        month = Integer.parseInt(strMonth);
        day = Integer.parseInt(strDay);
        year = Integer.parseInt(strYear);
    }

    /**
     * Takes a string month, string day, and string year and sets all of the
     * variables using it.
     * 
     * @param newMonth the month of this Date object.
     * @param newDay the day of this Date object.
     * @param newYear the year of this Date object.
     */
    public Date(String newMonth, String newDay, String newYear) {

        strMonth = newMonth;
        strDay = newDay;
        strYear = newYear;

        month = Integer.parseInt(strMonth);
        day = Integer.parseInt(strDay);
        year = Integer.parseInt(strYear);
    }

    /**
     * Default constructor, empty strings for all strings, 0s for all ints.
     */
    public Date() {

        strMonth = "";
        strDay = "";
        strYear = "";

        month = 0;
        day = 0;
        year = 0;
    }

    /**
     * Takes a date in long form: (typed out month) day, year as well as a
     * dateType specifier, that does not require a valid value it is present to
     * distinguish this constructor from the standard format Date constructor.
     * (MM/DD/YYYY)
     * 
     * @param longDate the String of a date in long format; Januaray 12, 2012.
     * @param dateType makes all Strings passed to this constructor read in as long format dates.
     */
    public Date(String longDate, String dateType) {
        
        if (longDate.length() != 0) {
        
            String[] dateBlock = longDate.split(" ");
            
            if (dateBlock.length == 3) {
                
                dateBlock[1] = dateBlock[1].replaceFirst(",", "");
        
                strMonth = interpretLongMonth(dateBlock[0]);
                month = Integer.parseInt(strMonth);
        
                strDay = dateBlock[1];
                day = Integer.parseInt(dateBlock[1]);
        
                strYear = dateBlock[2];
                year = Integer.parseInt(dateBlock[2]);
            }
            else {
                
                strMonth = "No set date";
                month = 0;
                
                strDay = "";
                day = 0;
                
                strYear = "";
                year = 0;
            }
        }
    }

    // ~Methods--------------------------------------------
    /**
     * Takes in a fully spelled out month and spits out the numerical
     * representation of the month in String form (January = 01 etc...).
     * 
     * @param month
     *            the long version of the month (ex. December).
     * @return the String of the number corresponding to the month, returns a 0
     *         if invalid string was passed in.
     */
    private String interpretLongMonth(String month) {

        // the switch is the fully spelled out month
        // ex. December, december, dEcEmbeR
        if (month.equalsIgnoreCase("january")) {

            return "01";
        }
        else if (month.equalsIgnoreCase("february")) {

            return "02";
        }
        else if (month.equalsIgnoreCase("march")) {

            return "03";
        }
        else if (month.equalsIgnoreCase("april")) {

            return "04";
        }
        else if (month.equalsIgnoreCase("may")) {

            return "05";
        }
        else if (month.equalsIgnoreCase("june")) {

            return "06";
        }
        else if (month.equalsIgnoreCase("july")) {

            return "07";
        }
        else if (month.equalsIgnoreCase("august")) {

            return "08";
        }
        else if (month.equalsIgnoreCase("september")) {

            return "09";
        }
        else if (month.equalsIgnoreCase("october")) {

            return "10";
        }
        else if (month.equalsIgnoreCase("november")) {

            return "11";
        }
        else if (month.equalsIgnoreCase("december")) {

            return "12";
        }
        else {

            return "0";
        }
    }

    /**
     * Takes in another Date object and compares these two to see if this one is
     * greater than, equal to, or less than the other one
     * 
     * greater than is defined as being later in the year than other less than
     * is defined as being earlier in the year than other
     * 
     * ;; this > other = 1;; this < other = -1;; this = other = 0;;
     * 
     * @param other
     *            the date object being compared to this one
     */
    public int compareTo(Date other) {

        if (other.getYear() < year) {

            return 1;
        }
        else if (other.getYear() > year) {

            return -1;
        }
        else {

            if (other.getMonth() < month) {

                return 1;
            }
            else if (other.getMonth() > month) {

                return -1;
            }
            else {

                if (other.getDay() < day) {

                    return 1;
                }
                else if (other.getDay() > day) {

                    return -1;
                }
                else {

                    return 0;
                }
            }
        }
    }

    /**
     * Prints out a string representation of the date (MM/DD/YYYY).
     * 
     * @return the string representation of the date object.
     */
    @Override
    public String toString() {
        
        return strMonth + "/" + strDay + "/" + strYear;
    }

    // ~Getters/Setters----------------------------------------------
    /**
     * @param month
     *            the month to set
     */
    public void setMonth(int month) {

        this.month = month;
    }

    /**
     * @return the month
     */
    public int getMonth() {

        return month;
    }

    /**
     * @param day
     *            the day to set
     */
    public void setDay(int day) {

        this.day = day;
    }

    /**
     * @return the day
     */
    public int getDay() {

        return day;
    }

    /**
     * @param year
     *            the year to set
     */
    public void setYear(int year) {

        this.year = year;
    }

    /**
     * @return the year
     */
    public int getYear() {

        return year;
    }

    /**
     * @param strMonth
     *            the strMonth to set
     */
    public void setStrMonth(String strMonth) {

        this.strMonth = strMonth;
    }

    /**
     * @return the strMonth
     */
    public String getStrMonth() {

        return strMonth;
    }

    /**
     * @param strDay
     *            the strDay to set
     */
    public void setStrDay(String strDay) {

        this.strDay = strDay;
    }

    /**
     * @return the strDay
     */
    public String getStrDay() {

        return strDay;
    }

    /**
     * @param strYear
     *            the strYear to set
     */
    public void setStrYear(String strYear) {

        this.strYear = strYear;
    }

    /**
     * @return the strYear
     */
    public String getStrYear() {

        return strYear;
    }
}