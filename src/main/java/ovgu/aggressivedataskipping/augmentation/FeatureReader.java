package ovgu.aggressivedataskipping.augmentation;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import ovgu.aggressivedataskipping.featurization.models.FeatureSet;
import ovgu.aggressivedataskipping.featurization.models.Query;
import ovgu.aggressivedataskipping.featurization.models.QuerySet;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.List;

public class FeatureReader {

    private String featuresPath;

    public FeatureReader(String featuresPath) {
        this.featuresPath = featuresPath;
    }

    public FeatureSet readFeatures() throws FileNotFoundException {
        Reader reader = new FileReader(featuresPath);
        JsonReader jsonReader = new JsonReader(reader);
        Gson gson = new Gson();
        return gson.fromJson(jsonReader, FeatureSet.class);
    }



}
