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
	public ElevatorPanel panel;
	
	public GUI() {
		super();
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(550, 600));
        this.setResizable(false);
        this.setLayout(new FlowLayout());
        this.addPanels();
        this.pack();
        this.setVisible(true);
	}
	
	public void addPanels() {
		panel = new ElevatorPanel();
		this.add(panel);
		this.add(new ElevatorPanel());
		this.add(new ElevatorPanel());
		this.add(new ElevatorPanel());
	}
	
	public static void main(String[] args) throws InterruptedException {
		GUI test = new GUI();
		
		while (true) {
		
			test.panel.closeDoorsLabel();
			test.panel.addStar(22);
			test.panel.handleMoving(Direction.UP);
			Thread.sleep(2000);
			test.panel.handleMoving(Direction.UP);
			Thread.sleep(2000);
			test.panel.removeStar(22);
			test.panel.handleStopped();
			test.panel.openDoorsLabel();
			Thread.sleep(2000);
			test.panel.handleHardFault();
			Thread.sleep(2000);
			break;
			
		}
		
	}

}
