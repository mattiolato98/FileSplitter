package splitter;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.*;
import java.nio.file.Path;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Gestisce:
 *  - Divisione di un file in più parti
 *  - Unione di una serie di file divisi in un unico file
 *  - Metodi generici di gestione di file
 * @author Gabriele Mattioli
 * @version  1.0
 */
public class FileSplitter {
    //attributi
    protected static final int MAX_READABLE_BUFFER_SIZE = 8 * 1024;
    protected File file;
    protected Path filePath;
    protected String fileName;
    protected String fileDir;
    protected String fileFormat;
    protected String splitTypeFormat;
    protected long sizeSplit;

    //costruttori

    /**
     * Costruttore, inizializza il FileSplitter per la divsione.
     * @param file Oggetto File.
     * @param sizeSplit Dimensione in byte della divisione (ciascun file diviso).
     */
    public FileSplitter(File file, long sizeSplit) {
        /*
         *  TODO:
         *   1. Gestire eventuali attributi null
         */
        initSplitter(file);
        if (sizeSplit != 0)
            this.sizeSplit = Math.min(sizeSplit, file.length());
        else
            this.sizeSplit = file.length();
    }

    /**
     * Costruttore, inizializza il FileSplitter per la divsione.
     * @param file Oggetto File.
     * @param parts Numero di file che si vogliono generare dalla divisione.
     */
    public FileSplitter(File file, int parts) {
        initSplitter(file);
        setSizeSplit(parts);
    }

    /**
     * Costruttore, inizializza il FileSplitter per l'unione.
     * @param file Oggetto File.
     */
    public FileSplitter(File file) {
        this(file, file.length());
    }

    //metodi

    /**
     * Inizializza gli attributi generici di FileSplitter.
     * @param file Oggetto File.
     */
    private void initSplitter(File file) {
        this.file = file;
        this.filePath = file.toPath();
        this.fileName = filePath.getFileName().toString().split("\\.")[0];
        this.fileDir = getDirectoryPathname(filePath);
        this.splitTypeFormat = "par";
        if (filePath.getFileName().toString().contains(".")) {
            fileFormat = filePath.getFileName().toString().split("\\.", 2)[1];
        } else {
            fileFormat = null;
        }
    }

    /**
     * Ritorna il percorso assoluto di una directory contenente il file passato (senza il nome del file).
     * @param pathName Percorso assoluto del file (compreso il nome del file).
     * @return Percorso assoluto senza il nome del file.
     */
    protected String getDirectoryPathname(Path pathName) {
        String dir = pathName.toString();
        dir = dir.substring(0, dir.lastIndexOf(File.separator));

        return dir;
    }

    /**
     * Toglie dal nome del file il numero indicante la copia.
     * @return Indice del primo file da dividere.
     */
    protected int cleanFileName() {
        int firstIndex;
        try {
            String[] fileNameSplitted = fileName.split("_");
            String fileNameSuffix = fileNameSplitted[fileNameSplitted.length - 1];
            firstIndex = Integer.parseInt(fileNameSuffix);
            int index = fileName.lastIndexOf("_" + fileNameSuffix);
            fileName = fileName.substring(0, index) + fileName.substring(index).replace("_" + fileNameSuffix, "");
        }
        catch (NumberFormatException e) {
            firstIndex = 0;
        }
        return firstIndex;
    }

    /**
     * Crea un nuovo file e lo apre in scrittura come BufferedOutputStream.
     * @param outFile Oggetto File che si vuole creare ed aprire in scrittura.
     * @return File aperto in scrittura.
     */
    protected BufferedOutputStream openOutputFile(File outFile) throws FileNotFoundException {
        return new BufferedOutputStream(new FileOutputStream(outFile));
    }

    /**
     * Apre il file in lettura come BufferedInputStream.
     * @param inFile Oggetto File che si vuole aprire in lettura.
     * @return File aperto in lettura.
     */
    protected BufferedInputStream openInputFile(File inFile) throws FileNotFoundException {
        return new BufferedInputStream(new FileInputStream(inFile));
    }

