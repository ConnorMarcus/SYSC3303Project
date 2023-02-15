/**
 * 
 */
package com.sysc3303.project.test;

import static org.junit.jupiter.api.Assertions.*;

import com.sysc3303.project.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * @author Group 9
 *
 */
class SchedulerTest {
	
	private static Scheduler scheduler;

	public static final String RECEIVING_STRING = "RECEIVING STATE";
	public static final String RECEIVING_SENDING_STRING = "RECEIVING AND SENDING STATE";

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeAll
	public static void setUpBeforeClass() throws Exception {
		scheduler = new Scheduler();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterEach
	public void tearDown() throws Exception {
	}

	@Test
	public void testFloorRequest() {
		FloorRequest floorRequest = new FloorRequest(new Floor(scheduler), new ElevatorEvent(new Time("1", "1", "1", "1"), 3, ElevatorEvent.Direction.UP, 4 ));
		scheduler.addFloorRequest(floorRequest);
		assertEquals(floorRequest, scheduler.getNextRequest());
	}
	
	@Test
	public void testElevatorResponse() {
		ElevatorResponse elevatorResponse = new ElevatorResponse(new Floor(scheduler), "Done!");
		scheduler.addElevatorResponse(elevatorResponse);
		assertEquals(elevatorResponse, scheduler.getElevatorResponse());
	}

	// Don't need to test with threads like in Elevator test, unit testing scheduler states is equivalent
	@Test
	public void testSetAndDefaultState() {
		Scheduler s = new Scheduler(); // new to not be affected by other tests
		assertEquals(s.getState().toString(), RECEIVING_STRING);

		s.setState(new SchedulerReceivingState());
		assertEquals(s.getState().toString(), RECEIVING_STRING);

		s.setState(new SchedulerReceivingSendingState());
		assertEquals(s.getState().toString(), RECEIVING_SENDING_STRING);

		s.setState(new SchedulerReceivingSendingState());
		assertEquals(s.getState().toString(), RECEIVING_SENDING_STRING);
	}
}
