package com.vtaccess.schedule;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

/**
 * Class to hold 6 day objects that represent the 5 work days of the week, and one for
 * AnyDay of the week to hold Courses that can be done on any day.
 * 
 * @author Ethan Gaebel (egaebel)
 * 
 */
public class Schedule {

    // ~DataFields----------------------------------------------
    /**
     * Day object for Monday.
     */
    protected Day monday;
    /**
     * Day object for Tuesday.
     */
    protected Day tuesday;
    /**
     * Day object for Wednesday.
     */
    protected Day wednesday;
    /**
     * Day object for Thursday.
     */
    protected Day thursday;
    /**
     * Day object for Friday.
     */
    protected Day friday;
    /**
     * Day object for variable Day Courses.
     */
    protected Day anyDay;
    /**
     * Assigned to the Day object that is today.
     */
    protected Day today;
    /**
     * The name of the person who this schedule belongs to.
     * 
     * "MySchedule" if no name is specified.
     */
    protected String whosSchedule;
    /**
     * Calendar object used to get what today's day is.
     */
    private Calendar cal;

    // ~Constructor----------------------------------------------
    /**
     * Default constructor for the Schedule class. Initializes all objects,
     * and sets whosSchedule String to "MySchedule."
     */
    public Schedule() {

        monday = new Day("Monday");
        tuesday = new Day("Tuesday");
        wednesday = new Day("Wednesday");
        thursday = new Day("Thursday");
        friday = new Day("Friday");
        anyDay = new Day("AnyDay");

        whosSchedule = "MySchedule";

        setToday();
    }

    /**
     * Constructor that initializes all objects and sets
     * whosSchedule String to the passed in scheduleOwner String.
     * 
     * Note: If null value is passed, scheduleOwner = "none";
     * 
     * @param scheduleOwner the String of the name of the scheduleOwner.
     */
    public Schedule(String scheduleOwner) {

        monday = new Day("Monday");
        tuesday = new Day("Tuesday");
        wednesday = new Day("Wednesday");
        thursday = new Day("Thursday");
        friday = new Day("Friday");
        anyDay = new Day("AnyDay");

        if (scheduleOwner != null) {
            whosSchedule = scheduleOwner;
        }
        else {
            whosSchedule = "none";
        }

        setToday();
    }

    /**
     * Initializes this schedule to a passed in Schedule. 
     * 
     * Used for receiving sent schedule objects.
     * 
     * Note: If a null value is passed, all objects are initialized,
     *          and whosSchedule = "MySchedule".
     * 
     * @param schedule
     *            a passed in schedule to set this schedule to.
     */
    public Schedule(Schedule schedule) {

        if (schedule != null) {
        
            monday = schedule.getMonday();
            tuesday = schedule.getTuesday();
            wednesday = schedule.getWednesday();
            thursday = schedule.getThursday();
            friday = schedule.getFriday();
            anyDay = schedule.getAnyDay();
    
            whosSchedule = schedule.getWhosSchedule();
    
            setToday();
        }
        else {
            
            monday = new Day("Monday");
            tuesday = new Day("Tuesday");
            wednesday = new Day("Wednesday");
            thursday = new Day("Thursday");
            friday = new Day("Friday");
            anyDay = new Day("AnyDay");

            whosSchedule = "MySchedule";

            setToday();
        }
    }

    // ~Methods-------------------------------------------------------------------------------------
    /**
     * Set the owner of this Schedule's name.
     * 
     * @param name
     *            the name of the owner of the Schedule.
     */
    public void setWhosSchedule(String name) {

        whosSchedule = name;
    }

    /**
     * Get the owner of this Schedule's name.
     * 
     * @return whosSchedule the name of the owner of this Schedule.
     */
    public String getWhosSchedule() {

        return whosSchedule;
    }

