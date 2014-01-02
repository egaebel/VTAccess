package test.vtaccess;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.vtaccess.CourseInfo;
import com.vtaccess.ScheduleScraper;
import com.vtaccess.exceptions.HokieSpaTimeoutException;
import com.vtaccess.exceptions.InvalidAreaException;
import com.vtaccess.exceptions.WrongLoginException;
import com.vtaccess.schedule.Course;
import com.vtaccess.schedule.Schedule;
import junit.framework.TestCase;


public class CourseInformationTests extends TestCase {

    //~Constants----------------------------------------------
    /**
     * The URL of the VT Course timetable.
     */                                                  
    public static final String NO_LOGIN_TIMETABLE_URL = "https://banweb.banner.vt.edu/ssb/prod/HZSKVTSC.P_ProcRequest";
    //TODO: Before running, ensure that these are current
    public static final String FALL_2012_CODE = "201309";
    public static final String SPRING_2013_CODE = "201401";
    //~Data Fields--------------------------------------------


    //~Constructors--------------------------------------------


    //~Methods-------------------------------------------------
    public void testGetAllCoursesFULL() {
        System.out.println("START testGetAllCourses--FULL");
        
        List<Course> allCourses = null;
        
        //Get all courses, with duplicates and closed courses included
        allCourses = CourseInfo.getAllCourses(SPRING_2013_CODE, true, false);
        for (Course c : allCourses) {
            if (c.getSubjectCode().contains("C21S")) {
                System.out.println(c.toXML());
            }
        }
        
        System.out.println("\n\nEND testGetAllCourses--FULL");
    }
    
    public void testGetAllCoursesSINGLESUBJECT() {
        System.out.println("START testGetAllCourses--SINGLESUBJECT");
        
        List<Course> allCourses = new LinkedList<Course>();
        List<Course> allCsCourses = null;
        List<Course> allMathCourses = null;
        List<Course> allPhysicsCourses = null;
        
        System.out.println("\n\nALLOW Duplicates;;ALLOW closed\n");
        //get all CS courses, with duplicates and closed courses included
        allCsCourses = CourseInfo.getAllCourses(SPRING_2013_CODE, "CS", true, false);
        for (Course c : allCsCourses) {
            
            System.out.println(c.toXML());
        }
        
        System.out.println("\n\nDISALLOW Duplicates;;ALLOW closed\n");
        //get all MATH courses, with no duplicates and closed courses included
        allMathCourses = CourseInfo.getAllCourses(SPRING_2013_CODE, "MATH", false, false);
        for (Course c : allMathCourses) {
            
            System.out.println(c.toXML());
        }
        
        System.out.println("\n\nLOOP OVER ALL SUBJECTS!!;;ALLOW Duplicates;;ALLOW closed\n");
        //get all of the options for subject code
        try {
            Document doc = Jsoup.connect(NO_LOGIN_TIMETABLE_URL).get();
            Elements elements = doc.select(".one tbody").get(0).select("tr").get(4)
                    .select("td").get(0).select("select option");
            
            for (Element e : elements) {
                
                allCourses.addAll(CourseInfo.getAllCourses(SPRING_2013_CODE, e.text().trim().split(" ")[0], false, false));
            }
        }
        catch(IOException ex) {
            
        }
        
        System.out.println("\n\nEND testGetAllCourses--SINGLESUBJECT");
    }
    
    public void testGetAllCoursesMULTIPLESUBJECTS() {
        
        System.out.println("START testGetAllCourses--MULTIPLESUBJECTS");
        
        List<Course> allCsAndMathCourses = null;
        List<Course> allMathAndPhysicsCourses = null;
        
        System.out.println("\n\nALLOW Duplicates;;ALLOW closed\n");
        allCsAndMathCourses = CourseInfo.getAllCourses(SPRING_2013_CODE, true, false, "CS", "MATH");
        for (Course c : allCsAndMathCourses) {
            
            System.out.println(c.toXML());
        }
        
        System.out.println("\n\nDISALLOW Duplicates;;ALLOW closed\n");
        allMathAndPhysicsCourses = CourseInfo.getAllCourses(SPRING_2013_CODE, false, false, "PHYS", "MATH");
        for (Course c : allMathAndPhysicsCourses) {
            
            System.out.println(c.toXML());
        }
        
        System.out.println("END testGetAllCourses--MULTIPLESUBJECTS");
    }
    
