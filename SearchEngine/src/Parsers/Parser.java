package Parsers;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;

import Indexing.DocIDMapper;
import Indexing.InvertedIndex;


public class Parser {
    // Delimiter includes all the possible punctuation that a text can include
    public static String DELIMITER = " ,.!:;'<>()@*&%${}#\"\t\n";
    public static String[] STOPWORDS = {"a", "about", "above", "after", "again", "against", "all", "am",
                                        "an", "any", "are", "aren't", "as", "at", "be", "because",
                                        "been", "before", "being", "below", "between", "both", "but", "by",
                                        "can't", "cannot", "could", "couldn't", "did", "didn't", "do",
                                        "does", "doesn't", "doing", "don't", "down", "during", "each", 
                                        "few", "for", "from", "further", "had", "hadn't", "has", "hasn't", 
                                        "have", "haven't", "having", "he", "he'd", "he'll", "he's", "her",
                                        "here", "here's", "hers", "herself", "him", "himself", "his", "how",
                                        "how's", "i", "i'd", "i'll", "i'm", "i've", "if", "in", "into",
                                        "is", "isn't", "it", "it's", "its", "itself", "let's", "me",
                                        "more", "most", "mustn't", "my", "myself", "off", "on", "once",
                                        "only", "other",  "ought", "our", "ours","ourselves" ,"out" ,"over",
                                        "own","same","shan't","she","she'd", "she'll" ,"she's","should",
                                        "shouldn't" ,"so" ,"some" ,"such" ,"than", "that", "that's", "the",
                                        "their", "theirs", "them", "themselves", "then", "there",
                                        "there's", "these", "they", "they'd", "they'll", "they're",
                                        "they've", "this", "those", "through", "to", "too", "under", "until",
                                        "up", "very", "was", "wasn't", "we", "we'd", "we'll", "we're",
                                        "we've", "were", "weren't", "what", "what's", "when",
                                        "when's", "where", "where's", "which", "while", "who", "who's",
                                        "whom", "why", "why's", "with", "won't", "would", "wouldn't"
                                        ,"you" ,"you'd" ,"you'll" ,"you're" ,"you've" ,"your"
                                        ,"yours", "yourself","yourselves"};
    private static int nDocId = 0;


    // Function that parses the data directories
    public static void parseData(File dir, DocIDMapper mapper, InvertedIndex invertedIndex) throws IOException {
        Parser parser = new Parser();
        File[] files = dir.listFiles();

        for (File file : files) {

            if (file.isDirectory()) {
                parseData(file, mapper, invertedIndex);
            }
            else if (!file.getName().endsWith(".txt")) {
                continue;
            }
            else {
                nDocId++;
                ArrayList<String> document = parser.parseFile(file.getCanonicalPath());
                HashMap<String, Long> word_freq_mapper = new HashMap<String, Long>();

                // Itterate through the document and give a frequency to each term in that document
                for (int i=0; i<document.size(); i++) {
                    String term = document.get(i);

                    if (!word_freq_mapper.containsKey(term)) {
                        word_freq_mapper.put(term, 1L);
                    }
                    else {
                        long termFrequency = word_freq_mapper.get(term);
                        word_freq_mapper.put(term, ++termFrequency);
                    }
                }

                // Insert the terms of the document with each frequency
                Set<String> stringDoc = word_freq_mapper.keySet();
                Iterator<String> iterator = stringDoc.iterator();


                while(iterator.hasNext()) {
                    String term = iterator.next();
                    long termFrequency = word_freq_mapper.get(term);
                    invertedIndex.insert(term, nDocId, termFrequency);
                }

                mapper.add(nDocId, file.getPath());
            }

        }
    }


    public static ArrayList<String> parseFile(String path) throws IOException {

        BufferedReader bfrReader = new BufferedReader(new FileReader(path));
        StringBuilder strBuilder = new StringBuilder();
        String setLine = "";

        while((setLine = bfrReader.readLine()) != null) {
            strBuilder.append(setLine).append(" ");
        }
        bfrReader.close();

        return tokenizer(strBuilder.toString());
    }


    public static ArrayList<String> tokenizer(String line) {

        ArrayList<String> tokens = new ArrayList<String>();
        StringTokenizer strTokenizer = new StringTokenizer(line, DELIMITER);
        String sToken = "";

        while (strTokenizer.hasMoreTokens()){
            sToken = strTokenizer.nextToken();
            sToken = sToken.toLowerCase();
            if (Arrays.stream(STOPWORDS).anyMatch(sToken::equals)) {
                continue;
            }
            else {
                tokens.add(sToken);
            }
        }

        return tokens;
    }

}