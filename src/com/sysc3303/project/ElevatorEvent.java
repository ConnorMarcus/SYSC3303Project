/**
 * 
 */
package com.sysc3303.project;

import java.util.Date;

/**
 * @author Group 9
 *
 */
public class ElevatorEvent {
	public enum Direction {
		UP,
		DOWN,
		STOPPED;
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
	 * @throws IllegalArgumentException exception is thrown if the ElevatorEvent being created is invalid
	 */
	public ElevatorEvent(Date time, int floor, Direction direction, int carButton) throws IllegalArgumentException {
		if (!isEventValid(time, floor, direction, carButton)) {
			throw new IllegalArgumentException("Cannot create this ElevatorEvent as it would be invalid!");
		}
		this.time = time;
		this.floorNumber = floor;
		this.direction = direction;
		this.carButtonNumber = carButton;			
	}
	
	private static boolean isEventValid(Date time, int floor, Direction direction, int carButton) {
		return (time != null && floor > 0 && !(floor == 1 && direction==Direction.DOWN) 
				&& floor <= Floor.NUM_FLOORS && !(floor==Floor.NUM_FLOORS && direction==Direction.UP)
				&& carButton != floor && carButton > 0 && carButton <= Floor.NUM_FLOORS && direction != Direction.STOPPED
				&& !(direction==Direction.UP && floor > carButton) && !(direction==Direction.DOWN && floor < carButton));
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
