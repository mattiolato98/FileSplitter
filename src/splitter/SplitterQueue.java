package splitter;

import graphics.SplitterPanel;
import javax.swing.*;
import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * Gestisce la divisione di una serie di file.
 * Estende SwingWorker.
 * @author Gabriele Mattioli
 * @version 1.0
 */
public class SplitterQueue<E extends FileSplitter> extends SwingWorker<Boolean, Integer> {
    //attributi
    private final SplitterPanel panel;
    private LinkedList<E> splitters;
    private ArrayList<Thread> threads;

    //costruttori

    /**
     * Costruttore, inizializza la SplitterQueue.
     * @param panel Pannello che contiene le informazioni della coda, su cui applicare le modifiche GUI.
     */
    public SplitterQueue(SplitterPanel panel) {
        splitters = new LinkedList<E>();
        threads = new ArrayList<>();
        this.panel = panel;
    }

    //metodi

    /**
     * Aggiunge un elemento alla coda.
     * @param splitter Elemento da aggiungere.
     * @return Stato dell'aggiunta. True se l'oggetto non è già presente in coda, False altrimenti.
     */
    public boolean push(E splitter) {
        for (E e : splitters) {
            if (e.getFile().equals(splitter.getFile()))
                return false;
        }
        splitters.push(splitter);
        return true;
    }

    /**
     * Sostituisce un elemento della coda con un altro elemento.
     * @param oldSplitter Vecchio oggetto da sostituire.
     * @param newSplitter Nuovo oggetto da aggiungere al posto del vecchio.
     */
    public void update(E oldSplitter, E newSplitter) {
        int index = splitters.indexOf(oldSplitter);
        splitters.set(index, newSplitter);
    }

    /**
     * Rimuove l'elemento passato come parametro.
     * @param splitter Elemento da rimuovere.
     */
    public void remove(E splitter) {
        splitters.remove(splitter);
    }

    /**
     * Esegue la divisione per ciascun file presente in coda, svuotando la coda.
     * Assegna la divisione di ciascun file a un thread concorrente.
     * Attende la terminazione di tutti i thread.
     * @return Esisto della divisione. True se completata con successo, False altrimenti.
     */
    @Override
    protected Boolean doInBackground() {
        panel.startProcessComponents();
        E splitting = splitters.poll();
        while (splitting != null) {
            ThreadSplitter thread = new ThreadSplitter(splitting);
            thread.start();
            threads.add(thread);
            splitting = splitters.poll();
        }

        int i = 1;
        try {
            for (Thread t : threads) {
                t.join();
                publish(i);
                i++;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * Aggiorna la GUI secondo l'andamento della divisione.
     * @param chunks File divisi fino a questo momento.
     * */
    @Override
    protected void process(List<Integer> chunks) {
        int mostRecentValue = chunks.get(chunks.size()-1);
        System.out.println("Chunk: " + mostRecentValue);
        panel.updateProcessComponents(mostRecentValue);
    }

    /** Aggiorna la GUI al termine del processo di divisione e a seconda dell'esito della stessa. */
    @Override
    protected void done() {
        boolean status;
        try {
            status = get();
            if (status)
                panel.successEndProcessComponents();
            else
                panel.failureEndProcessComponents();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            panel.failureEndProcessComponents();
        }
    }

    //getter e setter

    /**
     * Aggiorna la dimensione in byte dei file divisi di un elemento in coda.
     * @param splitter Elemento in coda da modificare.
     * @param sizeSplit Dimensione della divisione.
     */
    public void setSplitterSizeSplit(E splitter, long sizeSplit) {
        int index = splitters.indexOf(splitter);
        splitters.get(index).setSizeSplit(sizeSplit);
    }

    /**
     * Aggiorna la dimensione in byte dei file divisi di un elemento in coda.
     * @param splitter Elemento in coda da modificare.
     * @param partSplit Numero di file che si vogliono generare dalla divisione.
     */
    public void setSplitterSizeSplit(E splitter, int partSplit) {
        int index = splitters.indexOf(splitter);
        splitters.get(index).setSizeSplit(partSplit);
    }

    /**
     * @param splitter Elemento della coda di cui si vuole conoscere la sizeSplit.
     * @return Dimensione in byte della divisione dell'elemento passato come parametro.
     */
    public long getSplitterSizeSplit(E splitter) {
        int index = splitters.indexOf(splitter);
        return splitters.get(index).getSizeSplit();
    }

    /** @return Coda di oggetti FileSplitter. */
    public LinkedList<E> getSplitters() {
        return splitters;
    }
}
