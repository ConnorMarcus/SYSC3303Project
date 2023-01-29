package com.sysc3303.project;

import java.util.Set;

import com.sysc3303.project.ElevatorEvent.Direction;

/**
 * The ElevatorAction Object.
 * @author Group 9
 *
 */
public class ElevatorAction {
	private boolean peopleAreEntering;
	private boolean peopleAreExiting;
	private Direction nextDirection;
	
	/**
	 * Constructor for ElevatorAction.
	 * @param peopleEnter the peopleEnter flag.
	 * @param peopleExit the peopleExit flag.
	 * @param nextDirection the next direction that the elevator should move
	 */
	public ElevatorAction (boolean peopleEnter, boolean peopleExit, Direction nextDirection) {
		this.peopleAreEntering = peopleEnter;
		this.peopleAreExiting = peopleExit;
		this.nextDirection = nextDirection;
	}
	
	
	/**
	 * Gets whether the elevator should stop.
	 * @return true if the Elevator should stop and false otherwise.
	 */
	public boolean shouldStop() {
		return peopleAreEntering || peopleAreExiting;
	}
	
	/**
	 * Gets the arePeopleEnter flag.
	 * @return True if people are entering the Elevator.
	 */
	public boolean arePeopleEnter() {
		return this.peopleAreEntering;
	}
	
	/**
	 * Gets the arePeopleExit flag.
	 * @return True if people are entering the Elevator.
	 */
	public boolean arePeopleExit() {
		return peopleAreExiting;
	}
	
	/**
	 * @return The next direction of the elevator
	 */
	public Direction getNextDirection() {
		return nextDirection;
	}
	
}
