package com.sysc3303.project;

/**
 * @author Group 9
 *
 */
public class FloorButton {

    private boolean pressed;
    private ElevatorEvent.Direction direction;

    /**
     * A constructor for a Floor Button
     * @param direction Which direction the button gets the elevator to go from this floor
     */
    public FloorButton(ElevatorEvent.Direction direction) {
        pressed = false;
        this.direction = direction;
    }

    /**
     * Either sets the button as pressed or not pressed
     * @param pressed Whether the button is pressed
     */
    public void setPressed(Boolean pressed) {this.pressed = pressed;}

    /**
     * Checks if the button is pressed
     * @return Boolean whether the button is pressed or not
     */
    public boolean getButtonStatus() { return pressed; }

    public void pressButton() { pressed = !pressed; }

    public ElevatorEvent.Direction getDirection() { return direction; }
}
