/**
 * 
 */
package com.sysc3303.project;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.Set;
import com.sysc3303.project.ElevatorEvent.Direction;

/**
 * Corresponds to elevators in the Elevator subsystem.
 * 
 * @author Group 9
 */
public class Elevator implements Runnable {
	private ElevatorState state;
	private int currentFloor = Floor.BOTTOM_FLOOR;
	public static final int NUM_CARS = 3;
	private final DatagramSocket socket;
	private static ArrayList<Elevator> elevators = new ArrayList<>();

	/**
	 * Constructor for Elevator object.
	 */
	public Elevator() {
		this(true);
	}
	
	/**
	 * Constructor for Elevator that stops thread from sleeping.
	 * 
	 * @param shouldSleep a flag which indicates if the thread should sleep between events or not
	 */
	public Elevator(boolean shouldSleep) {
		socket = UDPUtil.createDatagramSocket();
		state = new ElevatorState(ElevatorEvent.Direction.STOPPED.toString(), shouldSleep);
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
	 * Closes the socket associated with the elevator object
	 */
	public void closeSocket() {
		socket.close();
	}

	/**
	 * Elevators thread run method.
	 */
	@Override
	public void run() {
		System.out.println(Thread.currentThread().getName() + ": elevator starting on floor " + currentFloor + " in state " + state.toString());
		while (true) {
			Set<FloorRequest> requests = getNextFloorRequest(); // gets next request from scheduler to process
			FloorRequest request = requests.iterator().next();
			if (requests.size() == 1 && request.isEndOfRequests()) {
				exitElevatorSubsystem();
			}
			processElevatorEvents(requests);
			updateState(requests);
		}
	}

	/**
	 * @return the Set of FloorRequest objects sent from the scheduler
	 */
	private Set<FloorRequest> getNextFloorRequest() {
		DatagramPacket receivePacket = sendAndReceiveRequest(currentFloor, Direction.STOPPED);
		return (Set<FloorRequest>) UDPUtil.convertFromBytes(receivePacket.getData(), receivePacket.getLength());
	}

	/**
	 * Sends an elebatorRequest and reccives a set of FLoorRequests from Scheduler.
	 * 
	 * @param floorNum The floor number of the elevator.
	 * @param direction The direction of the elevator.
	 * @return The receive packet sent by the scheduler.
	 */
	public DatagramPacket sendAndReceiveRequest(int floorNum, Direction direction) {
		byte[] data = UDPUtil.convertToBytes(new ElevatorRequest(floorNum, direction));
		DatagramPacket packet = new DatagramPacket(data, data.length, Scheduler.ADDRESS, Scheduler.ELEVATOR_REQUEST_PORT);
		UDPUtil.sendPacket(socket, packet);
		DatagramPacket receivePacket = new DatagramPacket(new byte[UDPUtil.RECEIVE_PACKET_LENGTH], UDPUtil.RECEIVE_PACKET_LENGTH);
		UDPUtil.receivePacket(socket, receivePacket);
		return receivePacket;
	}

	/**
	 * Helper method to process an ElevatorEvent from the scheduler
	 * 
	 * @param events The Set of events currently being processed
	 */
	private void processElevatorEvents(Set<FloorRequest> events) {

		for (FloorRequest f: events) {
			System.out.println(Thread.currentThread().getName() + ": received " + f.getElevatorEvent().toString());
		}

		int eventFloorNumber = events.iterator().next().getElevatorEvent().getFloorNumber();
		
		//Move to the appropriate floor if the elevator is not already there
		if (currentFloor != eventFloorNumber) {
			ElevatorEvent.Direction direction = currentFloor < eventFloorNumber ? ElevatorEvent.Direction.UP : ElevatorEvent.Direction.DOWN;
			state.goToFloor(this, direction, eventFloorNumber);
		}
		System.out.println(Thread.currentThread().getName() + ": people have entered into the elevator");
		
		for (FloorRequest f: events) {
			System.out.println(Thread.currentThread().getName() + ": Button " + f.getElevatorEvent().getCarButton() + " Light is ON");
		}
	}
	
	/**
	 * Helper method to update the state of the elevator.
	 * 
	 * @param requests the set of FloorRequests which the elevator is going to
	 */
	private void updateState(Set<FloorRequest> requests) {
		state.handleRequest(this, requests); // change state to reflect moving up/down
	}

	/**
	 * Helper method to set a response for the scheduler
	 * 
	 * @param events The Set of ElevatorEvents to response with
	 */
	public void setResponseForScheduler(Set<ElevatorEvent> events) {
		StringBuilder sb = new StringBuilder();
		for (ElevatorEvent e: events) {
			sb.append(e.toString() + " has been processed successfully").append("\n");
		}
		sb.setLength(sb.length() - 1);
		String responseMessage = sb.toString();
		ElevatorResponse response = new ElevatorResponse(responseMessage);
		sendElevatorResponse(response);
	}
	
	/**
	 * Adds elevator response object to scheduler's response queue
	 * 
	 * @param response the response object to send to the scheduler
	 */
	private void sendElevatorResponse(ElevatorResponse response) {
		byte[] data = UDPUtil.convertToBytes(response);
		DatagramPacket packet = new DatagramPacket(data, data.length, Scheduler.ADDRESS, Scheduler.ELEVATOR_RESPONSE_PORT);
		UDPUtil.sendPacket(socket, packet);
	}

	/**
	 * Closes the socket for each elevator so they are not waiting to receive from the scheduler after
	 * an elevator has already received the final message from the scheduler
	 */
	private static void closeElevatorSockets() {
		for (Elevator elevator : elevators) {
			elevator.closeSocket();
		}
	}
	
	/**
	 * exits the ElevatorSubsystem 
	 */
	private void exitElevatorSubsystem() {
		closeElevatorSockets();
		System.exit(0);
	}
	
	/**
	 * Main method for the Elevator Subsystem
	 */
	public static void main(String[] args) {
		for (int i=0; i<NUM_CARS; i++) {
			Elevator e = new Elevator();
			elevators.add(e);
			Thread t = new Thread(e, "ElevatorThread-" + (i + 1));
			t.start();
		}
	}

}
