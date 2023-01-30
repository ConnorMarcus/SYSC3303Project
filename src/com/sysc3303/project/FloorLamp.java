package com.sysc3303.project;

public class FloorLamp extends Lamp {

    private ElevatorEvent.Direction direction;
    private boolean litUp;

    /**
     * A constructor for a floor lamp
     * @param direction
     */
    public FloorLamp(ElevatorEvent.Direction direction) {
        super(direction);
    }
}
