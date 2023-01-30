package com.sysc3303.project;

import java.util.HashSet;
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
	private Set<Integer> carButtonFloors;
	
	/**
	 * Constructor for ElevatorAction.
	 * @param peopleEnter the peopleEnter flag.
	 * @param peopleExit the peopleExit flag.
	 * @param nextDirection the next direction that the elevator should move
	 */
	public ElevatorAction (boolean peopleEnter, boolean peopleExit, Direction nextDirection, Set<Integer> carButtonFloors) {
		this.peopleAreEntering = peopleEnter;
		this.peopleAreExiting = peopleExit;
		this.nextDirection = nextDirection;
		this.carButtonFloors = carButtonFloors;
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
	 * @return True if people are exiting the Elevator.
	 */
	public boolean arePeopleExit() {
		return peopleAreExiting;
	}
	
	/**
	 * Gets the next direction of the Elevator
	 * @return The next direction.
	 */
	public Direction getNextDirection() {
		return nextDirection;
	}
	
	/**
	 * Get's the car button floor selected.
	 * @return the car button floor.
	 */
	public Set<Integer> getCarButtonFloors() {
		return carButtonFloors;
	}
	
}
