package com.vtaccess.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import com.vtaccess.schedule.Course;
import com.vtaccess.schedule.Date;
import com.vtaccess.schedule.Schedule;

/**
 * Class containing methods to write a Schedule object to a XML file.
 * 
 * Contains fields which can be set to the File that is the file the exams are to be saved to/read from,
 * the File that is the file the Schedules are to be saved to/read from, also contains Strings which list
 * the file names of these Files. And a Sting that lists the semester the exam schedule to be loaded pertains to. 
 * 
 * Methods which use these fields are non-static so it will be necessary to use this ScheduleIO as 
 * an instantiated object. All static methods require specifically inputted File objects, they do not rely on
 * any fields local to this Class.
 * 
 * The suggested method of using this object is to create toXML methods for objects that you want to be saved,
 * and then in the case where you have objects, within objects being saved, you can easily print them all
 * by having the upper level object holding the other objects call toXML on its lower objects.
 * 
 * @author Ethan Gaebel (egaebel)
 * 
 */
public class ScheduleIO {

    // ~Data Fields--------------------------------------------
    /**
     * File used to store ExamSchedules.
     */
    private static File examsFile;
    /**
     * The read in semester value.
     */
    private static String semester;
    /**
     * The name of the examsFile.
     */
    private static String examsFileName;
    /**
     * File used to store schedules.
     */
    private static File schedulesFile;
    /**
     * File name of the schedulesFile.
     */
    private static String schedulesFileName;

    // ~Constructors--------------------------------------------
    /**
     * Default constructor, blocked from access.
     */
    @SuppressWarnings("unused")
    private ScheduleIO() {
        
        //unusable default constructor!!
    }

    /**
     * Sets the File object for saving Schedules and the File object
     * for saving an exam schedule.
     * 
     * @param schedulesFilePath the file path of the schedulesFile.
     * @param examsFilePath the file path of the examsFile.
     */
    public ScheduleIO(String schedulesFilePath, String examsFilePath) {
        
        if (schedulesFilePath != null) {
            schedulesFileName = schedulesFilePath;
            schedulesFile = new File(schedulesFilePath);
        }
        else {
            schedulesFileName = "test_file.xml";
            schedulesFile = new File("test_file.xml");
        }
        if (examsFilePath != null) {
            examsFileName = examsFilePath;
            examsFile = new File (examsFilePath);
        }
        else {
            examsFileName = "exams_file.xml";
            examsFile = new File(examsFileName);
        }
    }

    /**
     * Constructor, initializes new files to operate on, for Schedule saving,
     * and for exam schedule saving (which is in the form of a List<Course>.
     * 
     * @param newSchedulesFile File object that is to hold Schedules
     * @param newExamsFile File object that is to hold the exams schedule
     */
    public ScheduleIO(File newSchedulesFile, File newExamsFile) {

        if (newSchedulesFile != null) {
            schedulesFileName = newSchedulesFile.getName().toString();
            schedulesFile = newSchedulesFile;
        }
        else {
            
            schedulesFileName = "test_file.xml";
            schedulesFile = new File(schedulesFileName);
        }
        if (newExamsFile != null) {
            examsFileName = newExamsFile.getName().toString();
            examsFile = newExamsFile;
        }
        else {
            examsFileName = "exams_file.xml";
            examsFile = new File (examsFileName);
        }
    }

    // ~Methods--------------------------------------------
    //~ SAVING===============================================================================
    /**
     * Saves the passed in Schedule to file.
     * 
     * @param schedule the user's schedule.
     * @return true if successful, false otherwise.
     */    
    public boolean saveSchedule(Schedule schedule) {
        
        return saveSchedules(schedule, null);
    }
    
