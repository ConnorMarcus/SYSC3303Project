/**
 * 
 */
package com.sysc3303.project;

import java.io.Serializable;

/**
 * ElevatorEvents are objects representing the lines of input read in from the
 * Floor subsystem
 * 
 * @author Group 9
 *
 */
public class ElevatorEvent implements Serializable {
	public enum Direction {
		UP, DOWN, STOPPED;
	}

	private final Time time;
	private final int floorNumber;
	private final Direction direction;
	private final int carButtonNumber;

	private static final int END_OF_REQUESTS_NUMBER = -1; // The floor to set for the final invalid request
	private static final Time END_OF_REQUESTS_TIME = new Time("1","1","1","1");
	private static final Direction END_OF_REQUESTS_DIRECTION = Direction.STOPPED;

	/**
	 * @param time      the Time object corresponding to the time of the elevator
	 *                  request
	 * @param floor     the floor on which the elevator request was made
	 * @param direction the direction of the elevator request
	 * @param carButton the car button (button on inside of elevator) that was
	 *                  pressed in the elevator request
	 * @throws IllegalArgumentException exception is thrown if the ElevatorEvent
	 *                                  being created is invalid
	 */
	public ElevatorEvent(Time time, int floor, Direction direction, int carButton) throws IllegalArgumentException {
		if (!isEventValid(time, floor, direction, carButton) && !isValidEndOfRequestsEvent(time, floor, direction, carButton)) {
			throw new IllegalArgumentException("Cannot create this ElevatorEvent as it would be invalid!");
		}
		this.time = time;
		this.floorNumber = floor;
		this.direction = direction;
		this.carButtonNumber = carButton;
	}

	/**
	 * @param time      the Time object for the event
	 * @param floor     the floor on which the elevator request was made
	 * @param direction the direction of the elevator request
	 * @param carButton the car button (button on inside of elevator) that was
	 *                  pressed in the elevator request
	 * @return true if the event is valid, and false otherwise
	 */
	private static boolean isEventValid(Time time, int floor, Direction direction, int carButton) {
		return (time != null && floor > 0 && !(floor == 1 && direction == Direction.DOWN) && floor <= Floor.NUM_FLOORS
				&& !(floor == Floor.NUM_FLOORS && direction == Direction.UP) && carButton != floor && carButton > 0
				&& carButton <= Floor.NUM_FLOORS && direction != Direction.STOPPED
				&& !(direction == Direction.UP && floor > carButton)
				&& !(direction == Direction.DOWN && floor < carButton));
	}
	
	/**
	 * @return the time on which the elevator request was made
	 */
	public Time getTime() {
		return time; 
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
		return this.time.isTimeBefore(otherEvent.time);
	}

	/**
	 * Factory method for an ElevatorEvent that signifies that all requests have been sent
	 * @return ElevatorEvent that denotes the program should finish
	 */
	public static ElevatorEvent createEndOfRequestsEvent() {
		return new ElevatorEvent(END_OF_REQUESTS_TIME,
				END_OF_REQUESTS_NUMBER,
				END_OF_REQUESTS_DIRECTION,
				END_OF_REQUESTS_NUMBER);
	}

	/**
	 * @param time      the Time object for the event
	 * @param floor     the floor on which the elevator request was made
	 * @param direction the direction of the elevator request
	 * @param carButton the car button (button on inside of elevator) that was
	 *                  pressed in the elevator request
	 * @return true if the parameters would form a valid final event, and false otherwise
	 */
	private boolean isValidEndOfRequestsEvent(Time time, int floor, Direction direction, int carButton) {
		return time.getTimeDifferenceInMS(END_OF_REQUESTS_TIME) == 0 && floor == END_OF_REQUESTS_NUMBER &&
				direction == END_OF_REQUESTS_DIRECTION && carButton == END_OF_REQUESTS_NUMBER;
	}

	/** Checks if an ElevatorEvent signifies that all requests have been sent
	 * @return true if the event is a valid final event, and false otherwise
	 */
	public boolean isEndOfRequestsEvent() {
		return isValidEndOfRequestsEvent(this.time, this.floorNumber, this.direction, this.carButtonNumber);
	}

	/**
	 * Enables ElevatorEvent objects to be printed
	 */
	@Override
	public String toString() {
		return String.format("ElevatorEvent[Time: %s, Floor: %s, Floor Button: %s, Car Button: %s]", time.toString(),
				floorNumber, direction, carButtonNumber);
	}

}
