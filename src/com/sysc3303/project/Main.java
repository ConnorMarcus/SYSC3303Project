/**
 * 
 */
package com.sysc3303.project;

import java.text.ParseException;
import java.util.concurrent.atomic.AtomicLong;

import com.sysc3303.project.ElevatorEvent.Direction;

/**
 * @author Group 9
 *
 */
public class Main {
	
	
	/**
	 * @param args
	 * @throws ParseException 
	 * @throws IllegalArgumentException 
	 */
	public static void main(String[] args) throws IllegalArgumentException, ParseException {
		Scheduler s = new Scheduler();
		FloorSubsystem floor = new FloorSubsystem(s);
		Thread t1 = new Thread(new Elevator(s), "ElevatorThread");
		Thread t2 = new Thread(floor, "FloorThread");
		Thread t3 = new Thread(s, "SchedulerThread");
		t1.start();
		t2.start();
		t3.start();
		
	}

}
