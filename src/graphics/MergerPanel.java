package graphics;

import splitter.MergerManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Pannello che gestisce l'unione di file divisi.
 * Estende FilesProcessPanel.
 * @author Gabriele Mattioli
 * @version 1.0
 */
public class MergerPanel extends FilesProcessPanel implements ActionListener {
    //attributi
    private MergerManager mergerManager;

    //costruttori

    /**
     * Costruttore, inizializza il MergerPanel.
     * @param mainFrame Oggetto MainFrame.
     */
    public MergerPanel(MainFrame mainFrame) {
        super(mainFrame, "Merger");
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.PAGE_END;
        buttons = new FilesProcessButtonsPanel(this,
                "Scegli file da unire", "Avvia l'unione");
        add(buttons, gbc);
    }

    //metodi abstract implementati

    /**
     * Crea un oggetto MergerManager con il file passato.
     * @param file File da aggiungere al MergerManager.
     */
    public void push(File file) {
        mergerManager = new MergerManager(file);
        this.addFileRow(file);
    }

    /** Esegue l'unione del file memorizzato, gestendo gli elementi da mostrare nella GUI. */
    public void process() {
        new Thread(() -> {
            startProcessComponents();
            try {
                mergerManager.run();
                System.out.println("Unione completata");
                successEndProcessComponents();
            } catch (Exception e) {
                failureEndProcessComponents();
            }
            mergerManager = null;
        }).start();
    }

    // metodi

    /**
     * Gestisce gli eventi dei pulsanti Homepage, Eliminazione file.
     * Prima di tornare al pannello Home chiede conferma all'utente, nel caso ci sia un file memorizzato.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals(HOMEPAGE_ACTION)) {
            if (mergerManager != null) {
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
        else {
            this.removeFileRow();
        }
    }

    /** Personalizza il messaggio di unione terminata con successo. */
    @Override
    protected JLabel createSuccessEndProcessLabel() {
        return new JLabel("Unione completata con successo!");
    }

    /** Personalizza il messaggio di unione terminata con un errore. */
    @Override
    protected JLabel createFailureEndProcessLabel() {
        return new JLabel("Unione fallita! Ti invitiamo a riprovare.");
    }

    /**
     * Aggiunge il file passato come parametro al pannello del file.
     * @param file File da aggiungere al pannello.
     */
    private void addFileRow(File file) {
        JPanel fileRow = new JPanel();

        fileRow.setLayout(new GridLayout(1, 1, 10, 4));
        fileRow.setPreferredSize(new Dimension(800, 35));

        JLabel fileName = new JLabel("<html><h3>" + file.getName() + "</h3></html>");
        CoolButton deleteButton = new CoolButton(
                "Elimina", new Color(163, 34, 24), new Color(255, 255, 255));

        fileRow.add(fileName);
        fileRow.add(deleteButton);

        deleteButton.addActionListener(this);

        buttons.removeNewFileButton();
        buttons.addRunButton();
        filesPanel.add(fileRow);
        revalidate();
        repaint();
    }

    /** Rimuove la riga del file dal pannello dei file. */
    private void removeFileRow() {
        mergerManager = null;
        buttons.removeRunButton();
        buttons.addNewFileButton();
        filesPanel.removeAll();
        revalidate();
        repaint();
    }
}
