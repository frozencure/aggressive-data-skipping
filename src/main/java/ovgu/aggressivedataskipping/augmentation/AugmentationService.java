package ovgu.aggressivedataskipping.augmentation;

import org.springframework.stereotype.Service;
import ovgu.aggressivedataskipping.featurization.models.Feature;
import ovgu.aggressivedataskipping.featurization.models.FeatureSet;
import ovgu.aggressivedataskipping.livy.LivyClientWrapper;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class AugmentationService {

    final LivyClientWrapper client;

    public AugmentationService(LivyClientWrapper client) {
        this.client = client;
    }

    public long testAugmentation(String featuresPath) throws ExecutionException, InterruptedException, FileNotFoundException {
        FeatureReader reader = new FeatureReader(featuresPath);
        FeatureSet featureSet = reader.readFeatures();
        List<String> featuresAsConditions = featureSet.getFeatures()
                .stream().map(Feature::getFeatureAsCondition).collect(Collectors.toList());
        return client.getLivyClient().submit(new AugmentationJob(featuresAsConditions)).get();
    }

}
