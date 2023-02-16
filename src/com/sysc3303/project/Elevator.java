/**
 * 
 */
package com.sysc3303.project;

/**
 * Corresponds to elevators in the Elevator subsystem.
 * 
 * @author Group 9
 */
public class Elevator implements Runnable {
	private final Scheduler scheduler;
	private ElevatorState state = new ElevatorState(ElevatorEvent.Direction.STOPPED.toString();
	private int currentFloor = Floor.BOTTOM_FLOOR;

	/**
	 * Constructor for Elevator object.
	 * 
	 * @param scheduler The Elevator Scheduler.
	 */
	public Elevator(Scheduler scheduler) {
		this.scheduler = scheduler;
	}
	
	/**
	 * @return the current state of the elevator
	 */
	public ElevatorState getState() {
		return this.state;
	}
	
	/**
	 * @param state the state to set the elevator to
	 */
	public void setState(ElevatorState state) {
		this.state = state;
	}
	
	/**
	 * @return the current floor of the elevator
	 */
	public int getCurrentFloor() {
		return this.currentFloor;
	}
	
	/**
	 * @param floor the floor to set the elevator to
	 */
	public void setCurrentFloor(int floor) {
		this.currentFloor = floor;
	}

	/**
	 * Elevators thread run method.
	 */
	@Override
	public void run() {
		System.out.println(Thread.currentThread().getName() + ": elevator starting on floor " + currentFloor + " in state " + state.toString());
		while (true) {
			FloorRequest request = scheduler.getNextRequest(); // gets next request from scheduler to process
			ElevatorEvent elevatorEvent = request.getElevatorEvent();
			processElevatorEvent(elevatorEvent);
			updateState(elevatorEvent.getDirection(), elevatorEvent.getCarButton());
			System.out.println(Thread.currentThread().getName() + ": Button " + elevatorEvent.getCarButton() + " Light is OFF");
			System.out.println(Thread.currentThread().getName() + ": people have exited from the elevator");
			setResponseForScheduler(request); // send response to scheduler
		}
	}

	/**
	 * Helper method to process an ElevatorEvent from the scheduler
	 * 
	 * @param event The event currently being processed
	 */
	private void processElevatorEvent(ElevatorEvent event) {

		System.out.println(Thread.currentThread().getName() + ": received " + event.toString());
		int eventFloorNumber = event.getFloorNumber();
		
		//Move to the appropriate floor if the elevator is not already there
		if (currentFloor != eventFloorNumber) {
			ElevatorEvent.Direction direction = currentFloor < eventFloorNumber ? ElevatorEvent.Direction.UP : ElevatorEvent.Direction.DOWN;
			updateState(direction, eventFloorNumber); //Handle state changes
		}
		System.out.println(Thread.currentThread().getName() + ": people have entered into the elevator");
		System.out.println(Thread.currentThread().getName() + ": Button " + event.getCarButton() + " Light is ON");
	}
	
	/**
	 * Helper method to update the state of the elevator.
	 * 
	 * @param direction the direction which the elevator is traveling in
	 * @param floorNumber the floor number which the elevator is going to
	 */
	private void updateState(ElevatorEvent.Direction direction, int floorNumber) {
		state.handleRequest(this, direction); // change state to reflect moving up/down
		state.handleReachedDestination(this, floorNumber); // change state to reflect reaching destination and stopping
	}

	/**
	 * Helper method to set a response for the scheduler
	 * 
	 * @param request The request currently being processed
	 */
	private void setResponseForScheduler(FloorRequest request) {
		String responseMessage = request.getElevatorEvent().toString() + " has been processed successfully";
		Floor responseFloor = request.getFloor();
		ElevatorResponse response = new ElevatorResponse(responseFloor, responseMessage);
		scheduler.addElevatorResponse(response); // add elevator response object to scheduler's response queue
	}
	
	public Scheduler getScheduler() {
		return this.scheduler;
	}

}
