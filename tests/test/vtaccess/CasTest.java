package test.vtaccess;

import java.util.LinkedList;
import java.util.List;
import com.vtaccess.Cas;
import com.vtaccess.ScheduleScraper;
import com.vtaccess.Semester;
import com.vtaccess.exceptions.HokieSpaTimeoutException;
import com.vtaccess.exceptions.WrongLoginException;
import com.vtaccess.schedule.Course;
import com.vtaccess.schedule.Schedule;
import junit.framework.TestCase;


/**
 * TO RUN THIS TEST YOU MUST PLACE A VALID HOKIESPA USERNAME AND PASSWORD IN THE APPROPRIATE FIELDS BELOW
 */
public class CasTest extends TestCase {

    //~Constants----------------------------------------------
    private static final String SPRING_CODE = "201301";
    private static final String FALL_CODE = "201209";
    private static final String username = "";
    private static final String password = "";
    private static final String filePath = "";
    
    //~Data Fields--------------------------------------------
    private Schedule emptySchedule;

    //~Constructors--------------------------------------------
    protected void setUp() throws Exception {

        super.setUp();
        emptySchedule = new Schedule();
    }

    //~Methods--------------------------------------------
    public void testSwitchUsersNOFILEPATH() throws WrongLoginException, HokieSpaTimeoutException {

        Cas cas = new Cas(username.toCharArray(), password.toCharArray());
        ScheduleScraper s = new ScheduleScraper(cas);
        assertTrue(s.retrieveSchedule(new Schedule(), SPRING_CODE));

        cas.switchUsers(username.toCharArray(), password.toCharArray());
        assertTrue(s.retrieveSchedule(new Schedule(), SPRING_CODE));
    }

    public void testSwitchUsersFILEPATH() throws WrongLoginException, HokieSpaTimeoutException {

        Cas cas = new Cas(username.toCharArray(), password.toCharArray(), "");
        ScheduleScraper s = new ScheduleScraper(cas);
        assertTrue(s.retrieveSchedule(new Schedule(), SPRING_CODE));

        cas.switchUsers(username.toCharArray(), password.toCharArray(), "");
        assertTrue(s.retrieveSchedule(new Schedule(), SPRING_CODE));
    }

    public void testMultipleLogins() throws WrongLoginException, HokieSpaTimeoutException {
        System.out.println("TEST MULTIPLE LOGINS");
        Cas cas = null;
        cas = new Cas(username.toCharArray(), password.toCharArray());
        ScheduleScraper s = new ScheduleScraper(cas);

        Schedule s1 = new Schedule();
        Schedule s2 = new Schedule();
        Schedule s3 = new Schedule();
        
        //Pull Courses into 3 different schedules
        assertTrue(s.retrieveSchedule(s1, SPRING_CODE));
        assertTrue(s.retrieveSchedule(s2, FALL_CODE));
        assertTrue(s.retrieveSchedule(s3, FALL_CODE));
        
        assertNotSame(s3.getAllCourses().toString(), "");
        assertEquals(s3.getAllCourses().toString(), s2.getAllCourses().toString());
        s3 = new Schedule();

        //replace s3 with the next code
        assertTrue(s.retrieveSchedule(s3, Semester.nextSemesterCode(FALL_CODE)));
        assertNotSame(s3.getAllCourses().toString(), "");
        assertEquals(s3.getAllCourses().toString(), s1.getAllCourses().toString());
        s3 = new Schedule();

        assertTrue(s.retrieveSchedule(s3, FALL_CODE));
        assertNotSame(s3.getAllCourses().toString(), "");
        assertEquals(s3.getAllCourses().toString(), s2.getAllCourses().toString());




        System.out.println(s1.getAllCourses().toString());
        System.out.println(s2.getAllCourses().toString());
        assertNotSame(s1.getAllCourses().toString(), "");
        assertNotSame(s2.getAllCourses().toString(), "");

        //Finals stuff
        List<Course> finalsList1 = new LinkedList<Course>();
        List<Course> finalsList2 = new LinkedList<Course>();
        
        assertTrue(s.retrieveExamSchedule(SPRING_CODE, finalsList1));
        System.out.println("retrieve exam schedule 1 RESULTES!!!\n" + finalsList1.toString());
        assertTrue(finalsList1.size() > 0);
        
        assertTrue(s.retrieveExamSchedule(SPRING_CODE, finalsList2));
        System.out.println("retrieve exam schedule 2 RESULTES!!!\n" + finalsList2.toString());
        assertTrue(finalsList2.size() > 0);

        System.out.println("COMPARISON");
        System.out.println(finalsList1.toString());
        System.out.println(finalsList2.toString());
    }


