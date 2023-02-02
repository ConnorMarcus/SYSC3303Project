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

	/**
	 * Constructor for Elevator object.
	 * 
	 * @param scheduler The Elevator Scheduler.
	 */
	public Elevator(Scheduler scheduler) {
		this.scheduler = scheduler;
	}

	/**
	 * Elevators thread run method.
	 */
	@Override
	public void run() {
		while (true) {
			FloorRequest request = scheduler.getNextRequest();
			processElevatorEvent(request.getElevatorEvent());
			setResponseForScheduler(request);
		}
	}

	/**
	 * Helper method to process an ElevatorEvent from the scheduler
	 * 
	 * @param event The event currently being processed
	 */
	private void processElevatorEvent(ElevatorEvent event) {

		System.out.println(Thread.currentThread().getName() + ": received " + event.toString());
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
		scheduler.addElevatorResponse(response);
	}
	
	public Scheduler getScheduler() {
		return this.scheduler;
	}

}