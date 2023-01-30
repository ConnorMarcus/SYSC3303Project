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
	private final Map<Long, Set<ElevatorEvent>> curEvents; //Maps Elevator ID to current events to be done by that elevator
	private int floorEntering;
	private int floorExiting;
	private FloorSubsystem floor;

	public void setFloor(FloorSubsystem floor) {
		this.floor = floor;
	}

	/**
	 * Initializes all event queues and sets
	 */
	public Scheduler() {
		eventQueue = new ArrayDeque<ElevatorEvent>();
		upFloorQueue = new HashMap<>();
		downFloorQueue = new HashMap<>();
		curEvents = new HashMap<>();
		floorEntering = -1; 
		floorExiting = -1;
		this.floor = floor;
		
		for (int i=1; i<=Main.NUM_FLOORS; i++) {
			upFloorQueue.put(i, new HashSet<>());
			downFloorQueue.put(i, new HashSet<>());
		}
	}
	
	@Override
	public void run() {
		while (true) {
			if (floorExiting > 0) {
				floor.setFloorExiting(floorExiting);
			} 
			if (floorEntering > 0) {
				floor.setFloorEntering(floorEntering);
			}
		}
		
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
	public synchronized Set<ElevatorEvent> consumeEventFromStopped(long elevatorID) {
		while(eventQueue.isEmpty()) {
			try {
				wait();
			} 
			catch (InterruptedException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
		return getNextEventSet(elevatorID);
	}

	/**
	 * @param elevatorID the ID of the elevator
	 * @param floor the current floor of the elevator
	 * @param direction the current direction of the elevator
	 * @return the action the elevator should carry out
	 */
	public synchronized ElevatorAction getElevatorAction(long elevatorID, int floor, Direction direction) {
		if (curEvents.get(elevatorID) == null) {
			curEvents.put(elevatorID, new HashSet<>());
		}
		boolean peopleAreExiting = updatePeopleExiting(elevatorID, floor);
		Set<Integer> peopleAreEntering = updatePeopleEntering(elevatorID, floor, direction);
		Direction nextDirection = curEvents.get(elevatorID).isEmpty() ? Direction.STOPPED : direction;
		
		return new ElevatorAction(!peopleAreEntering.isEmpty(), peopleAreExiting, nextDirection, peopleAreEntering);
	}
	
	
	/**
	 * @param elevatorID The elevator ID to update the current event set
	 * @param floor The current floor of the elevator
	 * @param direction The direction that the elevator is currently moving in
	 * @return true if people are entering into the elevator, and false otherwise
	 */
	private Set<Integer> updatePeopleEntering(long elevatorID, int floor, Direction direction) {
		Set<ElevatorEvent> peopleEntering = getEventsOnFloor(floor, direction);
		curEvents.get(elevatorID).addAll(peopleEntering);
		Set<Integer> carButtons = new HashSet<>();
		for (ElevatorEvent elevatorEvent: peopleEntering) {
			carButtons.add(elevatorEvent.getCarButton());
		}
		return carButtons;
	}
	
	/**
	 * @param elevatorID The elevator ID to update the current event set
	 * @param floor The current floor of the elevator
	 * @return true if people are exiting into the elevator, and false otherwise
	 */
	private boolean updatePeopleExiting(long elevatorID, int floor) {
		boolean peopleAreExiting = false;
		for (ElevatorEvent event : new HashSet<>(curEvents.get(elevatorID))) {
			if (event.getCarButton() == floor) {
				curEvents.get(elevatorID).remove(event);
				peopleAreExiting = true;
			}
		}
		return peopleAreExiting;
	}
	
	
	/**
	 * Removes the next event from the event and floor queues, 
	 * and adds it to the set of current events. Also removes and adds any events on
	 * the same floor and going in the same direction.
	 * 
	 * @param elevatorID The ID of the elevator getting the event
	 * @return The set of events added to the elevator's current event set
	 */
	private Set<ElevatorEvent> getNextEventSet(long elevatorID) {
		ElevatorEvent nextEvent = eventQueue.peek();
		int floorNumber = nextEvent.getFloorNumber();
		Direction dir = nextEvent.getDirection();
		removeEvent(nextEvent);
		Set<ElevatorEvent> otherEvents = getEventsOnFloor(floorNumber, dir);
		otherEvents.add(nextEvent);
		
		if (curEvents.get(elevatorID) == null) {
			curEvents.put(elevatorID, new HashSet<>());
		}
		curEvents.get(elevatorID).addAll(otherEvents);
		
		return otherEvents;
	}
	
	/**
	 * Gets the ElevatorEvents corresponding to the current people on the floor the elevator is at. 
	 * 
	 * @param int current floor of the elevator
	 * @paraam Direction direction of the elevator
	 */
	private Set<ElevatorEvent> getEventsOnFloor(int floor, Direction dir) {
		Set<ElevatorEvent> events = new HashSet<>();
		Set<ElevatorEvent> floorQueue = null;
		
		if (dir == Direction.UP) { 
			floorQueue = upFloorQueue.get(floor);
		}
		else if (dir == Direction.DOWN) {
			floorQueue = downFloorQueue.get(floor);
		}
		
		for (ElevatorEvent event : new HashSet<>(floorQueue)) {
			events.add(event);
			removeEvent(event);
		}
		
		return events;
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
	
	/**
	 * Setting what floor people are entering the elevator. 
	 * 
	 * @param floor	current floor of the elevator
	 */
	public synchronized void setFloorEntering(int floor) {
		floorEntering = floor;
		try {
			wait();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	/**
	 * Setting what floor people are entering the elevator. 
	 * 
	 * @param floor	current floor of the elevator
	 */
	public synchronized void setFloorExiting(int floor) {
		floorExiting= floor;
		try {
			wait();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	/**
	 * Resets floorEntering to signal that nobody is entering the elevator. 
	 */
	public synchronized void resetFloorEntering() {
		floorEntering = -1;
		notifyAll();
	}
	
	/**
	 * Resets floorExiting to signal that nobody is exiting the elevator. 
	 */
	public synchronized void resetFloorExiting() {
		floorExiting = -1;
		notifyAll();
	}

}