    public void testLoginNoPath() throws HokieSpaTimeoutException {
        System.out.println("TEST LOGIN, NO PATH");

        Cas cas = null;
        ScheduleScraper scheduleScraper = null;


        try {
            cas = new Cas(username.toCharArray(), password.toCharArray());
            scheduleScraper = new ScheduleScraper(cas);
        }
        catch (WrongLoginException e) {
            e.printStackTrace();
            System.out.println("ERROR!!!!");
            assertTrue(false);
        }

        Schedule schedule = new Schedule();
        String semester = "201209";

        scheduleScraper.retrieveSchedule(schedule, semester);

        System.out.println("The schedule pulled in testLoginNoPath:");
        System.out.println(schedule.toXML());
        System.out.println("\n\n");
        
        assertTrue(!schedule.toXML().equals(emptySchedule.toXML()));

        assertTrue(cas.isActive());

        cas.closeSession();

        assertFalse(cas.isActive());

        assertFalse(scheduleScraper.retrieveSchedule(schedule, semester));
    }

    public void testLoginPath() throws HokieSpaTimeoutException {
        
        System.out.println("TEST LOGIN, WITH PATH");
        Cas cas = null;
        ScheduleScraper scheduleScraper = null;

        try {
            cas = new Cas(username.toCharArray(), password.toCharArray(), "");
            scheduleScraper = new ScheduleScraper(cas);
        }
        catch (WrongLoginException e) {
            e.printStackTrace();
            System.out.println("ERROR!!!!");
        }

        Schedule schedule = new Schedule();
        String semester = "201209";

        scheduleScraper.retrieveSchedule(schedule, semester);

        assertTrue(!schedule.toXML().equals(emptySchedule.toXML()));

        assertTrue(cas.isActive());

        cas.closeSession();

        assertFalse(cas.isActive());

        scheduleScraper.retrieveSchedule(schedule, semester);
    }

    public void testWrongLoginExceptionNoPath() {
        System.out.println("TEST WRONG LOGIN EXCEPTION NO PATH");
        WrongLoginException except = null;
        @SuppressWarnings("unused")
        Cas cas;

        //test wrong username
        try {
            cas = new Cas("egsadasdelasdasd".toCharArray(), password.toCharArray());
        }
        catch (WrongLoginException e) {

            except = e;
        }

        assertNotNull(except);

        except = null;

        //test wrong password
        try {
            cas = new Cas(username.toCharArray(), "hjkl;lkl;lk".toCharArray());
        }
        catch (WrongLoginException e) {

            except = e;
        }

        assertNotNull(except);
    }

    public void testWrongLoginExceptionPath() {
        System.out.println("TEST WRONG LOGIN EXCEPTION PATH");
        @SuppressWarnings("unused")
        Cas cas;
        WrongLoginException except = null;

        //test wrong password
        try {
            cas = new Cas(username.toCharArray(), "hjkl;lkl;lk".toCharArray(), "");
        }
        catch (WrongLoginException e) {

            except = e;
        }

        assertNotNull(except);


        except = null;

        //test wrong username
        try {
            cas = new Cas("egsadasdelasdasd".toCharArray(), password.toCharArray(), "");
        }
        catch (WrongLoginException e) {

            except = e;
            System.out.println("exception caught!");
        }

        assertTrue(except != null);
    }
}