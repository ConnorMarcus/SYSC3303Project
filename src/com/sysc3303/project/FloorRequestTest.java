package com.sysc3303.project;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class FloorRequestTest {
	private static FloorRequest floorRequest;
	private static Floor floor = new Floor(new Scheduler());
	private static ElevatorEvent elevatorEvent = new ElevatorEvent(new Time("1", "1", "1", "1"), 3, ElevatorEvent.Direction.UP, 4 );

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		floorRequest = new FloorRequest(floor, elevatorEvent);
	}

	@Test
	void gettersTest() {
		assertEquals(floor, floorRequest.getFloor());
		assertEquals(elevatorEvent, floorRequest.getElevatorEvent());
	}

}
