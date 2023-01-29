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
	public static final int NUM_FLOORS = 5;
	
	
	/**
	 * @param args
	 * @throws ParseException 
	 * @throws IllegalArgumentException 
	 */
	public static void main(String[] args) throws IllegalArgumentException, ParseException {
		Scheduler s = new Scheduler();
		s.addEvent(new ElevatorEvent(Floor.createElevatorTime("12:14:25.100"), 2, Direction.UP, 4));
		s.addEvent(new ElevatorEvent(Floor.createElevatorTime("12:14:28.100"), 2, Direction.UP, 5));
		Thread t = new Thread(new Elevator(s), "Elevator1");
		t.start();
	}

}
