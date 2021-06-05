package splitter;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Gestisce la compressione e decompressione dei file divisi.
 * Estende FileSplitter.
 * @author Gabriele Mattioli
 * @version 1.0
 */
public class FileSplitterCompress extends FileSplitter {
    //costruttori

    /**
     * Costruttore, inizializza il FileSplitterCompress per la divisione.
     * @param file Oggetto File.
     * @param sizeSplit Dimensione in byte della divisione (ciascun file diviso).
     */
    public FileSplitterCompress(File file, long sizeSplit) {
        super(file, sizeSplit);
        splitTypeFormat = "par.zip";
    }

    /**
     * Costruttore, inizializza il FileSplitterCompress per la divisione.
     * @param file Oggetto File.
     * @param parts Numero di file che si vogliono generare dalla divisione.
     */
    public FileSplitterCompress(File file, int parts) {
        super(file, parts);
        splitTypeFormat = "par.zip";
    }

    /**
     * Costruttore, inizializza il FileSplitterCompress per l'unione.
     * @param file Oggetto File.
     */
    public FileSplitterCompress(File file) {
        super(file);
        splitTypeFormat = "par.zip";
    }

    //metodi

    /**
     * Setta il nome dei file divisi.
     * @param copia Numero della copia di cui si vuole generare il nome.
     * @return Nome del file diviso.
     */
    @Override
    protected String setFileCopiaName(int copia) {
        if (fileFormat != null)
            return String.format("%s/%s(%s)_%d.%s", fileDir, fileName, fileFormat, copia,
                    splitTypeFormat);
        else
            return String.format("%s/%s(none)_%d.%s", fileDir, fileName, copia,
                    splitTypeFormat);
    }

    /**
     * Setta il nome del file che conterrà l'unione dei file.
     * @return Nome del file che conterrà l'unione dei file.
     */
    @Override
    protected String setFileUnitoName() {
        String[] splittedName = fileName.split("\\(");
        String format = splittedName[splittedName.length - 1].replaceAll("\\)", "");

        if (!format.equals("none"))
            return String.format("%s/%sMerge.%s", fileDir,
                    fileName.replaceAll("\\(" + format + "\\)", ""), format);
        else
            return String.format("%s/%sMerge", fileDir,
                    fileName.replaceAll("\\(" + format + "\\)", ""));
    }

    /**
     * Apre un file, che sarà uno dei file divisi, in scrittura come file compresso.
     * Imposta la entry sul file passato come parametro.
     * @param outFile Oggetto File che si vuole generare all'interno dello ZIP.
     * @return File aperto in scrittura all'interno di un archivio ZIP.
     */
    @Override
    protected FilterOutputStream openOutputFileSplitter(File outFile) throws IOException {
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(outFile));
        if (fileFormat != null)
            zos.putNextEntry(new ZipEntry(outFile.getName().replaceAll(splitTypeFormat, fileFormat)
                    .replaceAll("\\(" + fileFormat + "\\)", "")));
        else
            zos.putNextEntry(new ZipEntry(outFile.getName().replaceAll("\\." + splitTypeFormat, "")
                    .replaceAll("\\(none\\)", "")));

        return zos;
    }

    /**
     * Apre un file, che è uno dei file divisi, in lettura come file compresso.
     * Imposta la entry sul primo file presente nell'archivio.
     * @param inFile Archivio ZIP che contiene un file diviso.
     * @return File aperto in lettura all'interno di un archivio ZIP.
     */
    @Override
    protected FilterInputStream openInputFileMerger(File inFile) throws IOException {
        ZipInputStream zis = new ZipInputStream(new FileInputStream(inFile));
        zis.getNextEntry();

        return zis;
    }
}
