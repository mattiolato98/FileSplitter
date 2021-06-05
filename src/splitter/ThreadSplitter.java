package splitter;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Gestisce la divisione di un file affidandola a un thread.
 * Estende Thread.
 * @author Gabriele Mattioli
 * @version 1.0
 */
public class ThreadSplitter extends Thread {
    //attributi
    FileSplitter splitter;

    //costruttori

    /**
     * Costruttore, inizializza il ThreadSplitter.
     * @param splitter Oggetto splitter.
     */
    public ThreadSplitter(FileSplitter splitter) {
        this.splitter = splitter;
    }

    //metodi

    /** Esegue la divisione del file in parallelo. */
    @Override
    public void run() {
        System.out.println("Inizio: " + Thread.currentThread().getName() + ", " + Thread.currentThread().getId());
        try {
            splitter.split();
        } catch (IOException | NoSuchAlgorithmException |
                InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        System.out.println("Fine: " + Thread.currentThread().getName() + ", " + Thread.currentThread().getId());
    }

}

