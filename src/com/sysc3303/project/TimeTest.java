/**
 * 
 */
package com.sysc3303.project;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * @author ihasnaou
 *
 */
class TimeTest {
	private static Time time;
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		time = new Time("1", "1", "1", "1");
	}

	@Test
	void isTimeBeforeTest() {
		assertEquals(true, time.isTimeBefore(new Time("1", "1", "1", "2")));
		assertEquals(true, time.isTimeBefore(new Time("1", "1", "2", "1")));
		assertEquals(true, time.isTimeBefore(new Time("1", "2", "1", "1")));
		assertEquals(true, time.isTimeBefore(new Time("2", "1", "1", "1")));
		
		assertEquals(false, time.isTimeBefore(new Time("0", "1", "1", "1")));
		assertEquals(false, time.isTimeBefore(new Time("1", "0", "1", "1")));
		assertEquals(false, time.isTimeBefore(new Time("1", "1", "0", "1")));
		assertEquals(false, time.isTimeBefore(new Time("1", "1", "1", "0")));
		
		assertEquals(false, time.isTimeBefore(new Time("1", "1", "1", "1")));
	}
	
	@Test
	void toStringTest() {
		System.out.println(time.toString());
		assertEquals("1:1:1.1", time.toString());
	}
	
	@Test
	void createFromTimeStringTest() {
		Time test = Time.createFromTimeString("1:1:1.1");
		assertEquals(time.getHours(), test.getHours());
		assertEquals(time.getMinutes(), test.getMinutes());
		assertEquals(time.getSeconds(), test.getSeconds());
		assertEquals(time.getMilliseconds(), test.getMilliseconds());
	}

}
