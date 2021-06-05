package graphics;

import splitter.FileSplitter;
import splitter.SplitterQueue;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

import static java.lang.Thread.sleep;

/**
 * Pannello che gestisce la coda di file da dividere.
 * Estende FilesProcessPanel.
 * @author Gabriele Mattioli.
 * @version 1.0
 */
public class SplitterPanel extends FilesProcessPanel {
    //attributi
    private final SplitterQueue<FileSplitter> splitterQueue;

    //costruttori
    /**
     * Costruttore, inizializza lo SplitterPanel.
     * @param mainFrame Oggetto MainFrame.
     */
    public SplitterPanel(MainFrame mainFrame) {
        super(mainFrame, "Splitter");
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.PAGE_END;
        buttons = new SplitterButtonsPanel(this);
        add(buttons, gbc);

        this.splitterQueue = new SplitterQueue<FileSplitter>(this);
    }

    //metodi abstract implementati

    /**
     * Aggiunge il file passato in coda di divisione.
     * @param file File da aggiungere in coda.
     */
    @Override
    public void push(File file) {
        FileSplitter splitter = new FileSplitter(file,2);
        boolean pushed = splitterQueue.push(splitter);
        if (pushed) {
            this.addFileRow(file, splitter);
        }
    }

    /** Esegue la divisione dei file in coda. */
    @Override
    public void process() {
        splitterQueue.execute();
    }

    //metodi

    /** Prima di tornare al pannello Home chiede conferma all'utente, nel caso ci siano dei file presenti in coda. */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (splitterQueue.getSplitters().size() != 0) {
            String title = "Conferma uscita";
            String message = "Sicuro di voler abbandonare la pagina? Le modifiche verranno perse.";
            int action = JOptionPane.showConfirmDialog(this, message, title,
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (action == JOptionPane.OK_OPTION)
                super.actionPerformed(e);
        } else {
            super.actionPerformed(e);
        }
    }

    /** Personalizza il messaggio di divisione terminata con successo. */
    @Override
    protected JLabel createSuccessEndProcessLabel() {
        return new JLabel("Divisione completata con successo!");
    }

    /** Personalizza il messaggio di divisione terminata con un errore. */
    @Override
    protected JLabel createFailureEndProcessLabel() {
        return new JLabel("Divisione fallita! Ti invitiamo a riprovare.");
    }

    /** Personalizza il comportamento della progressBar a inizio divisione. */
    @Override
    public void startProcessComponents() {
        remove(buttons);
        progressBar.setMaximum((splitterQueue.getSplitters().size()) * 100);
        progressBar.setValue(0);
        progressBar.setVisible(true);
        revalidate();
        repaint();
    }

    /**
     * Aggiorna i componenti del pannello mentre il processo è in corso.
     * Incrementa il valore della progressBar.
     * @param value Valore attuale della progressBar.
     */
    public void updateProcessComponents(int value) {
        new Thread(() -> {
            int incrementValue = (value * 100) - progressBar.getValue();
            while(incrementValue > 0) {
                progressBar.setValue(progressBar.getValue() + 1);
                revalidate();
                repaint();
                try {
                    sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                incrementValue--;
            }
        }).start();
    }

    /**
     * Aggiunge una riga al pannello dei file.
     * @param file File di cui aggiungere la riga.
     * @param splitter Oggetto FileSplitter di cui aggiungere la riga
     */
    private void addFileRow(File file, FileSplitter splitter) {
        Component verticalSpacing = Box.createVerticalStrut(10);
        SplitterFileRow splitterFileRow = new SplitterFileRow(this, file, splitter, verticalSpacing);
        filesPanel.add(splitterFileRow);
        filesPanel.add(verticalSpacing);
        if (filesPanel.getComponentCount() == 2)
            buttons.addRunButton();
        revalidate();
        repaint();
    }

    /**
     * Rimuove una riga dal pannello dei file.
     * @param splitterFileRow Oggetto che fa riferimento alla riga da rimuovere.
     * @param splitter Oggetto FileSplitter a cui è riferita la riga da rimuovere.
     */
    public void removeFileRow(SplitterFileRow splitterFileRow, FileSplitter splitter) {
        splitterQueue.remove(splitter);
        filesPanel.remove(splitterFileRow);
        filesPanel.remove(splitterFileRow.getVerticalSpacing());
        if (filesPanel.getComponentCount() == 0)
            buttons.removeRunButton();
        revalidate();
        repaint();
    }

    /**
     * Aggiorna un elemento della coda di divisione.
     * @param oldSplitter Vecchio oggetto FileSplitter da aggiornare.
     * @param newSplitter Nuovo oggetto FileSplitter da aggiungere al posto del vecchio.
     */
    public void updateQueue(FileSplitter oldSplitter, FileSplitter newSplitter) {
        splitterQueue.update(oldSplitter, newSplitter);
    }

    /**
     * Aggiorna la sizeSplit di un elemento della coda di divisione.
     * @param splitter Oggetto FileSplitter da aggiornare.
     * @param bytes Nuova dimensione in byte della divisione.
     */
    public void updateQueueItemSizeSplit(FileSplitter splitter, long bytes) {
        splitterQueue.setSplitterSizeSplit(splitter, bytes);
    }

    /**
     * Aggiorna il numero di parti di un elemento della coda di divisione.
     * @param splitter Oggetto FileSplitter da aggiornare.
     * @param parts Nuovo numero di parti della divisione.
     */
    public void updateQueueItemSizeSplit(FileSplitter splitter, int parts) {
        splitterQueue.setSplitterSizeSplit(splitter, parts);
    }

    /**
     * @param splitter Oggetto FileSplitter.
     * @return Dimensione in byte della divisione dell'oggetto passato come parametro.
     */
    public long getQueueItemSizeSplit(FileSplitter splitter) {
        return splitterQueue.getSplitterSizeSplit(splitter);
    }
}
