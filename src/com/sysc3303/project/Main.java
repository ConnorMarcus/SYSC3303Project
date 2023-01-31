/**
 * 
 */
package com.sysc3303.project;


/**
 * @author Group 9
 *
 */
public class Main {

	/**
	 * main method (entry point of program)
	 */
	public static void main(String[] args) {
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
