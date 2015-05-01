package test.vtaccess;

import java.util.LinkedList;
import java.util.List;
import com.vtaccess.Cas;
import com.vtaccess.ScheduleScraper;
import com.vtaccess.exceptions.HokieSpaTimeoutException;
import com.vtaccess.exceptions.WrongLoginException;
import com.vtaccess.schedule.Course;
import com.vtaccess.schedule.Schedule;
import junit.framework.TestCase;


public class ScheduleScraperTest extends TestCase {

    //~Constants----------------------------------------------
    private static final String username = "";
    private static final String password = "";

    //~Data Fields--------------------------------------------


    //~Constructors--------------------------------------------


    //~Methods-------------------------------------------------
    /**
     * TO RUN THIS TEST YOU MUST PLACE A VALID HOKIESPA USERNAME AND PASSWORD IN THE APPROPRIATE FIELDS ABOVE
     */
    public void testRetrieveSchedule() {
        System.out.println("-----BEGIN RETRIEVE SCHEDULE TEST-----------");
        Cas cas = null;
        ScheduleScraper scheduleScraper = null;

        try {
            cas = new Cas(username.toCharArray(), password.toCharArray());
            scheduleScraper = new ScheduleScraper(cas);

            Schedule schedule = new Schedule();
            Schedule schedule2 = new Schedule();
            Schedule schedule3 = new Schedule();
            Schedule schedule4 = new Schedule();
            Schedule schedule5 = new Schedule();
            Schedule schedule6 = new Schedule();
            Schedule schedule7 = new Schedule();
            Schedule emptySchedule = new Schedule();

            assertTrue(scheduleScraper.retrieveSchedule(schedule, "201209"));
            assertTrue(!schedule.toXML().equals(emptySchedule.toXML()));
            System.out.println("GENERAL TEST PASSED!");

            //Failure cases
            assertFalse(scheduleScraper.retrieveSchedule(null, "201209"));
            assertFalse(scheduleScraper.retrieveSchedule(schedule, null));
            assertFalse(scheduleScraper.retrieveSchedule(null, null));
            System.out.println("FAILURE TESTS PASSED!");

            //far out of range case! (schedule 2)
            assertFalse(scheduleScraper.retrieveSchedule(schedule2, "209909"));
            assertTrue(schedule2.toXML().equals(emptySchedule.toXML()));
            System.out.println("FUTURE SEMESTERCODE TEST PASSED!");

            //far before range case (schedule 3)
            assertFalse(scheduleScraper.retrieveSchedule(schedule3, "200809"));
            assertTrue(schedule3.toXML().equals(emptySchedule.toXML()));
            System.out.println("WAY BACK WHEN TEST PASSED!");

            //Enrolled at VT but not semester
            assertFalse(scheduleScraper.retrieveSchedule(schedule4, "201207"));
            assertTrue(schedule4.toXML().equals(emptySchedule.toXML()));
            System.out.println("NON-PARTICIPATING SEMESTER TEST PASSED!");

            //Totally invalid semesterCode (but correct length)
            assertFalse(scheduleScraper.retrieveSchedule(schedule5, "548766"));
            assertTrue(schedule5.toXML().equals(emptySchedule.toXML()));
            System.out.println("INVALID SEMESTER CODE TEST PASSED!");

            //Wrong length semesterCode
            assertFalse(scheduleScraper.retrieveSchedule(schedule6, "20120999"));
            assertTrue(schedule6.toXML().equals(emptySchedule.toXML()));
            System.out.println("TOO LONG SEMESTER CODE TEST PASSED!");

            assertTrue(cas.isActive());
            //Additional Times course
            System.out.println("\n\nADDITIONAL TIMES CHECK!!!!!!!!");
            assertTrue(scheduleScraper.retrieveSchedule(schedule7, "201109"));
            System.out.println(schedule7.toXML());
            System.out.println("********************************************************");
            System.out.println("********************************************************");
            System.out.println(schedule7.toString());
        }
        catch (WrongLoginException e) {
            e.printStackTrace();
        }
        catch (HokieSpaTimeoutException e) {
            e.printStackTrace();
        }
    }

