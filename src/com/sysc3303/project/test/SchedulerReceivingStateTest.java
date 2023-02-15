/**
 *
 */
package com.sysc3303.project.test;

import static org.junit.jupiter.api.Assertions.*;

import com.sysc3303.project.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * @author Group 9
 *
 */
class SchedulerReceivingStateTest {

    private static Scheduler scheduler;
    private static SchedulerReceivingState receivingState;

    /**
     * @throws java.lang.Exception
     */
    @BeforeAll
    public static void setUpBeforeClass() throws Exception {
        scheduler = new Scheduler();
        receivingState = new SchedulerReceivingState();
    }

    @Test
    public void testHandleResponseReceived() {
        receivingState.handleResponseReceived(scheduler);
        assertEquals(SchedulerTest.RECEIVING_SENDING_STRING, scheduler.getState().toString());
    }
}
