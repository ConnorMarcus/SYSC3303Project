package com.sysc3303.project;

import java.util.ArrayList;

/**
 * @author Patrick Vafaie
 *
 */
public class Floor {

	private static final int NUM_FLOORS = 5;
	private static final int NUM_CARS = 1;

	private FloorSubsystem controller;
	private int floorNumber;
	// The buttons to go up or down on a given floor
	private ArrayList<FloorButton> floorButtons;

	/*
	TODO:
	 - Floor lamps
	 - Direction lamps
	 - Arrival sensors
	 */

	/**
	 * Constructor for a Floor object
	 * @param controller The FloorSubsystem responsible for the floor
	 * @param floorNumber Which floor number this is
	 */
	public Floor (FloorSubsystem controller, int floorNumber) {
		this.controller = controller;
		this.floorNumber = floorNumber;

		floorButtons = new ArrayList<>();
		// Top floor won't have an up button
		if (floorNumber < NUM_FLOORS) {
			floorButtons.add(new FloorButton(ElevatorEvent.Direction.UP));
		}
		// Bottom floor won't have a down button
		if (floorNumber > 1) {
			floorButtons.add(new FloorButton(ElevatorEvent.Direction.DOWN));
		}
	}

	public int getFloorNumber() {
		return floorNumber;
	}
}
