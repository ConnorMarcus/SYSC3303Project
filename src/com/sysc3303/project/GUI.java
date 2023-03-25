package com.sysc3303.project;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Panel;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import com.sysc3303.project.ElevatorEvent.Direction;

/**
 * Class to represent a request made by an Elevator.
 * 
 * @author Group 9
 */
public class GUI extends JFrame {
	
	public GUI() {
		super();
		initializeJFrame();
	}
	
	public void initializeJFrame() {
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLayout(new FlowLayout());
        this.addPanels();
        this.pack();
        this.setVisible(true);
	}
	
	public void addPanels() {
		for(Elevator e: Elevator.elevators) {
			this.add(e.getElevatorPanel());
		}
	}
	
}
