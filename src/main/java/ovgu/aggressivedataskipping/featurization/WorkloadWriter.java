package ovgu.aggressivedataskipping.featurization;

import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;
import ovgu.aggressivedataskipping.featurization.models.QuerySet;

import java.io.File;
import java.io.IOException;

public class WorkloadWriter {

    public void writeQueries(QuerySet querySet, String output) throws IOException {
        Gson gson = new Gson();
        String json = gson.toJson(querySet, QuerySet.class);
        FileUtils.writeStringToFile(new File(output), json);
    }


}
