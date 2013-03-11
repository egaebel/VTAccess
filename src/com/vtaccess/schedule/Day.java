package com.vtaccess.schedule;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains an ArrayList of Courses that represent a student's course-load for
 * any given day.
 * 
 * @author Ethan Gaebel(egaebel)
 * 
 */
public class Day {

    // ~Data Fields------------------------------------------------------------
    /**
     * holds a list of Courses in a given day
     */
    protected List<Course> daily;

    /**
     * the String designating what day of the week this day is
     */
    protected String thisDay;

    // ~Constructors------------------------------------------------------------
    /**
     * initializes the ArrayList daily and sets the String value of thisDay
     */
    public Day(String thisDay) {

        daily = new ArrayList<Course>();
        this.thisDay = thisDay;
    }

    // ~Methods------------------------------------------------------------
    /**
     * Adds a course to the Day's List
     * 
     * @param name the name of the Course.
     * @param teacherName the name of the teacher of the Course.
     * @param beginTime the time that the Course begins at.
     * @param endTime the time that the Course ends at.
     * @param building the building name that this Course is in.
     * @param room the room number that this Course is in.
     */
    public void addCourse(String name, String teacherName, String beginTime,
            String endTime, String building, String room) {

        Course addingCourse = new Course(name, teacherName, beginTime, endTime,
                building, room);

        daily.add(addingCourse);
    }

    /**
     * Adds a course to the Day's List
     * 
     * @param crn the crn of the Course.
     * @param name the name of the Course.
     * @param subjectCode the subjectCode of the Course. (e.g. MATH, CS)
     * @param courseNumber the courseNumber of the Course (e.g. 2114, 1114, 2505)
     * @param teacherName the name of the teacher of the Course.
     * @param beginTime the time that the Course begins at.
     * @param endTime the time that the Course ends at.
     * @param building the building name that this Course is in.
     * @param room the room number that this Course is in.
     * @param credits the number of credits (in string form) that this course is worth.
     * @param capactiy either the number of people in the class or the capacity of the class.
     * @param days letters denoting the days of the week this course is on.
     */
    public void addCourse(String crn, String name, String subjectCode, String courseNumber, String credits, String capacity, 
            String teacherName, String beginTime,
            String endTime, String building, String room, String days) {

        int numCredits;
        int numStudents;
        
        //Convert the credits to an int, or 0 by default
        if (isNumber(credits)) {
            
            numCredits = Integer.parseInt(credits);
        }
        else {
            
            numCredits = 0;
        }
        
        //Convert capacity to an int, or 0 by default
        if (isNumber(capacity)) {
            
            numStudents = Integer.parseInt(capacity);
        }
        else {
            
            numStudents = 0;
        }
        
        Course addingCourse = new Course(crn, name, subjectCode, courseNumber, 
                numCredits, numStudents, teacherName, beginTime, endTime,
                building, room);

        daily.add(addingCourse);
    }
    
    /**
     * Adds the passed course to this object's daily course list.
     * 
     * @param newCourse
     *            the course to add to the daily list.
     */
    public void addCourse(Course newCourse) {

        daily.add(newCourse);
    }

    /**
     * Removes the passed in course from the ArrayList
     * 
     * @param remove
     *            course to be removed
     */
    public void removeCourse(Course remove) {

        daily.remove(remove);
    }

    /**
     * Checks to see if the daily course list has any members in it.
     * Returns true if there are courses, false if there are none.
     * 
     * @return true if there are courses, false if there aren't.
     */
    public boolean hasCourses() {
        
        return !daily.isEmpty();
    }
    
    /**
     * Gets the Course at a specified index.
     * 
     * @param index
     *            the index of the Course to get.
     * @return the Course at the specified index, or null if the index is invalid.
     */
    public Course getCourse(int index) {

        if (daily.size() > index && index >= 0) {
            
            return daily.get(index);
        }
        
        return null;
        
    }

