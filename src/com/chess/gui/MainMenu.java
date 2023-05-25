package src.com.chess.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;


public class MainMenu extends JFrame {
    static JFrame MainMenuFr = new JFrame("PlayChessWithMachine");
    static JButton Start = new JButton("Start");
    static JButton HowToPlay = new JButton("Setting");
    static JButton Exit = new JButton("Exit");
    static JFrame Tutorial;
    static JLabel text;

    ActionEvent e;


    public MainMenu() {
        addButtons();
    }

    public void addButtons() {
        Start.setBounds(95, 50, 200, 80);
        HowToPlay.setBounds(95, 150, 200, 80);
        Exit.setBounds(95,250, 200, 80);


        MainMenuFr.add(Start);
        MainMenuFr.add(HowToPlay);
        MainMenuFr.add(Exit);

        try {
            ImageIcon image = new ImageIcon(getClass().getResource("start.jpg"));
            JLabel displayField = new JLabel(image);
            MainMenuFr.add(displayField);
        } catch (Exception i) {
            //System.out.println("Image cannot be found");
        }

        MainMenuFr.setSize(400, 420);
        MainMenuFr.setLayout(null);
        MainMenuFr.setResizable(false);
        MainMenuFr.setVisible(true);
        MainMenuFr.setLocationRelativeTo(null);
        MainMenuFr.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


        Start.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Table.get().show();
            }

        });
        HowToPlay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Tutorial = new JFrame("How to Play");
//                Tutorial.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                Tutorial.setVisible(true);
                Tutorial.setResizable(false);
                Tutorial.setSize(500, 500);
                Tutorial.setTitle("Hi, there!");
                Tutorial.setLocationRelativeTo(null);
                try {
                    ImageIcon image = new ImageIcon(getClass().getResource("tutorial.jpg"));
                    JLabel displayField = new JLabel(image);
                    Tutorial.add(displayField);
                } catch (Exception i) {
                    //System.out.println("Image cannot be found");
                }
            }

        });
        Exit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }

        });
    }

    }
