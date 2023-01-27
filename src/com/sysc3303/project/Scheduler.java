/**
 * 
 */
package com.sysc3303.project;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * @author
 *
 */
public class Scheduler implements Runnable {
	private final Queue<ElevatorEvent> eventQueue;
	private final Map<Integer, Set<ElevatorEvent>> floorQueue;
	
	public Scheduler() {
		eventQueue = new ArrayDeque<ElevatorEvent>();
		floorQueue = new HashMap<>();
		for (int i=1; i<=Main.NUM_FLOORS; i++) {
			floorQueue.put(i, new HashSet<>());
		}
	}
	
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
		eventQueue.add(elevatorEvent);
		floorQueue.get(elevatorEvent.getFloorNumber()).add(elevatorEvent);
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
