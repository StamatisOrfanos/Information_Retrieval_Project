package Indexing;
import java.io.Serializable;


public class Posting implements Serializable {
    private int nDocId;
    private long frequency;

    public Posting(int nDocId, long frequency){
        this.nDocId = nDocId;
        this.frequency = frequency;
    }

    public int getnDocId() {
        return nDocId;
    }

    public long getFrequency() {
        return frequency;
    }


    @Override
    public String toString() {
        return "[docId:"+ nDocId + ", frequency: " + frequency + "] ";
    }
}
