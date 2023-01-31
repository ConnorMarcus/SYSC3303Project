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
	 * main method (entry point of program)
	 */
	public static void main(String[] args) throws IllegalArgumentException, ParseException {
		Scheduler s = new Scheduler();
		Floor f = new Floor(s);
		Elevator e = new Elevator(s);
		Thread t1 = new Thread(e, "ElevatorThread");
		Thread t2 = new Thread(f, "FloorThread");
		Thread t3 = new Thread(s, "SchedulerThread");
		t1.start();
		t2.start();
		t3.start();
		
	}

}
