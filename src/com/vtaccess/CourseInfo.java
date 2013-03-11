package com.vtaccess;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.vtaccess.exceptions.InvalidAreaException;
import com.vtaccess.schedule.Course;

/**
 * CourseInformation class which provides static methods which allow users to
 * grab information about all of the Courses at Virginia Tech, such as listing
 * all Courses that are within a subject, listing Courses that require a specified Course
 * as a prerequisite, listing Courses that are prerequisite for a specified Course, and other
 * functions.  
 * 
 * When using this class to get prerequisites and post-requisites, it will be faster to
 * use methods that require more information, than less information. The maximum amount
 * of information you can give, is the amount required. So if less information is given,
 * then the remaining required information must be collected. Which takes time. 
 * However there are situations which warrant this, as you will not always have that information
 * at hand.
 * 
 * 
 * @author Ethan Gaebel (egaebel)
 */
public class CourseInfo {

    //~Constants----------------------------------------------
    /**
     * The URL of the VT Course timetable.
     */                                                  
    public static final String NO_LOGIN_TIMETABLE_URL = "https://banweb.banner.vt.edu/ssb/prod/HZSKVTSC.P_ProcRequest";

    //~Pop-Up Info. Box URL pieces~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    /**
     * Part 1.
     * The part of the URL that contains the meat of the whole thing. 
     */
    private static final String BEFORE_CRN = "https://banweb.banner.vt.edu/ssb/prod/HZSKVTSC.P_ProcComments?CRN=";
    /**
     * Part 2.
     * The part of the URL that comes after the CRN and right before the subject code.
     */
    private static final String BEFORE_SUBJECT = "&SUBJ=";
    /**
     * Part 3.
     * The part of the URL that comes after the subject code, and before the course number.
     */
    private static final String BEFORE_CRSE_NUM = "&CRSE=";
    /**
     * Part 4.
     * The part of the URL that comes right after the course number and right before the term.
     */
    private static final String BEFORE_TERM = "&TERM=";
    /**
     * Part 5.
     * The part of the URL that comes right after the term and right before the year.
     */
    private static final String BEFORE_YEAR = "&YEAR=";
    /**
     * Part 6.
     * The part of the URL that comes right after the year, and right before the exam number.
     */
    private static final String END_OF_URL = "&history=N";
    
    //~Methods-------------------------------------------------
    /**
     * Takes in the rows from the HokieSpa timetable page, reads the courses from them into Course objects.
     * Gets crn, courseCode, name, teacher, classSize, credits, days, beginTime, endTime, location 
     * (NOTE: some of these may not be given by hokiespa or may be default values such as 0 or 999)
     * 
     * @param rows Elements object of Jsoup that holds rows of the table from the timetable
     * @param allowDuplicates boolean indicating if courses with the same name but different other properties
     *          should be allowed. True they're allowed, false they are not.
     *          
     * @return List<Course> holds all the course objects that were read from the Element objects.
     */
    protected static List<Course> readRows(Elements rows, boolean allowDuplicates) {
        //System.out.println("rows:\n" + rows.toString() + "\n\n");
        Elements cols;
        List<Course> courses = new LinkedList<Course>();
        Course course;
        
        String crn = "";
        String courseCode = "";
        String name = "";
        String teacher = "";
        String strClassSize = "";
        int classSize = 0;
        String strCredits;
        int credits;
        String days;
        String begin;
        String end;
        String location;
        boolean additionalTime = false;
        
          
        //loop through all of the courses for a subject code
        for (int j = 1; j < rows.size(); j++) {
            
            cols = rows.get(j).select("td");

            if (cols.size() > 1) {
                //If EXTRA class (lab recitation etc
                if (cols.get(4).text().contains("Additional Times")) {
                    
                    additionalTime = true;
                    
                    days = cols.get(5).text();
                    begin = cols.get(6).text();
                    end = cols.get(7).text();
                    location = cols.get(8).text();
                    
                    course = new Course(crn,
                            courseCode,
                            name + " * Additional Time *",
                            0,
                            classSize,
                            teacher,
                            days,
                            begin,
                            end,
                            location);
                }
                else {

                    additionalTime = false;
                    
                    //Get and convert credits to int
                    strCredits = cols.get(4).text().trim();
                    if (strCredits != null 
                            && strCredits.length() == 1 
                            && Character.isDigit(strCredits.charAt(0))) {
                            
                        credits = Integer.parseInt(strCredits);
                    }
                    else {
                            
                        credits = 0;
                    }
                    
                    //Get and convert capacity to int
                    strClassSize = cols.get(5).text();
                    if (strClassSize != null 
                            && !strClassSize.contains("/")
                            && Semester.isNumber(strClassSize.trim())) {
                        
                        classSize = Integer.parseInt(strClassSize);
                    }
                    else {
                        
                        classSize = 0;
                    }
                    
                    crn = cols.get(0).text();
                    courseCode = cols.get(1).text();
                    name = cols.get(2).text();
                    teacher = cols.get(6).text();
                    
                    //if normally formatted (normalish number of columns)
                    if (cols.size() > 7) {
                    
                        days = cols.get(7).text();
                        
                        //account for Online classes
                        if (days.contains("ARR")) {
                         
                            begin = "N/A";
                            end = "N/A";
                            location = cols.get(9).text();
                        }
                        //regular class
                        else {
                            begin = cols.get(8).text();
                            end = cols.get(9).text();
                            location = cols.get(10).text();
                        }
                    }
                    //If cut off at Days and on
                    else {
                        
                        days = "N/A";
                        begin = "N/A";
                        end = "N/A";
                        location = "N/A";
                    }
                            
                    //crn, courseCode, name, credits, teacher, 
                    //days, begin, end, (building + room (location))
                    course = new Course(crn, 
                                courseCode, 
                                name,
                                credits,
                                classSize,
                                teacher,
                                days,
                                begin,
                                end,
                                location);
                }
                    
                //duplicates ARE allowed OR the course is an * Additional Time * 
                    //(the courseCode isn't already present in the courses List<Course>
                if (additionalTime || allowDuplicates || courses.size() == 0 || ((courses.size() > 0) 
                        && !course.getCourseCode().equals(courses.get(courses.size() - 1).getCourseCode()))) {
                    
                    courses.add(course);
                }
            }
        }
        
        return courses;
    }
    
