/**
 * 
 */
package com.sysc3303.project;

import java.util.ArrayDeque;
import com.sysc3303.project.ElevatorEvent.Direction;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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
	private final Set<ElevatorEvent> curEvents;
	
	/**
	 * Initializes all event queues and sets
	 */
	public Scheduler() {
		eventQueue = new ArrayDeque<ElevatorEvent>();
		floorQueue = new HashMap<>();
		curEvents = new HashSet<>();
		
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
		notifyAll();
	}
	
	/**
	 * Starts the next elevator event; called by the Elevator thread
	 */
	public synchronized void consumeEvent() {
		while(eventQueue.isEmpty()) {
			try {
				wait();
			} 
			catch (InterruptedException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
		startNextEvent();
	}
	
	
	/**
	 * Removes the next event from the event and floor queues, 
	 * and adds it to the set of current events
	 */
	private void startNextEvent() {
		ElevatorEvent nextEvent = eventQueue.remove();
		floorQueue.get(nextEvent.getFloorNumber()).remove(nextEvent);
		curEvents.add(nextEvent);
	}

}
