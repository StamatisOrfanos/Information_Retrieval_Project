package Parsers;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;

import Indexing.InvertedIndex;
import Indexing.Posting;
import Queries.Search;
import Queries.RankedSearch;


public class QueryParser {
    public static String DELIMITER = " ,.:;'<>()@*%${}#\"\t\n";
    public static String[] ANDOPERATORS = {"&", "&&", "AND"};
    public static String[] OROPERATORS = {"|", "||", "OR"};
    public static String[] NOTOPERATORS = {"-", "--", "~", "NOT"};
    public static String[] STOPWORDS = Parser.STOPWORDS;


    /**
     * This function will take raw query data and choose between a Boolean query and a Free-text Query
     * @param query: String
     * @param invertedIndex: InvertedIndex
     * @return: ArrayList of Postings that are ranked according to the term frequency of each doc
     */
    public static ArrayList<Posting> chooseQueryType(String query, InvertedIndex invertedIndex){
        boolean operatorExists = false;
        String operator = "";
        ArrayList<String> tokens = queryTokenizer(query);
        ArrayList<Posting> result = new ArrayList<>();

        for (String token : tokens) {
            if (token.equals("AND") || token.equals("OR") || token.equals("NOT")) {
                operator=token;
                operatorExists = true;
                break;
            }
        }


        if (operatorExists && tokens.size() > 3) {
            result = BooleanQueryParser.booleanQuery(query, invertedIndex);
        }
        else if (operatorExists && !operator.equals("")) {
            if (operator.equals("AND")) {
                result = Search.AND_Boolean(invertedIndex.get(tokens.get(0)),invertedIndex.get(tokens.get(2)), invertedIndex);
            }
            else if (operator.equals("OR")) {
                result = Search.OR_Boolean(invertedIndex.get(tokens.get(0)),invertedIndex.get(tokens.get(2)), invertedIndex);
            }
            else {
                result = Search.NOT_A(invertedIndex.get(tokens.get(0)), invertedIndex);
            }
        }
        else {
            result = Search.A_AND_B(tokens, invertedIndex);
        }

        // Rank the results we get from the search queries
        ArrayList<Posting> finalResult = RankedSearch.rankResults(result);
        return finalResult;
    }



    /**
     * This function will take the query and transform it into a way that the query parsers can 
     * understand, by removing stopwords and transforming all types of Boolean operators to AND
     * OR and NOT
     * @param query : String
     * @return: ArrayList of String
     */
    public static ArrayList<String> queryTokenizer(String query) {
        ArrayList<String> tokens = new ArrayList<String>();
        StringTokenizer strTokenizer = new StringTokenizer(query, DELIMITER);
        String sToken = "";

        while (strTokenizer.hasMoreTokens()){
            sToken = strTokenizer.nextToken();

            if (Arrays.stream(ANDOPERATORS).anyMatch(sToken::equals)) {
                sToken = "AND";
                tokens.add(sToken);
            }
            else if (Arrays.stream(OROPERATORS).anyMatch(sToken::equals)) {
                sToken = "OR";
                tokens.add(sToken);
            }
            else if (Arrays.stream(NOTOPERATORS).anyMatch(sToken::equals)) {
                sToken = "NOT";
                tokens.add(sToken);
            }
            else {
                sToken = sToken.toLowerCase();
                if (Arrays.stream(STOPWORDS).anyMatch(sToken::equals)) { continue; }
                tokens.add(sToken);
            }
        }
        return tokens;
    }

}