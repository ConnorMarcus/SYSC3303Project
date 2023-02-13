/**
 * 
 */
package com.sysc3303.project;

/**
 * Class to represent the elevator's state
 * @author Group 9
 *
 */
public class ElevatorState {
	private String stateName;
	
	/**
	 * Constructor; initializes ElevatorState with the state name.
	 * 
	 * @param stateName the name of the state
	 */
	public ElevatorState(String stateName) {
		this.stateName = stateName;
	}
	
	/**
	 * Handles an up or down request event in the elevator's state machine, and transitions the elevator into the appropriate state.
	 * 
	 * @param elevator the current elevator
	 * @param direction the direction the elevator should move
	 */
	public void handleRequest(Elevator elevator, ElevatorEvent.Direction direction) {
		System.out.println(Thread.currentThread().getName() + ": elevator doors closing");
		setNewState(elevator, direction.toString());
	}
	
	/**
	 * Handles a reach destination event in the elevator's state machine, and transitions the elevator to the STOPPED state.
	 * 
	 * @param elevator the current elevator
	 * @param floorNumber the floor number that the elevator is stopping at
	 */
	public void handleReachedDestination(Elevator elevator, int floorNumber) {
		elevator.setCurrentFloor(floorNumber);
		System.out.println(Thread.currentThread().getName() + ": elevator reached floor " + elevator.getCurrentFloor());
		setNewState(elevator, ElevatorEvent.Direction.STOPPED.toString());
	}
	
	/**
	 * Sets the state of the elevator.
	 * 
	 * @param elevator the current elevator
	 * @param name the name of the state to transition the elevator to
	 */
	private void setNewState(Elevator elevator, String name) {
		elevator.setState(new ElevatorState(name));
		System.out.println(Thread.currentThread().getName() + ": elevator currently in state " + elevator.getState());
		if (name == ElevatorEvent.Direction.STOPPED.toString() ) {
			System.out.println(Thread.currentThread().getName() + ": elevator doors opening");
		}
	}
	
	/**
	 * Gets the String representation of the state.
	 */
	@Override
	public String toString() {
		return stateName + " STATE";
	}
}
