/**
 * 
 */
package com.sysc3303.project.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.sysc3303.project.ElevatorEvent;
import com.sysc3303.project.ElevatorResponse;
import com.sysc3303.project.Floor;
import com.sysc3303.project.FloorRequest;
import com.sysc3303.project.Scheduler;
import com.sysc3303.project.Time;

/**
 * @author Group 9
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