    public void testGetAllAreaCourses() {
        
        System.out.println("START testGetAllAreaCourses");
        
        List<Course> area1 = null;
        List<Course> area1W = null;
        List<Course> area2 = null;
        List<Course> area3 = null;
        List<Course> area4 = null;
        List<Course> area5 = null;
        List<Course> area6 = null;
        List<Course> area7 = null;
        List<Course> allAreas = new LinkedList<Course>();
        
        try {
            System.out.println("\nArea 1");
            area1 = CourseInfo.getAllAreaCourses(SPRING_2013_CODE, true, "1", false);
            for (Course c : area1) {
                System.out.println(c.toXML());
            }
            System.out.println("\nArea 1W");
            area1W = CourseInfo.getAllAreaCourses(SPRING_2013_CODE, true, "1W", false);
            for (Course c : area1W) {
                System.out.println(c.toXML());
            }
            System.out.println("\nArea 2");
            area2 = CourseInfo.getAllAreaCourses(SPRING_2013_CODE, true, "2", false);
            for (Course c : area2) {
                System.out.println(c.toXML());
            }
            System.out.println("\nArea 3");
            area3 = CourseInfo.getAllAreaCourses(SPRING_2013_CODE, true, "3", false);
            for (Course c : area3) {
                System.out.println(c.toXML());
            }
            System.out.println("\nArea 4");
            area4 = CourseInfo.getAllAreaCourses(SPRING_2013_CODE, true, "4", false);
            for (Course c : area4) {
                System.out.println(c.toXML());
            }
            System.out.println("\nArea 5");
            area5 = CourseInfo.getAllAreaCourses(SPRING_2013_CODE, true, "5", false);
            for (Course c : area5) {
                System.out.println(c.toXML());
            }
            System.out.println("\nArea 6");
            area6 = CourseInfo.getAllAreaCourses(SPRING_2013_CODE, true, "6", false);
            for (Course c : area6) {
                System.out.println(c.toXML());
            }
            System.out.println("\nArea 7");
            area7 = CourseInfo.getAllAreaCourses(SPRING_2013_CODE, true, "7", false);
            for (Course c : area7) {
                System.out.println(c.toXML());
            }
            System.out.println("\nAreas ALL");
            for (int i = 1; i < 8; i++) {
                
                allAreas.addAll(CourseInfo.getAllAreaCourses(SPRING_2013_CODE, true, String.valueOf(i), false));
            }
            for (Course c : allAreas) {
                System.out.println(c.toXML());
            }
        }
        catch (InvalidAreaException e) {}
        
        System.out.println("END testGetAllAreaCourses");
    }
    
    public void testPreRequisitesCOURSECODEWithCourse2114() {
        
        System.out.println("TEST PRE REQUISITES WITH COURSES 2114");
        
        String testSubjectCode = "CS";
        String testCourseNumber = "2114";
        
        List<Course> preReqs = CourseInfo.getPrerequisites(testSubjectCode, testCourseNumber, false);
        System.out.println("OUTPUT:\nOUTPUT:\nOUTPUT:\nOUTPUT:\nOUTPUT:\nOUTPUT:\n");
        for (Course c : preReqs) {
            
            System.out.println(c.toString());
        }

        assertTrue(preReqs.size() > 0);
    }
    
    public void testPreRequisitesCOURSECODEWithCourse2505() {
        
        System.out.println("TEST PRE REQUISITES WITH COURSES 2505");
        
        String testSubjectCode = "CS";
        String testCourseNumber = "2505";
        
        List<Course> preReqs = CourseInfo.getPrerequisites(testSubjectCode, testCourseNumber, false);
        
        for (Course c : preReqs) {
            
            System.out.println(c.toString());
        }

        assertTrue(preReqs.size() > 0);
    }
    
    public void testPreRequisitesCOURSECODEOnAllCourses() {
        
        System.out.println("TEST PRE REQUISITES WITH ALL COURSES");
        
        List<Course> allCourses = CourseInfo.getAllCourses(SPRING_2013_CODE, true, false);
        
        int startingIndex = 0;
        for (int i = startingIndex; i < allCourses.size(); i++) {
         
            System.out.println("---------------------------------------");
            System.out.println(i + " " + i + " " + i + " " + i + " " + i + " " + i + " " + i + " ");
            System.out.println("---------------------------------------");
            
            System.out.println("Finding prereqs for: " 
                    + allCourses.get(i).getSubjectCode() + " " 
                    + allCourses.get(i).getCourseNumber());
            List<Course> preReqs = CourseInfo.getPrerequisites(allCourses.get(i).getSubjectCode(), 
                    allCourses.get(i).getCourseNumber(), 
                    false);
            
            System.out.println("PRINTING COURSES that are preReqs for " + 
                allCourses.get(i).getSubjectCode() + 
                " " + 
                allCourses.get(i).getCourseNumber());
            for (Course c : preReqs) {
                
                System.out.println(c.toString());
            }
        }
    }
    
