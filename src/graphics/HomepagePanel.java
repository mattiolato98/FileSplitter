package graphics;

import javax.swing.*;
import java.awt.*;

/**
 * Pannello Homepage.
 * @author Gabriele Mattioli
 * @version 1.0
 */
public class HomepagePanel extends JPanel {
    //attributi
    private HomepageButtonsPanel buttons;

    //costruttori

    /**
     * Costruttore, inizializza l'HomepagePanel.
     * @param mainFrame Oggetto MainFrame.
     */
    public HomepagePanel(MainFrame mainFrame) {
        super();
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.NORTH;
        add(new JLabel("<html><h1><strong>Benvenuto nel File Splitter</strong></h1><hr></html>"), gbc);
        add(new JLabel("<html><h2>Scegli un'opzione per continuare</h2></html>"), gbc);
        buttons = new HomepageButtonsPanel(gbc, mainFrame);
        gbc.weighty = 1;
        add(buttons, gbc);
    }
}
