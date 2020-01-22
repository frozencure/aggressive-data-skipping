package ovgu.aggressivedataskipping.featurization.models;

import java.util.ArrayList;
import java.util.List;

public class FeatureSet {

    private List<Feature> features;

    public FeatureSet(List<Feature> features) {
        this.features = features;
    }

    public List<Feature> getFeatures() {
        return features;
    }
}
