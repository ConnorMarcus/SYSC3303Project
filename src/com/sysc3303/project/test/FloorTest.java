/**
 * 
 */
package com.sysc3303.project.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.sysc3303.project.Floor;
import com.sysc3303.project.Scheduler;

/**
 * @author Group 9
 *
 */
class FloorTest {
	private static Floor floor;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		floor = new Floor(new Scheduler());
	}

	@Test
	void test() {
		floor.addResponse("Testing");
		assertEquals("Testing", floor.getLatestResponse());
	}

}