    /**
     * Saves the passed in schedule to file, then if the List<Schedule> buddies isn't null, 
     * and if it has members, saves all Schedule objects contained in it. If only one Schedule 
     * needs to be saved, simply pass in null for buddies.
     * 
     * @param schedule the user's schedule.
     * @param buddies the user's friends' schedules.
     * @return true if successful, false otherwise.
     */
    public boolean saveSchedules(Schedule schedule, List<Schedule> buddies) {
        
        if (schedule != null && schedulesFile != null) {
            
            String saveString = schedule.toXML();
    
            if (buddies != null && buddies.size() != 0) {
    
                for (Schedule friend : buddies) {
    
                    saveString = saveString + "\n" + friend.toXML();
                }
            }

            return saveXMLFile(saveString, "Schedules", schedulesFile);
        }
        
        return false;
    }
    
    /**
     * Saves all of the Courses stored in the finalsList to file.
     * 
     * @param finalsList the course List of final exam times/dates.
     * @param semester the semester that the passed in finalsList pertains to.
     * @return true if successful, false otherwise.
     */
    public boolean saveFinalsList(List<Course> finalsList, String semester) {
        
        if (finalsList != null && schedulesFile != null) {
             
            StringBuilder saveString = new StringBuilder("");
            
            //loop through all courses in finalsList arrayList, adding their xml
            for (Course course : finalsList) {
                
                saveString.append(course.toXML() + "\n");
            }
            
            saveString.append("<Semester>" + semester + "</Semester>\n");
            
            //always append the finalsList
            return saveXMLFile(saveString.toString(), "ExamSchedule", examsFile);
        }
        
        return false;
    }
    
    /**
     * @return the semester
     */
    public String getSemester() {

        return semester;
    }
    
    /**
     * Setter for semester.
     * 
     * @param theSemeseter
     */
    public void setSemeseter(String theSemeseter) {
        
        semester = theSemeseter;
    }
    
