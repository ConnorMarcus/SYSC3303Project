/**
 * 
 */
package com.sysc3303.project;

/**
 * @author
 *
 */
public class Scheduler implements Runnable {

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Adds an event to the scheduler's queue; called by the Floor thread
	 * 
	 * @param elevatorEvent The event to add to the scheduler's queue
	 */
	public synchronized void addEvent(ElevatorEvent elevatorEvent) {
		
	}
	
	/**
	 * Gets the next elevator event to do; called by the Elevator thread
	 * 
	 * @return the elevator event to be done next as determined by the scheduler
	 */
	public synchronized ElevatorEvent consumeEvent() {
		return null;
	}

}
