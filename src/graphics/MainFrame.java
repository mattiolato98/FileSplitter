package graphics;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;

/**
 * Frame principale della GUI.
 * @author Gabriele Mattioli.
 * @version 1.0
 */
public class MainFrame extends JFrame {
    //attributi
    private static final String ICON = "/home/mattiolato/Documents/Università/OOP/FileSplitter/icon.png";

    //costruttori

    /** Costruttore di default, inizializza il MainFrame. */
    public MainFrame() {
        this("");
    }

    /**
     * Costruttore, inizializza il MainFrame.
     * @param titolo Titolo della finestra della GUI.
     */
    public MainFrame(String titolo) {
        super(titolo);
        setBounds(500,300,1000,500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        HomepagePanel homepagePanel = new HomepagePanel(this);
        try {
            setIconImage(ImageIO.read(new File(ICON)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        add(homepagePanel);
    }

    //metodi

    /***
     * Aggiunge un nuovo pannello al MainFrame, rimuovendo tutti quelli presenti.
     * @param newPanel Pannello da aggiungere al MainFrame.
     * @param newTitle Titolo della finestra della GUI.
     */
    public void newPanel(JPanel newPanel, String newTitle) {
        getContentPane().removeAll();
        JScrollPane scrollPane = new JScrollPane(newPanel);
        getContentPane().add(scrollPane);
        setTitle(newTitle);
        revalidate();
        repaint();
    }
}