    //~Static Methods--------------------------------------------------------------
    //~ SAVING===============================================================================
    /**
     * Saves passed in text to a passed in file, with the passed in master tagName 
     * returns a boolean whether the save was successful or not.
     * 
     * Note: to use well, the textToAdd should be properly formatted XML text, the
     *          user will find it easiest to create a toXML method in any object they 
     *          wish to save to an XML file which returns a String containing the fields
     *          to be saved within the specified XML tags. Then the tagName can be any
     *          Master specifying XML tag the user likes.
     *          
     * Another Note: XML version of resultant XML file is 1.0, encoding is UTF-8, this information
     *                  is included automatically, and cannot be changed. If change is necessary, override.
     * 
     * @param textToAdd
     *            the text to be written to the XML file.
     * @param tagName the master tag of the XML document.
     * @param fileToAdd
     *            the file to write to.
     * @return true if successful false otherwise
     */
    public static boolean saveXMLFile(String textToAdd, String tagName, File fileToAdd) {

        boolean success = false;

        // print xml formatting string and the xml to add
        String text = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<" + tagName + ">\n" + textToAdd + "\n</" + tagName + ">";
        
        try {
            // creates an output stream for the FILEIO's myFile
            OutputStream os = new FileOutputStream(fileToAdd);

            // copy bytes into file
            os.write(text.getBytes());

            // close down stream
            os.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return success;
    }

    //~ LOADING===============================================================================
    /**
     * Loads a schedule from a string of XML and returns it. 
     * 
     * @param xmlSchedule
     *            Schedule String in XML representation.
     * 
     * @return loadedSchedules a List<Schedule> that contains all loaded Scheduled in 
     *          the xmlSchedule, and has the user's Schedule as the first element.
     */
    public static List<Schedule> loadSchedules(String xmlSchedule) {

        try {
            // translate string into a Document object
            DocumentBuilderFactory factory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            InputSource inputter = new InputSource(
                    new StringReader(xmlSchedule));

            Document doc = builder.parse(inputter);

            // normalize the text representation
            doc.getDocumentElement().normalize();

            return loadSchedules(doc);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        catch (SAXException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Loads schedules from XML file passed in.
     * 
     * @param file the File object that is to be read from.
     * @return loadedSchedules a List<Schedule> where the 
     *          first element is the user's Schedule, and all following
     *          elements are the other Schedules in the file.
     */
    public static List<Schedule> loadSchedules(File file) {

        try {

            // translate string into a Document object
            DocumentBuilderFactory factory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new FileInputStream(file));

            // if the file isn't empty
            if (doc.getDocumentElement() != null) {

                // normalize the text representation
                doc.getDocumentElement().normalize();

                // returns result of load
                return loadSchedules(doc);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        catch (SAXException e) {
            e.printStackTrace();
        }
       
        // return null if error thrown, or if file DNE
        return null;
    }

    /**
     * Loads all schedules contained in the passed in doc, returns a List<Schedule> where
     * the first element is the user's Schedule and all other elements are friends Schedules.
     * 
     * Load each element in schedule by XML tag.
     * 
     * @param doc the document from which to read the schedules from.
     * @return loadedSchedules a List<Schedule> where the first element is the user's Schedule
     *                          all other indices hold "friends" Schedule objects. 
     */
    private static List<Schedule> loadSchedules(Document doc) {

        //List of Schedules that hold the user's Schedule, and all other Schedules held in the doc
        List<Schedule> loadedSchedules = new ArrayList<Schedule>();
        
        //Schedule object that will hold the user's Schedule
        Schedule loadedSchedule = new Schedule();

        // Find base tag of <Schedules>
        NodeList theSchedules = doc.getElementsByTagName("Schedules");

        Node theSchedulesNode = theSchedules.item(0);
        Element theSchedulesEl = (Element) theSchedulesNode;

        // get all of the Schedule tagged objects (get schedule)
        NodeList schedules = theSchedulesEl.getElementsByTagName("Schedule");

        // record number of schedules for testing
        int numScheds = schedules.getLength();

        // set user's schedule values//----------------------------------
        Node scheduleNode = schedules.item(0);

        Element scheduleEl = (Element) scheduleNode;

        // set the owner of this schedule (should always be My Schedule)
        NodeList oneOwnerList = scheduleEl.getElementsByTagName("Owner");
        Element ownerEl = (Element) oneOwnerList.item(0);

        NodeList ownerFNList = ownerEl.getChildNodes();

        String ownerName = ownerFNList.item(0).getNodeValue().trim().toString();

        loadedSchedule.setWhosSchedule(ownerName);

        // ~VARIABLES USED WITHIN BIG LOOP BELOW----------------
        // holds list of elements tagged by "Course"
        NodeList courseList;
        int numCourses;
        Element courseEl = null;

        // Day setting
        NodeList dayList;
        Element dayEl;

        // creates an array with the String values of the days of the week
        // used as input for .getElementsByTagName
        String[] days = { "Monday", "Tuesday", "Wednesday", "Thursday",
                "Friday", "AnyDay" };

        // values used to create a new course object
        // NodeList used for Name, teacher, room, time, building element lists
        // Element used for grabbing the correct element from the NodeList
        // (always 0)
        NodeList theList = null;
        Element theEl = null;

        // ---------------------------------------------------
        String name = "";
        String teacher = "";
        String beginTime = "";
        String endTime = "";
        String building = "";
        String room = "";
        
        String subjectCode = "";
        String courseNumber = "";
        String crn = "";
        String credits = "";
        String daysOfCourse = "";
        String classSize = "";
        // -----------------------------------------------------//

        // loops through the days of loadedSchedule
        // then loops through the List of Course objects retrieving courseName,
        // teacherName etc
        for (int k = 0; k < loadedSchedule.size(); k++) {

            // Day setting
            dayList = scheduleEl.getElementsByTagName(days[k]);
            dayEl = (Element) dayList.item(0);

            // Course searches
            courseList = dayEl.getElementsByTagName("Course");
            numCourses = courseList.getLength();

            // if there are indeed courses on the current day
            if (numCourses > 0) {

                // loops through current course object extracting name, teacher,
                // time, building etc....
                for (int i = 0; i < courseList.getLength(); i++) {

                    // selects the correct course in the day via i
                    courseEl = (Element) courseList.item(i);

                    // Checks to see if there is in fact a course here
                    if (((Element) courseEl.getElementsByTagName("Name")
                            .item(0)).getChildNodes().getLength() != 0) {

                        // Finds each element in a course by Tag and assigns it
                        // to a variable
                        // reuses variables
                        theList = courseEl.getElementsByTagName("Name");
                        theEl = (Element) theList.item(0);
                        name = theEl.getChildNodes().item(0).getNodeValue()
                                .trim().toString();

                        theList = courseEl.getElementsByTagName("Teacher");
                        theEl = (Element) theList.item(0);
                        teacher = theEl.getChildNodes().item(0).getNodeValue()
                                .trim().toString();

                        theList = courseEl.getElementsByTagName("BeginTime");
                        theEl = (Element) theList.item(0);
                        beginTime = theEl.getChildNodes().item(0)
                                .getNodeValue().trim().toString();

                        theList = courseEl.getElementsByTagName("EndTime");
                        theEl = (Element) theList.item(0);
                        endTime = theEl.getChildNodes().item(0).getNodeValue()
                                .trim().toString();

                        theList = courseEl.getElementsByTagName("Building");
                        theEl = (Element) theList.item(0);
                        building = theEl.getChildNodes().item(0).getNodeValue()
                                .trim().toString();

                        theList = courseEl.getElementsByTagName("Room");
                        theEl = (Element) theList.item(0);
                        room = theEl.getChildNodes().item(0).getNodeValue()
                                .trim().toString();
                        
                        theList = courseEl.getElementsByTagName("Crn");
                        theEl = (Element) theList.item(0);
                        crn = theEl.getChildNodes().item(0).getNodeValue()
                                .trim().toString();
                        
                        theList = courseEl.getElementsByTagName("SubjectCode");
                        theEl = (Element) theList.item(0);
                        subjectCode = theEl.getChildNodes().item(0).getNodeValue()
                                .trim().toString();
                        
                        theList = courseEl.getElementsByTagName("CourseNumber");
                        theEl = (Element) theList.item(0);
                        courseNumber = theEl.getChildNodes().item(0).getNodeValue()
                                .trim().toString();

                        theList = courseEl.getElementsByTagName("Credits");
                        theEl = (Element) theList.item(0);
                        credits = theEl.getChildNodes().item(0).getNodeValue()
                                .trim().toString();
                        
                        theList = courseEl.getElementsByTagName("Days");
                        theEl = (Element) theList.item(0);
                        daysOfCourse = theEl.getChildNodes().item(0).getNodeValue()
                                .trim().toString();
                        
                        theList = courseEl.getElementsByTagName("ClassSize");
                        theEl = (Element) theList.item(0);
                        classSize = theEl.getChildNodes().item(0).getNodeValue()
                                .trim().toString();
                        
                        loadedSchedule.getDay(k).addCourse(crn, name, subjectCode, 
                                courseNumber, credits, classSize, 
                                teacher, beginTime, endTime,
                                building, room, daysOfCourse);
                    }
                    else {

                        continue;
                    }

                }// end loop through courses for day
            }
            else {

                System.out.println("No courses added from xml on " + days[k]);
            }

        }// end loop through days

        loadedSchedules.add(loadedSchedule);
        
        // if there is more than one schedule in the passed doc....
        if (numScheds > 1) {

            //load them into the loadedSchedules List<Schedule>.
            loadFriendsSchedulesFromFile(theSchedulesEl, numScheds, loadedSchedules);
        }

        // returns the first schedule in the Document
        return loadedSchedules;
    }

    /**
     * Helper method, loops through the remainder of theSchedulesEl Element loading all of the other schedules
     * into a List<Schedule>.
     * 
     * @param theSchedulesEl the schedule Element that holds all of the friendsSchedules
     * @param numScheds the number of schedules
     * @param schedulesList a List of Schedule objects for the loaded friends schedules to be placed in
     */
    private static void loadFriendsSchedulesFromFile(Element theSchedulesEl, int numScheds, List<Schedule> schedulesList) {

        // FRIEND'S SCHEDULE RETRIEVAL//
        // IF there are more schedules to
        // get//--------------------------------------------
        // get them and add them to the list in the same fashion
        // as the user's schedule was
        if (numScheds > 1) {

            // get all of the Schedule tagged objects (get schedule)
            NodeList schedules = theSchedulesEl
                    .getElementsByTagName("Schedule");

            // set up variables to hold users schedules values (used in j loop)
            Node scheduleNode;
            Element scheduleEl;

            // holds list of elements tagged by "Course"
            NodeList courseList;
            int numCourses;
            Element courseEl = null;

            // Day setting
            NodeList dayList;
            Element dayEl;

            // creates an array with the String values of the days of the week
            // used as input for .getElementsByTagName
            String[] days = { "Monday", "Tuesday", "Wednesday", "Thursday",
                    "Friday", "AnyDay" };

            // values used to create a new course object
            // NodeList used for Name, teacher, room, time, building element
            // lists
            // Element used for grabbing the correct element from the NodeList
            // (always 0)
            NodeList theList = null;
            Element theEl = null;

            // ---------------------------------------------------
            String name = "";
            String teacher = "";
            String beginTime = "";
            String endTime = "";
            String building = "";
            String room = "";

            String subjectCode = "";
            String courseNumber = "";
            String crn = "";
            String credits = "";
            String daysOfCourse = "";
            String classSize = "";
            // ---------------------------------------------------
            
            // allocates space for new schedule object
            Schedule friendSched;

            // loops through all schedules
            for (int j = 1; j < numScheds; j++) {

                // switches scheduleEl to new schedule object on each iteration
                scheduleNode = schedules.item(j);
                scheduleEl = (Element) scheduleNode;

                // creates new schedule object and resets theDay array to refer
                // to the friend's schedule
                friendSched = new Schedule();

                // set the owner of this schedule
                NodeList oneOwnerList = scheduleEl
                        .getElementsByTagName("Owner");
                Element ownerEl = (Element) oneOwnerList.item(0);
                NodeList ownerFNList = ownerEl.getChildNodes();

                // sets the name of the friendSched object to the proper name
                //String ownerName = "a friend";// ;
                friendSched.setWhosSchedule(ownerFNList.item(0).getNodeValue().trim().toString());

                // loops through the array theDay
                // gets courses from that day
                // then loops through the arraylist of courses retrieving
                // courseName, teacherName etc
                for (int k = 0; k < friendSched.size(); k++) {

                    // Day setting
                    dayList = scheduleEl.getElementsByTagName(days[k]);
                    dayEl = (Element) dayList.item(0);

                    // Course searches
                    courseList = dayEl.getElementsByTagName("Course");
                    numCourses = courseList.getLength();

                    // if there are indeed courses on the current day
                    if (numCourses > 0) {

                        // loops through current course object extracting name,
                        // teacher, time, building etc....
                        for (int i = 0; i < courseList.getLength(); i++) {

                            // selects the correct course in the day via i
                            courseEl = (Element) courseList.item(i);

                            // Checks to see if there is in fact a course here
                            if (((Element) courseEl
                                    .getElementsByTagName("Name").item(0))
                                    .getChildNodes().getLength() != 0) {


                                // Finds each element in a course by Tag and assigns it
                                // to a variable
                                // reuses variables
                                theList = courseEl.getElementsByTagName("Name");
                                theEl = (Element) theList.item(0);
                                name = theEl.getChildNodes().item(0).getNodeValue()
                                        .trim().toString();

                                theList = courseEl.getElementsByTagName("Teacher");
                                theEl = (Element) theList.item(0);
                                teacher = theEl.getChildNodes().item(0).getNodeValue()
                                        .trim().toString();

                                theList = courseEl.getElementsByTagName("BeginTime");
                                theEl = (Element) theList.item(0);
                                beginTime = theEl.getChildNodes().item(0)
                                        .getNodeValue().trim().toString();

                                theList = courseEl.getElementsByTagName("EndTime");
                                theEl = (Element) theList.item(0);
                                endTime = theEl.getChildNodes().item(0).getNodeValue()
                                        .trim().toString();

                                theList = courseEl.getElementsByTagName("Building");
                                theEl = (Element) theList.item(0);
                                building = theEl.getChildNodes().item(0).getNodeValue()
                                        .trim().toString();

                                theList = courseEl.getElementsByTagName("Room");
                                theEl = (Element) theList.item(0);
                                room = theEl.getChildNodes().item(0).getNodeValue()
                                        .trim().toString();
                                
                                theList = courseEl.getElementsByTagName("Crn");
                                theEl = (Element) theList.item(0);
                                crn = theEl.getChildNodes().item(0).getNodeValue()
                                        .trim().toString();
                                
                                theList = courseEl.getElementsByTagName("SubjectCode");
                                theEl = (Element) theList.item(0);
                                subjectCode = theEl.getChildNodes().item(0).getNodeValue()
                                        .trim().toString();
                                
                                theList = courseEl.getElementsByTagName("CourseNumber");
                                theEl = (Element) theList.item(0);
                                courseNumber = theEl.getChildNodes().item(0).getNodeValue()
                                        .trim().toString();

                                theList = courseEl.getElementsByTagName("Credits");
                                theEl = (Element) theList.item(0);
                                credits = theEl.getChildNodes().item(0).getNodeValue()
                                        .trim().toString();
                                
                                theList = courseEl.getElementsByTagName("Days");
                                theEl = (Element) theList.item(0);
                                daysOfCourse = theEl.getChildNodes().item(0).getNodeValue()
                                        .trim().toString();
                                
                                theList = courseEl.getElementsByTagName("ClassSize");
                                theEl = (Element) theList.item(0);
                                classSize = theEl.getChildNodes().item(0).getNodeValue()
                                        .trim().toString();
                                
                                friendSched.getDay(k).addCourse(crn, name, subjectCode, 
                                        courseNumber, credits, classSize, 
                                        teacher, beginTime, endTime,
                                        building, room, daysOfCourse);
                                
                            }
                            else {

                                continue;
                            }
                        }// end loop through courses for day
                    }
                    else {

                        System.out.println("No courses added from xml on " + days[k]);
                    }
                }// end loop through days
                
            }// end loop through all schedules
            
        }// end if more than 1 schedule
    }
    
    /**
     * Reads in a file and converts it to a doc object. Passes the doc object onto the loadExams(doc) method
     * where an List<Course> is filled with final exams and is returned through this method.
     * 
     * @param file the file to read from.
     * @return the List<Course> of the user's final exams.
     */
    public static List<Course> loadExams(File file) {

        try {

            // translate string into a Document object
            DocumentBuilderFactory factory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new FileInputStream(file));

            // if the file isn't empty
            if (doc.getDocumentElement() != null) {

                // normalize the text representation
                doc.getDocumentElement().normalize();

                // returns result of load
                return loadExams(doc);
            }
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // return null if error thrown, or if file DNE
        return null;
    }
    
    /**
     * Helper method, loads the user's exam schedule from the passed Document object.
     * 
     * @param doc the Document object to load the Exams from
     * @return a List of all of the Course objects that make up the exam Schedule.
     */
    private static List<Course> loadExams (Document doc) {
     
        List<Course> finalsList = new ArrayList<Course>();
        
        NodeList examSchedule = doc.getElementsByTagName("ExamSchedule");
        
        //distill down to a list of XML course objects
        Node examScheduleNode = examSchedule.item(0);
        Element examEl = (Element) examScheduleNode;
        NodeList examList = examEl.getElementsByTagName("Course");
        
        NodeList semesterList = examEl.getElementsByTagName("Semester");
        
        if(semesterList.getLength() > 0 && semesterList.item(0).getNodeValue() != null) {

            semester = semesterList.item(0).getNodeValue().trim().toString();
        }
        
        //~Loop Elements===========================
        // values used to create a new course object
        // NodeList used for Name, teacher, room, time, building element lists
        // Element used for grabbing the correct element from the NodeList
        // (always 0)
        NodeList theList = null;
        Element theEl = null;
        // ---------------------------------------------------
        String name = "";
        String teacher = "";
        String beginTime = "";
        String endTime = "";
        String building = "";
        String room = "";
        String date = "";
        
        String subjectCode = "";
        String courseNumber = "";
        String crn = "";
        String credits = "";
        String daysOfCourse = "";
        // -----------------------------------------------------//
        
        //loop through the examList, setting all courses in finalsList
        for (int i = 0; i < examList.getLength(); i++) {
            
            examEl = (Element) examList.item(i);
            
            // Checks to see if there is in fact a course here
            if (((Element) examEl.getElementsByTagName("Name")
                    .item(0)).getChildNodes().getLength() != 0) {
                
                // Finds each element in a course by Tag and assigns it
                // to a variable
                // reuses variables
                theList = examEl.getElementsByTagName("Name");
                theEl = (Element) theList.item(0);
                name = theEl.getChildNodes().item(0).getNodeValue()
                        .trim().toString();

                theList = examEl.getElementsByTagName("Teacher");
                theEl = (Element) theList.item(0);
                teacher = theEl.getChildNodes().item(0).getNodeValue()
                        .trim().toString();

                theList = examEl.getElementsByTagName("BeginTime");
                theEl = (Element) theList.item(0);
                beginTime = theEl.getChildNodes().item(0)
                        .getNodeValue().trim().toString();

                theList = examEl.getElementsByTagName("EndTime");
                theEl = (Element) theList.item(0);
                endTime = theEl.getChildNodes().item(0).getNodeValue()
                        .trim().toString();

                theList = examEl.getElementsByTagName("Building");
                theEl = (Element) theList.item(0);
                building = theEl.getChildNodes().item(0).getNodeValue()
                        .trim().toString();

                theList = examEl.getElementsByTagName("Room");
                theEl = (Element) theList.item(0);
                room = theEl.getChildNodes().item(0).getNodeValue()
                        .trim().toString();
                
                theList = examEl.getElementsByTagName("Crn");
                theEl = (Element) theList.item(0);
                crn = theEl.getChildNodes().item(0).getNodeValue()
                        .trim().toString();
                
                theList = examEl.getElementsByTagName("SubjectCode");
                theEl = (Element) theList.item(0);
                subjectCode = theEl.getChildNodes().item(0).getNodeValue()
                        .trim().toString();
                
                theList = examEl.getElementsByTagName("CourseNumber");
                theEl = (Element) theList.item(0);
                courseNumber = theEl.getChildNodes().item(0).getNodeValue()
                        .trim().toString();

                theList = examEl.getElementsByTagName("Credits");
                theEl = (Element) theList.item(0);
                credits = theEl.getChildNodes().item(0).getNodeValue()
                        .trim().toString();
                
                theList = examEl.getElementsByTagName("Days");
                theEl = (Element) theList.item(0);
                daysOfCourse = theEl.getChildNodes().item(0).getNodeValue()
                        .trim().toString();
                
                theList = examEl.getElementsByTagName("Date");
                theEl = (Element) theList.item(0);
                
                if (theEl != null) {
                    date = theEl.getChildNodes().item(0).getNodeValue()
                            .trim().toString();
                }
                else {
                    date = "00/00/0000";
                }
                
                //add the read in course to the finalsList
                
                int numCredits;
                
                if (isNumber(credits)) {
                    
                    numCredits = Integer.parseInt(credits);
                }
                else {
                    
                    numCredits = 0;
                }
                
                finalsList.add(new Course(crn, name, subjectCode, courseNumber, 
                        numCredits, teacher, beginTime, endTime, new Date(date), building, room, daysOfCourse));
            }
        }
        
        return finalsList;
    }
    
    /**
     * Tests a char to see if it is safe to convert into an int.
     * 
     * @param character the char to test.
     * @return true if the char is a number, false otherwise.
     */
    private static final boolean isNumber(char character) {

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
     * Tests a string to see if it is safe to convert to an int.
     * 
     * @param str string to check.
     * @return true if string is all numeric, false otherwise.
     */
    private static final boolean isNumber(String str) {
        
        for (int i = 0; i < str.length(); i++) {
            
            if (!isNumber(str.charAt(i))) {
                
                return false;
            }
        }
        
        return true;
    }
}
