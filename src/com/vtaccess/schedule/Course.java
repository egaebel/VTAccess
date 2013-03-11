package com.vtaccess.schedule;

/**
 * A course object that contains the crn, the course's subject Code, the course's number,
 * the number of credits this Course is worth, courseName, teacherName, building id, room number,
 * a Date object and beginning time, and ending time, and a Point object that holds formatted 
 * beginning and ending times.
 * 
 * These values are not all needed at once, they are to be used as needed. However it should
 * be noted that you should keep track of what values you have set so that you don't try and
 * get values that do not get set.
 * 
 * NOTE: Not all of these values are outputted when using toXML method. See the JavaDoc for
 * toXML to see which values are. Override toXML if you require different values.
 * 
 * @author Ethan Gaebel (egaebel)
 */
public class Course {
    
    //~Constants----------------------------------------------------
    /**
     * The value that beginTime and endTime are multiplied by after being converted to integers.
     * It can be useful to increase the size.
     */
    private final int DEFAULT_TIME_FACTOR = 1;

    // ~Data Fields-------------------------------------------------
    /**
     * String where each character denotes a day of the week that this Course
     * object is on.
     */
    protected String days;
    /**
     * Int representing the number of credits this Course is worth.
     */
    protected int credits;
    /**
     * The CRN of this Course.
     */
    protected String crn;
    /**
     * The subject code of this Course.
     */
    protected String subjectCode;
    /**
     * The 4-digit course number of this Course. ex. 1105.
     */
    protected String courseNumber;
    /**
     * The time the course begins
     */
    protected String beginTime;
    /**
     * The time the course ends
     */
    protected String endTime;
    /**
     * A Point object that contains the adjusted time values for the beginning
     * time and ending time.
     */
    protected Point coursePoint;
    /**
     * The name of the course.
     */
    protected String name;
    /**
     * The number of seats in the course, or the number of people enrolled in the course.
     * It depends on the method by which this field was filled. It is up to the user to 
     * be aware of which one this field will be based on the options they pass to a method.
     */
    private int classSize;
    /**
     * The teacher's name.
     */
    protected String teacherName;
    /**
     * The building of the Course.
     */
    protected String building;
    /**
     * The room number of the Course.
     */
    protected String room;
    /**
     * The date object that represents the day this course lies on.
     * 
     * Rarely needed.
     */
    protected Date date;
    /**
     * The factor by which to multiply beginTime and endTime by before 
     * storing them within coursePoint. 
     * 
     * Note: It can be useful to adjust time integers in some algorithms. 
     */
    protected int timeFactor;

    // ~Constructors-------------------------------------------------
    
    /**
     * Default constructor, initializes empty coursePoint object, and empty Date
     * object.
     * 
     */
    public Course() {

        coursePoint = new Point();

        date = null;
        timeFactor = DEFAULT_TIME_FACTOR;
        
        name = "";
        subjectCode = "";
        courseNumber = "";
        beginTime = "";
        endTime = "";
        building = "";
        room = "";
        crn = "";
        days = "";
        teacherName = "";
        classSize = -1;
        credits = -1;
    }
    
    /**
     * Final exam constructor.
     * 
     * @param name the name of the course.
     * @param subjectCode the subjectCode of the course.
     * @param courseNumber the courseNumber of the course.
     * @param begin the beginning time of the course.
     * @param end the ending time of the course.
     * @param newDate the date, in string form of the exam/course.
     */
    public Course (String name, String subjectCode, String courseNumber, 
            String begin, String end, String newDate) {
        
        this();
        
        setName(name);
        setTeacherName("");

        this.setBuilding("");
        this.setRoom("");

        coursePoint = new Point();
        setBeginTime(begin);
        setEndTime(end);
        timeFactor = DEFAULT_TIME_FACTOR;

        setSubjectCode(subjectCode);
        setCourseNumber(courseNumber);
        
        // sets the date object using the long string newDate
        date = new Date(newDate, "long date format");
    }
    
