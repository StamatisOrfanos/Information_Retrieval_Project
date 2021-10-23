package Queries;
import java.util.ArrayList;
import java.util.Collection;

import Indexing.InvertedIndex;
import Indexing.Posting;


public class Search {


    public static ArrayList<Posting> A_AND_B(ArrayList<String> queryTokens, InvertedIndex invertedIndex){
        ArrayList<Posting> result = new ArrayList<>();

        if (queryTokens.size() < 2) {
            System.out.println("In order to make a query we need at least two arguments");
        }

        ArrayList<Posting> firtsPosting = invertedIndex.get(queryTokens.get(0));
        ArrayList<Posting> secondPosting = invertedIndex.get(queryTokens.get(1));
        result = AND_Boolean(firtsPosting, secondPosting , invertedIndex);

        for (int i=2; i < queryTokens.size(); i++) {
            ArrayList<Posting> morePostings = invertedIndex.get(queryTokens.get(i));
            result = AND_Boolean(result, morePostings, invertedIndex);
        }

        return result;
    }


    public static ArrayList<Posting> AND_Boolean(ArrayList<Posting> aPost, ArrayList<Posting> bPost, InvertedIndex invertedIndex) {
        Posting[] aArray = aPost.toArray(new Posting[0]);
        Posting[] bArray = bPost.toArray(new Posting[0]);

        int sizeA = aArray.length;
        int sizeB = bArray.length;
        ArrayList<Posting> result = new ArrayList<>();
        int aIndex = 0, bIndex = 0;

        while(aIndex < sizeA && bIndex < sizeB) {
            if (aArray[aIndex].getnDocId() < bArray[bIndex].getnDocId()) {
                aIndex++;
            }
            else if (aArray[aIndex].getnDocId() > bArray[bIndex].getnDocId()) {
                bIndex++;
            }
            else {
                result.add(aArray[aIndex]);
                aIndex++;
                bIndex++;
            }
        }
        return result;
    }



    public static ArrayList<Posting> A_OR_B(ArrayList<String> queryTokens, InvertedIndex invertedIndex) {
        ArrayList<Posting> result = new ArrayList<>();

        if (queryTokens.size() < 2) {
            System.out.println("In order to make a query we need at least two arguements");
        }

        ArrayList<Posting> firtsPosting = invertedIndex.get(queryTokens.get(0));
        ArrayList<Posting> secondPosting = invertedIndex.get(queryTokens.get(1));
        result = OR_Boolean(firtsPosting, secondPosting , invertedIndex);

        for (int i=2; i < queryTokens.size(); i++) {
            ArrayList<Posting> morePostings = invertedIndex.get(queryTokens.get(i));
            result = OR_Boolean(result, morePostings, invertedIndex);
        }

        return result;
    }


    public static ArrayList<Posting> OR_Boolean(ArrayList<Posting> aPost, ArrayList<Posting> bPost, InvertedIndex invertedIndex) {
        Posting[] aArray = aPost.toArray(new Posting[0]);
        Posting[] bArray = bPost.toArray(new Posting[0]);

        int sizeA = aArray.length;
        int sizeB = bArray.length;
        ArrayList<Posting> result = new ArrayList<>();
        int aIndex = 0, bIndex = 0;


        while (aIndex < sizeA && bIndex < sizeB) {
            if (aArray[aIndex].getnDocId() < bArray[bIndex].getnDocId()) {
                result.add(aArray[aIndex]);
                aIndex++;
            }
            else if (aArray[aIndex].getnDocId() > bArray[bIndex].getnDocId()) {
               result.add(bArray[bIndex]);
                bIndex++;
            }
            else {
                aIndex++;
                bIndex++;
            }
        }

        return result;
    }



    public static ArrayList<Posting> NOT_A(ArrayList<Posting> aPost, InvertedIndex invertedIndex) {
        Collection<ArrayList<Posting>> allPostings = invertedIndex.get();
        ArrayList<ArrayList<Posting>> newList = new ArrayList<ArrayList<Posting>>(allPostings);

        for (int i=0; i< newList.size(); i++) {
            if (newList.get(i) == aPost) {
                newList.remove(i);
            }
        }

        ArrayList<Posting> result = new ArrayList<Posting>();
        // System.out.println(newList.get(0)); 
        for (int i = 0; i < newList.size(); ++i) {
            result.addAll(newList.get(i));
        }

        return result;
    }




    public static ArrayList<Posting> A_NOT_B(ArrayList<String> queryTokens, InvertedIndex invertedIndex) {
        ArrayList<Posting> result = new ArrayList<>();

        if (queryTokens.size() < 2) {
            System.out.println("In order to make a query we need at least two arguements");
        }

        ArrayList<Posting> firtsPosting = invertedIndex.get(queryTokens.get(0));
        ArrayList<Posting> secondPosting = invertedIndex.get(queryTokens.get(1));
        result = AND_NOT_Boolean(firtsPosting, secondPosting , invertedIndex);

        for (int i=2; i < queryTokens.size(); i++) {
            ArrayList<Posting> morePostings = invertedIndex.get(queryTokens.get(i));
            result = AND_NOT_Boolean(result, morePostings, invertedIndex);
        }

        return result;
    }


    public static ArrayList<Posting> AND_NOT_Boolean(ArrayList<Posting> aPost, ArrayList<Posting> bPost, InvertedIndex invertedIndex) {
        Posting[] aArray = aPost.toArray(new Posting[0]);
        Posting[] bArray = bPost.toArray(new Posting[0]);

        int sizeA = aArray.length;
        int sizeB = bArray.length;
        ArrayList<Posting> result = new ArrayList<>();
        int aIndex = 0, bIndex = 0;


        while (aIndex < sizeA && bIndex < sizeB) {
            if (aArray[aIndex].getnDocId() < bArray[bIndex].getnDocId()) {
                result.add(aArray[aIndex]);
                aIndex++;
            }
            else if (aArray[aIndex].getnDocId() > bArray[bIndex].getnDocId()) {
                bIndex++;
            }
            else {
                aIndex++;
                bIndex++;
            }
        }

        return result;
    }
}