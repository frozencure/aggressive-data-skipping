package ovgu.aggressivedataskipping.featurization;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import ovgu.aggressivedataskipping.featurization.models.Query;
import ovgu.aggressivedataskipping.featurization.models.QuerySet;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.List;

public class WorkloadReader {

    private String workloadPath;

    public WorkloadReader(String workloadPath) {
        this.workloadPath = workloadPath;
    }

    public QuerySet readQueries() throws FileNotFoundException {
        Reader reader = new FileReader(workloadPath);
        JsonReader jsonReader = new JsonReader(reader);
        Gson gson = new Gson();
        List<Query> queries = Arrays.asList(gson.fromJson(jsonReader, Query[].class));
        return new QuerySet(queries);
    }


}
