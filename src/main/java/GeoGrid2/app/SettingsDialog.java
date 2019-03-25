package GeoGrid2.app;

import javax.swing.*;
import java.awt.event.*;


/**
 * Not implemented, ignore me!!
 * Was going to be used for a settings initialization dialog
 */

public class SettingsDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JButton openMapButton;
    private JTextField longTextField;
    private JTextField latTextField;

    public SettingsDialog(DataTransfer dt) {


        setContentPane(contentPane);
        setModalityType(ModalityType.APPLICATION_MODAL);
        getRootPane().setDefaultButton(buttonOK);
        pack();
        setVisible(true);

        openMapButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                SelectionMap selectionMap = new SelectionMap(dt);

                longTextField.setText(String.format("%.2f", dt.getLon()));
                latTextField.setText(String.format("%.2f", dt.getLat()));
            }
        });


        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        // add your code here
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

}