    public void testRetrieveExamTimes() {
        System.out.println("-----BEGIN RETRIEVE EXAMS TEST-----------");
        Cas cas = null;
        ScheduleScraper scheduleScraper = null;

        String currentSemesterCode = "201501";
        
        try {
            cas = new Cas(username.toCharArray(), password.toCharArray());
            scheduleScraper = new ScheduleScraper(cas);

            List<Course> finals1 = new LinkedList<Course>();
            List<Course> finals2 = new LinkedList<Course>();
            List<Course> finals3 = new LinkedList<Course>();
            List<Course> finals4 = new LinkedList<Course>();
            List<Course> finals5 = new LinkedList<Course>();
            List<Course> finals6 = new LinkedList<Course>();
            List<Course> finals7 = new LinkedList<Course>();
            Course emptyCourse = new Course();

            assertTrue(scheduleScraper.retrieveExamSchedule(currentSemesterCode, finals1));
            assertTrue(finals1.size() > 0);
            for (Course course : finals1) {
                assertTrue(!course.toXML().equals(emptyCourse.toXML()));
                System.out.println(emptyCourse.toXML());
            }
            System.out.println("GENERAL TEST PASSED!");

            //Failure cases
            assertFalse(scheduleScraper.retrieveExamSchedule(currentSemesterCode, null));
            assertFalse(scheduleScraper.retrieveExamSchedule(null, finals1));
            assertFalse(scheduleScraper.retrieveExamSchedule(null, null));
            System.out.println("FAILURE TESTS PASSED!");

            //far out of range case! (schedule 2)
            assertFalse(scheduleScraper.retrieveExamSchedule("209909", finals2));
            for (Course course : finals2)
                assertTrue(course.toXML().equals(emptyCourse.toXML()));
            System.out.println("FUTURE SEMESTERCODE TEST PASSED!");

            //far before range case (schedule 3)
            assertFalse(scheduleScraper.retrieveExamSchedule("200809", finals3));
            for (Course course : finals3)
                assertTrue(course.toXML().equals(emptyCourse.toXML()));
            System.out.println("WAY BACK WHEN TEST PASSED!");

            //Enrolled at VT but not semester
            assertFalse(scheduleScraper.retrieveExamSchedule("201207", finals4));
            for (Course course : finals4)
                assertTrue(course.toXML().equals(emptyCourse.toXML()));
            System.out.println("NON-PARTICIPATING SEMESTER TEST PASSED!");

            //Totally invalid semesterCode (but correct length)
            assertFalse(scheduleScraper.retrieveExamSchedule("548766", finals5));
            for (Course course : finals5)
                assertTrue(course.toXML().equals(emptyCourse.toXML()));
            System.out.println("INVALID SEMESTER CODE TEST PASSED!");

            //Wrong length semesterCode
            assertFalse(scheduleScraper.retrieveExamSchedule("20120999", finals6));
            for (Course course : finals6)
                assertTrue(course.toXML().equals(emptyCourse.toXML()));
            System.out.println("TOO LONG SEMESTER CODE TEST PASSED!");

            assertTrue(cas.isActive());
            //Additional Times course
            System.out.println("\n\nADDITIONAL TIMES CHECK!!!!!!!!");
            assertTrue(scheduleScraper.retrieveExamSchedule("201109", finals7));
            for (Course course : finals7)
                System.out.println(course.toXML());
            System.out.println("********************************************************");
            System.out.println("********************************************************");
            for (Course course : finals7)
                System.out.println(course.toString());
            System.out.println("\nEND\nEND ADDITIONAL TIMES CHECK!!!!!!!!");
        }
        catch (WrongLoginException e) {
            e.printStackTrace();
        }
        catch (HokieSpaTimeoutException e) {
            e.printStackTrace();
        }
    }
}