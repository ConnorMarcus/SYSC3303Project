/**
 * 
 */
package com.sysc3303.project;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;

/**
 * Corresponds to elevators in the Elevator subsystem.
 * 
 * @author Group 9
 */
public class Elevator implements Runnable {
	private ElevatorState state = new ElevatorState(ElevatorEvent.Direction.STOPPED.toString());
	private int currentFloor = Floor.BOTTOM_FLOOR;
	public static final int NUM_CARS = 3;
	private final DatagramSocket socket;
	private static ArrayList<Elevator> elevators = new ArrayList<>();

	/**
	 * Constructor for Elevator object.
	 */
	public Elevator() {
		socket = UDPUtil.createDatagramSocket();
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
			FloorRequest request = getNextFloorRequest(); // gets next request from scheduler to process
			if (request.isEndOfRequests()) {
				exitElevatorSubsystem();
			}
			ElevatorEvent elevatorEvent = request.getElevatorEvent();
			processElevatorEvent(elevatorEvent);
			updateState(elevatorEvent.getDirection(), elevatorEvent.getCarButton());
			System.out.println(Thread.currentThread().getName() + ": Button " + elevatorEvent.getCarButton() + " Light is OFF");
			System.out.println(Thread.currentThread().getName() + ": people have exited from the elevator");
			setResponseForScheduler(request); // send response to scheduler
		}
	}

	/**
	 * @return the next FloorRequest object from the scheduler
	 */
	private FloorRequest getNextFloorRequest() {
		byte[] data = UDPUtil.convertToBytes(currentFloor);
		DatagramPacket packet = new DatagramPacket(data, data.length, Scheduler.ADDRESS, Scheduler.ELEVATOR_REQUEST_PORT);
		UDPUtil.sendPacket(socket, packet);
		DatagramPacket receivePacket = new DatagramPacket(new byte[UDPUtil.RECEIVE_PACKET_LENGTH], UDPUtil.RECEIVE_PACKET_LENGTH);
		UDPUtil.receivePacket(socket, receivePacket);
		return (FloorRequest) UDPUtil.convertFromBytes(receivePacket.getData(), receivePacket.getLength());
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
		state.handleRequest(this, direction, floorNumber); // change state to reflect moving up/down
		state.handleReachedDestination(this, floorNumber); // change state to reflect reaching destination and stopping
	}

	/**
	 * Helper method to set a response for the scheduler
	 * 
	 * @param request The request currently being processed
	 */
	private void setResponseForScheduler(FloorRequest request) {
		String responseMessage = request.getElevatorEvent().toString() + " has been processed successfully";
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
