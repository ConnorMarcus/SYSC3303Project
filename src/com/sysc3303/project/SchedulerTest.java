/**
 * 
 */
package com.sysc3303.project;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * @author ihasnaou
 *
 */
class SchedulerTest {
	
	private static Scheduler scheduler;
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		scheduler = new Scheduler();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void floorRequestTest() {
		FloorRequest floorRequest = new FloorRequest(new Floor(scheduler), new ElevatorEvent(new Time("1", "1", "1", "1"), 3, ElevatorEvent.Direction.UP, 4 ));
		scheduler.addFloorRequest(floorRequest);
		assertEquals(floorRequest, scheduler.getNextRequest());
	}
	
	@Test
	void elevatorResponseTest() {
		ElevatorResponse elevatorResponse = new ElevatorResponse(new Floor(scheduler), "Done!");
		scheduler.addElevatorResponse(elevatorResponse);
		assertEquals(elevatorResponse, scheduler.getElevatorResponse());
	}

}