    /**
     * sets a day by passed in parameter
     * 
     * @param name
     *            name of the course
     * @param teacherName
     *            name of the teacher of the course
     * @param beginTime
     *            time the class starts
     * @param endTime
     *            time the class ends
     * @param building
     *            Builing of the course
     * @param room 
     *            the room number that the course is in. 
     * @param days
     *            the one letter character indicating the day the course is
     *            taught (several of them)
     */
    public void setDay(String days, String name, String teacherName,
            String beginTime, String endTime, String building, String room) {

        String day = "";

        while (!days.isEmpty()) {

            // set day equal to one character denoting a day
            day = days.substring(0, 1);
            // eliminate the character that day was set to from days
            days = days.replaceFirst(day, "");

            if (day.equals("M")) {

                monday.addCourse(name, teacherName, beginTime, endTime,
                        building, room);
            }
            else if (day.equals("T")) {

                tuesday.addCourse(name, teacherName, beginTime, endTime,
                        building, room);
            }
            else if (day.equals("W")) {

                wednesday.addCourse(name, teacherName, beginTime, endTime,
                        building, room);
            }
            else if (day.equals("R")) {

                thursday.addCourse(name, teacherName, beginTime, endTime,
                        building, room);
            }
            else if (day.equals("F")) {

                friday.addCourse(name, teacherName, beginTime, endTime,
                        building, room);
            }
            else if (day.equals("TBA")) {

                anyDay.addCourse(name, teacherName, beginTime, endTime,
                        building, room);
            }
            else {

                // enter a string thing
            }
        }
    }
    
    /**
     * Takes in a String representing to the days that a course object is held in, 
     * as well as a course object itself.
     * 
     * @param days a string of characters where each day is represented by a letter.
     *      M = Monday
     *      T = Tuesday
     *      W = Wednesday
     *      R = Thursday
     *      F = Friday
     *      else = AnyDay
     */
    public void setCourseInDays(Course course, String days) {
        
        for (int i = 0; i < days.length(); i++) {
            if (days.substring(i, i + 1).equals("M")) {
                monday.addCourse(course);
            }
            else if (days.substring(i, i + 1).equals("T")) {
                tuesday.addCourse(course);
            }
            else if (days.substring(i, i + 1).equals("W")) {
                wednesday.addCourse(course);
            }
            else if (days.substring(i, i + 1).equals("R")) {
                thursday.addCourse(course);
            }
            else if (days.substring(i, i + 1).equals("F")) {
                friday.addCourse(course);
            }
            else {        
                anyDay.addCourse(course);
                break;
            }
        }
    }

    /**
     * Returns a Day object based on the index passed in
     * 
     * 0 = monday
     * 1 = tuesday
     * 2 = wednesday
     * 3 = thursday
     * 4 = friday
     * 5 = anyDay
     * 
     * @param index the index of the day to select, 
     *      0 = monday
     *      1 = tuesday
     *      2 = wednesday
     *      3 = thursday
     *      4 = friday
     *      5 = anyDay
     * @return the Day object corresponding to the passed in index. 
     *              Null if incorrect index supplied.
     */
    public Day getDay(int index) {
        
        switch(index) {
            
            case 0: return monday;
            case 1: return tuesday; 
            case 2: return wednesday;
            case 3: return thursday;
            case 4: return friday;
            case 5: return anyDay;
            default: return null;
        }
    }

    /**
     * Gets the index of the day that today is.
     * 
     * @return the index of the day that today is.
     */
    public int getTodayIndex() {
        
        if (today.equals(monday)) {
            
            return 0;
        }
        else if (today.equals(tuesday)) {
            
            return 1;
        }
        else if (today.equals(wednesday)) {
            
            return 2;
        }
        else if (today.equals(thursday)) {
            
            return 3;
        }
        else if (today.equals(friday)) {
            
            return 4;
        }
        else if (today.equals(anyDay)) {
            
            return 5;
        }
        else {
            
            return -1;
        }
    }
    
    /**
     * Gets the index of the passed Day object. 
     * 
     * @param theDay the Day to get the index of.
     * @return the index of the day passed. -1 if the Day passed
     *          was not one of the days in this Schedule.
     */
    public int getIndexFromDay(Day theDay) {
        
        if (theDay.equals(monday)) {
            
            return 0;
        }
        else if (theDay.equals(tuesday)) {
            
            return 1;
        }
        else if (theDay.equals(wednesday)) {
            
            return 2;
        }
        else if (theDay.equals(thursday)) {
            
            return 3;
        }
        else if (theDay.equals(friday)) {
            
            return 4;
        }
        else if (theDay.equals(anyDay)) {
            
            return 5;
        }
        else {
            
            return -1;
        }
    }
    
