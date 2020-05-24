package com.mycodefu.birthday.java;

import javax.imageio.ImageIO;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static com.mycodefu.birthday.java.SoundLoader.loadSoundFile;

public class Start extends JPanel {
    private static final long serialVersionUID = 5526103823891521741L;

    private static BufferedImage cake;
    private static BufferedImage pacman;
    private static int pacmanX;
    private static int pacmanY;

    private static Clip wakka = loadSoundFile("wakka.wav");
    private static Clip eat = loadSoundFile("eat.wav");

    public Start() throws Exception {
        cake = new BufferedImage(640, 480, BufferedImage.TYPE_INT_ARGB);
        cake.getGraphics().drawImage(ImageIO.read(new File("java-birthday.jpg")), 20, 20, 580, 400, null);
    }

    public static void main(String[] args) throws Exception {
        JPanel pane = new Start();
        JFrame frame = new JFrame("Happy Birthday Java!");
        frame.add(pane);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setBounds(screenSize.width / 2 - 320, screenSize.height / 2 - 240, 640, 480);
        frame.setDefaultCloseOperation(3);
        frame.setVisible(true);
        Graphics graphics = cake.getGraphics();
        eat.loop(Clip.LOOP_CONTINUOUSLY);
        long nextNanoTime = System.nanoTime() + 33 * 1000000;
        while (angle <= 274) {
            if (System.nanoTime() >= nextNanoTime) {
                nextNanoTime = System.nanoTime() + 25 * 1000000;
                if (angle < 273) {
                    angle += 0.0025d;
                    graphics.setColor(Color.BLUE);
                    graphics.drawLine(0, 240, (int) (Math.sin(angle) * 1024d), (int) (Math.cos(angle) * 768d));
                    graphics.drawLine(0, 240, (int) (Math.sin(angle) * 1024d), (int) (Math.cos(angle - 180d) * 768d));
                } else {
                    System.out.println("writing message");
                    angle += 100;
                    graphics.setColor(Color.BLACK);
                    graphics.setFont(new Font("Arial", Font.PLAIN, 30));
                    graphics.drawString("            public boolean tastedNice = true;", 0, 100);
                }
                pane.repaint();
            }
        }
        BufferedImage pacmanClosedMouth = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
        Graphics pacmanClosedMouthGraphics = pacmanClosedMouth.getGraphics();
        pacmanClosedMouthGraphics.setColor(Color.YELLOW);
        pacmanClosedMouthGraphics.fillOval(0, 0, 50, 50);

        AtomicReference<BufferedImage> pacmanOpenMouth = new AtomicReference<>();

        BufferedImage pacmanOpenMouthRight = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
        Graphics pacmanOpenMouthRightGraphics = pacmanOpenMouthRight.getGraphics();
        pacmanOpenMouthRightGraphics.setColor(Color.YELLOW);
        pacmanOpenMouthRightGraphics.fillArc(0, 0, 50, 50, 30, 300);

        BufferedImage pacmanOpenMouthLeft = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
        Graphics pacmanOpenMouthLeftGraphics = pacmanOpenMouthLeft.getGraphics();
        pacmanOpenMouthLeftGraphics.setColor(Color.YELLOW);
        pacmanOpenMouthLeftGraphics.fillArc(0, 0, 50, 50, 210, 300);

        pacman = pacmanClosedMouth;

        pacmanOpenMouth.set(pacmanOpenMouthRight);
        AtomicBoolean movingRight = new AtomicBoolean(true);

        eat.stop();

        wakka.loop(Clip.LOOP_CONTINUOUSLY);
        AtomicBoolean dying = new AtomicBoolean();
        final Timer pacmanTimer = new Timer(60, e -> {
            if (!dying.get()) {
                if (movingRight.get()) {
                    pacmanX += 10;
                } else {
                    pacmanX -= 10;
                }
                if (movingRight.get() && (pacmanX + 50) >= 640) {
                    pacmanOpenMouth.set(pacmanOpenMouthLeft);
                    movingRight.set(false);
                    pacmanY += 50;
                }
                if (!movingRight.get() && pacmanX <= 0) {
                    pacmanOpenMouth.set(pacmanOpenMouthRight);
                    movingRight.set(true);
                    pacmanY += 50;
                }
                if (pacman == pacmanClosedMouth) {
                    pacman = pacmanOpenMouth.get();
                } else {
                    pacman = pacmanClosedMouth;
                }
                graphics.setColor(Color.BLUE);
                graphics.fillOval(pacmanX - 1, pacmanY - 1, 52, 52);
                pane.repaint();

                if ((pacmanY + 50) >= 480) {
                    wakka.stop();

                    Clip die = loadSoundFile("die.wav");
                    die.addLineListener(event -> {
                        if (event.getType() == LineEvent.Type.STOP) {
                            System.exit(0);
                        }
                    });
                    die.start();
                    dying.set(true);
                }
            }
        });
        pacmanTimer.start();
    }

    private static double angle = 270d;

    @Override
    public void paint(Graphics graphics) {
        super.paint(graphics);
        graphics.setColor(Color.BLUE);
        graphics.fillRect(0, 0, 640, 480);
        graphics.drawImage(cake, 0, 0, null);
        if (pacman != null) {
            graphics.drawImage(pacman, pacmanX, pacmanY, null);
        }
    }

}
