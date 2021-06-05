package graphics;

import listeners.FilesProcessButtonsListener;

import javax.swing.*;
import java.awt.*;

/**
 * Pannello che contiene i pulsanti del FilesProcessPanel.
 * @author Gabriele Mattioli.
 * @version 1.0
 * @see FilesProcessPanel
 */
public class FilesProcessButtonsPanel extends JPanel {
    //attributi
    static final protected String NEW_FILE_ACTION = "new_file";
    static final protected String RUN_ACTION = "process";
    protected final JButton newFileButton;
    protected final JButton runButton;

    //costruttori

    /**
     * Costruttore, inizializza il FilesProcessButtonsPanel.
     * @param filesProcessPanel Pannello FilesProcessPanel.
     * @param newFileText Testo del pulsante nuovo file.
     * @param processFilesText Testo del pulsante avvia processo.
     */
    public FilesProcessButtonsPanel(FilesProcessPanel filesProcessPanel,
                                    String newFileText, String processFilesText) {
        newFileButton = new CoolButton(newFileText);
        runButton = new CoolButton(processFilesText, new Color(50, 168, 82), new Color(255,255,255));

        newFileButton.setActionCommand(NEW_FILE_ACTION);
        runButton.setActionCommand(RUN_ACTION);

        addListeners(filesProcessPanel);

        add(newFileButton);
        add(Box.createVerticalStrut(50));
    }

    //metodi

    /**
     * Aggiunge il listener ai pulsanti.
     * @param filesProcessPanel Pannello FilesProcessPanel.
     */
    protected void addListeners(FilesProcessPanel filesProcessPanel) {
        FilesProcessButtonsListener listener = new FilesProcessButtonsListener(this, filesProcessPanel);
        newFileButton.addActionListener(listener);
        runButton.addActionListener(listener);
    }

    /** Aggiunge il pulsante nuovo file al pannello. */
    public void addNewFileButton() {
        add(newFileButton);
    }

    /** Rimuove il pulsante nuovo file dal pannello. */
    public void removeNewFileButton() {
        remove(newFileButton);
    }

    /** Aggiunge il pulsante avvia processo al pannello. */
    public void addRunButton() {
        add(runButton);
    }

    /** Rimuove il pulsante avvia processo dal pannello. */
    public void removeRunButton() {
        remove(runButton);
    }

    //getter

    /** @return actionCommand del pulsante nuovo file. */
    public String getNewFileAction() {
        return NEW_FILE_ACTION;
    }

    /** @return actionCommand del pulsante avvia processo. */
    public String getRunAction() {
        return RUN_ACTION;
    }
}
