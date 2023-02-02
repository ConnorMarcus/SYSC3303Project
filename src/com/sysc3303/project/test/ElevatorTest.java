/**
 * 
 */
package com.sysc3303.project.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sysc3303.project.Elevator;
import com.sysc3303.project.Scheduler;

/**
 * @author Group 9
 *
 */
class ElevatorTest {
	private static Elevator elevator;
	private static Scheduler scheduler = new Scheduler();

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		elevator = new Elevator(scheduler);
	}

	@Test
	void getterTest() {
		assertEquals(scheduler, elevator.getScheduler());
	}

}
