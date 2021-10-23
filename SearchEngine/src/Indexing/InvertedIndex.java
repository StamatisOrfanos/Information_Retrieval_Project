package Indexing;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class InvertedIndex implements Serializable {
    private HashMap<String, ArrayList<Posting>> hashMap;


    public InvertedIndex() {
        hashMap = new HashMap<>();
    }


    public void insert(String term, int nDocID, long frequency) {
        if(hashMap.containsKey(term)) {
            ArrayList<Posting> postings = hashMap.get(term);
            postings.add(new Posting(nDocID, frequency));
        }
        else {
            ArrayList<Posting> postings = new ArrayList<>();
            Posting posting = new Posting(nDocID, frequency);
            postings.add(posting);
            hashMap.put(term, postings);
        }
    }


    public ArrayList<Posting> get(String term) {
        return hashMap.get(term);
    }

    public Collection<ArrayList<Posting>> get() {
        return hashMap.values();
    }


    public void print() {
        Set<String> keys = hashMap.keySet();
        Iterator<String> it = keys.iterator();

        while(it.hasNext()) {
            String term = it.next();
            ArrayList<Posting> postings = hashMap.get(term);

            System.out.println(term + " (" + postings.size() + "):");

            for(Posting posting : postings) {
                System.out.println("\t" + posting.toString());
            }
        }
    }


    public void write(String sFile) throws IOException{
        FileOutputStream fileOutput = new FileOutputStream(sFile, true);
        ObjectOutputStream objectOuput = new ObjectOutputStream(fileOutput);

        objectOuput.writeObject(hashMap);

        objectOuput.close();
        fileOutput.close();
    }


    public void load(String path) throws IOException, ClassNotFoundException {
        FileInputStream inputFile = new FileInputStream(path);
        ObjectInputStream objectInput = new ObjectInputStream(inputFile);

        hashMap = (HashMap<String, ArrayList<Posting>>) objectInput.readObject();

        objectInput.close();
        inputFile.close();
    }

}