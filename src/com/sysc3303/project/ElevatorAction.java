package com.sysc3303.project;

import java.util.Set;

/**
 * The ElevatorAction Object.
 * @author Group 9
 *
 */
public class ElevatorAction {
	private boolean shouldStop;
	private boolean peopleEnter;
	private boolean peopleExit;
	private Set<ElevatorEvent> peopleEntering;
	
	/**
	 * Constructor for ElevatorAction.
	 * @param shouldStop the should stop flag.
	 * @param peopleEnter the peopleEnter flag.
	 * @param peopleExit the peopleExit flag.
	 * @param peopleEntering the set of ElevatorEvents for the entering people.
	 */
	public ElevatorAction (boolean shouldStop, boolean peopleEnter, boolean peopleExit, Set<ElevatorEvent> peopleEntering) {
		if (!shouldStop && (peopleEnter || peopleExit)) {
			throw new IllegalArgumentException("Invalid argument; cannot exit/leave elevator while it is still moving!");
		}
		this.shouldStop = shouldStop;
		this.peopleEnter = peopleEnter;
		this.peopleExit = peopleExit;
		this.peopleEntering = peopleEntering;
	}
	
	
	/**
	 * Gets the shouldStop flag.
	 * @return True if Elevator should stop.
	 */
	public boolean shouldStop() {
		return shouldStop;
	}
	
	/**
	 * Gets the arePeopleEnter flag.
	 * @return True if people are entering the Elevator.
	 */
	public boolean arePeopleEnter() {
		return this.peopleEnter;
	}
	
	/**
	 * Gets the arePeopleExit flag.
	 * @return True if peoplr are entering the Elevator.
	 */
	public boolean arePeopleExit() {
		return peopleExit;
	}
	
	/**
	 * Gets the set of ElevatorEvents for the entering people.
	 * @return set of ElevatorEvents of entering people.
	 */
	public Set<ElevatorEvent> getPeopleEntering() {
		return peopleEntering;
	}
}
