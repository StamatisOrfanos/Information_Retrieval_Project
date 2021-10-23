package Parsers;

import Indexing.InvertedIndex;
import Indexing.Posting;
import Queries.Search;

import java.util.ArrayList;

public class BooleanQueryParser {

// small AND boy AND cry

    private BooleanQueryParser() {}

    public static ArrayList<Posting> booleanQuery(String query, InvertedIndex invertedIndex) {
        ArrayList<Posting> finalPostings = new ArrayList<>();

        String[] tokens = query.split(" ");

        for (int i=0; i<tokens.length; i++) {
            String token = tokens[i];

            if (token.equals("AND")) {
                finalPostings = parseANDQuery(finalPostings, invertedIndex, tokens, i);
            }
            else if (token.equals("OR")) {
                finalPostings = parseORQuery(finalPostings, invertedIndex, tokens, i);
            }
            else if (token.equals("NOT")) {
                finalPostings = parseNOTQuery(finalPostings, invertedIndex, tokens, i);
            }
        }
        return finalPostings;
    }


    private static ArrayList<Posting> parseANDQuery(ArrayList<Posting> finalPostings, InvertedIndex invertedIndex, String[] tokens, int index){
        ArrayList<String> postOperatorString = new ArrayList<>();
        ArrayList<Posting> andList = new ArrayList<Posting>();


        if (finalPostings.size() == 0) {
            ArrayList<String> initialTokens = new ArrayList<String>();
            for (int i=0; i<index; i++) {
                initialTokens.add(tokens[i]);
            }
            finalPostings = setTokens(initialTokens, invertedIndex);
        }


        for (int i=index + 1; i<tokens.length; i++) {
            String token = tokens[i];
            if (token.equals("AND") || token.equals("OR") || token.equals("NOT")) {
                if (token.equals("NOT") && index == i-1) {
                    andList = parseNOTQuery(finalPostings, invertedIndex, tokens, i);
                }
                break;
            }
            else { postOperatorString.add(token); }
        }

        // Check if after the operator we have one or multiple data (avoid throws null exception)
        andList = setTokens(postOperatorString, invertedIndex);

        return Search.AND_Boolean(finalPostings, andList, invertedIndex);
    }



    private static ArrayList<Posting> parseORQuery(ArrayList<Posting> finalPostings, InvertedIndex invertedIndex, String[] tokens, int index){
        ArrayList<String> postOperatorString = new ArrayList<>();

        if (finalPostings.size() == 0) {
            ArrayList<String> initialTokens = new ArrayList<String>();
            for (int i=0; i<index; i++) {
                initialTokens.add(tokens[i]);
            }
            finalPostings = setTokens(initialTokens, invertedIndex);
        }

        ArrayList<Posting> orList = new ArrayList<Posting>();
        for (int i=index + 1; i<tokens.length; i++) {
            String token = tokens[i];

            if (token.equals("AND") || token.equals("OR") || token.equals("NOT")) {

                if (token.equals("NOT") && index == i-1) {
                    orList = parseNOTQuery(finalPostings, invertedIndex, tokens, i);
                }
                break;
            }
            else { postOperatorString.add(token); }
        }

        // Check if after the operator we have one or multiple data (avoid throws null exception)
        orList = setTokens(postOperatorString, invertedIndex);

        return Search.OR_Boolean(finalPostings, orList, invertedIndex);
    }



    /**
     * 
     * @param finalPostings
     * @param invertedIndex
     * @param tokens
     * @param index
     * @return
     */
    private static ArrayList<Posting> parseNOTQuery(ArrayList<Posting> finalPostings, InvertedIndex invertedIndex, String[] tokens, int index){
        ArrayList<Posting> notList = new ArrayList<>();

        for (int i=index+1; i<tokens.length; i++) {
            String token = tokens[i];

            if (token.equals("AND") || token.equals("OR") || token.equals("NOT")) {
                break;
            }
            else {
                finalPostings = Search.AND_Boolean(finalPostings, notList, invertedIndex);
            }
        }

        return Search.NOT_A(notList, invertedIndex);
    }



    private static ArrayList<Posting> setTokens(ArrayList<String> tokens, InvertedIndex invertedIndex) {
        ArrayList<Posting> result = new ArrayList<>();

        if (tokens.size() <=1 ) {
            result = invertedIndex.get(tokens.get(0));
        }
        else {
            result = Search.A_AND_B(tokens, invertedIndex);
        }
        return result;
    }

}
