package listeners;

import graphics.FilesProcessPanel;
import graphics.SplitterButtonsPanel;

import javax.swing.*;
import java.io.File;

/**
 * Gestisce i listener del pannello dei pulsanti dello SplitterPanel.
 * Estende FilesProcessButtonsListener.
 * @author Gabriele Mattioli
 * @version 1.0
 * @see SplitterButtonsPanel
 */
public class SplitterButtonsListener extends FilesProcessButtonsListener {
    //costruttori

    /**
     * Costruttore, inizializza lo SplitterButtonsListener.
     * @param splitterButtonsPanel Pannello dei pulsanti a cui fa riferimento.
     * @param filesProcessPanel Pannello di processazione a cui fa riferimento.
     */
    public SplitterButtonsListener(SplitterButtonsPanel splitterButtonsPanel,
                                   FilesProcessPanel filesProcessPanel) {
        super(splitterButtonsPanel, filesProcessPanel);
    }

    //metodi

    /** Recupera una moltitudine di file selezionati e li aggiunge al pannello dei file. */
    @Override
    protected void retrieveAndPushFile() {
        File[] selectedFiles = getFiles();
        if (selectedFiles != null) {
            for (File f: selectedFiles)
                filesProcessPanel.push(f);
        }
    }

    /**
     * Crea il pannello per la scelta dei file, settato per accettare una moltitudine di file.
     * @return Pannello per la scelta dei file.
     */
    @Override
    protected JFileChooser createFileChooser() {
        JFileChooser fileChooser = super.createFileChooser();
        fileChooser.setMultiSelectionEnabled(true);
        return fileChooser;
    }

    /**
     * Gestisce le azioni sul pannello per la scelta dei file.
     * In caso di esito positivo, recupera i file selezionati e li ritorna.
     * @return File selezionati, null altrimenti.
     */
    private File[] getFiles() {
        JFileChooser fc = createFileChooser();
        int result = fc.showDialog(filesProcessPanel, "Aggiungi alla coda");
        if (result == JFileChooser.APPROVE_OPTION) {
            return fc.getSelectedFiles();
        }
        return null;
    }
}
