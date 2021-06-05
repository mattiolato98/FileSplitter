package splitter;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.File;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.util.Objects;

/**
 * Gestisce l'unione di una serie di file divisi.
 * @author Gabriele Mattioli
 * @version 1.0
 */
public class MergerManager {
    //attributi
    private final FileSplitter merger;

    //costruttori

    /**
     * Costruttore, inizializzaa il MergerManager.
     * @param file Oggetto File.
     */
    public MergerManager(File file) {
        if (Objects.equals(getSplittedType(file), "crypt")) {
            merger = new FileSplitterEncrypt(file);
        } else if (Objects.equals(getSplittedType(file), "zip")) {
            merger = new FileSplitterCompress(file);
        } else {
            merger = new FileSplitter(file);
        }
    }

    //metodi

    /**
     * Ritorna il tipo di divisione applicata sul file passato come paremtro.
     * @return Tipo di divisione applicata al file.
     */
    public static String getSplittedType(File file) {
        String fileSuffix = "";
        if (file.toPath().getFileName().toString().contains("."))
            fileSuffix = file.toPath().getFileName().toString().split("\\.", 2)[1];

        if (fileSuffix.contains("crypt"))
            return "crypt";
        else if (fileSuffix.contains("zip"))
            return "zip";
        else
            return "default";
    }

    /** Avvia l'unione dei file. */
    public void run() throws IllegalBlockSizeException, BadPaddingException,
            InvalidAlgorithmParameterException, InvalidKeyException, IOException {
        merger.merge();
    }
}
