package com.vtaccess;

import java.util.Calendar;

/**
 * Class which holds constants for every semester. Also has static methods used for operating
 * on semesterCodes, and getting the current semesterCode.
 * 
 * @author Ethan Gaebel (egaebel)
 *
 */
public final class Semester {

    public static final String FALL = "09";
    public static final String SPRING = "01";
    public static final String SUMMER1 = "05";
    public static final String SUMMER2 = "07";
   
    /**
     * Takes in a semesterCode and returns a String with the semesterCode that denotes the semester
     * after the one passed into this method.
     * 
     * @param semesterCode
     *            the numerical identifier of the semester, along with the year. (ex. YYYYMM).
     * @return String representation of the next semesterCode that for the semester AFTEr this one.
     */
    public static final String nextSemesterCode(String semesterCode) {
        
        if (semesterCode.length() == 6) {
            
            String year = semesterCode.substring(0, 4);
            String semester = semesterCode.substring(4);
            
            if (isNumber(year)) {
                
                if (semester.equals(FALL)) {
                    
                    int intYear = Integer.parseInt(year);
                    intYear++;
                    return String.valueOf(intYear) + SPRING;
                    
                }
                else if (semester.equals(SPRING)) {
                    
                    return year + SUMMER1;
                }
                else if (semester.equals(SUMMER1)) {
                    
                    return year + SUMMER2;
                }
                else if (semester.equals(SUMMER2)) {
                    
                    return year + FALL;
                }
            }
        }
        
        return "";
    }

    /**
     * Takes in a semesterCode and returns the string that denotes the semesterCode of the semester
     * that came before the semester denoted by the passed in semesterCode.
     * 
     * @param semesterCode
     *            the numerical identifier of the semester, along with the year. (ex. YYYYMM).
     * @return String representation of the next semesterCode that for the semester AFTEr this one.
     */
    public static final String previousSemesterCode(String semesterCode) {
        
        if (semesterCode.length() == 6) {
            
            String year = semesterCode.substring(0, 4);
            String semester = semesterCode.substring(4);
            
            if (isNumber(year)) {
                
                if (semester.equals(SPRING)) {
                    
                    int intYear = Integer.parseInt(year);
                    intYear--;
                    return String.valueOf(intYear) + FALL;
                    
                }
                else if (semester.equals(FALL)) {
                    
                    return year + SUMMER2;
                }
                else if (semester.equals(SUMMER2)) {
                    
                    return year + SUMMER1;
                }
                else if (semester.equals(SUMMER1)) {
                    
                    return year + SPRING;
                }
            }
        }
        
        return "";
    }
    
    /**
     * Takes a passed in a string and checks to see if it is a valid semesterCode (YYYYMM).
     * Also checks that the year is over 2000, and that the semester is either FALL, SPRING, SUMMER1 or SUMMER2.
     * Also checks for nullness.
     * 
     * @param semesterCode
     *            the numerical identifier of the semester, along with the year. (ex. YYYYMM).
     * @return true if valid, false otherwise.
     */
    public static final boolean isSemesterCode(String semesterCode) {
        
        if (semesterCode != null) {
            if (semesterCode.length() == 6) {
                
                if (isNumber(semesterCode)) {
                    
                    String semester = semesterCode.substring(4);
                    int year = Integer.parseInt(semesterCode.substring(0, 4));
                    
                    if (year > 2000) {
                    
                        if (semester.equals(FALL) || semester.equals(SPRING) || semester.equals(SUMMER1) || semester.equals(SUMMER2)) {
                            
                            return true;
                        }
                    }
                }
            }
        }
        
        return false;
    }
    
