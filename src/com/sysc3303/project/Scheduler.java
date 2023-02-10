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
	private final Queue<FloorRequest> events;
	private final Queue<ElevatorResponse> responses;

	/**
	 * Constructor; initializes all attributes
	 */
	public Scheduler() {
		events = new ArrayDeque<>();
		responses = new ArrayDeque<>();
	}

	/**
	 * Scheduler thread's run method
	 */
	@Override
	public void run() {
		while (true) {
			ElevatorResponse response = getElevatorResponse();
			response.getFloor().addResponse(response.getMessage());
		}

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
		notifyAll();
	}

	/**
	 * Gets and removes the FloorRequest at the head of the queue
	 * 
	 * @return The next FloorRequest in the queue
	 */
	public synchronized FloorRequest getNextRequest() {
		while (events.isEmpty()) {
			try {
				wait();
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
	 * @param elevatorResponse The ElevatorResponse to add
	 */
	public synchronized void addElevatorResponse(ElevatorResponse response) {
		if (response == null) {
			throw new IllegalArgumentException("The ElevatorResponse object cannot be null");
		}
		responses.add(response);
		notifyAll();
	}

	/**
	 * Gets and removes the ElevatorResponse from the head of the response queue
	 * 
	 * @return elevatorResponse The next ElevatorResponse object
	 */
	public synchronized ElevatorResponse getElevatorResponse() {
		while (responses.isEmpty()) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
		return responses.remove();
	}

}
