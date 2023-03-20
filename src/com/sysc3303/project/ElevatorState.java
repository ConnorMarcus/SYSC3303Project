/**
 * 
 */
package com.sysc3303.project;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.HashSet;
import java.util.Set;
import com.sysc3303.project.ElevatorEvent.Direction;

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
	 * Gets the direction of the Elevator.
	 * 
	 * @return The direction emum.
	 */
	public Direction getDirection() {
		return Direction.valueOf(stateName.toUpperCase());
	}

	/**
	 * Handles an up or down request event in the elevator's state machine, and
	 * transitions the elevator into the appropriate state.
	 * 
	 * @param elevator the current elevator
	 * @param requests the Set of FLoorRequests
	 */
	public void handleRequest(Elevator elevator, Set<FloorRequest> requests) {
		closeDoors();
		Direction direction = requests.iterator().next().getElevatorEvent().getDirection();
		setNewState(elevator, direction.toString());
		moveBetweenFloors(elevator, requests);
	}

	/**
	 * Moves an elevator to a floor.
	 * 
	 * @param elevator       the elevator object.
	 * @param direction      the direction of the elevator.
	 * @param requestedFloor the requested floor.
	 */
	public void goToFloor(Elevator elevator, ElevatorEvent.Direction direction, int requestedFloor) {
		closeDoors();
		setNewState(elevator, direction.toString());
		if (shouldSleep)
			sleepWhileMoving(Math.abs(requestedFloor - elevator.getCurrentFloor()));
		elevator.setCurrentFloor(requestedFloor);
		handleReachedDestination(elevator, requestedFloor, false);

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
	 * Sleeps for an amount of time corresponding to the distance the elevator is
	 * moving
	 * 
	 * @param elevator the object
	 * @param requests the FloorRequest Sets
	 */
	private void moveBetweenFloors(Elevator elevator, Set<FloorRequest> requests) {
		Direction direction = requests.iterator().next().getElevatorEvent().getDirection();

		while (!requests.isEmpty()) {
			if (shouldSleep) {
				try {
					Thread.sleep(TIME_TO_MOVE_AFTER_DOORS_CLOSE);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			int nextFloor = direction == Direction.UP ? elevator.getCurrentFloor() + 1 : elevator.getCurrentFloor() - 1;
			elevator.setCurrentFloor(nextFloor);
			Set<ElevatorEvent> events = new HashSet<>();

			DatagramPacket receivePacket = elevator.sendAndReceiveRequest(nextFloor, direction);
			Set<FloorRequest> newRequests = (Set<FloorRequest>) UDPUtil.convertFromBytes(receivePacket.getData(), receivePacket.getLength());
			
			
			for (FloorRequest f : new HashSet<>(requests)) {
				if (f.getElevatorEvent().getCarButton() == nextFloor) {
					events.add(f.getElevatorEvent());
					requests.remove(f);
				}
			}
			
			boolean peopleExitedOnFloor = !events.isEmpty();

			if (peopleExitedOnFloor) {
				handleReachedDestination(elevator, events.iterator().next().getCarButton(), true);
				elevator.setResponseForScheduler(events);
			}
			
			if (!newRequests.isEmpty()) { 
				requests.addAll(newRequests); //adds new people entering into elevator
				elevator.printReceivedRequests(newRequests);
				
				//Elevator has to stop at floor if not already stopped
				if(!peopleExitedOnFloor)
					handleReachedDestination(elevator, nextFloor, false);
				
				elevator.printPeopleEntered(newRequests);
			}
		
			//continue moving if there are still more requests to serve
			if (!requests.isEmpty() && this.getDirection() != direction) {
				closeDoors();
				setNewState(elevator, direction.toString());
			}
		}
	}

	/**
	 * Handles a reach destination event in the elevator's state machine, and
	 * transitions the elevator to the STOPPED state.
	 * 
	 * @param elevator      the current elevator
	 * @param floorNumber   the floor number that the elevator is stopping at
	 * @param peopleExiting indicates if peoeple are exiting the elevator
	 */
	public void handleReachedDestination(Elevator elevator, int floorNum, boolean peopleExiting) {
		try {
			System.out.println(Thread.currentThread().getName() + ": elevator reached floor " + elevator.getCurrentFloor());
			setNewState(elevator, ElevatorEvent.Direction.STOPPED.toString());
			if (shouldSleep)
				Thread.sleep(TIME_REACH_FLOOR_BEFORE_DOORS_OPEN);
			System.out.println(Thread.currentThread().getName() + ": elevator doors opening");
			if (peopleExiting) {
				System.out.println(Thread.currentThread().getName() + ": Button " + floorNum + " Light is OFF");
				System.out.println(Thread.currentThread().getName() + ": people have exited from the elevator");
			}
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
		this.stateName = name;
		System.out.println(Thread.currentThread().getName() + ": elevator currently in state " + elevator.getState());
	}
	
	/**
	 * Closes the Elevator's doors
	 */
	private void closeDoors() {
		System.out.println(Thread.currentThread().getName() + ": elevator doors closing");
	}

	/**
	 * Gets the String representation of the state.
	 */
	@Override
	public String toString() {
		return stateName + " STATE";
	}
}
