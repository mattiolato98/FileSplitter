package listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import graphics.HomepageButtonsPanel;
import graphics.MainFrame;
import graphics.MergerPanel;
import graphics.SplitterPanel;
import graphics.FilesProcessPanel;

/**
 * Gestisce i listener del pannello dei pulsanti della Homepage.
 * @author Gabriele Mattioli
 * @version 1.0
 * @see HomepageButtonsPanel
 */
public class HomepageListener implements ActionListener {
    //attributi
    private final MainFrame mainFrame;

    //costruttori

    /**
     * Costruttore, inizializza l'HomepageListener.
     * @param mainFrame Oggetto MainFrame.
     */
    public HomepageListener(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    //metodi

    /**
     * Gestisce le azioni sui pulsanti:
     *  - Strumento di divisione
     *  - Strumento di unione
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        FilesProcessPanel filesElaborationPanel;
        String actionCommand = e.getActionCommand();

        if (actionCommand.equals(HomepageButtonsPanel.getSplitAction())) {
            filesElaborationPanel = new SplitterPanel(mainFrame);
            mainFrame.newPanel(filesElaborationPanel, "FileSplitter - Strumento di divisione");
        }
        if (actionCommand.equals(HomepageButtonsPanel.getMergeAction())) {
            filesElaborationPanel = new MergerPanel(mainFrame);
            mainFrame.newPanel(filesElaborationPanel, "FileSplitter - Strumento di unione");
        }
    }
}
