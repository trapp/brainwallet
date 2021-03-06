package net.zaeda.brainwallet;

import com.google.bitcoin.core.Address;
import com.google.bitcoin.core.DumpedPrivateKey;
import com.google.bitcoin.core.ECKey;
import com.google.bitcoin.core.NetworkParameters;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import org.spongycastle.util.encoders.Hex;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class AddressForm {
    public JPanel container;
    private JTextField secretField;
    private JTextArea outputField;
    private JButton calculateButton;
    private JTextField addressField;
    private JTextField privateKeyField;
    private JButton lookUpButton;
    private JTabbedPane tabbedPane1;

    Map<Character, Character> easy16HexToEasy = new HashMap<Character, Character>();

    public AddressForm() {

        easy16HexToEasy.put('0', 'a');
        easy16HexToEasy.put('1', 's');
        easy16HexToEasy.put('2', 'd');
        easy16HexToEasy.put('3', 'f');
        easy16HexToEasy.put('4', 'g');
        easy16HexToEasy.put('5', 'h');
        easy16HexToEasy.put('6', 'j');
        easy16HexToEasy.put('7', 'k');
        easy16HexToEasy.put('8', 'w');
        easy16HexToEasy.put('9', 'e');
        easy16HexToEasy.put('a', 'r');
        easy16HexToEasy.put('b', 't');
        easy16HexToEasy.put('c', 'u');
        easy16HexToEasy.put('d', 'i');
        easy16HexToEasy.put('e', 'o');
        easy16HexToEasy.put('f', 'n');

        calculateButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {

                    String secret = secretField.getText();
                    MessageDigest sha512 = MessageDigest.getInstance("SHA-512");
                    byte[] secretBytes = sha512.digest(secret.getBytes("UTF-8"));

                    byte[] rootCode = new byte[16];
                    System.arraycopy(secretBytes, 0, rootCode, 0, 16);

                    byte[] chainCode = new byte[16];
                    System.arraycopy(secretBytes, 16, chainCode, 0, 16);

                    // Root and Chain key with appended checksum.
                    byte[][] lines = {new byte[18], new byte[18], new byte[18], new byte[18]};

                    for (int i = 0; i < 4; i++) {
                        byte[] part = new byte[16];
                        // Copy the responding sub part of the key to the temporary part variable.
                        System.arraycopy(secretBytes, i * 16, part, 0, 16);

                        // Copy the 16 byte part of the key to the line.
                        System.arraycopy(part, 0, lines[i], 0, 16);
                        // Calculate the checksum of the part and append it to the line.
                        System.arraycopy(checksum(part), 0, lines[i], 16, 2);
                    }

                    // Format the lines in a readable format
                    // That is:
                    // * Add a space every 4 characters
                    // * Convert the bytes to hex.
                    // * Convert the hex characters to armorys "easy16" characters. (See easy16HexToEasy for a list).
                    StringBuilder output = new StringBuilder();
                    int i = 0;
                    for (byte[] bytes : lines) {
                        output.append(easify(new String(Hex.encode(bytes))));
                        i++;
                        if (i < lines.length) {
                            output.append("\n");
                        }
                    }

                    outputField.setText(output.toString());

                } catch (NoSuchAlgorithmException e) {
                    outputField.setText("Error: " + e.getMessage());
                } catch (UnsupportedEncodingException e) {
                    outputField.setText("Error: " + e.getMessage());
                }

                AddressCreator creator = new AddressCreator();
                ECKey key = creator.createAddress(secretField.getText());
                DumpedPrivateKey privateKey = key.getPrivateKeyEncoded(NetworkParameters.prodNet());
                Address address = key.toAddress(NetworkParameters.prodNet());
                privateKeyField.setText(privateKey.toString());
                addressField.setText(address.toString());
            }
        });
        lookUpButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
                if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
                    try {
                        desktop.browse(new URL("http://blockchain.info/de/address/" + addressField.getText()).toURI());
                    } catch (IOException e1) {
                        outputField.setText("Error: " + e1.getMessage());
                    } catch (URISyntaxException e1) {
                        outputField.setText("Error: " + e1.getMessage());
                    }
                }
            }
        });
    }

    byte[] checksum(byte[] target) throws NoSuchAlgorithmException {
        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        return sha256.digest(sha256.digest(target));
    }

    private String easify(String string) {
        StringBuilder builder = new StringBuilder();

        int i = 0;
        for (char ch : string.toCharArray()) {
            if (easy16HexToEasy.containsKey(ch)) {
                builder.append(easy16HexToEasy.get(ch));
            } else {
                // Should not be possible as easy16HexToEasy contains all hex characters.
                builder.append(ch);
            }
            i++;
            if (i == 4) {
                builder.append(' ');
                i = 0;
            }
        }
        return builder.toString().trim();
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        container = new JPanel();
        container.setLayout(new GridLayoutManager(3, 3, new Insets(10, 10, 10, 10), -1, -1));
        container.setMinimumSize(new Dimension(500, 110));
        container.setPreferredSize(new Dimension(620, 240));
        final JLabel label1 = new JLabel();
        label1.setText("Secret");
        container.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        secretField = new JTextField();
        container.add(secretField, new GridConstraints(0, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        calculateButton = new JButton();
        calculateButton.setText("Calculate");
        container.add(calculateButton, new GridConstraints(1, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        tabbedPane1 = new JTabbedPane();
        container.add(tabbedPane1, new GridConstraints(2, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(2, 2, new Insets(10, 10, 10, 10), -1, -1));
        tabbedPane1.addTab("Armory Wallet", panel1);
        final JLabel label2 = new JLabel();
        label2.setText("Armory Paper Backup");
        panel1.add(label2, new GridConstraints(0, 0, 2, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        outputField = new JTextArea();
        outputField.setEditable(false);
        outputField.setFont(new Font("Monospaced", outputField.getFont().getStyle(), 14));
        outputField.setMargin(new Insets(10, 10, 10, 10));
        panel1.add(outputField, new GridConstraints(0, 1, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(4, 3, new Insets(10, 10, 10, 10), -1, -1));
        tabbedPane1.addTab("Single Bitcoin Address", panel2);
        final JLabel label3 = new JLabel();
        label3.setText("Bitcoin Private Key");
        panel2.add(label3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Bitcoin Address");
        panel2.add(label4, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        privateKeyField = new JTextField();
        privateKeyField.setEditable(false);
        privateKeyField.setFont(new Font("Monospaced", privateKeyField.getFont().getStyle(), 14));
        panel2.add(privateKeyField, new GridConstraints(2, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        addressField = new JTextField();
        addressField.setEditable(false);
        addressField.setFont(new Font("Monospaced", addressField.getFont().getStyle(), 14));
        panel2.add(addressField, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        lookUpButton = new JButton();
        lookUpButton.setText("Look up");
        panel2.add(lookUpButton, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setFont(new Font(label5.getFont().getName(), label5.getFont().getStyle(), 9));
        label5.setText("Be careful when sending funds from this address. Most clients create new change addresses not covered by this private key.");
        panel2.add(label5, new GridConstraints(0, 0, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setFont(new Font(label6.getFont().getName(), label6.getFont().getStyle(), 9));
        label6.setText("You should prefer \"Armory Wallet\" for future proof deterministic address generation based on your secret.");
        panel2.add(label6, new GridConstraints(1, 0, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return container;
    }
}
