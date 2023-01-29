/**
 * 
 */
package com.sysc3303.project;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Group 9
 *
 */
public class ElevatorIDGenerator {
	private static final AtomicLong ID = new AtomicLong(0);    
    public static long getUniqueID() {
		return ID.getAndIncrement();
	}
}
