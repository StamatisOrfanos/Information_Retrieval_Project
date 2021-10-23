package Indexing;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class DocIDMapper {
    private String[] map;


    public DocIDMapper(int size) {
        map = new String[size];
    }

    public void add(int pos, String filename) {
        map[pos] = filename;
    }

    public String get(int pos) {
        return map[pos];
    }


    // Write the mapper file, so that we can use it after we made the inverted index
    public void write(String mapperFile) throws IOException{
        FileOutputStream fileOutput = new FileOutputStream(mapperFile, true);
        ObjectOutputStream objectOuput = new ObjectOutputStream(fileOutput);

        objectOuput.writeObject(map);

        objectOuput.close();
        fileOutput.close();
    }


    // Load the mapper file, so that we can use search functions after we have made the inverted index
    public void load(String path) throws IOException, ClassNotFoundException {
        FileInputStream inputFile = new FileInputStream(path);
        ObjectInputStream objectInput = new ObjectInputStream(inputFile);

        map = (String[]) objectInput.readObject();

        objectInput.close();
        inputFile.close();
    }

}
