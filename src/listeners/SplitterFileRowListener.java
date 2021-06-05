package listeners;

import graphics.SplitterFileRow;
import graphics.SplitterPanel;
import splitter.FileSplitterCompress;
import splitter.FileSplitter;
import splitter.FileSplitterEncrypt;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Gestisce i listener del pannello della SplitterFileRow.
 * @author Gabriele Mattioli
 * @version 1.0
 * @see SplitterFileRow
 */
public class SplitterFileRowListener implements ItemListener, ChangeListener, ActionListener {
    //attributi
    private final SplitterFileRow splitterFileRow;
    private final SplitterPanel splitterPanel;

    //costruttori

    /**
     * Costruttore, inizializza lo SplitterFileRowListener.
     * @param splitterFileRow Riga a cui fa riferimento.
     * @param splitterPanel Pannello di divisione a cui fa riferimento.
     */
    public SplitterFileRowListener(SplitterFileRow splitterFileRow, SplitterPanel splitterPanel) {
        this.splitterFileRow = splitterFileRow;
        this.splitterPanel = splitterPanel;
    }

    //metodi

    /**
     * Gestisce il cambio di elemento delle tendine:
     *  - Tipo di divisione
     *  - Tipo di dimensione della divisione
     */
    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.DESELECTED) {
            if (e.getSource().equals(splitterFileRow.getSelectSplitType())) {
                splitterFileRow.setSelectSplitTypePreviousValue(e.getItem());
            }
        }
        if (e.getStateChange() == ItemEvent.SELECTED) {
            if (e.getSource().equals(splitterFileRow.getSelectSplitType())) {
                setSplitType(e);
            }
            if (e.getSource().equals(splitterFileRow.getSelectSplitSize())) {
                setSplitSize(e);
            }
        }
    }

    /** Gestisce il cambio di stato degli input numerici. */
    @Override
    public void stateChanged(ChangeEvent e) {
        setSplitSize(e);
    }

    /** Gestisce le azioni sul pulsante elimina riga. */
    @Override
    public void actionPerformed(ActionEvent e) {
        splitterPanel.removeFileRow(splitterFileRow, splitterFileRow.getSplitter());
    }

    /**
     * Gestisce il cambio di tipo di divisione. Crea un nuovo oggetto FileSplitter a seconda dell'opzione selezionata
     * ed aggiorna la coda di divisione di conseguenza.
     * @param e Evento di cambio di opzione della tendina.
     */
    private void setSplitType(ItemEvent e) {
        FileSplitter fileSplitter = null;
        long sizeSplit = splitterPanel.getQueueItemSizeSplit(splitterFileRow.getSplitter());

        if (e.getItem().equals(SplitterFileRow.getDefaultSplit()))
            fileSplitter = new FileSplitter(splitterFileRow.getFile(), sizeSplit);

        if (e.getItem().equals(SplitterFileRow.getCryptedSplit()))
            fileSplitter = new FileSplitterEncrypt(splitterFileRow.getFile(), sizeSplit);

        if (e.getItem().equals(SplitterFileRow.getCompressedSplit()))
            fileSplitter = new FileSplitterCompress(splitterFileRow.getFile(), sizeSplit);

        if (fileSplitter != null) {
            splitterPanel.updateQueue(splitterFileRow.getSplitter(), fileSplitter);
            splitterFileRow.setSplitter(fileSplitter);
        } else {
            splitterFileRow.getSelectSplitType().setSelectedItem(splitterFileRow.getSelectSplitTypePreviousValue());
        }
    }

    /**
     * Gestisce il cambio del tipo di dimensione della divisione.
     * Cambia il tipo di input numerico sulla GUI.
     * Solleva un evento stateChanged sull'input numerico mostrato, in modo da aggiornare la coda.
     * @param e Evento di cambio di opzione della tendina.
     */
    private void setSplitSize(ItemEvent e) {
        if (e.getItem().equals(SplitterFileRow.getPartsSplit())) {
            splitterFileRow.fromBytesToParts();
            stateChanged(new ChangeEvent(splitterFileRow.getPartsField()));
        }
        if (e.getItem().equals(SplitterFileRow.getBytesSplit())) {
            splitterFileRow.fromPartsToBytes();
            stateChanged(new ChangeEvent(splitterFileRow.getBytesField()));
        }
    }

    /**
     * Gestisce l'aggiornamento dei valori degli input numerici:
     *  - byte della divisione
     *  - numero di parti della divisione
     * Aggiorna la coda di divisione sulla base di tali valori.
     * @param e Evento di aggiornamento dell'input numerico.
     */
    private void setSplitSize(ChangeEvent e) {
        if (e.getSource().equals(splitterFileRow.getPartsField())) {
            int partSplit = splitterFileRow.getPartsFieldValue();
            splitterPanel.updateQueueItemSizeSplit(splitterFileRow.getSplitter(), partSplit);
        }
        if (e.getSource().equals(splitterFileRow.getBytesField())) {
            long byteSplit = splitterFileRow.getBytesFieldValue();
            splitterPanel.updateQueueItemSizeSplit(splitterFileRow.getSplitter(), byteSplit);
        }
    }
}