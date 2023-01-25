/**
 * 
 */
package com.sysc3303.project;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author
 *
 */
public class Floor implements Runnable {
	private final Scheduler scheduler;
	
	public Floor(Scheduler scheduler) {
		this.scheduler = scheduler;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Helper method to create a Date object from a time string (called when reading event from text file)
	 * 
	 * @param time A time string in the following format hh:mm:ss.mmm
	 * @return A Date object corresponding to the time string 
	 * @throws ParseException An exception is thrown if the time string is not in the correct format
	 */
	public static Date createElevatorTime(String time) throws ParseException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss.mmm");
		return simpleDateFormat.parse(time);
	}
	
	private void readEvent() {
		
	}

}
