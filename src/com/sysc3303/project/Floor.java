package com.sysc3303.project;

import java.io.*;
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
	public static final int NUM_CARS = 1;
	private static final String INPUT_FILE_PATH = "Resources/floor_input.txt";
	private final Scheduler scheduler;
	private final Queue<ElevatorEvent> eventQueue;
	private final Queue<String> responseQueue;

	/**
	 * A constructor for a FloorSubsystem
	 * 
	 * @param scheduler The scheduler used to communicate with elevators
	 */
	public Floor(Scheduler scheduler) {
		this.scheduler = scheduler;
		eventQueue = new ArrayDeque<>();
		responseQueue = new ArrayDeque<>();
	}

	/**
	 * The Floor thread's run method
	 */
	@Override
	public void run() {
		readFloorInputFile();

		// Send events to scheduler
		for (ElevatorEvent e : eventQueue) {
			scheduler.addFloorRequest(new FloorRequest(this, e));
		}
		for (int i = 0; i < eventQueue.size(); i++) {
			System.out.println(Thread.currentThread().getName() + ": " + getLatestResponse());
		}

		System.exit(0);
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
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
		return responseQueue.remove();
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
			while ((line = br.readLine()) != null) {
				lineValues = line.split(" ");
				Time elevatorTime = Time.createFromTimeString((lineValues[0]));
				int elevatorFloor = Integer.parseInt(lineValues[1]);
				ElevatorEvent.Direction elevatorDirection = ElevatorEvent.Direction.UP;
				if (lineValues[2].equals("Down"))
					elevatorDirection = ElevatorEvent.Direction.DOWN;
				int elevatorButton = Integer.parseInt(lineValues[3]);
				ElevatorEvent elevatorEvent = new ElevatorEvent(elevatorTime, elevatorFloor, elevatorDirection,
						elevatorButton);
				eventQueue.add(elevatorEvent);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}

		try {
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
