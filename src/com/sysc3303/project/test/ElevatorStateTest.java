package com.sysc3303.project.test;

import com.sysc3303.project.*;
import com.sysc3303.project.ElevatorEvent.Direction;
import com.sysc3303.project.ElevatorEvent.Fault;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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
    	Set<FloorRequest> test = new HashSet<>(Arrays.asList(new FloorRequest(new ElevatorEvent(new Time("1", "1", "1", "1"), 5, Direction.DOWN, 1, Fault.NO_FAULT)))); 
        elevatorState.handleRequest(elevator, test);
        assertEquals(DOWN_STRING, elevator.getState().toString());
        // Try going down from down state
    	Set<FloorRequest> test2 = new HashSet<>(Arrays.asList(new FloorRequest(new ElevatorEvent(new Time("1", "1", "1", "1"), 3, Direction.DOWN, 1, Fault.NO_FAULT)))); 
        elevatorState.handleRequest(elevator, test2);
        assertEquals(DOWN_STRING, elevator.getState().toString());

    	Set<FloorRequest> test3 = new HashSet<>(Arrays.asList(new FloorRequest(new ElevatorEvent(new Time("1", "1", "1", "1"), 1, Direction.UP, 4, Fault.NO_FAULT)))); 
        elevatorState.handleRequest(elevator, test3);
        assertEquals(UP_STRING, elevator.getState().toString());
        
        // Try going up from up state
    	Set<FloorRequest> test4 = new HashSet<>(Arrays.asList(new FloorRequest(new ElevatorEvent(new Time("1", "1", "1", "1"), 4, Direction.UP, 5, Fault.NO_FAULT)))); 
        elevatorState.handleRequest(elevator, test4);
        assertEquals(UP_STRING, elevator.getState().toString());
    }

    @Test
    public void testHandleReachedDestination() {
        // Stop from stopped
    	
        elevatorState = new ElevatorState(ElevatorEvent.Direction.STOPPED.toString(), SHOULD_SLEEP);
        elevatorState.handleReachedDestination(elevator, 0, false);
        assertEquals(STOPPED_STRING, elevator.getState().toString());
        // Stop from moving up to top floor
        Set<FloorRequest> test = new HashSet<>(Arrays.asList(new FloorRequest(new ElevatorEvent(new Time("1", "1", "1", "1"), 1, Direction.UP, 2, Fault.NO_FAULT)))); 
        elevatorState.handleRequest(elevator, test);
        elevatorState.handleReachedDestination(elevator, Floor.NUM_FLOORS, false);
        assertEquals(STOPPED_STRING, elevator.getState().toString());
        // Stop from moving up to bottom floor somehow
        Set<FloorRequest> test2 = new HashSet<>(Arrays.asList(new FloorRequest(new ElevatorEvent(new Time("1", "1", "1", "1"), 3, Direction.UP, 4, Fault.NO_FAULT)))); 
        elevatorState.handleRequest(elevator, test2);
        elevatorState.handleReachedDestination(elevator, Floor.BOTTOM_FLOOR, false);
        assertEquals(STOPPED_STRING, elevator.getState().toString());
        // Stop from moving down to bottom floor
        Set<FloorRequest> test3 = new HashSet<>(Arrays.asList(new FloorRequest(new ElevatorEvent(new Time("1", "1", "1", "1"), 4, Direction.DOWN, 2, Fault.NO_FAULT)))); 
        elevatorState.handleRequest(elevator, test3);
        elevatorState.handleReachedDestination(elevator, Floor.BOTTOM_FLOOR, false);
        assertEquals(STOPPED_STRING, elevator.getState().toString());
        // Stop from moving down to top floor somehow
        Set<FloorRequest> test4 = new HashSet<>(Arrays.asList(new FloorRequest(new ElevatorEvent(new Time("1", "1", "1", "1"), 3, Direction.DOWN, 1, Fault.NO_FAULT)))); 
        elevatorState.handleRequest(elevator, test4);
        elevatorState.handleReachedDestination(elevator, Floor.NUM_FLOORS, false);
        assertEquals(STOPPED_STRING, elevator.getState().toString());
    }
}