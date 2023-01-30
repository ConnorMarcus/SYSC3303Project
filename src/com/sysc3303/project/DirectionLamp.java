package com.sysc3303.project;

public class DirectionLamp extends Lamp {

    /**
     * A constructor for a Direction Lamp
     * @param direction which elevator the lamp denotes the arrivals of
     */
    public DirectionLamp(ElevatorEvent.Direction direction) {
        super(direction);
    }
}
