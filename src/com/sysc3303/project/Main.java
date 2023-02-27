/**
 * 
 */
package com.sysc3303.project;

import java.util.Arrays;

/**
 * @author Group 9
 *
 */
public class Main {

	/**
	 * FOR TESTING ONLY (REMOVE CLASS)
	 */
	public static void main(String[] args) {
		FloorRequest request = new FloorRequest(new ElevatorEvent(new Time("5", "12", "30", "123"), 1, ElevatorEvent.Direction.UP, 4));
		System.out.println(request.getElevatorEvent());
		byte[] bytes = UDPUtil.convertToBytes(request);
		System.out.println("length: " + bytes.length);
		for (byte b : bytes) {
			System.out.print(b + ", ");
		}
		System.out.println();
		FloorRequest request2 = (FloorRequest) UDPUtil.convertFromBytes(bytes, bytes.length);
		System.out.println(request2.getElevatorEvent());

	}

}
