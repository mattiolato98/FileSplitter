package graphics;

import javax.swing.*;
import java.awt.*;

/**
 * ProgressBar personalizzata.
 * @author Gabriele Mattioli
 * @version 1.0
 */
public class CoolProgressBar extends JProgressBar {
    //costruttori

    /**
     * Costruttore, inizializza la ProgressBar.
     * @param orient Orientamento della ProgressBar.
     * @param min Valore minimo.
     * @param max Valore massimo.
     */
    public CoolProgressBar(int orient, int min, int max) {
        super(orient, min, max);
        setSize(800,20);
        setPreferredSize(new Dimension(800,20));
        setBorderPainted(true);
        setForeground(new Color(9, 125, 143));
    }
}