    /**
     * Sets the 'today' Day object to today's date.
     */
    public void setToday() {

        cal = Calendar.getInstance();

        switch (cal.get(Calendar.DAY_OF_WEEK)) {

            case Calendar.MONDAY:

                today = monday;
                break;

            case Calendar.TUESDAY:

                today = tuesday;
                break;

            case Calendar.WEDNESDAY:

                today = wednesday;
                break;

            case Calendar.THURSDAY:

                today = thursday;
                break;

            case Calendar.FRIDAY:

                today = friday;
                break;

            default:

                if (anyDay.getList().size() > 0) {

                    today = anyDay;
                }
                else {

                    today = monday;
                }
                break;
        }
    }

    /**
     * returns the Day object that represents today
     * 
     * @return today the day that is today
     */
    public Day getToday() {

        return today;
    }

    /**
     * Flips today to the "left" of today and returns today. If AnyDay has no Course 
     * objects it is skipped.
     * 
     * @return today the Day object to the "left" of the previous today Day.
     *          ex. Tuesday to Monday; Thursday to Wednesday.
     */
    public Day getLeftOfToday() {

        Day[] days = daysToArray();

        int index = -1;

        // loop through the days array until...
        for (int i = 0; i < days.length; i++) {

            // the day representing today is found
            if (days[i].equals(getToday())) {

                index = i;
                break;
            }
        }

        // if the index is at its minimum
        if (index == 0) {

            // today is the "last" day (anyDay)
            today = days[days.length - 1];
            if (!today.hasCourses()) {
                return getLeftOfToday();
            }
        }
        else {

            // today is the day before the old today
            if (index - 1 >= 0) {
                
                today = days[index - 1];
            }
        }

        return today;
    }

    /**
     * Flips today to the "right" of today and returns today. If AnyDay has no Course 
     * objects it is skipped.
     * 
     * @return today the Day object to the "right" of the previous today Day.
     *          ex. Monday to Tuesday; Wednesday to Thursday.
     */
    public Day getRightOfToday() {

        Day[] days = daysToArray();

        int index = -1;

        // loop through the days array until...
        for (int i = 0; i < days.length; i++) {

            // the day representing today is found
            if (days[i].equals(getToday())) {

                index = i;
                break;
            }
        }

        // if the index is at its max already
        if (index == (days.length - 1)) {

            // today is the first day of the week
            today = days[0];
        }
        // otherwise
        else {

            // today is the day after the old today
            today = days[index + 1];
            if (!today.hasCourses() && today.getThisDay().equalsIgnoreCase("anyday")) {
                
                return getRightOfToday();
            }
                
        }

        return today;
    }

    /**
     * Returns true if the Schedule has any classes, false if it doesn't.
     * 
     * @return value true if the Schedule is empty, false otherwise.
     */
    public boolean isEmpty() {

        boolean value;

        if (monday.getList().isEmpty() && tuesday.getList().isEmpty()
                && wednesday.getList().isEmpty()
                && thursday.getList().isEmpty() && friday.getList().isEmpty()
                && anyDay.getList().isEmpty()) {

            value = true;
        }
        else {

            value = false;
        }

        return value;
    }

    /**
     * Returns a Day array of all of the day objects in this class.
     * 
     * @return days an array of all the Day objects.
     */
    public Day[] daysToArray() {

        Day[] days = { monday, tuesday, wednesday, thursday, friday, anyDay };

        return days;
    }

    /**
     * Takes an array of Days and updates the Day objects with them
     * 
     * @param days
     *            array of all of the days to be translated into the day objects.
     */
    public void arrayToDays(Day[] days) {

        monday = days[0];
        tuesday = days[1];
        wednesday = days[2];
        thursday = days[3];
        friday = days[4];
        anyDay = days[5];
    }

    /**
     * Runs insertion sort on all of the days of the week.
     */
    public void sortDays() {
        
        monday.sortCourses();
        tuesday.sortCourses();
        wednesday.sortCourses();
        thursday.sortCourses();
        friday.sortCourses();
    }
    
    /**
     * Returns the number of Day objects contained in this Schedule.
     * Should be overriden if extending Schedule, so that it may function
     * with the ScheduleIO class, and others correctly. 
     * 
     * @return the number of days in this Schedule. Which is 6.
     */
    public int size() {
        
        return 6;
    }
    
