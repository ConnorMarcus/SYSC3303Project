/**
 * 
 */
package com.sysc3303.project.test;

import static org.junit.jupiter.api.Assertions.*;

import com.sysc3303.project.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

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
	public static void setUpBeforeClass() throws Exception {
		elevator = new Elevator(scheduler);
	}

	@Test
	public void testGetter() {
		assertEquals(scheduler, elevator.getScheduler());
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
	public void testUpdateElevator() {
		Scheduler scheduler = new Scheduler();
		Time eventTime = new Time("14", "05", "15", "000");
		ElevatorEvent elevatorEvent = new ElevatorEvent(eventTime, Floor.NUM_FLOORS, ElevatorEvent.Direction.DOWN,
				Floor.BOTTOM_FLOOR);
		FloorRequest floorRequest = new FloorRequest(new Floor(scheduler), elevatorEvent);

		// Need to run threads since elevator methods are private and invoked when events are received
		Thread t1 = new Thread(elevator, "ElevatorThread");
		Thread t2 = new Thread(scheduler, "SchedulerThread");

		Future<?> timeoutControl1 = Executors.newSingleThreadExecutor().submit(t1);
		Future<?> timeoutControl2 = Executors.newSingleThreadExecutor().submit(t2);

		// Run the threads for 1 second
		try {
			timeoutControl1.get(1, TimeUnit.SECONDS);
			timeoutControl2.get(1, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			// The threads will time out since they loop forever
			assertEquals(ElevatorStateTest.STOPPED_STRING, elevator.getState().toString());
			assertEquals(Floor.BOTTOM_FLOOR, elevator.getCurrentFloor());
		}
	}
}
