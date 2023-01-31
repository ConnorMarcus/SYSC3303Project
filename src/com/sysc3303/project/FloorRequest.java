/**
 * 
 */
package com.sysc3303.project;

/**
 * Represents a request sent from the Floor to the Scheduler
 * 
 * @author Group 9
 *
 */
public class FloorRequest {
	private final Floor floor;
	private final ElevatorEvent elevatorEvent;

	/**
	 * Constructor
	 * 
	 * @param floor         The Floor object that is sending the request
	 * @param elevatorEvent The ElevatorEvent object that it is sending
	 */
	public FloorRequest(Floor floor, ElevatorEvent elevatorEvent) {
		if (floor == null || elevatorEvent == null) {
			throw new IllegalArgumentException("The Floor and ElevatorEvent objects cannot be null");
		}
		this.floor = floor;
		this.elevatorEvent = elevatorEvent;
	}

	/**
	 * @return the Floor object for the request
	 */
	public Floor getFloor() {
		return floor;
	}

	/**
	 * @return the ElevatorEvent object of the request
	 */
	public ElevatorEvent getElevatorEvent() {
		return elevatorEvent;
	}
}
