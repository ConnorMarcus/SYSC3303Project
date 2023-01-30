package com.sysc3303.project;

import java.io.*;
import java.nio.file.FileSystem;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.Date;
import java.util.HashMap;

/**
 * @author Patrick Vafaie
 *
 */
public class FloorSubsystem implements Runnable {

    private final String INPUT_PATH = "Resources/floor_input.txt";

    private final Scheduler scheduler;

    private ArrayDeque<ElevatorEvent> eventQueue;

    // The floors in the building by floor number
    private HashMap<Integer, Floor> floors;
    private int floorEntering = -1;
    private int floorExiting = -1;

    /**
     * A constructor for a FloorSubsystem
     * @param scheduler The scheduler used to communicate with elevators
     */
    public FloorSubsystem(Scheduler scheduler) {
        this.scheduler = scheduler;
        eventQueue = new ArrayDeque<>();
        floors = new HashMap<>();
        for (int i = 0; i < Floor.NUM_FLOORS; ++i) {
            floors.put(i + 1, new Floor(i + 1));
        }
    }

    @Override
    public void run() {
        readFloorInputFile();
        // Send events to scheduler
        for (ElevatorEvent e : eventQueue) {
            scheduler.addEvent(e);
        }

        while(true) {
            if (floorExiting > 0) {
                System.out.println(Thread.currentThread().getName() + ": people are exiting on floor: " + floorExiting);
                floorExiting = -1;
                scheduler.resetFloorExiting();
            }
            if (floorEntering > 0) {
                System.out.println(Thread.currentThread().getName() + ": people are entering on floor: " + floorEntering);
                floorEntering = -1;
                scheduler.resetFloorEntering();
            }
        }
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

    /**
     * Read an event from the elevator via the scheduler+
     * @param floor which floor the elevator has arrived at
     * @param elevatorId the ID of the elevator
     */
    public synchronized void respondToElevatorArrival(int floor, int elevatorId) {
        floors.get(floor).handleElevatorArrival(elevatorId);
    }

    /**
     * Reads the input file for elevator timings
     * Parses input into a queue of elevator events
     */
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


    /**
     * Setting what floor people are entering the elevator.
     *
     * @param floor	current floor of the elevator
     */
    public synchronized void setFloorEntering(int floor) {
        floorEntering = floor;
    }

    /**
     * Setting what floor people are entering the elevator.
     *
     * @param floor	current floor of the elevator
     */
    public synchronized void setFloorExiting(int floor) {
        floorExiting= floor;
    }
}
