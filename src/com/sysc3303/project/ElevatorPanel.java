package com.sysc3303.project;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Hashtable;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import com.sysc3303.project.ElevatorEvent.Direction;

/**
 * @author Group 9
 *
 */
public class ElevatorPanel extends JPanel {
	private final int WIDTH = 125;
	private final int HEIGHT = 600;
	private JLabel floorLabel;
	public JSlider slider;
	private JLabel doorsLabel;
    private final Color GREEN_COLOR = new Color(0, 192, 0);
    private final Color RED_COLOR = new Color(255, 48, 0);
    private final Color YELLOW_COLOR = new Color(255, 193, 70);
    private final String UP_ICON  = "Resources/images/upArrowIcon.png";
    private final String DOWN_ICON  = "Resources/images/downArrowIcon.png";
    private final String TRANS_FAULT_ICON  = "Resources/images/transientFaultIcon.png";
    private final String HARD_FAULT_ICON  = "Resources/images/hardFaultIcon.png";
    
	
	public ElevatorPanel() {
		super();
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.setBackground(Color.black);
		initializeComponents();
	}
	
	private void initializeComponents() {
		createFloorLabel();
		createSlider();
		createDoorLabel();
		this.add(floorLabel);
		this.add(slider);
		this.add(doorsLabel);
	}
	
	private void createSlider() {
		slider = new JSlider(SwingConstants.VERTICAL, Floor.BOTTOM_FLOOR, Floor.NUM_FLOORS, Floor.BOTTOM_FLOOR);
		slider.setPreferredSize(new Dimension(WIDTH, HEIGHT - 150));
		slider.setBackground(Color.black);
		slider.setMajorTickSpacing(1);
	    slider.setPaintLabels(true);
	    slider.setPaintTicks(true);
	    
	    slider.removeMouseListener(slider.getMouseListeners()[0]);
	    slider.setFocusable(false);
	    
	    Hashtable<Integer, JLabel> table = new Hashtable<>();
	    
	    for (int i = Floor.BOTTOM_FLOOR; i <= Floor.NUM_FLOORS; i++) {
	    	JLabel label = new JLabel(String.valueOf(i));
	    	label.setForeground(Color.white);
	    	label.setPreferredSize(new Dimension(label.getPreferredSize().width + 10, label.getPreferredSize().height));
	        label.setFont(new Font("Aharoni", 0, 13));
	    	table.put(i, label);
	    }

	    slider.setLabelTable(table);
	}
	
	private void createFloorLabel() {
		 floorLabel = new JLabel(String.valueOf(Floor.BOTTOM_FLOOR));
	     floorLabel.setFont(new Font("Aharoni", 0, 20));
	     floorLabel.setPreferredSize(new Dimension(WIDTH, 35));
	     floorLabel.setForeground(Color.white);
	     floorLabel.setIconTextGap(5);
	     floorLabel.setVerticalAlignment(JLabel.CENTER);
	     floorLabel.setHorizontalAlignment(JLabel.CENTER);
	}
	
	
	private void createDoorLabel() {
		 doorsLabel = new JLabel("[|   |]");
	     doorsLabel.setFont(new Font("Aharoni", 0, 20));
	     doorsLabel.setPreferredSize(new Dimension(WIDTH, 35));
	     doorsLabel.setForeground(Color.white);
	     doorsLabel.setVerticalAlignment(JLabel.CENTER);
	     doorsLabel.setHorizontalAlignment(JLabel.CENTER);
	}
	
	
	public void closeDoorsLabel() {
		doorsLabel.setText("[|]");
	}
	
	
	public void openDoorsLabel() {
		doorsLabel.setText("[|   |]");
	}
	
	private void updateFloorLabel(String path, Color color) {
		floorLabel.setIcon(new ImageIcon(path));
		floorLabel.setForeground(color);
	}
	
	public void handleStateChange(String state) {
		if (state.equals(Direction.UP.toString())) {
			handleGoingUpState();
		}
		else if (state.equals(Direction.DOWN.toString())) {
			handleGoingDownState();
		}
		else if (state.equals(Direction.STOPPED.toString())) {
			handleStopped();
		}
		else if (state.equals(ElevatorState.REPAIRING_STATE_NAME)){
			handleTransientFault();
		} 
	}
	
	private void handleGoingUpState() {
		if (floorLabel.getIcon() == null) {
			updateFloorLabel(UP_ICON, GREEN_COLOR);
		}
	}
	
	
	private void handleGoingDownState() {
		if (floorLabel.getIcon() == null) {
			updateFloorLabel(DOWN_ICON, RED_COLOR);
		}
	
	}
	
	public void goingUp() {
		int floorNum = slider.getValue() + 1;
		slider.setValue(floorNum);
		floorLabel.setText(String.valueOf(floorNum));

	}
	
	public void goingDown() {
		int floorNum = slider.getValue() - 1;
		slider.setValue(floorNum);
		floorLabel.setText(String.valueOf(floorNum));

	}
	
	private void handleStopped() {
		floorLabel.setIcon(null);
		floorLabel.setForeground(Color.white);
	}
	
	private void handleTransientFault() {
		updateFloorLabel(TRANS_FAULT_ICON, YELLOW_COLOR);
	}
	
	public void handleHardFault() {
		updateFloorLabel(HARD_FAULT_ICON, RED_COLOR);
		slider.setForeground(RED_COLOR);
	    for (int i = Floor.BOTTOM_FLOOR; i <= Floor.NUM_FLOORS; i++) {
	    	((JLabel) slider.getLabelTable().get(i)).setForeground(RED_COLOR);
	    }
	}
	
	public void highlightDestination(int floorNum) {
	   ((JLabel) slider.getLabelTable().get(floorNum)).setForeground(Color.cyan);
	   slider.repaint();
	}
	
	public void unHighlightDestination(int floorNum) {
		((JLabel) slider.getLabelTable().get(floorNum)).setForeground(Color.white);	
		slider.repaint();
	}
	
	public void addStar(int floorNum) {
		((JLabel) slider.getLabelTable().get(floorNum)).setText(floorNum + " *");	
		slider.repaint();
	}
	
	public void removeStar(int floorNum) {
		((JLabel) slider.getLabelTable().get(floorNum)).setText(String.valueOf(floorNum));
		slider.repaint();
	}
	
	/*
	 * Get request --> put star on request origin
	 * 
	 * Navigate to request origin --> 	if you have to move --> closeDoors, handleMoving, handleStop, remove star, open doors
	 * 								--> don't move --> remove star 
	 * 
	 * Navigate to destination --> highlight destination, close doors, handleMoving, handle stop, remove highlight, open doors 
	 * 
	 * faults --> handle it
	 * 
	 */

}
