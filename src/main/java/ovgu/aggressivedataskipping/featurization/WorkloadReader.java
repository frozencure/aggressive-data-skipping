package ovgu.aggressivedataskipping.featurization;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

public class WorkloadReader {

    private String workloadPath;

    public WorkloadReader(String workloadPath) {
        this.workloadPath = workloadPath;
    }

    public String readQueries() throws FileNotFoundException {
        Reader reader = new FileReader(workloadPath);
        JsonReader jsonReader = new JsonReader(reader);
        Gson gson = new Gson();
        QuerySet querySet = gson.fromJson(jsonReader, QuerySet.class);
        return querySet.queries.get(0).toString();
    }



}
