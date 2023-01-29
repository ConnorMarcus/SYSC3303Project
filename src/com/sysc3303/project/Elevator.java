/**
 * 
 */
package com.sysc3303.project;

import java.util.HashSet;
import java.util.Set;
import com.sysc3303.project.ElevatorEvent.Direction;

/**
 * The Elevator object.
 * @author Group 9
 */
public class Elevator implements Runnable {
	private final Scheduler scheduler;
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
		this.currentFloor = 1;
		elevatorDirection = Direction.STOPPED;
		curEvents = new HashSet<>();
	}
	
	/**
	 * Elevators thread run method.
	 */
	@Override
	public void run() {
		while (true) {
			if (curEvents.isEmpty()) {
				Set<ElevatorEvent> events = scheduler.consumeEventFromStopped();
				ElevatorEvent event = events.iterator().next();
				int floorNumber = event.getFloorNumber();
				
				if (getCurrentFloor() != floorNumber) {
					goToFloor(floorNumber);
				}
				
				elevatorDirection = event.getDirection();
				enterIntoElevator(events);
				closeDoors();
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
	
	
	/**
	 * Goes to the floorNumber floor.
	 * @param floorNumber the floor number to go to.
	 */
	private void goToFloor(int floorNumber) {
		closeDoors();
		while (getCurrentFloor() < floorNumber) {
			moveUp();
			try {
				Thread.sleep(1000);
			} 
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		while (getCurrentFloor() > floorNumber) {
			moveDown();
			try {
				Thread.sleep(1000);
			} 
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		stopMoving();
	}
	
	/**
	 * Gets the current floor the Elevator is on.
	 * @return The current floor.
	 */
	public synchronized int getCurrentFloor() {
		return this.currentFloor;
	}
	
	
	/**
	 * Increments the current floor.
	 */
	private synchronized void incrementCurrentFloor() {
		this.currentFloor++;		
	}
	
	/**
	 * Decrement the current floor.
	 */
	private synchronized void decrementCurrentFloor() {
		this.currentFloor--;		
	}
	
	
	/**
	 * Gets the direction of the Elevator.
	 * @return Direction of the Elevator.
	 */
	public synchronized Direction getDirection() {
		return elevatorDirection;
	}
	
	
	/**
	 * Sets the direction of the Elevator.
	 * @param direction the direction of the Elevator.
	 */
	private synchronized void setDirection(Direction direction) {
		this.elevatorDirection = direction;
	}
	
	
	/**
	 * Checks if people are exiting the Elevator.
	 * @return True if people are exiting the Elevator.
	 */
	public synchronized boolean arePeopleExiting() {
		for (ElevatorEvent event : curEvents) {
			if (event.getCarButton() == getCurrentFloor()) {
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * Removes Elevator Event from Elevators current events.
	 * @param event the event to remove.
	 */
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
		setDirection(Direction.UP);
		incrementCurrentFloor();
		System.out.println("Elevator moving up to floor " + getCurrentFloor());	
	}

	/**
	 * Stops the Elevator from moving.
	 */
	private void stopMoving() {
		setDirection(Direction.STOPPED);
		System.out.println("Elevator is stopping at floor " + getCurrentFloor());
		System.out.println("Elevator doors opening...");
	}
	
	/**
	 * Moves the Elevator down one floor
	 */
	private void moveDown() {
		setDirection(Direction.DOWN);
		decrementCurrentFloor();
		System.out.println("Elevator moving down to floor " + getCurrentFloor());			
	}
	
	
	/**
	 * Responds to Elevator Action sent by the Scheduler.
	 * @param action the action sent by the Scheduler.
	 */
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
				closeDoors();
			}
		}
	}
	
	
	/**
	 * Notifies Scheduler that people have entered the Elevator.
	 * @param events the event which represents people entering the Elevator.
	 */
	private void enterIntoElevator(Set<ElevatorEvent> events) {
		curEvents.addAll(events);
		for (ElevatorEvent e: events) {
			elevatorButtonAction(e.getCarButton(), true);
		}
		scheduler.setFloorEntering(getCurrentFloor());
	}
	
	
	/**
	 * Notifies Scheduler that people have exited the Elevator.
	 */
	private void exitFromElevator() {
		for (ElevatorEvent event : new HashSet<>(curEvents)) {
			if (event.getCarButton() == getCurrentFloor()) {
				curEvents.remove(event);
				elevatorButtonAction(event.getCarButton(), false);
			}
		}
		scheduler.setFloorExiting(getCurrentFloor());
	}
	
	
	/**
	 * Closes the Elevators doors.
	 */
	private void closeDoors() {
		System.out.println("Elevator doors closing...");
		
	}
	
	/**
	 * Handles outputting a message for the Elevators button.
	 * @param floor the floor the elevator is travling to.
	 * @param isClick indicates if it was a button click action.
	 */
	private void elevatorButtonAction(int floor, boolean isClick) {
		String message = "Elevator button " + floor;
		if (isClick) {
			System.out.println(message + " has been pressed");
		}
		else {
			System.out.println(message + "'s lamp has been turned off");
		}
		
	}
	
	
}