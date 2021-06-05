package splitter;

import encryption.Encrypter;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Gestisce la crittazione e decrittazione dei file divisi.
 * Estende FileSplitter.
 * @author Gabriele Mattioli
 * @version  1.0
 */
public class FileSplitterEncrypt extends FileSplitter implements Encryptable {
    //attributi
    private Encrypter encrypter;

    //costruttori
    /**
     * Costruttore, inizializza il FileSplitterEncrypt per la divisione.
     * @param file Oggetto File.
     * @param sizeSplit Dimensione in byte della divisione (ciascun file diviso).
     */
    public FileSplitterEncrypt(File file, long sizeSplit) {
        super(file, sizeSplit);
        initSplitterEncrypter();
    }

    /**
     * Costruttore, inizializza il FileSplitterEncrypt per la divisione.
     * @param file Oggetto File.
     * @param parts Numero di file che si vogliono generare dalla divisione.
     */
    public FileSplitterEncrypt(File file, int parts) {
        super(file, parts);
        initSplitterEncrypter();
    }

    /**
     * Costruttore, inizializza il FileSplitterEncrypt per l'unione.
     * @param file Oggetto File.
     */
    public FileSplitterEncrypt(File file) {
        super(file);
        initSplitterEncrypter();
    }

    //metodi

    /** Inizializza gli attributi generici di FileSplitterEncrypter */
    private void initSplitterEncrypter() {
        splitTypeFormat = "crypt.par";
        encrypter = new Encrypter();
    }

    /**
     * Prima di scrivere il contenuto del buffer su un file, effettua la crittazione/decrittazione dei byte
     * presenti nel buffer.
     */
    @Override
    protected void writeOnFile (byte[] buffer, FilterOutputStream os, int length) throws IOException {
        byte[] processed = encrypter.update(buffer);
        super.writeOnFile(processed, os, processed.length);
    }

    /** Al termine della copia del contenuto di un file su un altro, aggiunge/rimuove il padding finale per la crittazione. */
    @Override
    protected void readWriteFiles(FilterInputStream is, FilterOutputStream os, long length) throws IOException,
            BadPaddingException, IllegalBlockSizeException {
        super.readWriteFiles(is, os, length);
        byte[] finalPadding = encrypter.doFinal();
        os.write(finalPadding, 0, finalPadding.length);
    }

    /** Copia tutti i dati di un file su un altro usando il metodo ridefinito in questa classe. */
    @Override
    protected void readWriteFiles(FilterInputStream is, FilterOutputStream os) throws IOException,
            BadPaddingException, IllegalBlockSizeException {
        this.readWriteFiles(is, os, is.available());
    }

    /**
     * Scrive la chiave di crittazione su un file.
     * @see Encryptable
     */
    @Override
    public void writeKey(FilterOutputStream os) throws InvalidKeyException, NoSuchAlgorithmException, IOException {
        encrypter.turnOnEncrypter();
        byte[] key = encrypter.getKey();
        os.write(key, 0, key.length);
    }

    /**
     * Legge la chiave di crittazione da un file.
     * @see Encryptable
     */
    @Override
    public void readKey(FilterInputStream is) throws IOException,
            InvalidAlgorithmParameterException, InvalidKeyException {
        byte[] key = new byte[16];
        int read = is.read(key);
        encrypter.turnOnDecrypter(key, read);
    }
}
