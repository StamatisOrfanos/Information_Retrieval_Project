package Queries;
import java.util.ArrayList;

import Indexing.Posting;

public class RankedSearch {

    public static void main(String[] args) {
           // This file is not really used as the ranked search, but more so as the 
          // experiment to check if we can achieve some kind of sorting without the 
         // use of the Comperable operator for the Posting object as I had face many different 
        // bugs and errors (I am a bad programmer)
        ArrayList<Posting> ar = new ArrayList<>();
        ar.add(new Posting(1, 9));
        ar.add(new Posting(1, 2));
        ar.add(new Posting(1, 4));
        ar.add(new Posting(1, 6));
        ar.add(new Posting(1, 7));
        ar.add(new Posting(1, 10));

        ArrayList<Posting> result = rankResults(ar);
        for (int i=0; i<result.size(); i++) {
            System.out.println(result.get(i));
        }
    }


    public static ArrayList<Posting> rankResults(ArrayList<Posting> initialResult) {
        ArrayList<Posting> finalResult = new ArrayList<>();

        while (!initialResult.isEmpty()) {
            long max = initialResult.get(0).getFrequency();
            int index = 0;
    
            for (int i=0; i<initialResult.size(); i++) {
                if (max < initialResult.get(i).getFrequency()) {
                    max = initialResult.get(i).getFrequency();
                    index = i;
                }
            }
            finalResult.add(initialResult.get(index));
            initialResult.remove(index);
        }
        return finalResult;
    }

}