    /**
     * Performs an insertion sort. Sorted by start time on the ArrayList<Course>.
     */
    public void sortCourses() {

        Course course = null;
        int j = 0;
        for (int i = 1; i < size(); i++) {
            
            j = i; 
            //checks to see if the start time of the element j is less than the previous element, and if j > 0
            while (j > 0) {
                
                if ((daily.get(j).getCoursePoint().getX() < daily.get(j - 1).getCoursePoint().getX())) {
                
                    //if so, swap the two
                    course = daily.get(j);
                    daily.set(j, daily.get(j - 1));
                    daily.set(j - 1, course);
                }
                
                //decrement j
                j--;
            }
        }
    }

    /**
     * Setter method for the daily List of courses.
     * 
     * @param theList
     *            the ArrayList<Course> to set daily equal to.
     */
    public void setList(List<Course> theList) {

        daily = new ArrayList<Course>(theList);
    }

    /**
     * Getter method for the daily ArrayList of courses.
     * 
     * @return daily the arrayList with the courses of the day.
     */
    public List<Course> getList() {

        return daily;
    }

    /**
     * Sets the thisDay object which designated what day this Day object
     * represents.
     * 
     * @param thisDay
     *            the String designating which day of the week thisDay is.
     */
    public void setThisDay(String thisDay) {

        this.thisDay = thisDay;
    }
    
    /**
     * Getter method for the thisDay String which identifies the name of 
     * the Day. (e.g. Monday, Tuesday etc.).
     * @return thisDay the name of this day.
     */
    public String getThisDay() {
        
        return thisDay;
    }

    /**
     * Tells what day of the week this day object represents. 
     * 
     * @return thisDay the String representing this day.
     */
    @Override
    public String toString() {

        return thisDay;
    }

    /**
     * Returns the size of the daily arrayList.
     * @return the size of the Course List.
     */
    public int size() {

        return daily.size();
    }

    /**
     * toXML method for the Day object.
     * 
     * <"THISDAY"><Course>.....</Course></"THISDAY">
     */
    public String toXML() {

        String returnString = "";
        String courseList = "";

        for (Course course : daily) {

            courseList = courseList + course.toXML();
        }

        returnString = "\n<" + thisDay + ">" + courseList + "\n</" + thisDay
                + ">";

        return returnString;
    }

    /**
     * Compares this day object to a passed in day object, returns an ArrayList
     * of all of the Courses that the two days share.
     * 
     * @param other
     *            the passed in day to compare this day to.
     * @return shared the ArrayList of courses the two days have in common.
     */
    public Day compareDays(Day other) {

        Day shared;

        // if the passed in day is null, return null
        if (other == null) {

            shared = null;
        }
        else {

            // initialized shared with the string representation of "this" day
            shared = new Day(thisDay);

            // loops through this course list
            for (int i = 0; i < this.getList().size(); i++) {

                // loops through other course List
                for (int j = 0; j < other.getList().size(); j++) {

                    // if two days have the same course, then add them to the
                    // shared Day
                    if (this.getList().get(i).equals(other.getList().get(j))) {

                        shared.addCourse(this.getList().get(i));
                    }
                }
            }
        }

        return shared;
    }

    /**
     * Equals method for Day, takes in a Day object and checks to see if it 
     * is equal to this Day object.
     * 
     * @param other the other Day object.
     * @return true if the Days are equal, false otherwise.
     */
    @Override
    public boolean equals(Object other) {

        boolean value = false;

        // if other is null
        if (other == null) {

            value = false;
        }
        // otherwise
        else {

            // if other isnt a Day object
            if (!(other instanceof Day)) {

                value = false;
            }
            // otherwise
            else {

                if (this.toString().equals(((Day) other).toString())) {

                    // if the sizes of the courseLists arent the same, false
                    if (this.size() != ((Day) other).size()) {

                        value = false;
                    }
                    // if the sizes are both 0, true
                    else if (this.size() == 0) {

                        value = true;
                    }
                    // otherwise cycle through courses
                    else {

                        // cycle through course lists of both days
                        for (int i = 0; i < this.size(); i++) {

                            // if there is a course in other that doesnt equal
                            // the corresponding course in this,
                            // then break loop, value = false
                            if (!(this.getCourse(i).equals(((Day) other)
                                    .getCourse(i)))) {

                                value = false;
                                break;
                            }
                            // otherwise
                            else {

                                value = true;
                            }
                        }
                    }
                }
                // otherwise
                else {

                    value = false;
                }
            }
        }

        return value;
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
}