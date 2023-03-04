package com.sysc3303.project;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayDeque;
import java.util.Queue;

/**
 * The Floor subsystem
 * 
 * @author Group 9
 *
 */
public class Floor implements Runnable {
	public static final int NUM_FLOORS = 5;
	public static final int BOTTOM_FLOOR = 1;
	public static final int PORT = 5552;
	public static final InetAddress ADDRESS = UDPUtil.getLocalHost();
	private static final String INPUT_FILE_PATH = "Resources/floor_input.txt";
	private final DatagramSocket receiveSocket, sendSocket;
	private final Queue<ElevatorEvent> eventQueue = new ArrayDeque<>();
	private final Queue<String> responseQueue = new ArrayDeque<>();

	/**
	 * A constructor for a FloorSubsystem
	 */
	public Floor() {
		receiveSocket = UDPUtil.createDatagramSocket(PORT);
		sendSocket = UDPUtil.createDatagramSocket();
	}

	/**
	 * Closes the sockets associated with the object
	 */
	public void closeSockets() {
		receiveSocket.close();
		sendSocket.close();
	}
	
	/**
	 * The Floor thread's run method
	 */
	@Override
	public void run() {
		readFloorInputFile();

		Thread sendToScheduler = new Thread(() -> {
			Time currentTime, previousTime; 
			previousTime = eventQueue.peek().getTime(); 
			// Send events to scheduler
			for (ElevatorEvent e : eventQueue) {
				sendFloorRequest(new FloorRequest(e));
				currentTime = e.getTime(); 
				int milliseconds = previousTime.getTimeDifferenceInMS(currentTime);
				try {
					Thread.sleep(milliseconds);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				previousTime = currentTime; 
		
			}
			sendSocket.close();
		});
		
		Thread receiveResponses = new Thread(() -> {
			for (int i = 0; i < eventQueue.size(); i++) {
				receiveResponse();
				System.out.println(Thread.currentThread().getName() + ": " + getLatestResponse());
			}
			receiveSocket.close();
		}, "FloorThread");

		sendToScheduler.start();
		receiveResponses.start();
	}

	/**
	 * @param responseMessage the response message from the elevator
	 */
	public synchronized void addResponse(String responseMessage) {
		responseQueue.add(responseMessage);
		notifyAll();
	}

	/**
	 * @return the response message at the front of the queue
	 */
	public synchronized String getLatestResponse() {
		while (responseQueue.isEmpty()) {
			try {
				wait(); // wait if there are no responses available
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
		return responseQueue.remove();
	}
	
	/**
	 * Receives a response packet from the Scheduler subsystem
	 */
	private void receiveResponse() {
		DatagramPacket responsePacket = new DatagramPacket(new byte[UDPUtil.RECEIVE_PACKET_LENGTH], UDPUtil.RECEIVE_PACKET_LENGTH);
		UDPUtil.receivePacket(receiveSocket, responsePacket);
		System.out.println("Floor: Received response from Scheduler");
		String response = new String(responsePacket.getData(), 0, responsePacket.getLength());
		addResponse(response);
	}
	
	/**
	 * Sends a FloorRequest object to the Scheduler subsystem
	 * 
	 * @param request the FloorRequest to send
	 */
	private void sendFloorRequest(FloorRequest request) {
		byte[] data = UDPUtil.convertToBytes(request);
		DatagramPacket packet = new DatagramPacket(data, data.length, Scheduler.ADDRESS, Scheduler.FLOOR_REQUEST_PORT);
		UDPUtil.sendPacket(sendSocket, packet);
		System.out.println("Floor: Sent FloorRequest object to Scheduler");
	}

	/**
	 * Reads the input file for elevator timings Parses input into a queue of
	 * elevator events
	 */
	private void readFloorInputFile() {
		BufferedReader br = null;
		try {
			String rootProjectPath = new File("").getAbsolutePath();
			br = new BufferedReader(new FileReader(rootProjectPath.concat("/" + INPUT_FILE_PATH)));
			String line = br.readLine(); // First line is just the column names

			String[] lineValues;
			
			// read remaining lines from the text file
			while ((line = br.readLine()) != null) {
				lineValues = line.split(" ");
				Time elevatorTime = Time.createFromTimeString((lineValues[0]));
				int elevatorFloor = Integer.parseInt(lineValues[1]);
				ElevatorEvent.Direction elevatorDirection = ElevatorEvent.Direction.UP;
				if (lineValues[2].equals("Down"))
					elevatorDirection = ElevatorEvent.Direction.DOWN;
				int elevatorButton = Integer.parseInt(lineValues[3]); //Read from input file all necessary info
				ElevatorEvent elevatorEvent = new ElevatorEvent(elevatorTime, elevatorFloor, elevatorDirection,
						elevatorButton); //Create new instance of ElevatorEvent with info read from file
				eventQueue.add(elevatorEvent); //Add ElevatorEvent to Queue
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * Entry point for the Floor subsystem
	 */
	public static void main(String[] args) {
		Thread floorThread = new Thread(new Floor(), "FloorSubsystem");
		floorThread.start();
	}
}
