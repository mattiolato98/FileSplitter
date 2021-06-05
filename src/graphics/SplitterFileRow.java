package graphics;

import listeners.SplitterFileRowListener;
import splitter.FileSplitter;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Vector;

/**
 * Pannello che contiene le informazioni e i pulsanti di una riga di un file.
 * @author Gabriele Mattioli
 * @version 1.0
 * @see SplitterPanel
 */
public class SplitterFileRow extends JPanel {
    //attributi
    static private final String DEFAULT_SPLIT = "Default";
    static private final String CRYPTED_SPLIT = "Criptato";
    static private final String COMPRESSED_SPLIT = "Compresso";
    static private final String PARTS_SPLIT = "Numero di parti";
    static private final String BYTES_SPLIT = "Numero di byte";
    private final SplitterPanel splitterPanel;
    private final File file;
    private JComboBox<String> selectSplitType;
    private JComboBox<String> selectSplitSize;
    private Object selectSplitTypePreviousValue;
    private JSpinner bytesField;
    private JSpinner partsField;
    private FileSplitter splitter;
    private final Component verticalSpacing;

    //costruttori

    /**
     * Costruttore, inizializza la SplitterFileRow.
     * @param splitterPanel Pannello SplitterPanel.
     * @param file File di cui aggiungere la riga.
     * @param splitter Oggetto Splitter a cui fa riferimento la riga.
     * @param verticalSpacing Spaziatura verticale sotto la riga, utile al fine di poterla rimuovere.
     */
    public SplitterFileRow(SplitterPanel splitterPanel, File file, FileSplitter splitter, Component verticalSpacing) {
        super();
        setLayout(new GridLayout(0,5,4,4));
        setPreferredSize(new Dimension(800,35));
        this.splitterPanel = splitterPanel;
        this.file = file;
        this.splitter = splitter;
        this.verticalSpacing = verticalSpacing;
        addRow();
    }

    //metodi

    /** Aggiunge le informazioni e i pulsanti alla riga. */
    private void addRow() {
        /*
         * TODO:
         *  1. Gestire nomi file troppo lunghi
         *  2. Rendere il pannello scrollable
         */
        JLabel fileName = new JLabel("<html><h3>" + this.file.getName() + "</h3></html>");

        Long val = 1000000000L;
        Long min = 0L;
        Long max = Long.MAX_VALUE;
        Long step = 1L;
        SpinnerNumberModel bytesModel = new SpinnerNumberModel(val, min, max, step);
        bytesField = new JSpinner(bytesModel);

        SpinnerNumberModel partsModel = new SpinnerNumberModel(2, 0, Integer.MAX_VALUE, 1);
        partsField = new JSpinner(partsModel);

        selectSplitType = createSplitTypesComboBox();
        selectSplitSize = createSplitSizeComboBox();

        CoolButton deleteButton = new CoolButton(
                "Elimina", new Color(163,34,24), new Color(255,255,255));

        SplitterFileRowListener listener = new SplitterFileRowListener(this, splitterPanel);

        selectSplitType.addItemListener(listener);
        selectSplitSize.addItemListener(listener);
        partsField.addChangeListener(listener);
        bytesField.addChangeListener(listener);
        deleteButton.addActionListener(listener);

        add(fileName);
        add(selectSplitType);
        add(selectSplitSize);
        add(partsField);
        add(deleteButton);
    }

    /** Aggiunge l'input numerico per i byte (che supporta numeri long) e rimuove quello per il numero di parti. */
    public void fromPartsToBytes() {
        remove(partsField);
        add(bytesField, 3);
        revalidate();
        repaint();
    }

    /** Aggiunge l'input numerico per il numero di parti e rimuove quello per i byte (che supporta numeri long). */
    public void fromBytesToParts() {
        remove(bytesField);
        add(partsField, 3);
        revalidate();
        repaint();
    }

    /** @return Tendina dei tipi di divisione. */
    private JComboBox<String> createSplitTypesComboBox() {
        Vector<String> splitTypes = new Vector<String>();
        splitTypes.add(DEFAULT_SPLIT);
        splitTypes.add(CRYPTED_SPLIT);
        splitTypes.add(COMPRESSED_SPLIT);
        JComboBox<String> select = new JComboBox<String>(splitTypes);
        select.setBackground(new Color(255,255,255));

        return select;
    }

    /** @return Tendina dei tipi di dimensione della divisione. */
    private JComboBox<String> createSplitSizeComboBox() {
        Vector<String> splitMethods = new Vector<String>();
        splitMethods.add(PARTS_SPLIT);
        splitMethods.add(BYTES_SPLIT);
        JComboBox<String> select = new JComboBox<String>(splitMethods);
        select.setBackground(new Color(255,255,255));

        return select;
    }

    //getter e setter

    /**
     * Setter dello splitter della riga.
     * @param splitter Oggetto FileSplitter.
     */
    public void setSplitter(FileSplitter splitter) {
        this.splitter = splitter;
    }

    /**
     * Setter del valore più recente del tipo di divisione.
     * @param selectSplitTypePreviousValue Oggetto della tendina selectSplitType.
     */
    public void setSelectSplitTypePreviousValue(Object selectSplitTypePreviousValue) {
        this.selectSplitTypePreviousValue = selectSplitTypePreviousValue;
    }

    /** @return File della riga. */
    public File getFile() {
        return file;
    }

    /** @return Oggetto FileSplitter della riga. */
    public FileSplitter getSplitter() {
        return splitter;
    }

    /** @return Input numerico per i byte. */
    public JSpinner getBytesField() {
        return bytesField;
    }

    /** @return Input numerico per il numero di parti. */
    public JSpinner getPartsField() {
        return partsField;
    }

    /** @return Valore dell'input numerico per i byte. */
    public long getBytesFieldValue() {
        return (long) bytesField.getValue();
    }

    /** @return Valore dell'input numerico per il numero di parti. */
    public int getPartsFieldValue() {
        return (int) partsField.getValue();
    }

    /** @return Tendina dei tipi di divisione. */
    public JComboBox<String> getSelectSplitType() {
        return selectSplitType;
    }

    /** @return Tendina dei tipi di dimensione della divisione. */
    public JComboBox<String> getSelectSplitSize() {
        return selectSplitSize;
    }

    /** @return Valore più recente del tipo di divisione. */
    public Object getSelectSplitTypePreviousValue() {
        return selectSplitTypePreviousValue;
    }

    /** @return Spazio verticale sotto la riga. */
    public Component getVerticalSpacing() {
        return verticalSpacing;
    }

    /** @return Opzione divisione default. */
    public static String getDefaultSplit() {
        return DEFAULT_SPLIT;
    }

    /** @return Opzione divisione criptata. */
    public static String getCryptedSplit() {
        return CRYPTED_SPLIT;
    }

    /** @return Opzione divisione compressa. */
    public static String getCompressedSplit() {
        return COMPRESSED_SPLIT;
    }

    /** @return Opzione divisione per numero di byte. */
    public static String getBytesSplit() {
        return BYTES_SPLIT;
    }

    /** @return Opzione divisione per numero di parti. */
    public static String getPartsSplit() {
        return PARTS_SPLIT;
    }
}
