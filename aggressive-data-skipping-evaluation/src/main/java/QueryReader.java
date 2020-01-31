import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class QueryReader {

    public String getQueryAsString(String fileName) throws IOException {
        File file = new File(fileName);
        BufferedReader br = new BufferedReader(new FileReader(file));
        String vector;
        StringBuilder builder = new StringBuilder();
        while((vector = br.readLine()) != null) {
            builder.append(vector);
        }
        return builder.toString();
    }



}
