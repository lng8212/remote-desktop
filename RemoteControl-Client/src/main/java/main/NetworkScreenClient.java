package main;

import util.Constant;
import view.ControlPanel;

import javax.swing.*;
import java.awt.event.*;

public class NetworkScreenClient extends JFrame {
	private JFrame jFrame = this;
	private ControlPanel controlPanel = new ControlPanel(jFrame);
	public NetworkScreenClient() {
		setTitle("Remove Assistance");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
		setLayout(null);
		setContentPane(controlPanel);
		setSize(Constant.FRAME_WIDTH, Constant.FRAME_HEIGHT);
		setVisible(true);
		setLocationRelativeTo(null);
		addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				setLocation(e.getXOnScreen(), e.getYOnScreen());
			}
		});
	}
}