    /**
     * The constructor for the Course, takes in a crn, name, courseCode, 
     * name, credits, beginning time, ending time, location (building and room separated by a space).
     * 
     * 
     * @param crn the crn of the Course.
     * @param courseCode the courseCode of the Course in the format
     *          <SubjectCode>-<CourseNumber>
     * @param name
     *            the name of the course.
     * @param credits int that is the number of credits for this Course.
     * @param teacherName
     *            the teacher's name of the course
     * @param days a String where each character denotes a day of the week this Course
     *          is taught on.
     * @param begin
     *            the beginning time of the course
     * @param end
     *            the ending time of the course
     * @param location a String holding first the building code, then the room number, separated
     *            by a space
     *            
     */
    public Course(String crn, String courseCode, String name, 
            int credits, int capacity, String teacherName, String days, String begin, String end, 
            String location) {

        this();
        
        setCrn(crn);
        setDays(days);
        
        String[] temp = splitCourseCode(courseCode);
        setSubjectCode(temp[0]);
        setCourseNumber(temp[1]);
        
        this.credits = credits;
        classSize = capacity;
        
        temp = splitBuildingRoom(location);
        if (temp != null) {
            setBuilding(temp[0]);
            setRoom(temp[1]);
        }
        else {
            setBuilding("");
            setRoom("");
        }
        
        setName(name);
        setTeacherName(teacherName);

        coursePoint = new Point();
        setBeginTime(begin);
        setEndTime(end);
        timeFactor = DEFAULT_TIME_FACTOR;

        date = null;
    }
    
    /**
     * The constructor for the Course, takes a beginTime, as well as the name,
     * teacherName, and location.
     * 
     * 
     * @param crn the crn of the Course.
     * @param name the name of the Course.
     * @param subjectCode the subjectCode of the course. 
     * @param courseNumber the courseNumber of the course.
     * @param credits an int, the number of credits that this course has.
     * @param capactiy either the number of people in the class or the capacity of the class.
     * @param teacherName
     *            the teacher's name of the course
     * @param begin
     *            the beginning time of the course
     * @param end
     *            the ending time of the course
     * @param building
     *            the building that the class is in
     * @param room
     *            the room that the class is in
     */
    public Course(String crn, String name, String subjectCode, 
            String courseNumber, int credits, int capacity, String teacherName, 
            String begin, String end, String building, String room) {

        this();
        
        setCrn(crn);
        setSubjectCode(subjectCode);
        setCourseNumber(courseNumber);
        
        setName(name);
        setTeacherName(teacherName);

        this.credits = credits;
        classSize = capacity;
        
        setBuilding(building);
        setRoom(room);

        coursePoint = new Point();
        setBeginTime(begin);
        setEndTime(end);
        timeFactor = DEFAULT_TIME_FACTOR;

        date = null;
    }
    
    /**
     * The ULTIMATE constructor for the Course, takes parameters for every field in Course.
     * 
     * @param crn 
     *            the crn of the course.
     * @param name
     *            the name of the course.
     * @param subjectCode the subjectCode of the course.
     * @param courseNumber the number of the course.
     * @param credits the number of credits this course is worth
     * @param teacherName
     *            the teacher's name of the course.
     * @param begin
     *            the beginning time of the cour.se
     * @param end
     *            the ending time of the course.
     * @param theDate
     *            Date object that this course's date object will be set equal to.
     * @param building
     *            the building that the class is in.
     * @param room
     *            the room that the class is in.
     * @param days short string where each single character indicates a day the course is on.
     */
    public Course(String crn, String name, String subjectCode, 
            String courseNumber, int credits, String teacherName, 
            String begin, String end, Date theDate, String building, String room, String days) {

        this();
        
        this.crn = crn;
        this.name = name;
        this.subjectCode = subjectCode;
        this.courseNumber = courseNumber;
        
        this.credits = credits;
        this.teacherName = teacherName;
        
        coursePoint = new Point();
        setBeginTime(begin);
        setEndTime(end);
        timeFactor = DEFAULT_TIME_FACTOR;
        
        date = theDate;

        this.days = days;
        this.building = building;
        this.room = room;
    }
    
    // ~Methods------------------------------------------------------
    /**
     * Takes in a course code in the format: XX->XXXX-nnnn or XX->XXXX nnnn and splits it into
     * a two element String array where the subject code (the first portion) 
     * is the first element, and the course number (second portion) is the 
     * second element. 
     * 
     * Course code must have its subjectCode and courseNumber separated by either a dash "-"
     * or a space " ".
     * 
     * XX->XXXX signifies that there are 2 to 4 characters.
     * nnnn signifies that there are 4 numbers. 
     * 
     * @param courseCode the course code in the format: XX->XXXX-nnnn.
     *              If this format is not followed, null is returned.
     * @return courseCodes 2 element String array holding the subject code and
     *              course number, or null if the format specified was not followed.
     */
    public static String[] splitCourseCode(String courseCode) {
        
        String[] courseCodes = courseCode.split("-");
        
        if (courseCodes.length == 2) {
            //if the subject code was entered properly
            if(courseCodes[0].length() >= 2 && courseCodes[0].length() <= 4) {
                
                //if the course number was entered properly
                if ((courseCodes[1].length() == 4) 
                        || (courseCodes[1].length() == 5 && Character.isLetter(courseCodes[1].charAt(4)))) {
                    
                    return courseCodes;
                }
            }
        }
        else {
            
            courseCodes = courseCode.split(" ");
            
            if (courseCodes.length == 2) {
                //if the subject code was entered properly
                if(courseCodes[0].length() >= 2 && courseCodes[0].length() <= 4) {
                    
                    //if the course number was entered properly
                    if ((courseCodes[1].length() == 4) 
                            || (courseCodes[1].length() == 5 && Character.isLetter(courseCodes[1].charAt(4)))) {
                        
                        return courseCodes;
                    }
                }
            }
        }
        
        return null;
    }
    
