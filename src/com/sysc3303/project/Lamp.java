package com.sysc3303.project;

public abstract class Lamp {

    private ElevatorEvent.Direction direction;
    private boolean litUp;

    public Lamp(ElevatorEvent.Direction direction) {
        this.direction = direction;
        litUp = false;
    }

    public void turnOn() { litUp = true; }

    public void turnOff() { litUp = false; }

    public boolean isLampOn() { return litUp; }
}
