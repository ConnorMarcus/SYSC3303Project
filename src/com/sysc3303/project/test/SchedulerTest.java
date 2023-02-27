/**
 * 
 */
package com.sysc3303.project.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;

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
	@AfterAll
	public static void tearDown() throws Exception {
		scheduler.closeSockets();
	}

	@Test
	public void testFloorRequest() {
		FloorRequest floorRequest = new FloorRequest(new ElevatorEvent(new Time("1", "1", "1", "1"), 3, ElevatorEvent.Direction.UP, 4 ));
		scheduler.addFloorRequest(floorRequest);
		assertEquals(floorRequest, scheduler.getNextRequest());
	}
	
	@Test
	public void testElevatorResponse() {
		ElevatorResponse elevatorResponse = new ElevatorResponse("Done!");
		scheduler.addElevatorResponse(elevatorResponse);
		assertEquals(elevatorResponse, scheduler.getElevatorResponse());
	}

	// Don't need to test with threads like in Elevator test, unit testing scheduler states is equivalent
	@Test
	public void testSetAndDefaultState() {
		assertEquals(scheduler.getState().toString(), RECEIVING_STRING);

		scheduler.setState(new SchedulerReceivingState());
		assertEquals(scheduler.getState().toString(), RECEIVING_STRING);

		scheduler.setState(new SchedulerReceivingSendingState());
		assertEquals(scheduler.getState().toString(), RECEIVING_SENDING_STRING);

		scheduler.setState(new SchedulerReceivingSendingState());
		assertEquals(scheduler.getState().toString(), RECEIVING_SENDING_STRING);
		
		scheduler.setState(new SchedulerReceivingState()); //return to initial state
	}
}
