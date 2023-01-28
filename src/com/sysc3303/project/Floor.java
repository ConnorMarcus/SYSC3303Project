package com.sysc3303.project;

/**
 * @author Patrick Vafaie
 *
 */
public class Floor {

	private static final int NUM_FLOORS = 5;

	private FloorSubsystem controller;
	private int floorNumber;

	/*
	TODO:
	 - Up and down floor buttons
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
	}


}
