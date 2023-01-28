/**
 * 
 */
package com.sysc3303.project;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.sysc3303.project.ElevatorEvent.Direction;

/**
 * The Elevator object.
 * @author Noah Hammoud
 */
public class Elevator implements Runnable {
	private final Scheduler scheduler;
//	private int numberOfFloors;
	private int currentFloor;
	private Direction elevatorDirection;
	private final Set<ElevatorEvent> curEvents;
	
	//TODO only increment currentFloor and change elevatorDirection in a synchronized method since they
	// are also accessed by Scheduler? Note: actually may not need synchronized for this class
	
	/**
	 * Constructor for Elevator object.
	 * @param scheduler The Elevator Scheduler.
	 * @param numberOfFloors The number of floors of the Elevator.
	 */
	public Elevator(Scheduler scheduler) {
		this.scheduler = scheduler;
//		this.numberOfFloors = numberOfFloors;
		this.currentFloor = 1;
		elevatorDirection = Direction.STOPPED;
		curEvents = new HashSet<>();
	}
	
	@Override
	public void run() {
		while (true) {
			if (curEvents.isEmpty()) {
				Set<ElevatorEvent> events = scheduler.consumeEventFromStopped();
				ElevatorEvent event = events.iterator().next();
				Direction dir = event.getDirection();
				int floorNumber = event.getFloorNumber();
				goToFloor(floorNumber);
				elevatorDirection = event.getDirection();
				enterIntoElevator(events);
			}
			else {
				ElevatorAction action = scheduler.getElevatorAction(this);
				respondToElevatorAction(action);
			}
			
			
			try {
				Thread.sleep(1000);
			} 
			catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (elevatorDirection == Direction.UP) {
				moveUp();
			}
			else if (elevatorDirection == Direction.DOWN) {
				moveDown();
			}
			
		}
		
	}
	
	
	private void goToFloor(int floorNumber) {
		while (this.currentFloor < floorNumber) {
			moveUp();
			try {
				Thread.sleep(1000);
			} 
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		while (this.currentFloor > floorNumber) {
			moveDown();
			try {
				Thread.sleep(1000);
			} 
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public synchronized int getCurrentFloor() {
		return this.currentFloor;
	}
	
	public synchronized Direction getDirection() {
		return elevatorDirection;
	}
	
	public synchronized boolean arePeopleExiting() {
		for (ElevatorEvent event : curEvents) {
			if (event.getCarButton() == currentFloor) {
				return true;
			}
		}
		return false;
	}
	
	public synchronized void removeEvent(ElevatorEvent event) {
		if (!curEvents.contains(event)) {
			throw new IllegalArgumentException("Cannot remove elevator event, it is not part of the elevator's current events!");
		}
		curEvents.remove(event);
	}
	
	/**
	 * Moves the Elevator up one floor.
	 */
	private void moveUp() {
		this.elevatorDirection = Direction.UP;
		this.currentFloor++;
		System.out.println("Elevator moving up to floor " + this.currentFloor);
		
	}

	/**
	 * Stops the Elevator from moving.
	 */
	private void stopMoving() {
		this.elevatorDirection = Direction.STOPPED;
		System.out.println("Elevator is stopping at floor " + getCurrentFloor());
	}
	
	/**
	 * Moves the Elevator down one floor
	 */
	private void moveDown() {
		this.elevatorDirection = Direction.DOWN;
		this.currentFloor--;
		System.out.println("Elevator moving down to floor " + this.currentFloor);			
	}
	
	private void respondToElevatorAction(ElevatorAction action) {
		if (action.shouldStop()) {
			Direction prevDirection = elevatorDirection;
			stopMoving();
			if (action.arePeopleExit()) {
				exitFromElevator();
			}
			if (action.arePeopleEnter()) {
				enterIntoElevator(action.getPeopleEntering());
			}
			if (!curEvents.isEmpty()) {
				elevatorDirection = prevDirection;
			}
		}
		
	}
	
	private void enterIntoElevator(Set<ElevatorEvent> events) {
		curEvents.addAll(events);
		System.out.println("People are entering into the elevator at floor " + currentFloor);
	}
	
	private void exitFromElevator() {
		for (ElevatorEvent event : new HashSet<>(curEvents)) {
			if (event.getCarButton() == currentFloor) {
				curEvents.remove(event);
			}
		}
		System.out.println("People are exiting the elevator at floor " + currentFloor);
	}
	
}