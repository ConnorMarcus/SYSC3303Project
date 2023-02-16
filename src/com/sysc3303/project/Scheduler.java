/**
 * 
 */
package com.sysc3303.project;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * The Scheduler class which schedules the elevators and enables communication
 * between the Floor and Elevator subsystems
 * 
 * @author Group 9
 *
 */
public class Scheduler implements Runnable {
	private final Queue<FloorRequest> events; //request queue
	private final Queue<ElevatorResponse> responses; // response queue
	private SchedulerState state; //the state of the scheduler

	/**
	 * Constructor; initializes all attributes with default values
	 */
	public Scheduler() {
		events = new ArrayDeque<>();
		responses = new ArrayDeque<>();
		state = new SchedulerReceivingState();
	}

	/**
	 * Scheduler thread's run method
	 */
	@Override
	public void run() {
		System.out.println(Thread.currentThread().getName() + ": scheduler currently in state "  + state.toString());
		while (true) { //To ensure that all inputs are always read
			ElevatorResponse response = getElevatorResponse();
			state.handleResponseProcessed(this); // response has been received and processed
			response.getFloor().addResponse(response.getMessage()); // add response to floor's response queue
		}

	}
	
	/**
	 * Gets the state of the Scheduler
	 * 
	 * @return the state of the Scheduler
	 */
	public SchedulerState getState() {
		return state;
	}
	
	/**
	 * Sets the state of the Scheduler
	 * 
	 * @param state the new state of the Scheduler
	 */
	public void setState(SchedulerState state) {
		this.state = state;
	}

	/**
	 * Adds a FloorRequest to the request queue
	 * 
	 * @param floorRequest The FloorRequest to add
	 */
	public synchronized void addFloorRequest(FloorRequest floorRequest) {
		if (floorRequest == null) {
			throw new IllegalArgumentException("The FloorRequest object cannot be null");
		}
		events.add(floorRequest);
		notifyAll(); // notifies Elevator threads that a floor request is available
	}

	/**
	 * Gets and removes the FloorRequest at the head of the queue
	 * 
	 * @return The next FloorRequest in the queue
	 */
	public synchronized FloorRequest getNextRequest() {
		while (events.isEmpty()) {
			try {
				wait(); // wait until a request is available
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
		return events.remove();
	}

	/**
	 * Adds an ElevatorResponse to the response queue
	 * 
	 * @param response The ElevatorResponse to add
	 */
	public synchronized void addElevatorResponse(ElevatorResponse response) {
		if (response == null) {
			throw new IllegalArgumentException("The ElevatorResponse object cannot be null");
		}
		responses.add(response);
		notifyAll(); // notifies scheduler that response can be sent to Floor thread
	}

	/**
	 * Gets and removes the ElevatorResponse from the head of the response queue
	 * 
	 * @return elevatorResponse The next ElevatorResponse object
	 */
	public synchronized ElevatorResponse getElevatorResponse() {
		while (responses.isEmpty()) {
			try {
				wait(); // waits for response to be available
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
		state.handleResponseReceived(this); //handle response received event
		return responses.remove();
	}
	
	/**
	 * @return true if there is at least one response in the response queue, and false otherwise
	 */
	public synchronized boolean isResponseInQueue() {
		return !responses.isEmpty();
	}

}
