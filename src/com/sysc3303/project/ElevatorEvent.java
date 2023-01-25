/**
 * 
 */
package com.sysc3303.project;

import java.util.Date;

/**
 * @author Connor Marcus
 *
 */
public class ElevatorEvent {
	public enum Direction {
		UP,
		Down;
	}
	
	private final Date time;
	private final int floorNumber;
	private final Direction direction;
	private final int carButtonNumber;
	
	/**
	 * @param time the Date object corresponding to the time of the elevator request
	 * @param floor the floor on which the elevator request was made
	 * @param direction the direction of the elevator request
	 * @param carButton the car button that was pressed in the elevator request
	 */
	public ElevatorEvent(Date time, int floor, Direction direction, int carButton) {
		this.time = time;
		this.floorNumber = floor;
		this.direction = direction;
		this.carButtonNumber = carButton;
		
		
	}
	
	/**
	 * @return the floor number on which the elevator request was made
	 */
	public int getFloorNumber() {
		return floorNumber;
	}
	
	/**
	 * @return the direction of the elevator request
	 */
	public Direction getDirection() {
		return direction;
	}
	
	/**
	 * @return the button number in the elevator that was pressed by the passenger
	 */
	public int getCarButton() {
		return carButtonNumber;
	}
	
	/**
	 * @param otherEvent the other elevator event to compare this event to
	 * @return true if this event occurs before the other event, and false otherwise
	 */
	public boolean eventOccursBefore(ElevatorEvent otherEvent) {
		return this.time.before(otherEvent.time);
	}
	
}