    /**
     * Takes in a string holding the building code, and the room number
     * where the two are separated by a space, and splits them into their
     * own Strings and places them in a String array, building followed by 
     * room. If the passed in String is malformed, this returns null.
     * 
     * @param location String holding the building id and room number.
     * @return 2 element String array with the building code, followed by
     *          the room number. If the String passed in is malformed, returns
     *          null.
     */
    public static String[] splitBuildingRoom(String location) {
        
        String[] theLocation = location.split(" ");
        
        if (theLocation.length == 2) {
            
            return theLocation;
        }
        
        return null;
    }
    
    /**
     * Setter for the beginTIme. Sets both the beginTime variable and the coursePoint's x value.
     * 
     * @param begin
     *            the beginning time of the course.
     */
    public void setBeginTime(String begin) {
        
        int adjustedTime = 0;
        
        if (begin != null) {
            
            beginTime = begin;
        }
        else {
            
            beginTime = " ";
        }
        
        // sets the Point's X value in this Course object to the value of begin
        // after formatting
        if ((begin != null) && !(begin.equals("N/A"))
                && !(begin.contains("ARR")) && !(begin.equalsIgnoreCase("TBA"))
                && !(begin.equals(""))) {
            
            // split the time string apart by the colon
            // ex. 1:30 pm --> {1, 30 pm}
            String[] temp = begin.split(":");
            String[] temp2;
            
            // rejoin the split strings, into a string the same without the
            // colon
            if (temp.length == 2) {
                String amPm = temp[0] + temp[1];
                
                // split the time string apart by the space
                // ex. 130 pm --> {130, pm}
                temp2 = amPm.split(" ");
                
            
                //check to see if the am or pm was stuck to the end of the
                //time or not... (stuck to example. 3:00PM)
                if (temp2.length != 2) {
                    
                    //place temp 2 in temp22
                    String[] temp22 = temp2;
                    
                    //reset temp2 and make it 2 large
                    temp2 = new String[2];
                    //place the cut up temp22 back into temp 2 to work with everything else
                    temp2[1] = temp22[0].substring(temp22[0].length() - 2, temp22[0].length());
                    temp2[0] = temp22[0].substring(0, temp22[0].length() - 2);
                }
    
                adjustedTime = Integer.parseInt(temp2[0]);
                adjustedTime *= timeFactor;
    
                if (((temp2[1].contains("PM")) || (temp2[1].contains("pm")))
                        && (!(temp[0].equals("12")))) {
                    
                    adjustedTime += 1200 * timeFactor;
                }
            }
            
            coursePoint.x = adjustedTime;
        }
    }

    /**
     * setter for the endTime
     * 
     * @param end
     *            the ending time of the course
     */
    public void setEndTime(String end) {

        int adjustedTime = 0;
        
        if (end != null) {
            
            endTime = end;
        }
        else {
            
            endTime = " ";
        }

        // sets the Point in this Course object's Y value to end after
        // formatting
        if ((end != null) && !(end.equals("N/A")) 
                && !(end.contains("ARR")) && !(end.equalsIgnoreCase("TBA"))
                && !(end.equals(""))) {
            
            // split the time string apart by the colon
            // ex. 1:30 pm --> {1, 30 pm}
            String[] temp = end.split(":");
            String[] temp2;
            
            if (temp.length >= 2) {
                // rejoin the split strings, into a string the same without the
                // colon
                String amPm = temp[0] + temp[1];
    
                if (amPm.contains(" ")) {
                    // split the time string apart by the space
                    // ex. 130 pm --> {130, pm}
                    temp2 = amPm.split(" ");
                }
                else {
    
                    //am
                    if (amPm.trim().contains("am") || amPm.trim().contains("AM")) {
                    
                        amPm = amPm.replace("am", " am");
                        amPm = amPm.replace("AM", " AM");
                    }
                    //pm
                    else {
                        amPm = amPm.replace("pm", " pm");
                        amPm = amPm.replace("PM", " PM");                    
                    }
                    
                    temp2 = amPm.split(" ");
                }
    
                //check to see if the am or pm was stuck to the end of the
                //time or not... (stuck to example. 3:00PM)
                if (temp2.length != 2) {
                    
                    //place temp 2 in temp22
                    String[] temp22 = temp2;
                    
                    //reset temp2 and make it 2 large
                    temp2 = new String[2];
                    //place the cut up temp22 back into temp 2 to work with everything else
                    temp2[1] = temp22[0].substring(temp22[0].length() - 2, temp22[0].length());
                    temp2[0] = temp22[0].substring(0, temp22[0].length() - 2);
                }
                
                adjustedTime = Integer.parseInt(temp2[0]);
                adjustedTime *= timeFactor;
    
                // check if the time is am or pm
                if (((temp2[1].contains("PM")) || (temp2[1].contains("pm")))
                        && (!(temp[0].equals("12")))) {
    
                    adjustedTime += 1200 * timeFactor;
                }
            }
            
            coursePoint.y = adjustedTime;
        }
    }

