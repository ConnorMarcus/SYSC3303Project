package com.sysc3303.project;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.Date;

/**
 * @author Patrick Vafaie
 *
 */
public class FloorSubsystem implements Runnable {

    private final String INPUT_PATH = "Resources/floor_input.txt";

    private final Scheduler scheduler;

    private ArrayDeque<ElevatorEvent> eventQueue;

    public FloorSubsystem(Scheduler scheduler) {
        this.scheduler = scheduler;
        eventQueue = new ArrayDeque<>();
    }

    @Override
    public void run() {
        readFloorInputFile();

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

    private void readFloorInputFile() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(INPUT_PATH));
            String line = br.readLine(); // First line is just the column names
            String[] lineValues;
            while (line != null) {
                line = br.readLine();
                lineValues = line.split("\t");
                Date elevatorTime = createElevatorTime(lineValues[0]);
                int elevatorFloor = Integer.parseInt(lineValues[1]);
                ElevatorEvent.Direction elevatorDirection = ElevatorEvent.Direction.UP;
                if (lineValues[2].equals("Down")) elevatorDirection = ElevatorEvent.Direction.DOWN;
                int elevatorButton = Integer.parseInt(lineValues[3]);
                ElevatorEvent elevatorEvent = new ElevatorEvent(
                        elevatorTime, elevatorFloor, elevatorDirection, elevatorButton
                );
                eventQueue.add(elevatorEvent);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}
