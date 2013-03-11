/**
 * This package contains a set of classes used to interact with Virginia Tech's website.
 * ScheduleScraper requires an active login session with CAS to perform any operations,
 * and it can be used to pull user schedule from any past/current term, or exam schedule.
 * CourseInfo requires no login information, and can be used to pull all sorts of information
 * about courses offered in particular semesters from the timetable.
 * Semester provides several static methods that can be used to validate and operate on semesterCodes.
 *      NOTE: A semesterCode is a string of the format- YYYYMM, where month is either 09, 01, 06, 07. These
 *          values are all encapsulated as public constants within Semester, so it should never be necessary to
 *          specifically refer to a number. Access can be achieved through: Semester.FALL or Semester.SPRING etc.
 */
package com.vtaccess;