package com.sysc3303.project.test;

import com.sysc3303.project.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;

class ElevatorStateTest {

    private static ElevatorState elevatorState;
    private static Elevator elevator;
    private static final String STATE_STRING = " STATE";
    public static final String STOPPED_STRING = ElevatorEvent.Direction.STOPPED.toString() + STATE_STRING;
    public static final String UP_STRING = ElevatorEvent.Direction.UP.toString() + STATE_STRING;
    public static final String DOWN_STRING = ElevatorEvent.Direction.DOWN.toString() + STATE_STRING;
    private static final boolean SHOULD_SLEEP = false;

    @BeforeAll
    public static void setUpBeforeClass() {
        elevatorState = new ElevatorState(ElevatorEvent.Direction.STOPPED.toString(), SHOULD_SLEEP);
        elevator = new Elevator(SHOULD_SLEEP);
    }
    
    @AfterAll
    public static void tearDown() {
    	elevator.closeSocket();
    }

    @Test
    public void testInitialState() {
        ElevatorState es = new ElevatorState(ElevatorEvent.Direction.STOPPED.toString(), SHOULD_SLEEP);
        assertEquals(STOPPED_STRING, es.toString());

        es = new ElevatorState(ElevatorEvent.Direction.UP.toString(), SHOULD_SLEEP);
        assertEquals(UP_STRING, es.toString());

        es = new ElevatorState(ElevatorEvent.Direction.DOWN.toString(), SHOULD_SLEEP);
        assertEquals(DOWN_STRING, es.toString());
    }

    @Test
    public void testHandleRequest() {
        // Elevator object's initial state is STOPPED
        elevatorState.handleRequest(elevator, ElevatorEvent.Direction.DOWN, 1);
        assertEquals(DOWN_STRING, elevator.getState().toString());
        // Try going down from down state
        elevatorState.handleRequest(elevator, ElevatorEvent.Direction.DOWN, 0);
        assertEquals(DOWN_STRING, elevator.getState().toString());

        elevatorState.handleRequest(elevator, ElevatorEvent.Direction.UP, 2);
        assertEquals(UP_STRING, elevator.getState().toString());
        // Try going up from up state
        elevatorState.handleRequest(elevator, ElevatorEvent.Direction.UP, 4);
        assertEquals(UP_STRING, elevator.getState().toString());
    }

    @Test
    public void testHandleReachedDestination() {
        // Stop from stopped
        elevatorState = new ElevatorState(ElevatorEvent.Direction.STOPPED.toString(), SHOULD_SLEEP);
        elevatorState.handleReachedDestination(elevator, 0);
        assertEquals(STOPPED_STRING, elevator.getState().toString());
        // Stop from moving up to top floor
        elevatorState.handleRequest(elevator, ElevatorEvent.Direction.UP, 2);
        elevatorState.handleReachedDestination(elevator, Floor.NUM_FLOORS);
        assertEquals(STOPPED_STRING, elevator.getState().toString());
        // Stop from moving up to bottom floor somehow
        elevatorState.handleRequest(elevator, ElevatorEvent.Direction.UP, 4);
        elevatorState.handleReachedDestination(elevator, Floor.BOTTOM_FLOOR);
        assertEquals(STOPPED_STRING, elevator.getState().toString());
        // Stop from moving down to bottom floor
        elevatorState.handleRequest(elevator, ElevatorEvent.Direction.DOWN, 2);
        elevatorState.handleReachedDestination(elevator, Floor.BOTTOM_FLOOR);
        assertEquals(STOPPED_STRING, elevator.getState().toString());
        // Stop from moving down to top floor somehow
        elevatorState.handleRequest(elevator, ElevatorEvent.Direction.DOWN, 1);
        elevatorState.handleReachedDestination(elevator, Floor.NUM_FLOORS);
        assertEquals(STOPPED_STRING, elevator.getState().toString());
    }
}