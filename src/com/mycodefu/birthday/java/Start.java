package com.mycodefu.birthday.java;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Start extends JPanel {
	private static final long serialVersionUID = 5526103823891521741L;

private BufferedImage cake;

public Start() throws Exception{
	cake = ImageIO.read(new File("java-birthday.jpg"));
}

public static void main(String[] args) throws Exception {
	JPanel pane = new Start();
	JFrame frame = new JFrame("Happy Birthday Java!");
	frame.add(pane);
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	frame.setBounds(screenSize.width/2-320, screenSize.height/2-240, 640, 480);
	frame.setDefaultCloseOperation(3);
	frame.setVisible(true);
	long nextNanoTime = System.nanoTime()+33*1000000;
	while(angle <= 274) {
		if(System.nanoTime() >= nextNanoTime) {
			nextNanoTime = System.nanoTime()+25*1000000;
			pane.repaint();
		}
	}
}

private static double angle = 270d;

@Override
public void paint(Graphics graphics) {
	super.paint(graphics);
	graphics.setColor(Color.BLUE);
	graphics.fillRect(0, 0, 640, 480);
	graphics.drawImage(cake, 20, 20, 580, 400, null);
	if(angle < 273) {
		angle+= 0.0025d;
		Graphics cg = cake.getGraphics();
		cg.setColor(Color.BLUE);
		cg.drawLine(0, 240, (int)(Math.sin(angle)*320d), (int)(Math.cos(angle)*240d));
		cg.drawLine(0, 240, (int)(Math.sin(angle)*320d), (int)(Math.cos(angle-180d)*240d));
	}else {
		System.out.println("writing message");
		angle+=100;
		graphics.setColor(Color.BLACK);
		graphics.setFont(new Font("airial", 30, 30));
		graphics.drawString("            public boolean tastedNice = true;", 0, 240);
	}
}
}
