import java.io.File;
import java.io.IOException;

import Indexing.DocIDMapper;
import Indexing.InvertedIndex;

import Parsers.Parser;


public class Application {

    public static void main(String[] args) throws ClassNotFoundException, IOException {

        InvertedIndex invertedIndex = new InvertedIndex();
        String indexPath = "/home/stamatiosorfanos/git/SearchEngine/src/invertedIndex.ser";
        File invertedIndexFile = new File(indexPath);

        DocIDMapper mapper = new DocIDMapper(8001);
        String mapperPath = "/home/stamatiosorfanos/git/SearchEngine/src/mapper.ser";
        File mapperFile = new File(mapperPath);


        if (!invertedIndexFile.exists() || !mapperFile.exists()) {
            File data = new File("/home/stamatiosorfanos/git/SearchEngine/Reuters8Kdata");
            Parser.parseData(data, mapper, invertedIndex);
            invertedIndex.write(indexPath);
            mapper.write(mapperPath);
        }
        else {
            invertedIndex.load(indexPath);
            mapper.load(mapperPath);
        }

        RunnerManager.runner_manager(invertedIndex, mapper);
    }
}



