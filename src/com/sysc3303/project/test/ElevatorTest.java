/**
 * 
 */
package com.sysc3303.project.test;

import static org.junit.jupiter.api.Assertions.*;

import com.sysc3303.project.*;
import com.sysc3303.project.ElevatorEvent.Direction;
import com.sysc3303.project.ElevatorEvent.Fault;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;

/**
 * @author Group 9
 *
 */
class ElevatorTest {
	private static Elevator elevator;
	private static final boolean SHOULD_SLEEP = false;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeAll
	public static void setUpBeforeClass() throws Exception {
		elevator = new Elevator(SHOULD_SLEEP);
	}
	
	@AfterAll
	public static void tearDown() {
		elevator.closeSocket();
	}


	@Test
	public void testSetStates() {
		assertEquals(ElevatorStateTest.STOPPED_STRING, elevator.getState().toString());

		elevator.setState(new ElevatorState(ElevatorEvent.Direction.UP.name()));
		assertEquals(ElevatorStateTest.UP_STRING, elevator.getState().toString());

		elevator.setState(new ElevatorState(ElevatorEvent.Direction.DOWN.name()));
		assertEquals(ElevatorStateTest.DOWN_STRING, elevator.getState().toString());

		elevator.setState(new ElevatorState(ElevatorEvent.Direction.STOPPED.name()));
		assertEquals(ElevatorStateTest.STOPPED_STRING, elevator.getState().toString());
	}
	
	@Test
	public void testFaults() {
		Set<ElevatorEvent> events = new HashSet<ElevatorEvent>();
		ElevatorEvent ee1 = new ElevatorEvent(
				new Time("1", "1", "1", "1"), 
				1, 
				Direction.UP, 
				2, 
				Fault.NO_FAULT
		); 
		
		ElevatorEvent ee2 = new ElevatorEvent(
				new Time("2", "2", "2", "2"), 
				4, 
				Direction.DOWN, 
				3, 
				Fault.HARD_FAULT
		);
		events.add(ee1); 
		assertEquals(false, elevator.checkAndHandleFault(events));
		
		events.clear();
		events.add(ee2);
		
    	//Need a thread for the scheduler to send acknowledgement
		Scheduler scheduler = new Scheduler(); 
    	Thread schedulerThread= new Thread(() -> {
    			scheduler.receiveResponseFromElevator();
    	});
		schedulerThread.start();
		
    	//Need a thread for the elevator to receive acknowledgement of hard fault
    	Thread ackThread = new Thread(() -> {
    		elevator.receiveAcknowledgment(); 
    	});
		ackThread.start();
		
		assertEquals(true, elevator.checkAndHandleFault(events));
		
	}
}
