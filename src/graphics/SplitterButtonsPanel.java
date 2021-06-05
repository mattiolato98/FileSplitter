package graphics;

import listeners.SplitterButtonsListener;

/**
 * Pannello che contiene i pulsanti dello SplitterPanel.
 * Estende FilesProcessButtonsPanel.
 * @author Gabriele Mattioli
 * @version 1.0
 * @see SplitterPanel
 */
public class SplitterButtonsPanel extends FilesProcessButtonsPanel {
    //costruttori

    /**
     * Costruttore, inizializza lo SplitterButtonsPanel.
     * @param filesProcessPanel Pannello FilesProcessPanel.
     */
    public SplitterButtonsPanel(FilesProcessPanel filesProcessPanel) {
        super(filesProcessPanel, "Aggiungi un file alla coda", "Avvia la divisione");
    }

    //metodi

    /**
     * Aggiunge il listener ai pulsanti.
     * @param filesProcessPanel Pannello FilesProcessPanel.
     */
    @Override
    protected void addListeners(FilesProcessPanel filesProcessPanel) {
        SplitterButtonsListener listener = new SplitterButtonsListener(this, filesProcessPanel);
        newFileButton.addActionListener(listener);
        runButton.addActionListener(listener);
    }
}
