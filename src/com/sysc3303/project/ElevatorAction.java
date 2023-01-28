package com.sysc3303.project;

import java.util.Set;


public class ElevatorAction {
	private boolean shouldStop;
	private boolean peopleEnter;
	private boolean peopleExit;
	private Set<ElevatorEvent> peopleEntering;
	
	public ElevatorAction (boolean shouldStop, boolean peopleEnter, boolean peopleExit, Set<ElevatorEvent> peopleEntering) {
		if (!shouldStop && (peopleEnter || peopleExit)) {
			throw new IllegalArgumentException("Invalid argument; cannot exit/leave elevator while it is still moving!");
		}
		this.shouldStop = shouldStop;
		this.peopleEnter = peopleEnter;
		this.peopleExit = peopleExit;
		this.peopleEntering = peopleEntering;
	}
	
	public boolean shouldStop() {
		return shouldStop;
	}
	
	public boolean arePeopleEnter() {
		return this.peopleEnter;
	}
	
	public boolean arePeopleExit() {
		return peopleExit;
	}
	
	public Set<ElevatorEvent> getPeopleEntering() {
		return peopleEntering;
	}
}
