/**
 * 
 */
package com.sysc3303.project;

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
	
	
	/**
	 * Constructor for Elevator object.
	 * @param scheduler The Elevator Scheduler.
	 * @param numberOfFloors The number of floors of the Elevator.
	 */
	public Elevator(Scheduler scheduler) {
		this.scheduler = scheduler;
//		this.numberOfFloors = numberOfFloors;
		this.currentFloor = 1;
		elevatorDirection = null;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
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
		this.elevatorDirection = null;
	}
	
	/**
	 * Moves the Elevator down one floor
	 */
	private void moveDown() {
		this.elevatorDirection = Direction.Down;
		this.currentFloor--;
		System.out.println("Elevator moving down to floor " + this.currentFloor);			
	}
	
}
