package com.sysc3303.project;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Patrick Vafaie
 *
 */
public class Floor {

	public static final int NUM_FLOORS = 5;
	private static final int NUM_CARS = 1;

	private int floorNumber;
	// The buttons to go up or down on a given floor
	private HashMap<ElevatorEvent.Direction, FloorButton> floorButtons;
	// Lamps that light up when a floor button is pressed and turn off when an elevator going in that direction arrives
	private HashMap<ElevatorEvent.Direction, FloorLamp> floorLamps;
	// Lamps that light up temporarily to indicate which direction an elevator is going when it arrives, until it leaves
	private ArrayList<DirectionLamp[]> directionLamps;
	// Each elevator shaft at each floor has a sensor to detect the presence of an elevator
	// private ArrayList<ArrivalSensor> arrivalSensors;

	/*
	TODO:
	 - Arrival sensors
	 */

	/**
	 * Constructor for a Floor object
	 * @param floorNumber Which floor number this is
	 */
	public Floor (int floorNumber) {
		this.floorNumber = floorNumber;

		floorButtons = new HashMap<>();
		floorLamps = new HashMap<>();
		directionLamps = new ArrayList<>();

		// Top floor won't have an up button
		if (floorNumber < NUM_FLOORS) {
			floorButtons.put(ElevatorEvent.Direction.UP, new FloorButton(ElevatorEvent.Direction.UP));
			floorLamps.put(ElevatorEvent.Direction.UP, new FloorLamp(ElevatorEvent.Direction.UP));
		}
		// Bottom floor won't have a down button
		if (floorNumber > 1) {
			floorButtons.put(ElevatorEvent.Direction.DOWN, new FloorButton(ElevatorEvent.Direction.DOWN));
			floorLamps.put(ElevatorEvent.Direction.DOWN, new FloorLamp(ElevatorEvent.Direction.DOWN));
		}

		// Direction lamps and arrival sensors are for every elevator
		for (int i = 0; i < NUM_CARS; ++i) {
			DirectionLamp[] elevatorCarDirectionLamps = {
					new DirectionLamp(ElevatorEvent.Direction.UP),
					new DirectionLamp(ElevatorEvent.Direction.DOWN)};
			directionLamps.add(elevatorCarDirectionLamps);
		}
	}

	public int getFloorNumber() {
		return floorNumber;
	}

	/**
	 * Presses the button to request an elevator in a given direction
	 * @param requestedDirection Which button to press
	 * @throws IllegalArgumentException if trying to go above top floor/below bottom floor
	 */
	public void pressButton(ElevatorEvent.Direction requestedDirection) throws IllegalArgumentException {
		if (floorButtons.get(requestedDirection) == null) {
			throw new IllegalArgumentException();
		}
		floorButtons.get(requestedDirection).setPressed(true);
		floorLamps.get(requestedDirection).turnOn();
		// TODO: make some call?
	}

	public void handleElevatorArrival(int elevatorId) {
		System.out.println("Elevator with ID " + String.valueOf(elevatorId) + " arrived at floor " + String.valueOf(floorNumber));
		/*
		add some lamp/button handling
		 */
	}

}
