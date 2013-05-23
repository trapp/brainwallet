package net.zaeda.brainwallet;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                showGui();
            }
        });
    }

    private static void showGui() {

        JFrame frame = new JFrame("Brain Wallet Calculator");
        frame.setContentPane(new AddressForm().container);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}

