package src.com.chess.gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class MainMenu extends JFrame {
    static JButton Start = new JButton("Start");
    static JButton Setting = new JButton("Setting");
    static JFrame MainMenuFr = new JFrame("PlayChessWithMachine");

    ActionEvent e;


    public MainMenu() {
        addButtons();
    }

    public static void addButtons() {
        Start.setBounds(95, 50, 200, 80);
        Setting.setBounds(95, 160, 200, 80);


        MainMenuFr.add(Start);
        MainMenuFr.add(Setting);

        MainMenuFr.setSize(400, 500);
        MainMenuFr.setLayout(null);
        MainMenuFr.setResizable(false);
        MainMenuFr.setVisible(true);
        MainMenuFr.setLocationRelativeTo(null);


        Start.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Table.get().show();
            }

        });
        Setting.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Table.get().show();
            }

        });
    }
}