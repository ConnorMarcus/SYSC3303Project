/**
 * 
 */
package com.sysc3303.project;

/**
 * Class to represent the elevator's state
 * 
 * @author Group 9
 *
 */
public class ElevatorState {
	private boolean shouldSleep = true; // sets whether the elevator should sleep between events; by default set to true
	private String stateName;
	private final int TIME_REACH_FLOOR_BEFORE_DOORS_OPEN = 1000;
	private final int TIME_DOORS_OPEN = 3000;
	private final int TIME_TO_MOVE_AFTER_DOORS_CLOSE = 2000;

	/**
	 * Constructor; initializes ElevatorState with the state name.
	 * 
	 * @param stateName the name of the state
	 */
	public ElevatorState(String stateName) {
		this(stateName, true);
	}

	/**
	 * Constructor; initializes ElevatorState with the state name and the
	 * shouldSleep flag
	 * 
	 * @param stateName   the name of the state
	 * @param shouldSleep the flag which indicated whether the calling thread should
	 *                    sleep when using this object
	 */
	public ElevatorState(String stateName, boolean shouldSleep) {
		this.stateName = stateName;
		this.shouldSleep = shouldSleep;
	}

	/**
	 * Handles an up or down request event in the elevator's state machine, and
	 * transitions the elevator into the appropriate state.
	 * 
	 * @param elevator       the current elevator
	 * @param direction      the direction the elevator should move
	 * @param requestedFloor the floor number that was requested in the elevator
	 */
	public void handleRequest(Elevator elevator, ElevatorEvent.Direction direction, int requestedFloor) {
		System.out.println(Thread.currentThread().getName() + ": elevator doors closing");
		setNewState(elevator, direction.toString());
		if (shouldSleep)
			sleepWhileMoving(Math.abs(requestedFloor - elevator.getCurrentFloor())); // sleep for a certain time to
																						// simulate moving between floors
	}

	/**
	 * Sleeps for an amount of time corresponding to the distance the elevator is
	 * moving
	 * 
	 * @param numFloors the number of floors that the elevator is moving
	 */
	private void sleepWhileMoving(int numFloors) {
		for (int i = 0; i < numFloors; i++) {
			try {
				Thread.sleep(TIME_TO_MOVE_AFTER_DOORS_CLOSE);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * Handles a reach destination event in the elevator's state machine, and
	 * transitions the elevator to the STOPPED state.
	 * 
	 * @param elevator    the current elevator
	 * @param floorNumber the floor number that the elevator is stopping at
	 */
	public void handleReachedDestination(Elevator elevator, int floorNumber) {
		try {
			elevator.setCurrentFloor(floorNumber);
			System.out.println(
					Thread.currentThread().getName() + ": elevator reached floor " + elevator.getCurrentFloor());
			setNewState(elevator, ElevatorEvent.Direction.STOPPED.toString());
			if (shouldSleep)
				Thread.sleep(TIME_REACH_FLOOR_BEFORE_DOORS_OPEN);
			System.out.println(Thread.currentThread().getName() + ": elevator doors opening");
			if (shouldSleep)
				Thread.sleep(TIME_DOORS_OPEN);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Sets the state of the elevator.
	 * 
	 * @param elevator the current elevator
	 * @param name     the name of the state to transition the elevator to
	 */
	private void setNewState(Elevator elevator, String name) {
		elevator.setState(new ElevatorState(name));
		System.out.println(Thread.currentThread().getName() + ": elevator currently in state " + elevator.getState());
	}

	/**
	 * Gets the String representation of the state.
	 */
	@Override
	public String toString() {
		return stateName + " STATE";
	}
}