    /**
     * Apre un file, che sarà uno dei file divisi, in scrittura.
     * @param outFile Oggetto File che si vuole aprire in scrittura.
     * @return File aperto in scrittura.
     */
    protected FilterOutputStream openOutputFileSplitter(File outFile) throws IOException {
        return openOutputFile(outFile);
    }

    /**
     * Apre un file, che è uno dei file divisi, in lettura.
     * @param inFile Oggetto File che si vuole aprire in lettura.
     * @return File aperto in lettura.
     */
    protected FilterInputStream openInputFileMerger(File inFile) throws IOException {
        return openInputFile(inFile);
    }

    /**
     * Scrive il contenuto di un buffer su un file, per la lunghezza specificata.
     * @param buffer Buffer che contiene i dati da scrivere.
     * @param os OutputStream su cui scrivere.
     * @param length Numero di byte da scrivere.
     */
    protected void writeOnFile(byte[] buffer, FilterOutputStream os, int length) throws IOException {
        os.write(buffer, 0, length);
    }

    /**
     * Copia i dati di un file in un altro file, per la lunghezza specificata.
     * Se si tratta di un ZipStream, chiude la entry a fine esecuzione.
     * @param is InputStream da cui si vuole leggere.
     * @param os OutputStream su cui si vuole scrivere.
     * @param length Numero di byte da copiare da un file all'altro.
     */
    protected void readWriteFiles(FilterInputStream is, FilterOutputStream os, long length) throws IOException,
            BadPaddingException, IllegalBlockSizeException {
        while (length > MAX_READABLE_BUFFER_SIZE) {
            byte[] buffer = new byte[MAX_READABLE_BUFFER_SIZE];
            int read = is.read(buffer);
            if (read != -1)
                writeOnFile(buffer, os, read);
            length -= read;
        }
        byte[] buffer = new byte[(int) length];
        int read = is.read(buffer);
        if (read != -1)
            writeOnFile(buffer, os, read);

        if (is instanceof ZipInputStream)
            ((ZipInputStream) is).closeEntry();
        if (os instanceof ZipOutputStream)
            ((ZipOutputStream) os).closeEntry();
    }

    /**
     * Copia tutti i dati di un file in un altro file.
     * Se si tratta di un ZipStream, chiude la entry a fine esecuzione.
     * @param is InputStream da cui si vuole leggere.
     * @param os OutputStream su cui si vuole scrivere.
     */
    protected void readWriteFiles(FilterInputStream is, FilterOutputStream os) throws IOException,
            BadPaddingException, IllegalBlockSizeException {
        int read;
        byte[] buffer = new byte[MAX_READABLE_BUFFER_SIZE];
        while ((read = is.read(buffer)) > 0) {
            writeOnFile(buffer, os, read);
        }

        if (is instanceof ZipInputStream)
            ((ZipInputStream) is).closeEntry();
        if (os instanceof ZipOutputStream)
            ((ZipOutputStream) os).closeEntry();
    }

    /**
     * Setta il nome dei file divisi.
     * @param copia Numero della copia di cui si vuole generare il nome.
     * @return Nome del file diviso.
     */
    protected String setFileCopiaName(int copia) {
        if (fileFormat != null)
            return String.format("%s/%s_%d.%s.%s", fileDir, fileName, copia,
                    splitTypeFormat, fileFormat);
        else
            return String.format("%s/%s_%d.%s", fileDir, fileName, copia,
                    splitTypeFormat);
    }

