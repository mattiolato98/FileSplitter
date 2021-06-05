package graphics;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Classe astratta per la gestione di un pannello di file da processare.
 * @author Gabriele Mattioli
 * @version 1.0
 */
public abstract class FilesProcessPanel extends JPanel implements ActionListener {
    //attributi
    static final protected String HOMEPAGE_ACTION = "homepage";
    protected final MainFrame mainFrame;
    protected final JPanel filesPanel;
    protected final CoolProgressBar progressBar;
    protected final GridBagConstraints gbc;
    protected FilesProcessButtonsPanel buttons;

    //costruttori

    /**
     * Costruttore, inizializza gli attributi generici del FilesProcessPanel.
     * @param mainFrame Oggetto MainFrame.
     * @param title Titolo del pannello.
     */
    protected FilesProcessPanel(MainFrame mainFrame, String title) {
        super();
        setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();

        gbc.weightx = 1;
        gbc.gridy = 0;
        gbc.gridx = 0;

        gbc.anchor = GridBagConstraints.NORTHWEST;
        CoolButton homeButton = new CoolButton("Home");
        homeButton.setActionCommand(HOMEPAGE_ACTION);
        homeButton.addActionListener(this);
        add(homeButton, gbc);

        gbc.anchor = GridBagConstraints.NORTH;
        add(new JLabel("<html><h1><strong>" + title + "</strong></h1></html>"), gbc);

        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        filesPanel = new JPanel();
        filesPanel.setLayout(new BoxLayout(filesPanel, BoxLayout.Y_AXIS));
        filesPanel.setBorder(new EmptyBorder(60,0,10,0));
        add(filesPanel, gbc);

        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.SOUTH;
        progressBar = new CoolProgressBar(JProgressBar.HORIZONTAL, 0, 100);
        progressBar.setVisible(false);
        add(progressBar, gbc);

        this.mainFrame = mainFrame;
    }

    // metodi abstract
    /**
     * Dovrebbe inserire un file passato in un oggetto che lo può processare.
     * @param file File da aggiungere all'oggetto processatore.
     */
    public abstract void push(File file);

    /** Dovrebbe chiamare un metodo dell'oggetto processatore che processa i file che contiene. */
    public abstract void process();

    // metodi

    /** Torna al pannello home. */
    @Override
    public void actionPerformed(ActionEvent e) {
        mainFrame.newPanel(new HomepagePanel(mainFrame), "FileSplitter - Homepage");
    }

    /** Setta una serie di componenti per l'inizio del processo. */
    public void startProcessComponents() {
        remove(buttons);
        progressBar.setIndeterminate(true);
        progressBar.setVisible(true);
        revalidate();
        repaint();
    }

    /** Setta una serie di componenti nel caso in cui il processo termini con successo. */
    public void successEndProcessComponents() {
        progressBar.setValue(progressBar.getMaximum());
        progressBar.setIndeterminate(false);
        progressBar.setBorderPainted(false);
        filesPanel.removeAll();
        filesPanel.setLayout(new GridBagLayout());
        JLabel splitCompleted = createSuccessEndProcessLabel();
        splitCompleted.setFont(new Font("Arial", Font.BOLD, 20));
        filesPanel.add(splitCompleted);
        revalidate();
        repaint();
    }

    /** Setta una serie di componenti nel caso in cui il processo termini con un errore. */
    public void failureEndProcessComponents() {
        remove(progressBar);
        filesPanel.removeAll();
        filesPanel.setLayout(new GridBagLayout());
        JLabel splitFailure = createFailureEndProcessLabel();
        splitFailure.setFont(new Font("Arial", Font.BOLD, 20));
        splitFailure.setForeground(new Color(173, 26, 21));
        filesPanel.add(splitFailure);
        revalidate();
        repaint();
    }

    /** Crea la label nel caso in cui il processo termini con successo. */
    protected JLabel createSuccessEndProcessLabel() {
        return new JLabel("Operazione completata con successo!");
    }

    /** Crea la label nel caso in cui il processo termini con un errore. */
    protected JLabel createFailureEndProcessLabel() {
        return new JLabel("Operazione fallita! Ti invitiamo a riprovare.");
    }
}