    /**
     * Tests a string to see if it is safe to convert to an int.
     * 
     * @param str string to check.
     * @return true if string is all numeric, false otherwise.
     */
    protected static final boolean isNumber(String str) {
        
        for (int i = 0; i < str.length(); i++) {
            
            if (!isNumber(str.charAt(i))) {
                
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Tests a char to see if it is safe to convert into an int.
     * 
     * @param character the char to test.
     * @return true if the char is a number, false otherwise.
     */
    protected static final boolean isNumber(char character) {

        boolean value;

        String tester = String.valueOf(character);

        if (tester.contains("0") || tester.contains("1")
                || tester.contains("2") || tester.contains("3")
                || tester.contains("4") || tester.contains("5")
                || tester.contains("6") || tester.contains("7")
                || tester.contains("8") || tester.contains("9")) {

            value = true;
        }
        else {

            value = false;
        }

        return value;
    }
    
    
    /**
     * Gets the year to tack on to the submitted semester such that the returned 
     * semesterCode will be the current, or next occurrence of that semester.
     * 
     * @param semester the semester.
     * @return a String that goes <year><semester>
     */
    public static String getSemesterCode(String semester) {
        
        Calendar cal = Calendar.getInstance();
        
        int year = cal.get(Calendar.YEAR);
        
        //If the spring semester is the target, and it's November or December.
        if (Integer.valueOf(semester) == 1 && cal.get(Calendar.MONTH) > 10) {
            
            return (year + 1) + semester;
        }

        return year + semester;
    }
    
    /**
     * Gets the semesterCode for the current month and year.
     * 
     * There are several months where there are two semesters going on at different
     * days. These days vary year to year so here are the hard-coded (unfortunately) values,
     * I have tried to set them so they will include most cases, but they will definitely be wrong 
     * at times in the coming years.
     * 
     * The below ranges are inclusive:
     * January -> May 20 == Spring Semester
     * May 21 -> July 8 == Summer I Semester
     * July 9 -> August 19 == Summer II Semester
     * August 20 -> December == Fall Semester
     * 
     * @return the semesterCode for this month and year.
     */
    public static String getCurrentSemesterCode() {
        
        Calendar cal = Calendar.getInstance();
        
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        
        switch (month) {
            case 0:
                return year + SPRING; 
            case 1:
                return year + SPRING;
            case 2:
                return year + SPRING;
            case 3:
                return year + SPRING;
            case 4:
                if (day > 20) {
                    return year + SUMMER1;
                   }
                   else {
                       return year + SPRING;
                   }
            case 5:
                return year + SUMMER1;
            case 6:
                if (day > 8) {
                    return year + SUMMER2;
                }
                else {
                    return year + SUMMER1;
                }
            case 7:
                if (day > 19) {
                    return year + FALL;
                }
                else {
                    return year + SUMMER2;
                }
            case 8:
                return year + FALL;
            case 9:
                return year + FALL;
            case 10:
                return year + FALL;
            case 11:
                return year + FALL;
            default:
                return "";
        }
    }
    
    /**
     * Gets the semester for the current month and day.
     * 
     * There are several months where there are two semesters going on at different
     * days. These days vary year to year so here are the hard-coded (unfortunately) values,
     * I have tried to set them so they will include most cases, but they will definitely be wrong 
     * at times in the coming years.
     * 
     * The below ranges are inclusive:
     * January -> May 20 == Spring Semester
     * May 21 -> July 8 == Summer I Semester
     * July 9 -> August 19 == Summer II Semester
     * August 20 -> December == Fall Semester
     * 
     * @return the semester for this month and day.
     */
    public static String getCurrentSemester() {
        
        Calendar cal = Calendar.getInstance();
        
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        
        switch (month) {
            case 0:
                return SPRING; 
            case 1:
                return SPRING;
            case 2:
                return SPRING;
            case 3:
                return SPRING;
            case 4:
                if (day > 20) {
                    return SUMMER1;
                   }
                   else {
                       return SPRING;
                   }
            case 5:
                return SUMMER1;
            case 6:
                if (day > 8) {
                    return SUMMER2;
                }
                else {
                    return SUMMER1;
                }
            case 7:
                if (day > 19) {
                    return FALL;
                }
                else {
                    return SUMMER2;
                }
            case 8:
                return FALL;
            case 9:
                return FALL;
            case 10:
                return FALL;
            case 11:
                return FALL;
            default:
                return "";
        }
    }
    
    /**
     * Get the semester that comes after the one passed in.
     * 
     * @param curSemester the semester to get the next one from.
     * @return the next semester, following the passed one.
     */
    public static String nextSemester(String curSemester) {
        
        if (curSemester != null) {
            
            if (curSemester.equals(FALL)) {
                return SPRING;
            }
            else if (curSemester.equals(SPRING)) {
                return SUMMER1;
            }
            else if (curSemester.equals(SUMMER1)) {
                return SUMMER1;
            }
            else if (curSemester.equals(SUMMER2)) {
                return FALL;
            }
        }
        
        return "";
    }
    
    /**
     * Get the semester that comes before the one passed in.
     * 
     * @param curSemester the semester to get the previous one from.
     * @return the previous semester, before the one that was passed in.
     */
    public static String prevSemester(String curSemester) {
        
        if (curSemester != null) {
            
            if (curSemester.equals(FALL)) {
                return SUMMER2;
            }
            else if (curSemester.equals(SPRING)) {
                return FALL;
            }
            else if (curSemester.equals(SUMMER1)) {
                return SPRING;
            }
            else if (curSemester.equals(SUMMER2)) {
                return SUMMER1;
            }
        }
        
        return "";
    }
}