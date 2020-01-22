package ovgu.aggressivedataskipping.featurization;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.FileUtils;
import ovgu.aggressivedataskipping.featurization.models.FeatureSet;

import java.io.File;
import java.io.IOException;

public class WorkloadWriter {

    public void writeQueries(FeatureSet featureSet, String output) throws IOException {
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        String json = gson.toJson(featureSet, FeatureSet.class);
        FileUtils.writeStringToFile(new File(output), json);
    }


}
