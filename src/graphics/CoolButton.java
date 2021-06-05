package graphics;

import javax.swing.*;
import java.awt.*;

/**
 * Pulsante personalizzato.
 * @author Gabriele Mattioli
 * @version 1.0
 */
public class CoolButton extends JButton {
    //costruttori

    /**
     * Costruttore, inizializza il pulsante.
     * @param text Testo da scrivere sul pulsante.
     */
    public CoolButton(String text) {
        this(text, new Color(40,40,40), new Color(226,226,226));
    }

    /**
     * Costruttore, inizializza il pulsante.
     * @param text Testo da scrivere sul pulsante.
     * @param bg Background del pulsante.
     */
    public CoolButton(String text, Color bg) {
        this(text, bg, new Color(126,126,126));
    }

    /**
     * Costruttore, inizializza il pulsante.
     * @param text Testo da scrivere sul pulsante.
     * @param bg Background del pulsante.
     * @param fg Colore del testo del pulsante.
     */
    public CoolButton(String text, Color bg, Color fg) {
        super(text);
        setBackground(bg);
        setForeground(fg);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                setBackground(new Color(bg.getRed()+20, bg.getGreen()+20, bg.getBlue()+20));
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                setBackground(bg);
            }
        });
        setFont(new Font("Arial", Font.BOLD, 20));
        setBorderPainted(false);
        setFocusPainted(false);
    }
}