    /**
     * Gets all of the Courses offered at Virginia Tech in a specified year & semester by the semesterCode.
     * 
     * @param semesterCode thesemesterCode of the year+semester that holds the classes of interest. 
     *          The semesterCode must be in the format (YYYYMM) year followed by 2 digit month
     *          with no spaces.
     * @param allowDuplicates boolean switch that specifies whether duplicates are allowed or not.
     *          True to allow duplicates, false to dis-allow.
     * @param onlyOpen boolean switch that specifies if only courses that are currently open
     *          are desired. Pass true to only get courses that are open.
     * @return a List of Course objects that holds all Courses offered at Virginia Tech. Or null if an exception occurs
     */
    public static List<Course> getAllCourses (String semesterCode, boolean allowDuplicates, boolean onlyOpen) {
        
        if (Semester.isSemesterCode(semesterCode)) {
            try {
                
                List<Course> courses = new LinkedList<Course>();
                
                Document doc = Jsoup.connect(NO_LOGIN_TIMETABLE_URL).get();
                //get all of the options for subject code
                Elements elements = doc.select(".one tbody").get(0).select("tr").get(4)
                        .select("td").get(0).select("select option");
    
                //Course reference
                Elements rows;
                
                //loop through all of the subject codes.
                for (int i = 1; i < elements.size(); i++) {
                    
                    if (onlyOpen) {
                        
                        doc = Jsoup.connect(NO_LOGIN_TIMETABLE_URL).data("CAMPUS", "0")
                                .data("TERMYEAR", semesterCode)
                                .data("SCHDTYPE", "%")
                                .data("SUBJ_CODE", elements.get(i).text().trim().split(" ")[0])
                                .data("CORE_CODE", "AR%")
                                .data("open_only", "on")
                                .data("PRINT_FRIEND", "Y")
                                .data("history", "N")
                                .data("BTN_PRESSED", "Printer Friendly List")
                                .timeout(0)
                                .post();                    
                    }
                    else {
                     
                        doc = Jsoup.connect(NO_LOGIN_TIMETABLE_URL).data("CAMPUS", "0")
                            .data("TERMYEAR", semesterCode)
                            .data("SCHDTYPE", "%")
                            .data("SUBJ_CODE", elements.get(i).text().trim().split(" ")[0])
                            .data("CORE_CODE", "AR%")
                            .data("PRINT_FRIEND", "Y")
                            .data("history", "N")
                            .data("BTN_PRESSED", "Printer Friendly List")
                            .timeout(0)
                            .post();
                    }
                    
                    rows = doc.select("table").get(0).select("tr");
                    
                    courses.addAll(CourseInfo.readRows(rows, allowDuplicates));
                }
                
                return courses;
            }
            catch (SocketTimeoutException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
        
    /**
     * Gets all of the Courses with the specified subject offered at Virginia Tech in 
     * a given term. User specifies if duplicates of course codes are allowed with the passed in boolean.
     * 
     * NOTE: Course codes are the 2-4 character subject codes, followed by the Course number
     *      e.g. CS-2114, MATH-2214 etc. 
     * 
     * @param semesterCode the semesterCode YYYYMM to get courses for.
     * @param subjectCode the 2-4 digit Course subject whose Courses are to be returned.
     * @param allowDuplicates boolean switch that specifies whether duplicates are allowed or not.
     *          True to allow duplicates, false to dis-allow.
     * @param onlyOpen boolean switch that specifies if only courses that are currently open
     *          are desired. Pass true to only get courses that are open.
     *          
     * @return a List of Course objects that holds all Courses within the specified 
     *          subject offered at Virginia Tech. Or null if an exception occurs.
     */
    public static List<Course> getAllCourses(String semesterCode, String subjectCode, boolean allowDuplicates, boolean onlyOpen) {
        
        if (Semester.isSemesterCode(semesterCode) && checkSubjectCodeFormat(subjectCode)) {
            try {
                
                List<Course> courses = new LinkedList<Course>();
    
                //loop variables
                Elements rows;
                Document doc;
                //------------//
                if (onlyOpen) {
                    
                    doc = Jsoup.connect(NO_LOGIN_TIMETABLE_URL).data("CAMPUS", "0")
                            .data("TERMYEAR", semesterCode)
                            .data("SCHDTYPE", "%")
                            .data("SUBJ_CODE", subjectCode)
                            .data("CORE_CODE", "AR%")
                            .data("open_only", "on")
                            .data("PRINT_FRIEND", "Y")
                            .data("history", "N")
                            .data("BTN_PRESSED", "Printer Friendly List")
                            .timeout(0)
                            .post();                    
                }
                else {
                 
                    doc = Jsoup.connect(NO_LOGIN_TIMETABLE_URL).data("CAMPUS", "0")
                        .data("TERMYEAR", semesterCode)
                        .data("SCHDTYPE", "%")
                        .data("SUBJ_CODE", subjectCode)
                        .data("CORE_CODE", "AR%")
                        .data("PRINT_FRIEND", "Y")
                        .data("history", "N")
                        .data("BTN_PRESSED", "Printer Friendly List")
                        .timeout(0)
                        .post();
                }
                
                rows = doc.select("table").get(0).select("tr");
                
                courses.addAll(CourseInfo.readRows(rows, allowDuplicates));
                
                return courses;
            }
            catch (SocketTimeoutException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    
    /**
     * Gets all Courses with one of the specified subjects offered at Virginia Tech.
     * 
     * @param semesterCode the semesterCode in YYYYMM format to return List<Course> for.
     * @param allowDuplicates boolean value that specifies whether to include duplicates in the List<Course> returned.
     * @param onlyOpen boolean switch that specifies if only courses that are currently open
     *          are desired. Pass true to only get courses that are open.      
     * @param subjects an array of String objects with the course subject specifiers that are to be included
     *          in the returning List<Course>.
     *          
     * @return a List of Course objects that holds all Courses within the specified
     *          subjects offered at Virginia Tech. It is guaranteed that Courses of 
     *          a subject are grouped together. So you can check to see when a Course's 
     *          subject identifier changes. 
     *          Or null if an invalid semesterCode was entered.
     */
    public static LinkedList<Course> getAllCourses(String semesterCode, boolean allowDuplicates, boolean onlyOpen, String... subjects) {
        
        if (Semester.isSemesterCode(semesterCode) && subjects != null) {
            LinkedList<Course> courses = new LinkedList<Course>();
            List<Course> tempCourses = null;
            for (String subject : subjects) {
                
                tempCourses = getAllCourses(semesterCode, subject, allowDuplicates, onlyOpen);
                if (tempCourses != null) {
                    courses.addAll(tempCourses);
                }
            }
            
            return courses;
        }
        return null;
    }
    
    /**
     * Gets all Courses within the Area specified by the passed in area int.
     * 
     * @param semesterCode the year+semester to get courses from.
     * @param allowDuplicates boolean value indicating whether 
     *          the user desires duplicates or not. True for duplicates, false otherwise.
     * @param area the number of the Area to get Courses for. Or 1W (the special option).
     * @param onlyOpen boolean switch that specifies if only courses that are currently open
     *          are desired. Pass true to only get courses that are open.      
     * @return the List of Course objects that holds all of the Courses within
     *          the specified area passed in. Returns null if the area is invalid or the semesterCode is invalid, or if there is an error.
     *          
     * @throws InvalidAreaException is thrown if the area String does not designate any valid area 
     */
    public static List<Course> getAllAreaCourses(String semesterCode, boolean allowDuplicates, String area, boolean onlyOpen) throws InvalidAreaException {

        if (Semester.isSemesterCode(semesterCode)) {
            try {
                
                List<Course> courses = new LinkedList<Course>();
                
                area = checkAreaFormat(area);
                if (area != null) {
                
                    //loop variables
                    Elements rows;
                    Document doc;
                    //------------//
                    
                    if (onlyOpen) {
                        
                        doc = Jsoup.connect(NO_LOGIN_TIMETABLE_URL).data("CAMPUS", "0")
                                .data("TERMYEAR", semesterCode)
                                .data("CORE_CODE", area)
                                .data("history", "N")
                                .data("PRINT_FRIEND", "Y")
                                .data("BTN_PRESSED", "Printer Friendly List")
                                .data("open_only", "on")
                                .timeout(0)
                                .post();                    
                    }
                    else {
                     
                        doc = Jsoup.connect(NO_LOGIN_TIMETABLE_URL).data("CAMPUS", "0")
                            .data("TERMYEAR", semesterCode)
                            .data("CORE_CODE", area)
                            .data("history", "N")
                            .data("PRINT_FRIEND", "Y")
                            .data("BTN_PRESSED", "Printer Friendly List")
                            .timeout(0)
                            .post();
                    }
                        
                    rows = doc.select("table").get(0).select("tr");
                    
                    courses.addAll(CourseInfo.readRows(rows, allowDuplicates));
                    
                    return courses;
                }
            }
            catch (SocketTimeoutException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        return null;
    }
    
    /**
     * Gets the Course object denoted by the crn, in the year/semester specified.
     * Will grab any course, open or closed.
     * Returns null if there does not exists a 
     * Course denoted by the passed in crn in the semesterCode. 
     * 
     * @param semesterCode the year and semester YYYYMM that the Course is to be pulled from.
     * @param crn the crn of the Course desired.      
     * @return the Course object denoted by the subjectCode and courseNumber.
     *          Returns null if the course does not exist.
     */
    public static Course getCourse(String semesterCode, String crn) {
        
        return getCourse(semesterCode, crn, false);
    }
    
    /**
     * Gets the Course object denoted by the crn, in the year+semester specified.
     * Returns null if there does not exists a 
     * Course denoted by the passed in crn in the term. 
     * 
     * @param semesterCode the year+semester, YYYYMM, that the Course is to be pulled from.
     * @param crn the crn of the Course desired.
     * @param onlyOpen boolean switch that specifies if only courses that are currently open
     *          are desired. Pass true to only get courses that are open.      
     * @return the Course object denoted by the subjectCode and courseNumber.
     *          Returns null if the course does not exist, or if an invalid semesterCode or null crn was passed.
     */
    public static Course getCourse(String semesterCode, String crn, boolean onlyOpen) {
        
        if (Semester.isSemesterCode(semesterCode) && crn != null) {
            try {
                
                Document doc;
                
                if (onlyOpen) {
                    
                    doc = Jsoup.connect(NO_LOGIN_TIMETABLE_URL).data("CAMPUS", "0")
                            .data("TERMYEAR", semesterCode)
                            .data("SCHDTYPE", "%")
                            .data("crn", crn)
                            .data("CORE_CODE", "AR%")
                            .data("open_only", "on")
                            .data("PRINT_FRIEND", "Y")
                            .data("history", "N")
                            .data("BTN_PRESSED", "Printer Friendly List")
                            .timeout(0)
                            .post();                    
                }
                else {
                 
                    doc = Jsoup.connect(NO_LOGIN_TIMETABLE_URL).data("CAMPUS", "0")
                        .data("TERMYEAR", semesterCode)
                        .data("SCHDTYPE", "%")
                        .data("crn", crn)
                        .data("CORE_CODE", "AR%")
                        .data("PRINT_FRIEND", "Y")
                        .data("history", "N")
                        .data("BTN_PRESSED", "Printer Friendly List")
                        .timeout(0)
                        .post();
                }
                
                Elements rows = doc.select("table").get(0).select("tr");
                
                List<Course> courses = CourseInfo.readRows(rows, false);
                
                if (courses.size() != 0) {
                    return courses.get(0);
                }
                else {
                    return null;
                }
            }
            catch (SocketTimeoutException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    
    /**
     * Gets a List of Course objects (Either open or closed) denoted by the subjectCode and 
     * courseNumber passed that are in the semesterCode specified.
     * Returns null if there does not exists a 
     * Course by that subjectCode and courseNumber. 
     * 
     * @param semesterCode the semesterCode to get the Course from.
     * @param subjectCode the 2-4 character subject code identifying the Course.
     * @param courseNumber the 4 digit courseNumber identifying the Course.      
     * @return a List of Course objects denoted by the subjectCode and courseNumber.
     *          Returns null if the course does not exist.
     */
    public static List<Course> getCourses(String semesterCode, String subjectCode, String courseNumber) {

        return CourseInfo.getCourses(semesterCode, subjectCode, courseNumber, false);
    }
    
    
    /**
     * Gets a List of Course objects denoted by the subjectCode and 
     * courseNumber passed that are in the semesterCode specified.
     * Returns null if there does not exists a 
     * Course by that subjectCode and courseNumber. 
     * 
     * @param semesterCode the year + semester to look in. ssemesterCode is in format YYYYMM.
     * @param subjectCode the 2-4 character subject code identifying the Course.
     * @param courseNumber the 4 digit courseNumber identifying the Course.
     * @param onlyOpen boolean switch that specifies if only courses that are currently open
     *          are desired. Pass true to only get courses that are open.      
     * @return a List of Course objects denoted by the subjectCode and courseNumber.
     *          Returns null if the course does not exist or invalid values are passed.
     */
    public static List<Course> getCourses(String semesterCode, String subjectCode, String courseNumber, boolean onlyOpen) {
        
        try {

            if (checkSubjectCodeFormat(subjectCode)
                    && checkCourseNumberFormat(courseNumber)
                    && Semester.isSemesterCode(semesterCode)) {

                //loop variables
                Elements rows;
                Document doc;
                //------------//
                
                if (onlyOpen) {
                    
                    doc = Jsoup.connect(NO_LOGIN_TIMETABLE_URL).data("CAMPUS", "0")
                            .data("TERMYEAR", semesterCode)
                            .data("SUBJ_CODE", subjectCode)
                            .data("CRSE_NUMBER", courseNumber)
                            .data("CORE_CODE", "AR%")
                            .data("open_only", "on")
                            .data("history", "N")
                            .data("PRINT_FRIEND", "Y")
                            .data("BTN_PRESSED", "Printer Friendly List")
                            .timeout(0)
                            .post();
                }
                else {
                    doc = Jsoup.connect(NO_LOGIN_TIMETABLE_URL).data("CAMPUS", "0")
                            .data("TERMYEAR", semesterCode)
                            .data("SUBJ_CODE", subjectCode)
                            .data("CRSE_NUMBER", courseNumber)
                            .data("CORE_CODE", "AR%")
                            .data("history", "N")
                            .data("PRINT_FRIEND", "Y")
                            .data("BTN_PRESSED", "Printer Friendly List")
                            .timeout(0)
                            .post();
                }
                rows = doc.select("table").get(0).select("tr");
                
                return CourseInfo.readRows(rows, true);
            }
        }
        catch (SocketTimeoutException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        
        return null;
    }

    /**
     * Gets the Courses specified by the passed in subjectCode, courseNumber, and teacherName
     * in the specified semesterCode. Returns them in a List<Course>, if no Courses found, return null.
     * 
     * Note: The teacherName must be the last name. Ex. "Allevato" or "allevato"
     * 
     * @param semesterCode the year+semester to check in.
     * @param subjectCode the subjectCode of the Course.
     * @param courseNumber the courseNumber of the Course.
     * @param teacherName the name of the teacher who teaches the Course.      
     * @return courses List of Course objects that are the specified Course, and taught by the
     *          specified teacher. Returns null if no courses are found.
     */
    public static List<Course> getCourses(String semesterCode, String subjectCode, String courseNumber, String teacherName) {
        
        return CourseInfo.getCourses(semesterCode, subjectCode, courseNumber, teacherName, false);
    }
    
    /**
     * Gets the Courses specified by the passed in subjectCode, courseNumber, and teacherName
     * in the specified year+semester. Returns them in a List<Course>, if no Courses found, return null.
     * 
     * Note: It should be kept in mind that you must enter the teacher name correctly, exact string checking
     *          is not done so if you are looking for classes taught by mcquain, simply entering mcquain will suffice.
     * 
     * @param semesterCode the year+semester, YYYYMM to check in.
     * @param subjectCode the subjectCode of the Course.
     * @param courseNumber the courseNumber of the Course.
     * @param teacherName the name of the teacher who teaches the Course.
     * @param onlyOpen boolean switch that specifies if only courses that are currently open
     *          are desired. Pass true to only get courses that are open.      
     * @return courses List of Course objects that are the specified Course, and taught by the
     *          specified teacher. Returns null if no courses are found, or if invalid parameters are passed (i.e. null ).
     */
    public static List<Course> getCourses(String semesterCode, String subjectCode, String courseNumber, String teacherName, boolean onlyOpen) {

        try {
            
            if (checkSubjectCodeFormat(subjectCode)
                    && checkCourseNumberFormat(courseNumber)
                    && Semester.isSemesterCode(semesterCode)
                    && teacherName != null) {
            
                //loop variables
                Elements rows;
                Document doc;
                //------------//
                
                if (onlyOpen) {
                    
                    doc = Jsoup.connect(NO_LOGIN_TIMETABLE_URL).data("CAMPUS", "0")
                            .data("TERMYEAR", semesterCode)
                            .data("SUBJ_CODE", subjectCode)
                            .data("CRSE_NUMBER", courseNumber)
                            .data("CORE_CODE", "AR%")
                            .data("open_only", "on")
                            .data("PRINT_FRIEND", "Y")
                            .data("history", "N")
                            .data("BTN_PRESSED", "Printer Friendly List")
                            .timeout(0)
                            .post();
                }
                else {
                    
                    doc = Jsoup.connect(NO_LOGIN_TIMETABLE_URL).data("CAMPUS", "0")
                            .data("TERMYEAR", semesterCode)
                            .data("SUBJ_CODE", subjectCode)
                            .data("CRSE_NUMBER", courseNumber)
                            .data("CORE_CODE", "AR%")
                            .data("PRINT_FRIEND", "Y")
                            .data("history", "N")
                            .data("BTN_PRESSED", "Printer Friendly List")
                            .timeout(0)
                            .post();
                }
                    
                rows = doc.select("table").get(0).select("tr");
                
                List<Course> tempCourses = CourseInfo.readRows(rows, true);
                List<Course> courses = new LinkedList<Course>();
                for (Course c : tempCourses) {
                    
                    if (c.getTeacherName().contains(teacherName)) {
                        
                        courses.add(c);
                    }
                }
                
                return courses;
            }
        }
        catch (SocketTimeoutException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    
    /**
     * Gets all of the prerequisites for a Course identified by its
     * subject identifier and number. 
     * 
     * Only grabs one of each course, no duplicates allowed, if you desire a comprehensive
     * list of prerequisite courses offered, use the getPrerequisites method with the
     * boolean allow Duplicates field.
     * 
     * e.x. (CS, 3114)
     * @param course the Course whose prerequisites are desired. 
     *          NOTE: this course object must have the subjectCode, courseNumber AND crn attributes.
     * 
     * @return a List of Course objects that comprise the prerequisites
     *          for the Course object passed in. Or null if there are invalid inputs.
     */
    public static List<Course> getPrerequisites(Course course) {
        
        return getPrerequisites(course, false);
    }
    
    
    /**
     * Gets all of the prerequisites for a passed in Course object. 
     * 
     * NOTE: The passed Course object must have all of the fields required:
     *          the subjectCode, the courseNumber and the crn.
     * @param course the Course whose prerequisites are desired. 
     *          NOTE: this course object must have the subjectCode, courseNumber AND crn attributes.
     * @param allowDuplicates boolean that indicates if duplicate courses are allowed. If true, duplicates are allowed.
     *          (i.e. say there's two 3214 classes, either one is a prerequisite
     * 
     * @return a List of Course objects that comprise the prerequisites
     *          for the Course object passed in. The Courses within the List<Course>
     *          are organized in alphabetical chunks by subject code name.
     *          Returns null if no prerequisites were found, or if an IOException occurred.
     */
    public static List<Course> getPrerequisites(Course course, boolean allowDuplicates) {
        
        if (course != null && course.hasURLFields()) {
            
            String semesterCode = Semester.getCurrentSemesterCode();
            
            String year =semesterCode.substring(0, 4);
            String term = semesterCode.substring(4);
            
            List<Course> courses = new LinkedList<Course>();
            try {
                
                Document prereqDoc = Jsoup.connect(BEFORE_CRN + course.getCrn()
                        + BEFORE_TERM + term + BEFORE_YEAR + year 
                        + BEFORE_SUBJECT + course.getSubjectCode() + BEFORE_CRSE_NUM + course.getCourseNumber()
                        + END_OF_URL).timeout(0).post();
                
                String preReqText = "";
                Elements rows;
                List<Course> preReqCourses = null;
                String[] preReqs;
                String[] preReqCodes;
                String preReqSubjectCode;
                String preReqCourseNumber;
                String curSemester;
                
                //TODO: here is where the editing is!
                //rows = prereqDoc.select("body center").get(1).select("table tbody tr").get(14).select("td").get(1).text().trim();
                rows = prereqDoc.select("body center").get(1).select("table tbody tr");
                for (Element row : rows) {
                    
                    if (row.select("td").get(0).text().trim().contains("Prerequisites:")) {
                        
                        preReqText = row.select("td").get(1).text().trim();
                    }
                }

                //Format the preREqText appropriately
                preReqText = preReqText.replaceAll(" or ", ", ");
                preReqText = preReqText.replaceAll("\\(MIN grade of [A-Z]?\\)", "");
                preReqText = preReqText.replaceAll("\\(", "");
                preReqText = preReqText.replaceAll("\\)", "");

                //If there are pre-reqs for the current course
                if (!preReqText.contains("None")) {
                    
                    preReqs = preReqText.split(",");
                    
                    //loop over pre-reqs
                    for (int m = 0; m < preReqs.length; m++) {
                        
                        preReqCodes = preReqs[m].trim().split(" ");
                        preReqSubjectCode = preReqCodes[0];
                        preReqCourseNumber = preReqCodes[1];
                        
                        //Loop over 4 semesters to find preReqCourses
                        curSemester = Semester.previousSemesterCode(semesterCode);
                        for (int l = 0; l < 4; l++) {
                            
                            preReqCourses = getCourses(curSemester, preReqSubjectCode, preReqCourseNumber);
                            
                            //if preReqCourses were found by getCourses
                            if (preReqCourses != null && preReqCourses.size() > 0) {
                                
                                //if duplicates are allowed
                                if (allowDuplicates) {
                                    courses.addAll(preReqCourses);
                                }
                                else {
                                    courses.add(preReqCourses.get(0));
                                }
                                preReqCourses.clear();
                                break;
                            }
                            
                            curSemester = Semester.nextSemesterCode(curSemester);
                        }
                    }
                
                    //remove * Additional Time* courses from preReqCourses list
                    Iterator<Course> it = courses.iterator();
                    Course c;
                    while (it.hasNext()) {
                        
                        c = it.next();
                        
                        if (c.getName().contains("Additional Time")) {
                            it.remove();
                        }
                    }
                
                }
                return courses;
            }
            catch (IOException e) {
                e.printStackTrace();
                return courses;
            }
        }
        
        return null;
    }
    
    /**
     * Gets all of the prerequisites for a Course identified by its
     * subject identifier and number. 
     * 
     * Only grabs one of each course, no duplicates allowed, if you desire a comprehensive
     * list of prerequisite courses offered, use the getPrerequisites method with the
     * boolean allow Duplicates field.
     * 
     * e.x. (CS, 3114)
     * @param subjectCode the code for the subject that the course is a member of.
     * @param courseNumber the number identifying the course in 
     *          the set of courses in its subject.
     * 
     * @return a List of Course objects that comprise the prerequisites
     *          for the Course object passed in. Or null if there are invalid inputs.
     */
    public static List<Course> getPrerequisites(String subjectCode, String courseNumber) {
        
        return getPrerequisites(subjectCode, courseNumber, false);
    }
    
    /**
     * Gets all of the prerequisites for a Course identified by its
     * subject identifier and number. 
     * 
     * e.x. (CS, 3114)
     * @param subjectCode the code for the subject that the course is a member of.
     * @param courseNumber the number identifying the course in 
     *          the set of courses in its subject.
     * @param allowDuplicates boolean value indicating if duplicate courses should be grabbed. 
     *          For example, in a particular semester 5 CS 1114 classes may be taught, if allowDuplicates is true
     *              this method will grab all 5, if it is false, it will grab one, arbitrarily.
     * 
     * @return a List of Course objects that comprise the prerequisites
     *          for the Course object passed in. Or null if there are invalid inputs.
     */
    public static List<Course> getPrerequisites(String subjectCode, String courseNumber, boolean allowDuplicates) {
       
        
        if (checkSubjectCodeFormat(subjectCode) 
                && checkCourseNumberFormat(courseNumber)) {
            
            String semesterCode = Semester.getCurrentSemesterCode();

            String year = semesterCode.substring(0, 4);
            String term = semesterCode.substring(4);

            //get the course info for the passed in subjectCode and courseNumber
            List<Course> tempCourses = null;
            String curSemester = Semester.previousSemesterCode(semesterCode);
            for (int i = 0; i < 4; i++) {
                
                tempCourses = getCourses(curSemester, subjectCode, courseNumber);
                
                //break when a course is found
                if (tempCourses != null && tempCourses.size() > 0) {
                    break;
                }
                //increment semester
                curSemester = Semester.nextSemesterCode(curSemester);
            }
            Course course;
            
            //check grabbed courses for a found course
            if (tempCourses != null && tempCourses.size() > 0) {
                course = tempCourses.get(0);
            }
            else {
                course = null;
            }
            
            //a course was found? Has the right fields? Go grab the preReqs!!!!
            if (course != null && course.hasURLFields()) {

                List<Course> courses = new LinkedList<Course>();
                try {
                    
                    Document prereqDoc = Jsoup.connect(BEFORE_CRN + course.getCrn()
                            + BEFORE_TERM + term + BEFORE_YEAR + year 
                            + BEFORE_SUBJECT + course.getSubjectCode() + BEFORE_CRSE_NUM + course.getCourseNumber()
                            + END_OF_URL).timeout(0).post();
                    
                    String preReqText = "";
                    Elements rows;
                    List<Course> preReqCourses = null;
                    String[] preReqs;
                    String[] preReqCodes;
                    String preReqSubjectCode;
                    String preReqCourseNumber;
                    
                    //TODO: here is where the editing is!
                    //rows = prereqDoc.select("body center").get(1).select("table tbody tr").get(14).select("td").get(1).text().trim();
                    rows = prereqDoc.select("body center").get(1).select("table tbody tr");
                    for (Element row : rows) {
                        
                        if (row.select("td").get(0).text().trim().contains("Prerequisites:")) {
                            
                            preReqText = row.select("td").get(1).text().trim();
                        }
                    }

                    //Format the preREqText appropriately
                    preReqText = preReqText.replaceAll(" or ", ", ");
                    preReqText = preReqText.replaceAll("\\(MIN grade of [A-Z]?\\)", "");
                    preReqText = preReqText.replaceAll("\\(", "");
                    preReqText = preReqText.replaceAll("\\)", "");

                    //If there are pre-reqs for the current course
                    if (!preReqText.contains("None")) {
                        
                        preReqs = preReqText.split(",");
                        
                        //loop over pre-reqs
                        for (int m = 0; m < preReqs.length; m++) {
                            
                            preReqCodes = preReqs[m].trim().split(" ");
                            preReqSubjectCode = preReqCodes[0];
                            preReqCourseNumber = preReqCodes[1];
                            
                            //Loop over 4 semesters to find preReqCourses
                            curSemester = Semester.previousSemesterCode(year + term);
                            for (int l = 0; l < 4; l++) {
                                
                                preReqCourses = getCourses(curSemester, preReqSubjectCode, preReqCourseNumber);
                                
                                //if preReqCourses were found by getCourses
                                if (preReqCourses != null && preReqCourses.size() > 0) {
                                    
                                    //if duplicates are allowed
                                    if (allowDuplicates) {
                                        courses.addAll(preReqCourses);
                                    }
                                    else {
                                        courses.add(preReqCourses.get(0));
                                    }
                                    preReqCourses.clear();
                                    break;
                                }
                                
                                curSemester = Semester.nextSemesterCode(curSemester);
                            }
                        }
                    
                        //remove * Additional Time* courses from preReqCourses list
                        Iterator<Course> it = courses.iterator();
                        Course c;
                        while (it.hasNext()) {
                            
                            c = it.next();
                            
                            if (c.getName().contains("Additional Time")) {
                                it.remove();
                            }
                        }
                    
                    }
                    return courses;
                }
                catch (SocketTimeoutException e) {
                    e.printStackTrace();
                    return courses;
                }
                catch (IOException e) {
                    e.printStackTrace();
                    return courses;
                }
            }
        }
        
        return null;
    }
    
    /**
     * Gets all Courses in one of the passed in subjects that hold a passed Course specified by the courseCode
     * as a prerequisite.
     * *experimental method* 
     * Disallow duplicates, that means that if a course is a post-requisite, 
     *      only one ARBITRARY section of the course offered will be returned
     * 
     * @param semesterCode the semesterCode for the course that is being passed
     * @param course a Course object that holds subjectCode and courseNumber.
     * @param subjects a List of strings holding the subjects to look for post requisites in.
     *          NOTE: You can only check a maximum of 10 subjects at once.
     *          This method returns null if more are passed.
     * @return a List of Course objects that comprise the "post" requisites
     *          for the Course object passed in.
     *          Or null if the input is invalid (i.e. semesterCode=null, subjectCode=null, subjects.size() >10)
     */
    /*
    public static List<Course> getPostrequisites(String semesterCode, Course course, List<String> subjects) {
        
        if (course != null && course.getSubjectCode() != null && course.getCourseNumber() != null) {
            return CourseInfo.getPostrequisites(semesterCode, course.getSubjectCode(), course.getCourseNumber(), subjects, false);
        }
        
        return null;
    }
    */
    
    /**
     * Gets all Courses in one of the passed in subjects that hold a passed Course specified by the courseCode
     * as a prerequisite.
     * *experimental method* 
     * Disallow duplicates, that means that if a course is a post-requisite, 
     *      only one ARBITRARY section of the course offered will be returned
     * 
     * @param semesterCode the semesterCode for the course that is being passed
     * @param subjectCode the subject of the course whose post requisites are desired.
     * @param courseNumber the number of the course whose post requisites are desired.
     * @param subjects a List of strings holding the subjects to look for post requisites in.
     *          NOTE: You can only check a maximum of 10 subjects at once.
     *          This method returns null if more are passed.
     * @return a List of Course objects that comprise the "post" requisites
     *          for the Course object passed in.
     *          Or null if the input is invalid (i.e. semesterCode=null, subjectCode=null, subjects.size() >10)
     */
    /*
    public static List<Course> getPostrequisites(String semesterCode, String subjectCode, String courseNumber, List<String> subjects) {
        
        return CourseInfo.getPostrequisites(semesterCode, subjectCode, courseNumber, subjects, false);
    }
    */
    
    /**
     * Gets all Courses in one of the passed in subjects that hold a passed Course specified by the courseCode
     * as a prerequisite. 
     * NOTE1: The courses returned from this method are not guaranteed to be in any particular semester. They are merely indicative
     *          of courses that are offered at Virginia Tech which are post-requisites for the passed course.  
     * *experimental method*
     * NOTE2: The list of subjects should be kept as small as possible, this method has the potential to run for hours if
     *          too many subjects are passed.
     * 
     * @param semesterCode the YYYYMM that the Course is in.
     * @param subjectCode the subject of the course whose post requisites are desired.
     * @param courseNumber the number of the course whose post requisites are desired.
     * @param subjects a List of strings holding the subjects to look for post requisites in.
     *          NOTE: You can only check a maximum of 10 subjects at once. This method returns null if more are passed.
     * @param allowDuplicates if true then duplicate courses are permitted to be returned, if false they are not.
     *          i.e. CS 3214 is a post requisite for cs 2506, however if there are 2 cs3214 courses, they will both be returned if allowDuplicates is true.
     * @return a List of Course objects that comprise the "post" requisites
     *          for the Course object passed in. 
     *          Or null if the input is invalid (i.e. semesterCode=null, subjectCode=null, subjects.size() >10)
     */
    /*
    public static List<Course> getPostrequisites(String semesterCode, String subjectCode, String courseNumber, List<String> subjects, boolean allowDuplicates) {
        
        if (semesterCode != null
                && Semester.isSemesterCode(semesterCode)
                && checkCourseNumberFormat(courseNumber) 
                && checkSubjectCodeFormat(subjectCode)
                && subjects.size() < 11) {
            
            //get the course info for the passed in subjectCode and courseNumber
            List<Course> tempCourses = null;
            String curSemester = Semester.previousSemesterCode(semesterCode);
            for (int i = 0; i < 4; i++) {
                tempCourses = getCourses(curSemester, subjectCode, courseNumber);
                
                //break when a course is found
                if (tempCourses.size() > 0) {
                    break;
                }
                //increment semester
                curSemester = Semester.nextSemesterCode(curSemester);
            }
            Course course;
            
            //if a course was found while looping over semesters
            if (tempCourses != null && tempCourses.size() > 0) {
                course = tempCourses.get(0);
            }
            else {
                course = null;
            }
            
            //course with passed parameters exists!
            if (course != null) {
                
                try {
                    
                    List<Course> courses = new LinkedList<Course>();
                    
                    //Course reference
                    Elements rows;
                    
                    List<Course> preReqCourses = null;
                    List<Course> passedCourses;
                    Document doc;
                    String curSemesterCode = Semester.previousSemesterCode(semesterCode);
                    
                    //loop through all of the subject codes passed in.
                    for (String subject : subjects) {

                        doc = Jsoup.connect(NO_LOGIN_TIMETABLE_URL).data("CAMPUS", "0")
                                .data("TERMYEAR", semesterCode)
                                .data("SCHDTYPE", "%")
                                .data("SUBJ_CODE", subject)
                                .data("CORE_CODE", "AR%")
                                .data("PRINT_FRIEND", "Y")
                                .data("history", "N")
                                .data("BTN_PRESSED", "Printer Friendly List")
                                .timeout(0)
                                .post();
    
                        rows = doc.select("table").get(0).select("tr");
                        passedCourses = readRows(rows, allowDuplicates);
                        //loop through all of the courses for a subject acquired form readRows (ex. CS, MATH) (all of them!)
                        for (Course p : passedCourses) {
                            
                            //loop over 4 semesters searching
                            for (int i = 0; i < 4; i++) {
                             
                                //get all the pre-requisite courses for the current course
                                preReqCourses = getPrerequisites(curSemesterCode, p.getSubjectCode(), p.getCourseNumber());
                                //check if preReqCourses holds the course passed in
                                if (preReqCourses != null && preReqCourses.contains(course) 
                                        && (allowDuplicates || !courses.contains(p))) {

                                    courses.add(p);
                                }
                                curSemesterCode = Semester.nextSemesterCode(curSemesterCode);
                                preReqCourses = null;
                            }
                            
                            curSemesterCode = Semester.previousSemesterCode(semesterCode);
                        }
                    }
                
                    return courses;
                
                }
                catch (SocketTimeoutException e) {
                    e.printStackTrace();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        
        }
        
        return null;
    }
    */
    
    /**
     * Helper method. Takes in a String representing a courseNumber and checks to
     * see if it is valid. Valid only if courseNumber is 4 digits.
     * 
     * @param courseNumber String to check for validity.
     * @return true if the courseNumber is valid, false otherwise.
     */
    private static boolean checkCourseNumberFormat(String courseNumber) {
        
        if (courseNumber != null && courseNumber.length() >= 4) {
            
            if (courseNumber.length() > 4) {
                
                courseNumber = courseNumber.substring(0, 4);
            }
            
            for (int i = 0; i < courseNumber.length(); i++) {
                
                if (!Character.isDigit(courseNumber.charAt(i))) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    /**
     * Helper method, checks to see if the passed in string is formatted properly
     * to be one of the subject codes. Must be 2-4 characters long, and all characters,
     * no digits.
     * 
     * @param subjectCode the subjectCode to check.
     * @return true if subjectCode is valid, false otherwise.
     */
    private static boolean checkSubjectCodeFormat(String subjectCode) {

        if (subjectCode != null 
                && subjectCode.length() >= 2 
                && subjectCode.length() <= 4) {
            
            return true;
        }
            
        return false;
    }
    
    /**
     * Helper method, checks to see if a passed in String is formatted properly to be
     * one of the 8 areas. If it is not, returns null. If it is, the string has the proper
     * characters prepended to it, depending on if a single digit was entered, 2 digits was entered,
     * or if a digit and a character was entered. If the area is in an incorrect format, InvalidAreaException is thrown. 
     * Also checks for nullness
     * 
     * @param area the area that the user inputted
     * @return if area is valid, the fully formatted area String. 
     *          e.g. AR01,...., AR07, AR1W
     *          
     * @throws InvalidAreaException thrown if the area String is not allowable
     */
    private static String checkAreaFormat(String area) throws InvalidAreaException {
        
        if (area != null) {
            if (area.length() == 1 || area.length() == 2) {
                if (area.equals("1W")) {
                    
                    return "AR" + area;
                }
                else if (area.length() == 1 && Character.isDigit(area.charAt(0)) 
                        && (Integer.parseInt(area) <= 7 && Integer.parseInt(area) > 0)) {
                    
                    return "AR0" + area;
                }
                else if (area.length() == 2 && Character.isDigit(area.charAt(0)) 
                        && Character.isDigit(area.charAt(1)) && (Integer.parseInt(area.substring(1)) == 0)
                        && (Integer.parseInt(area.substring(0)) > 0 && Integer.parseInt(area.substring(10)) <= 7)) {
                    
                    return "AR" + area;
                }
                else {
                    
                    throw new InvalidAreaException(area);
                }
            }
        }
        
        return null;
    }
}