    /** Divide un file in più file a seconda del numero di byte specificato per ciascuno. */
    public void split() throws IOException, NoSuchAlgorithmException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException, NullPointerException {
        /* TODO:
         *   1. Verificare presenza nomi file divisi prima di iniziare a crearli
         */
        FilterInputStream is = openInputFile(file);
        long i = 0;

        File fileCopia = new File(setFileCopiaName(0));

        FilterOutputStream os = openOutputFileSplitter(fileCopia);

        if (this instanceof Encryptable)
            ((Encryptable) this).writeKey(os);

        for (int numCopia = 1; i < file.length(); numCopia++) {
            /*
             * Se l'ultimo indice che verrebbe letto eccede la lunghezza del file
             * si limita la lughezza da leggere all'ultimo indice disponibile.
             */
            long cutLength;
            long lastIndex = i + sizeSplit;
            if (lastIndex > file.length())
                cutLength = file.length() - i;
            else
                cutLength = sizeSplit;

            readWriteFiles(is, os, cutLength);
            os.close();

            i += cutLength;
            fileCopia = new File(setFileCopiaName(numCopia));
            os = (i < file.length()) ? openOutputFileSplitter(fileCopia) : null;
        }
        is.close();
    }

    /**
     * Setta il nome del file che conterrà l'unione dei file.
     * @return Nome del file che conterrà l'unione dei file.
     */
    protected String setFileUnitoName() {
        return String.format("%s/%sMerge%s", fileDir, fileName,
                fileFormat.replaceAll(splitTypeFormat, ""));
    }

    /**
     * Setta il nome del file che conterrà l'unione dei file, nel caso esistesse già un file con il nome di default.
     * @param copy Numero della copia.
     */
    protected String setFileUnitoName(int copy) {
        return String.format("%s/%sMerge%d%s", fileDir, fileName, copy,
                fileFormat.replaceAll(splitTypeFormat, ""));
    }

    /** Unisce i file divisi in un unico file. */
    public void merge() throws IOException, InvalidAlgorithmParameterException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException {
        int firstIndex = cleanFileName(); // deve essere sempre la prima istruzione

        File fileUnito = new File(setFileUnitoName());

        for (int i = 1; fileUnito.exists(); i++)
            fileUnito = new File(setFileUnitoName(i));

        BufferedOutputStream os = openOutputFile(fileUnito);

        File fileDaCopiare = new File(String.format("%s/%s_%d.%s", fileDir, fileName, firstIndex, fileFormat));
        FilterInputStream is = openInputFileMerger(fileDaCopiare);

        if (this instanceof Encryptable)
            ((Encryptable) this).readKey(is);

        for (int i = firstIndex + 1; fileDaCopiare.exists();  i++) {
            readWriteFiles(is, os);
            is.close();

            boolean deleted = fileDaCopiare.delete();
            if (!deleted)
                System.err.println("File criptato non eliminato correttamente");

            fileDaCopiare = new File(String.format("%s/%s_%d.%s", fileDir, fileName, i, fileFormat));
            is = (fileDaCopiare.exists()) ? openInputFileMerger(fileDaCopiare) : null;
        }
        os.close();
    }

    //getter e setter

    /** @return File dell'oggetto. */
    public File getFile() {
        return file;
    }

    /** @return Dimensione in byte della divisione. */
    public long getSizeSplit() {
        return sizeSplit;
    }

    /**
     * Setter della dimensione della divsione in byte.
     * @param sizeSplit Dimensione della divisione in byte.
     */
    public void setSizeSplit(long sizeSplit) {
        if (sizeSplit != 0)
            this.sizeSplit = Math.min(sizeSplit, file.length());
        else
            this.sizeSplit = file.length();
    }

    /**
     * Setter della dimensione della divisione in byte.
     * @param parts Numero di parti della divisione.
     */
    public void setSizeSplit(int parts) {
        /*
         * TODO:
         *  1. Sicuro basti fare sizeSplit += 1? Verificare
         */
        if (parts != 0) {
            sizeSplit = file.length() / parts;
            long remainder = file.length() % parts;
            if (remainder != 0)
                sizeSplit += 1;
        } else {
            sizeSplit = file.length();
        }
    }
}
