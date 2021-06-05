package listeners;

import graphics.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Gestisce i listener del pannello dei pulsanti del FilesProcessPanel.
 * @author Gabriele Mattioli
 * @version 1.0
 * @see FilesProcessButtonsPanel
 */
public class FilesProcessButtonsListener implements ActionListener {
    //attributi
    protected final FilesProcessButtonsPanel filesProcessButtonsPanel;
    protected final FilesProcessPanel filesProcessPanel;

    //costruttori

    /**
     * Costruttore, inizializza il FilesProcessButtonsListner.
     * @param filesProcessButtonsPanel Pannello dei pulsanti a cui fa riferimento.
     * @param filesProcessPanel Pannello di processazione a cui fa riferimento.
     */
    public FilesProcessButtonsListener(FilesProcessButtonsPanel filesProcessButtonsPanel,
                                       FilesProcessPanel filesProcessPanel) {
        this.filesProcessButtonsPanel = filesProcessButtonsPanel;
        this.filesProcessPanel = filesProcessPanel;
    }

    //metodi

    /**
     * Gestisce le azioni sui pulsanti:
     *  - Nuovo file
     *  - Avvia processo
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();
        if (actionCommand.equals(filesProcessButtonsPanel.getNewFileAction())) {
            retrieveAndPushFile();
        }
        if (actionCommand.equals(filesProcessButtonsPanel.getRunAction())) {
            filesProcessPanel.process();
        }
    }

    /** Recupera il file selezionato e lo aggiunge al pannello dei file. */
    protected void retrieveAndPushFile() {
        File selectedFile = getFile();
        if (selectedFile != null) {
            filesProcessPanel.push(selectedFile);
        }
    }

    /**
     * Crea il pannello per la scelta del file.
     * @return Pannello per la scelta del file.
     */
    protected JFileChooser createFileChooser() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("/home/mattiolato/Documents/Università"));
        return fileChooser;
    }

    /**
     * Gestisce le azioni sul pannello per la scelta dei file.
     * In caso di esito positivo, recupera il file selezionato e lo ritorna.
     * @return File selezionato, null altrimenti.
     */
    protected File getFile() {
        JFileChooser fc = createFileChooser();
        int result = fc.showDialog(filesProcessPanel, "Aggiungi");
        if (result == JFileChooser.APPROVE_OPTION) {
            return fc.getSelectedFile();
        }
        return null;
    }
}
