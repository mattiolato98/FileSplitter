package graphics;

import listeners.HomepageListener;

import javax.swing.*;
import java.awt.*;

/**
 * Pannello che contiene i pulsanti della Homepage.
 * @author Gabriele Mattioli
 * @version 1.0
 * @see HomepagePanel
 */
public class HomepageButtonsPanel extends JPanel {
    //attributi
    static final private String SPLIT_ACTION = "split";
    static final private String MERGE_ACTION = "merge";
    static final private String SPLIT_TEXT = "Strumento di divisione";
    static final private String MERGE_TEXT = "Strumento di unione";

    //costruttori

    /**
     * Costruttore, inizializza l'HomepageButtonsPanel.
     * @param gbc Costante GridBag utilizzato nella Homepage per posizionare correttamente gli elementi.
     * @param mainFrame Oggetto MainFrame.
     */
    public HomepageButtonsPanel(GridBagConstraints gbc, MainFrame mainFrame) {
        setLayout(new GridBagLayout());
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        addButton(gbc, SPLIT_TEXT, SPLIT_ACTION, mainFrame);
        add(Box.createVerticalStrut(50));
        addButton(gbc, MERGE_TEXT, MERGE_ACTION, mainFrame);
    }

    //metodi

    /**
     * Aggiunge un pulsante al pannello.
     * @param gbc Costante GridBag, utilizzata per posizionare correttamente gli elementi nel pannello.
     * @param text Testo da scrivere sul pulsante.
     * @param actionCommand Stringa del comando del pulsante.
     * @param mainFrame Oggetto MainFrame.
     */
    private void addButton(GridBagConstraints gbc, String text, String actionCommand, MainFrame mainFrame) {
        JButton button = new CoolButton(text);
        button.setActionCommand(actionCommand);
        button.addActionListener(new HomepageListener(mainFrame));
        add(button, gbc);
    }

    //getter

    /** @return actionCommand del pulsante split. */
    public static String getSplitAction() {
        return SPLIT_ACTION;
    }

    /** @return actionCommand del pulsante merge. */
    public static String getMergeAction() {
        return MERGE_ACTION;
    }
}
