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
	private final long elevatorID; //a unique elevator ID
	private final Scheduler scheduler;
	private int currentFloor;
	private Direction elevatorDirection;
	
	
	/**
	 * Constructor for Elevator object.
	 * @param scheduler The Elevator Scheduler.
	 * @param numberOfFloors The number of floors of the Elevator.
	 */
	public Elevator(Scheduler scheduler) {
		this.scheduler = scheduler;
		this.currentFloor = 1;
		elevatorDirection = Direction.STOPPED;
		elevatorID = ElevatorIDGenerator.getUniqueID();
	}
	
	/**
	 * Elevators thread run method.
	 */
	@Override
	public void run() {
		while (true) {
			if (getDirection() == Direction.STOPPED) {
				Set<ElevatorEvent> events = scheduler.consumeEventFromStopped(elevatorID);
				ElevatorEvent event = events.iterator().next();
				int floorNumber = event.getFloorNumber();
				
				if (getCurrentFloor() != floorNumber) {
					goToFloor(floorNumber);
				}
				
				elevatorDirection = event.getDirection();
				enterIntoElevator();
				getButtonPressFromEvents(events);
				closeDoors();
			}
			else {
				ElevatorAction action = scheduler.getElevatorAction(elevatorID, getCurrentFloor(), getDirection());
				respondToElevatorAction(action);
			}
			
			
//			try {
//				Thread.sleep(1000);
//			}
//			catch (InterruptedException e) {
//				e.printStackTrace();
//			}
			
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
//			try {
//				Thread.sleep(1000);
//			}
//			catch (InterruptedException e) {
//				e.printStackTrace();
//			}
		}
		while (getCurrentFloor() > floorNumber) {
			moveDown();
//			try {
//				Thread.sleep(1000);
//			}
//			catch (InterruptedException e) {
//				e.printStackTrace();
//			}
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
	 * Moves the Elevator up one floor.
	 */
	private void moveUp() {
		setDirection(Direction.UP);
		incrementCurrentFloor();
		System.out.println(Thread.currentThread().getName() + ": Elevator moving up to floor " + getCurrentFloor());	
	}

	/**
	 * Stops the Elevator from moving.
	 */
	private void stopMoving() {
		setDirection(Direction.STOPPED);
		int currentFloor = getCurrentFloor();
		System.out.println(Thread.currentThread().getName() + ": Elevator is stopping at floor " + currentFloor);
		System.out.println(Thread.currentThread().getName() + ": Elevator doors opening...");
	}
	
	/**
	 * Moves the Elevator down one floor
	 */
	private void moveDown() {
		setDirection(Direction.DOWN);
		decrementCurrentFloor();
		System.out.println(Thread.currentThread().getName() + ": Elevator moving down to floor " + getCurrentFloor());			
	}
	
	
	/**
	 * Responds to Elevator Action sent by the Scheduler.
	 * @param action the action sent by the Scheduler.
	 */
	private void respondToElevatorAction(ElevatorAction action) {
		if (action.shouldStop()) {
			stopMoving();
			if (action.arePeopleExit()) {
				exitFromElevator();
			}
			if (action.arePeopleEnter()) {
				enterIntoElevator();
				pressElevatorButton(action.getCarButtonFloors());	
			}
			elevatorDirection = action.getNextDirection();
			if (elevatorDirection != Direction.STOPPED) closeDoors();
		}
	}
	
	
	/**
	 * Notifies Scheduler that people have entered the Elevator.
	 */
	private void enterIntoElevator() {
		scheduler.setFloorEntering(getCurrentFloor());
	}
	
	
	/**
	 * Notifies Scheduler that people have exited the Elevator.
	 */
	private void exitFromElevator() {
		System.out.println(Thread.currentThread().getName() + ": Elevator floor button " + getCurrentFloor() + "'s lamp has been turned off");
		scheduler.setFloorExiting(getCurrentFloor());
	}
	
	
	/**
	 * Closes the Elevators doors.
	 */
	private void closeDoors() {
		System.out.println(Thread.currentThread().getName() + ": Elevator doors closing...");
		
	}
	
	private void getButtonPressFromEvents(Set<ElevatorEvent> events) {
		Set<Integer> buttonFloors = new HashSet<>();
		for (ElevatorEvent event: events) {
			buttonFloors.add(event.getCarButton());
		}
		pressElevatorButton(buttonFloors);
	}
	
	/**
	 * Handles outputting a message for the Elevators button.
	 * @param buttonFloors the integer set of floor numbers the elevator is traveling to.
	 */
	private void pressElevatorButton(Set<Integer> buttonFloors) {
		for (int floor: buttonFloors) {
			System.out.println(Thread.currentThread().getName() + ": Elevator floor button " + floor + " has been pressed");
		}
	}
	
	
}