    /**
     * Outputs schedule in xml format: <Schedule> <Owner></Owner> <Monday>
     * <Course></Course> <Course></Course> <Course></Course> </Monday> .....
     * </Schedule>
     * 
     * @return returnString the String representation of the schedule object in
     *         the above form.
     */
    public String toXML() {

        String returnString = "<Schedule>" + "\n<Owner>" + whosSchedule
                + "</Owner>" + monday.toXML() + tuesday.toXML()
                + wednesday.toXML() + thursday.toXML() + friday.toXML()
                + anyDay.toXML() + "\n</Schedule>";

        return returnString;
    }

    /**
     * Checks to see if this Schedule is equal to a passed in Schedule.
     * Returns true if the objects are equal, false otherwise.
     * 
     * @param other a Schedule object to compare to this one.
     * @return true if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object other) {

        boolean value = false;

        if (other instanceof Schedule) {

            for (int i = 0; i < 6; i++) {

                if (this.daysToArray()[i].equals(((Schedule) other)
                        .daysToArray()[i])) {

                    value = true;
                }
                else {

                    value = false;
                    break;
                }
            }
        }

        return value;
    }

    /**
     * Takes another Schedule and compares it to this schedule to see which
     * Courses the two have in common.
     * 
     * Returns a Schedule of all courses that the two schedules have in common.
     * 
     * @param other
     *            the Schedule to compare this one to.
     * @return shared the Schedule holding all of the courses that are in
     *         common.
     */
    public Schedule compareSchedules(Schedule other) {

        Schedule shared;

        Day[] days;

        if (other == null) {

            shared = null;
            days = null;
        }
        else {

            shared = new Schedule();
            days = shared.daysToArray();

            // compares each day to see if there are similar courses
            for (int i = 0; i < daysToArray().length; i++) {

                // compares the courses for this day, adds them to the
                // shared Schedule object
                days[i] = this.daysToArray()[i].compareDays(other.daysToArray()[i]);
            }

            shared.arrayToDays(days);
        }
        
        //sets 'Today' within the shared Schedule, because the days have NOW all been set
        shared.setToday();
        
        //returns the shared Schedule
        return shared;
    }

    // ~THE DAYS OF THE WEEK SETTER AND
    // GETTERS------------------------------------------------------------------------
    /**
     * Gets a list of all of the individual course objects in this Schedule.
     * 
     * @return a List<Course> of all unique Courses in this schedule.
     */
    public List<Course> getAllCourses() {
        
        List<Course> courses = new LinkedList<Course>();
        
        for (int i = 0; i < 6; i++) {
            
            if(getDay(i).size() > 0) {
                for (Course course : getDay(i).getList()) {
                    
                    if (!courses.contains(course)) {
                        
                        courses.add(course);
                    }
                }
            }
        }
        
        return courses;
    }
    
    /**
     * @param monday
     *            the monday to set
     */
    public void setMonday(Day monday) {

        this.monday = monday;
    }

    /**
     * @return the monday
     */
    public Day getMonday() {

        return monday;
    }

    /**
     * @param tuesday
     *            the tuesday to set
     */
    public void setTuesday(Day tuesday) {

        this.tuesday = tuesday;
    }

    /**
     * @return the tuesday
     */
    public Day getTuesday() {

        return tuesday;
    }

    /**
     * @param wednesday
     *            the wednesday to set
     */
    public void setWednesday(Day wednesday) {

        this.wednesday = wednesday;
    }

    /**
     * @return the wednesday
     */
    public Day getWednesday() {

        return wednesday;
    }

    /**
     * @param thursday
     *            the thursday to set
     */
    public void setThursday(Day thursday) {

        this.thursday = thursday;
    }

    /**
     * @return the thursday
     */
    public Day getThursday() {

        return thursday;
    }

    /**
     * @param friday
     *            the friday to set
     */
    public void setFriday(Day friday) {

        this.friday = friday;
    }

    /**
     * @return the friday
     */
    public Day getFriday() {

        return friday;
    }

    /**
     * @param anyDay
     *            the anyDay to set
     */
    public void setAnyDay(Day anyDay) {

        this.anyDay = anyDay;
    }

    /**
     * @return the anyDay
     */
    public Day getAnyDay() {

        return anyDay;
    }
}
