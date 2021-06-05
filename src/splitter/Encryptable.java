package splitter;

import java.io.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Interfaccia per le classi che effettuano crittazione di dati tramite chiave.
 * @author mattiolato
 * @version 1.0
 */
public interface Encryptable {
    /**
     * Scrive la chiave di crittazione su un file.
     * @param os OutputStream su cui scrivere.
     */
    void writeKey(FilterOutputStream os) throws InvalidKeyException, NoSuchAlgorithmException, IOException;

    /**
     * Legge la chiave di crittazione da un file.
     * @param is InputStream da cui leggere.
     */
    void readKey(FilterInputStream is) throws IOException, InvalidAlgorithmParameterException, InvalidKeyException;
}