    /**
     * getter for the beginTIme
     * 
     * @return beginTime
     */
    public String getBeginTime() {

        return beginTime;
    }

    /**
     * getter for the endTime
     * 
     * @return endTime
     */
    public String getEndTime() {

        return endTime;
    }

    /**
     * returns a Point object that contains the adjusted beginning and end times
     * for this object
     * 
     * @return coursePoint an object holding the start and end times for this
     *         object
     */
    public Point getCoursePoint() {

        return coursePoint;
    }

    /**
     * setter method for the name of the course
     * 
     * @param name
     *            the name to set
     */
    public void setName(String name) {

        if (name != null && !name.equals("")) {
            name = name.replaceAll("&", "and");
            this.name = name;
        }
        else {
            
            name = "UNKNOWN";
        }
    }

    /**
     * getter method for the name of the course
     * 
     * @return the name
     */
    public String getName() {

        return name;
    }

    /**
     * @param teacherName
     *            the teacherName to set
     */
    public void setTeacherName(String teacherName) {

        if (teacherName != null && !teacherName.equals("") && !teacherName.equals("null")) {
            
            this.teacherName = teacherName;
        }
        else {
            
            teacherName = " ";
        }
    }

    /**
     * @return the teacherName
     */
    public String getTeacherName() {

        return teacherName;
    }

    /**
     * @param building
     *            the building to set
     */
    public void setBuilding(String building) {

        if (building != null && !building.equals("")) {
            
            this.building = building;
        }
        else {
            
            this.building = " ";
        }
        
    }

    /**
     * @return the building
     */
    public String getBuilding() {

        return building;
    }

    /**
     * @param room
     *            the room to set
     */
    public void setRoom(String room) {

        if (room != null && !room.equals("") && !room.equals("null")) {

            this.room = room;
        }
        else {

            this.room = " ";
        }

    }

    /**
     * @return the room
     */
    public String getRoom() {

        return room;
    }

    /**
     * @return the date
     */
    public Date getDate() {

        return date;
    }

    /**
     * @param date
     *            the date to set
     */
    public void setDate(Date date) {

        if (date != null) {
            
            this.date = date;
        }
        else {
            
            date = new Date();
        }
    }
    
    /**
     * @return the timeFactor
     */
    public int getTimeFactor() {

        return timeFactor;
    }

    /**
     * @param timeFactor the timeFactor to set
     */
    public void setTimeFactor(int timeFactor) {

        this.timeFactor = timeFactor;
    }

    /**
     * Equals method for Course, determines if two Course objects are equal.
     * 
     * Equality between Courses is defined as two Courses that have the same name,
     * same building, same room, and the same beginTime. 
     * 
     * @param thing
     *            the course to compare this one to
     * @return value true if they're equal, false otherwise
     */
    @Override
    public boolean equals(Object thing) {
        
        boolean value = false;

        if (thing == null) {

            value = false;
        }
        else if (thing instanceof Course) {

            Course other = (Course) thing;

            if (name == null || building == null || room == null
                    || beginTime == null) {

                value = false;
            }
            else if (name.equals(other.getName())
                    && building.equals(other.getBuilding())
                    && room.equals(other.getRoom())
                    && beginTime.equals(other.getBeginTime())) {
                
                value = true;
            }
        }

        return value;
    }

