package GeoGrid2.game;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingsGUI extends JFrame {
    private JPanel mainPanel;
    private JButton openMapButton;
    private JTextField textField1;

    SettingsGUI(DataTransfer dt) {

        this.setContentPane(mainPanel);
        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);

        openMapButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                SelectionMap selectionMap = new SelectionMap(dt);

            }
        });
    }
}