    /*public void testPrerequisitesCOURSEOnAllCourses() {
        
        System.out.println("TEST PRE REQUISITES WITH ALL COURSES");
        
        List<Course> allCourses = CourseInfo.getAllCourses(SPRING_2013_CODE, true, false);
        
        int startingIndex = 0;
        for (int i = startingIndex; i < allCourses.size(); i++) {
         
            System.out.println("---------------------------------------");
            System.out.println(i + " " + i + " " + i + " " + i + " " + i + " " + i + " " + i + " ");
            System.out.println("---------------------------------------");
            
            System.out.println("Finding prereqs for: " 
                    + allCourses.get(i).getSubjectCode() + " " 
                    + allCourses.get(i).getCourseNumber());
            List<Course> preReqs = CourseInfo.getPrerequisites(SPRING_2013_CODE, 
                    allCourses.get(i), 
                    false);
            
            System.out.println("PRINTING COURSES that are preReqs for " + 
                allCourses.get(i).getSubjectCode() + 
                " " + 
                allCourses.get(i).getCourseNumber());
            for (Course c : preReqs) {
                
                System.out.println(c.toString());
            }
        }
    }*/
    /*
    //~------------------------------------------------------------------------
    //TODO: NOTE MUST ENTER username and password to run test------------------
    //~------------------------------------------------------------------------
    public void testPostrequisitesWithMyCoursesSPRING() throws WrongLoginException, HokieSpaTimeoutException {
        
        char[] username = myUsername.toCharArray();
        char[] password = myPassword.toCharArray();
        
        Schedule schedule = new Schedule();
        ScheduleScraper s = new ScheduleScraper(username, password);
        s.retrieveSchedule(schedule, SPRING_2013_CODE);
        List<Course> myCourses = schedule.getAllCourses();
        List<String> subjects = new LinkedList<String>();
        subjects.add("CS");
        
        for (Course c : myCourses) {
            System.out.println("running postReqs on: " + c.getSubjectCode() + c.getCourseNumber() + "|" + "\n\n\n");
            List<Course> postReqs = CourseInfo.getPostrequisites(SPRING_2013_CODE, c, subjects);
            System.out.println("returned from postReqs, postReqs holds: " + postReqs);
            for (Course p : postReqs) {
                
                System.out.println(p.toString());
            }
        }
    }

    public void testPostrequisitesWitCS3214() throws WrongLoginException, HokieSpaTimeoutException {

        List<String> subjects = new LinkedList<String>();
        subjects.add("CS");

        List<Course> postReqs = CourseInfo.getPostrequisites(SPRING_2013_CODE, "CS", "3214", subjects, false);
        for (Course p : postReqs) {
            
            System.out.println(p.toString());
        }
    }
    
    public void testPostRequisitesWithCourses2114() {
        
        System.out.println("TEST POST REQUISITES WITH COURSES 2114");
        
        String testSubjectCode = "CS";
        String testCourseNumber = "2114";
        List<String> subjects= new LinkedList<String>();
        subjects.add("CS");
        
        List<Course> postReqs = CourseInfo.getPostrequisites(SPRING_2013_CODE, testSubjectCode, testCourseNumber, subjects);
        
        for (Course c : postReqs) {
            
            System.out.println(c.toString());
        }

        assertTrue(postReqs.size() > 0);
    }
    
    public void testPostRequisitesWithCourses2505() {
        
        System.out.println("TEST POST REQUISITES WITH COURSES 2505");
        
        String testSubjectCode = "CS";
        String testCourseNumber = "2505";
        
        List<String> subjects= new LinkedList<String>();
        subjects.add("CS");
        
        List<Course> postReqs = CourseInfo.getPostrequisites(SPRING_2013_CODE, testSubjectCode, testCourseNumber, subjects);
        
        for (Course c : postReqs) {
            
            System.out.println(c.toString());
        }

        if (postReqs.size() > 0) {
            assertTrue(true);
        }
        else {
            //Fail
            assertFalse(true);
        }
    }
    
    public void testPostRequisitesWithALLCOURSES() {
        
        System.out.println("TEST POST REQUISITES WITH ALL COURSES");
        
        //pull all courses
        List<Course> allCourses = CourseInfo.getAllCourses(SPRING_2013_CODE, true, false);
        
        //extract all subjects
        List<String> subjects = new LinkedList<String>();
        HashSet<String> set = new HashSet<String>();
        for (Course c : allCourses) {
            
            if (!set.contains(c.getSubjectCode())) {
                subjects.add(c.getSubjectCode());
                set.add(c.getSubjectCode());
            }
            
        }
        
        //Find all post reqs!
        int startingIndex = 0;
        for (int i = startingIndex; i < allCourses.size(); i++) {
         
            System.out.println("---------------------------------------");
            System.out.println(i + " " + i + " " + i + " " + i + " " + i + " " + i + " " + i + " ");
            System.out.println("---------------------------------------");
            
            System.out.println("Finding postreqs for: " 
                    + allCourses.get(i).getSubjectCode() + " " 
                    + allCourses.get(i).getCourseNumber());
            List<Course> preReqs = CourseInfo.getPostrequisites(SPRING_2013_CODE, 
                    allCourses.get(i).getSubjectCode(), allCourses.get(i).getCourseNumber(), 
                    subjects,
                    false);
            
            System.out.println("PRINTING COURSES that are postReqs for " + 
                allCourses.get(i).getSubjectCode() + 
                " " + 
                allCourses.get(i).getCourseNumber());
            for (Course c : preReqs) {
                
                System.out.println(c.toString());
            }
        }
    }
    
    */
    