    /**
     * toXML method for the Course object returns an xml representation of
     * the course as below:
     * 
     * <Course> <Name></Name> <Teacher></Teacher> <Time></Time>
     * <Building></Building> <Room></Room> </Course>
     * 
     * If a date object exists in this Course, then the XML tags
     * <Date></Date> with the date between them comes after the
     * <Room></Room> tags.
     * 
     * @return returnString the XML string representation of the course.
     */
    public String toXML() {

        String returnString = "";

        String dateXML = "";
        
        if (date != null) {
            
            dateXML = "\n<Date>" + date.toString() + "</Date>";
        }
        
        returnString = "\n<Course>" + "\n<Name>" + name + "</Name>" 
                + "\n<Crn>" + crn + "</Crn>"
                + "\n<SubjectCode>" + subjectCode + "</SubjectCode>" 
                + "\n<CourseNumber>" + courseNumber + "</CourseNumber>"
                + "\n<Teacher>" + teacherName + "</Teacher>" + "\n<BeginTime>"
                + beginTime + "</BeginTime>" + "\n<EndTime>" + endTime
                + "</EndTime>" + "\n<Building>" + building + "</Building>"
                + "\n<Room>" + room + "</Room>" + dateXML
                + "\n<Credits>" + credits + "</Credits>" 
                + "\n<ClassSize>" + classSize + "</ClassSize>"
                + "\n<Days>" + days + "</Days>"+ "\n</Course>";

        return returnString;
    }

    /**
     * toString method for Course object, prints the Course's information 
     * in the following format:
     *      <name>
     *      <subjectCode> - <courseNumber>
     *      <teacherName>
     *      <beginTime> - <endTime>
     *      <building> <room>
     *      <date.toString()> (if date isn't null)
     */
    @Override
    public String toString() {

        String dateString = "";
        
        if (date != null) {

            dateString = date.toString();
        }

        return "\n" + name + "\n" + subjectCode + "-" + courseNumber + "\n" + teacherName + "\n" + beginTime + " - "
                + endTime + "\n" + building + " " + room + "\n" + dateString;
    }
    
    /**
     * Prints every field in the course object.
     * 
     * Useful debugging tool!
     * 
     * @return string holding all fields of this course.
     */
    public String totalToString() {

        String dateString = "";
        
        if (date != null) {

            dateString = date.toString();
        }
        
        return "\n" + crn + "\n" + name + "\n" + subjectCode + "-" + courseNumber + "\n" + teacherName + "\n" + beginTime + " - "
                + endTime + "\n" + building + " " + room + "\n" + dateString + "\n" + days + "\n" + credits + "\n" + classSize + "\n";
    }
    
    /**
     * Checks to see if all of the fields needed to build a URL. 
     * Those fields are: 
     *      subjectCode, courseNumber and crn.
     *      
     * The URL also requires term and year but those are not stored here.
     *  
     * @return true if the fields listed contain something, false otherwise.
     */
    public boolean hasURLFields() {
        
        if (subjectCode != null && subjectCode.length() >= 2 
                && courseNumber != null && courseNumber.length() >= 4
                && crn != null && crn.length() >= 3) {
            
            return true;
        }
        System.out.println("DOESN'T HAVE URL FIELDS??????????????????????????????????????????????????");
        return false;
    }

    /**
     * @return the subjectCode
     */
    public String getSubjectCode() {

        return subjectCode;
    }

    /**
     * @param subjectCode the subjectCode to set
     */
    public void setSubjectCode(String subjectCode) {

        this.subjectCode = subjectCode;
    }

    /**
     * @return the courseNumber
     */
    public String getCourseNumber() {

        return courseNumber;
    }

    /**
     * @param courseNumber the courseNumber to set
     */
    public void setCourseNumber(String courseNumber) {

        this.courseNumber = courseNumber;
    }

    /**
     * @return the crn
     */
    public String getCrn() {

        return crn;
    }

    /**
     * @param crn the crn to set
     */
    public void setCrn(String crn) {

        this.crn = crn;
    }
    
    /**
     * @return the Course code of the Course in the format:
     *          <subjectCode>-<courseNumber>
     */
    public String getCourseCode() {
        
        return subjectCode + "-" + courseNumber;
    }

    /**
     * @return the credits
     */
    public int getCredits() {

        return credits;
    }

    /**
     * @param credits the credits to set
     */
    public void setCredits(int credits) {

        this.credits = credits;
    }

    /**
     * @return the days
     */
    public String getDays() {

        return days;
    }

    /**
     * @param days the days to set
     */
    public void setDays(String days) {

        this.days = days;
    }

    /**
     * @return the classSize
     */
    public int getClassSize() {

        return classSize;
    }

    /**
     * @param classSize the classSize to set
     */
    public void setClassSize(int classSize) {

        this.classSize = classSize;
    }
}