/**
 * 
 */
package com.sysc3303.project;

import java.util.ArrayDeque;
import com.sysc3303.project.ElevatorEvent.Direction;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * @author Group 9
 *
 */
public class Scheduler implements Runnable {
	private final Queue<ElevatorEvent> eventQueue;
	private final Map<Integer, Set<ElevatorEvent>> upFloorQueue;
	private final Map<Integer, Set<ElevatorEvent>> downFloorQueue;
	
	/**
	 * Initializes all event queues and sets
	 */
	public Scheduler() {
		eventQueue = new ArrayDeque<ElevatorEvent>();
		upFloorQueue = new HashMap<>();
		downFloorQueue = new HashMap<>();
		
		for (int i=1; i<=Main.NUM_FLOORS; i++) {
			upFloorQueue.put(i, new HashSet<>());
			downFloorQueue.put(i, new HashSet<>());
		}
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		/*
		 * Tell elevator go up/down based on curEvents
		 */
		
	}
	
	/**
	 * Adds an event to the scheduler's queue; called by the Floor thread
	 * 
	 * @param elevatorEvent The event to add to the scheduler's queue
	 */
	public synchronized void addEvent(ElevatorEvent elevatorEvent) {
		eventQueue.add(elevatorEvent);

		if (elevatorEvent.getDirection() == Direction.UP) {
			upFloorQueue.get(elevatorEvent.getFloorNumber()).add(elevatorEvent);
		}
		else {
			downFloorQueue.get(elevatorEvent.getFloorNumber()).add(elevatorEvent);
		}
		notifyAll();
	}
	
	/**
	 * Starts the next elevator event from stopped state; called by the Elevator thread
	 */
	public synchronized Set<ElevatorEvent> consumeEventFromStopped() {
		while(eventQueue.isEmpty()) {
			try {
				wait();
			} 
			catch (InterruptedException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
		return getNextEventSet();
	}

	/**
	 * Gets what the elevator should do given then current elevator object. 
	 * 
	 * @param elevator	currente elevator
	 * @return ElevatorAction	the action the elevator should carry out
	 */
	public synchronized ElevatorAction getElevatorAction(Elevator elevator) {
		boolean peopleExit = false;
		Set<ElevatorEvent> peopleEntering = new HashSet<>();
		
		if (elevator.arePeopleExiting()) {
			peopleExit = true;
		}
		if (elevator.getDirection() == Direction.UP) {
			Set<ElevatorEvent> floorQueue = upFloorQueue.get(elevator.getCurrentFloor());
			for (ElevatorEvent event : floorQueue) {
				removeEvent(event);
				peopleEntering.add(event);
			}
		}
		else if (elevator.getDirection() == Direction.DOWN) {
			Set<ElevatorEvent> floorQueue = downFloorQueue.get(elevator.getCurrentFloor());
			for (ElevatorEvent event : floorQueue) {
				removeEvent(event);
				peopleEntering.add(event);
			}
		}
		
		boolean peopleEnter = !peopleEntering.isEmpty();
		return new ElevatorAction(peopleEnter || peopleExit, peopleEnter, peopleExit, peopleEntering);
	}
	
	
	/**
	 * Removes the next event from the event and floor queues, 
	 * and adds it to the set of current events. Also removes and adds any events on
	 * the same floor and going in the same direction.
	 */
	private Set<ElevatorEvent> getNextEventSet() {
		ElevatorEvent nextEvent = eventQueue.remove();
		int floorNumber = nextEvent.getFloorNumber();
		Direction dir = nextEvent.getDirection();
		removeEvent(nextEvent);
		Set<ElevatorEvent> otherEvents = getEventsOnFloor(floorNumber, dir);
		otherEvents.add(nextEvent);
		
		return otherEvents;
	}
	
	/**
	 * Gets the current people on the floor the elevator is at. 
	 * 
	 * @param int	current floor of the elevator
	 * @paraam Direction	direction of the elevator
	 */
	private Set<ElevatorEvent> getEventsOnFloor(int floor, Direction dir) {
		Set<ElevatorEvent> newEvents = new HashSet<>();
		
		if (dir == Direction.UP) { 
			for (ElevatorEvent event : new HashSet<>(upFloorQueue.get(floor))) {
				newEvents.add(event);
				removeEvent(event);
			}
		}
		else if (dir == Direction.DOWN) {
			for (ElevatorEvent event : new HashSet<>(downFloorQueue.get(floor))) {
				newEvents.add(event);
				removeEvent(event);
			}
		}
		return newEvents;
	}
	
	/**
	 * Removes an event from the fifo event queue and the respective floor queue. 
	 * 
	 * @param ElevatorEvent	event representing the person's elevator request
	 */
	private void removeEvent(ElevatorEvent elevatorEvent) {
		eventQueue.remove(elevatorEvent);
		if (elevatorEvent.getDirection() == Direction.UP) {
			upFloorQueue.get(elevatorEvent.getFloorNumber()).remove(elevatorEvent);
		}
		else if (elevatorEvent.getDirection() == Direction.DOWN) {
			downFloorQueue.get(elevatorEvent.getFloorNumber()).remove(elevatorEvent);
		}
	}

}