    public void testGetCourseCRN() {
        
        Course theCourse = CourseInfo.getCourse(SPRING_2013_CODE, "12108");
        
        assertEquals(theCourse.getCourseNumber(), "3214");
        assertEquals(theCourse.getName(), "Computer Systems");
        
        Course aCourse = CourseInfo.getCourse(SPRING_2013_CODE, "12109");
        
        assertEquals(aCourse.getCourseNumber(), "3214");
        assertEquals(aCourse.getName(), "Computer Systems");
    }
    
    public void testGetCoursesCourseCode() {
        
        List<Course> theCourses = CourseInfo.getCourses(SPRING_2013_CODE, "CS", "3214");
        
        for (Course theCourse : theCourses) {
            assertEquals(theCourse.getCourseNumber(), "3214");
            assertEquals(theCourse.getName(), "Computer Systems");
        }
    }
    
    public void testGetCoursesCourseCodeTEACHER() {
        
        List<Course> theCourses = CourseInfo.getCourses(SPRING_2013_CODE, "CS", "3214", "McQuain");
        
        assertEquals(theCourses.size(), 1);
        assertEquals(theCourses.get(0).getCrn(), "12109");
    }
    
    //WORKS!
    public void testSubjectCodeCourseEntry() {
        
        try {
            
            String testSubjectCode = "CHN";
            String testCourseNumber = "1106";
            
            Document doc = Jsoup.connect(NO_LOGIN_TIMETABLE_URL).data("CAMPUS", "0")
                    .data("TERMYEAR", SPRING_2013_CODE)
                    .data("SCHDTYPE", "%")
                    .data("SUBJ_CODE", testSubjectCode)
                    .data("CRSE_NUMBER", testCourseNumber)
                    .data("CORE_CODE", "AR%")
                    .data("PRINT_FRIEND", "Y")
                    .data("history", "N")
                    .data("BTN_PRESSED", "Printer Friendly List")
                    .post();
            //Test verification
            //System.out.println("doc is:\n" + doc.toString());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
    //WORKS!
    public void testCrnCourseEntry() {
        
        String testCRN = "98618";
        
        try {
            Document doc = Jsoup.connect(NO_LOGIN_TIMETABLE_URL).data("CAMPUS", "0")
                    .data("TERMYEAR", SPRING_2013_CODE)
                    .data("SCHDTYPE", "%")
                    .data("crn", testCRN)
                    .data("CORE_CODE", "AR%")
                    .data("PRINT_FRIEND", "Y")
                    .data("history", "N")
                    .data("BTN_PRESSED", "Printer Friendly List")
                    .post();
            
            //Test verification
            //System.out.println("doc is:\n" + doc.toString());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
    
    
    
    
    
    
    public static final String myUsername = "";
    public static final String myPassword = "";
    }
