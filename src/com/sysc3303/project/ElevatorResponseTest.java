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
class ElevatorResponseTest {
	private static ElevatorResponse elevatorResponse;
	private static Floor floor = new Floor(new Scheduler());

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		elevatorResponse = new ElevatorResponse(floor, "Testing");
	}

	@Test
	void gettersTest() {
		assertEquals(floor, elevatorResponse.getFloor());
		assertEquals("Testing", elevatorResponse.getMessage());
	}

}
