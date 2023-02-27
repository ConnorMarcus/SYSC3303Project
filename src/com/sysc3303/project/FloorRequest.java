/**
 * 
 */
package com.sysc3303.project;

import java.io.Serializable;

/**
 * Represents a request sent from the Floor to the Scheduler
 * 
 * @author Group 9
 *
 */
public class FloorRequest implements Serializable {
	private final ElevatorEvent elevatorEvent;

	/**
	 * Constructor
	 * 
	 * @param elevatorEvent The ElevatorEvent object that it is sending
	 */
	public FloorRequest(ElevatorEvent elevatorEvent) {
		if (elevatorEvent == null) {
			throw new IllegalArgumentException("The ElevatorEvent object cannot be null");
		}
		this.elevatorEvent = elevatorEvent;
	}

	/**
	 * @return the ElevatorEvent object of the request
	 */
	public ElevatorEvent getElevatorEvent() {
		return elevatorEvent;
	}
}
