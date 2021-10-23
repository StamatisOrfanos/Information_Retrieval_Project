import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import Indexing.DocIDMapper;
import Indexing.InvertedIndex;
import Indexing.Posting;
import Parsers.QueryParser;

public class RunnerManager {

    public static void runner_manager(InvertedIndex invertedIndex, DocIDMapper mapper) throws IOException {
        Scanner scanner = new Scanner(System.in);
        String running = "";
        System.out.println("\n\nWelcome to the browser");


        while (!running.equals("q")) {
            System.out.println("Please enter the query:");
            String query = scanner.nextLine();

            ArrayList<Posting> finalResult = QueryParser.chooseQueryType(query, invertedIndex);


            if (finalResult.size() == 0) {
                System.out.println("\n\n\nUnfortuantely there are no documents to match your query");
                System.out.println("\n\n\nEnter q to exit the browser or hit enter to continue\n\n");
                running = scanner.nextLine();
            }
            else {
                for(int i=0; i<finalResult.size(); i++) {
                    // Use the print below to verify that the data are sorted
                    //System.out.println(finalResult.get(i) + " --> " +mapper.get(finalResult.get(i).getnDocId()));
                    System.out.println((i+1) + "." + mapper.get(finalResult.get(i).getnDocId()));
                }
                System.out.println("\n\nPlease type the number of the page you would like to visit or 'c' to continue");
                String choice = scanner.nextLine();

                if (choice.equals("c")) {
                    System.out.println("\n\n\nEnter q to exit the browser or hit enter to continue");
                    running = scanner.nextLine();
                }
                else {
                    int page = Integer.parseInt(choice);
                    getDocument(finalResult, page, mapper);
                    System.out.println("\n\n\nEnter q to exit the browser or hit enter to continue");
                    running = scanner.nextLine();
                }
            }

        }
        scanner.close();
    }


    public static void getDocument(ArrayList<Posting> result, int choice, DocIDMapper mapper) throws IOException {

        String path = mapper.get(result.get(choice-1).getnDocId());
        BufferedReader bfrReader = new BufferedReader(new FileReader(path));
        String setLine = "";

        while((setLine = bfrReader.readLine()) != null) {
            System.out.println(setLine);
        }
        bfrReader.close();
    }
}
