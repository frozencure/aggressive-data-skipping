package ovgu.aggressivedataskipping.clustering;

import org.springframework.stereotype.Service;
import ovgu.aggressivedataskipping.augmentation.FeatureReader;
import ovgu.aggressivedataskipping.featurization.models.Feature;
import ovgu.aggressivedataskipping.featurization.models.FeatureSet;
import ovgu.aggressivedataskipping.livy.LivyClientWrapper;
import scala.Tuple2;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class ClusteringService {

    final LivyClientWrapper client;

    private static final char TRUE_KEY = '1';
    private static final char FALSE_KEY = '0';

    public ClusteringService(LivyClientWrapper client) {
        this.client = client;
    }

    public void partitionTable(String featuresPath, String databaseName, String tableName, String columnName, int limit,
                               String newTableName, String blockVectorColumnName)
            throws ExecutionException, InterruptedException, FileNotFoundException {
        FeatureReader reader = new FeatureReader(featuresPath);
        FeatureSet featureSet = reader.readFeatures();
        List<Integer> featureWeights = getFeatureWeights(featureSet);
        Tuple2<String, Long>[] featureVectors = client.getLivyClient()
                .submit(new ClusteringReadJob(tableName, databaseName, columnName)).get();
        Map<List<Boolean>, Long> booleanFeatureVectors = convertToBoolean(featureVectors);
        HaClusterer clusterer = new HaClusterer(booleanFeatureVectors, featureWeights, limit);
        clusterer.mergePartitions();
        Map<List<Boolean>, List<Boolean>> blockingVectors = clusterer.getVectorsWithBlockingVector();
        List<Tuple2<String, String>> blockingVectorTuples = blockingVectorsToTuples(blockingVectors);
        client.getLivyClient()
                .submit(new ClusteringWriteJob(blockingVectorTuples, tableName,
                        databaseName, columnName, newTableName, blockVectorColumnName)).get();
    }

    private Map<List<Boolean>, Long> convertToBoolean(Tuple2<String, Long>[] tuples) {
        Map<List<Boolean>, Long> map = new HashMap<>();
        for (Tuple2<String, Long> tuple : tuples) {
            char[] chars = tuple._1.toCharArray();
            List<Boolean> booleanArray = new ArrayList<>();
            for (char aChar : chars) {
                if (aChar == TRUE_KEY) booleanArray.add(true);
                else booleanArray.add(false);
            }
            map.put(booleanArray, tuple._2);
        }
        return map;
    }

    private List<Tuple2<String, String>> blockingVectorsToTuples(Map<List<Boolean>, List<Boolean>> blockingVectors) {
        List<Tuple2<String, String>> tuples = new ArrayList<>();
        for (List<Boolean> blockingVectorKey : blockingVectors.keySet()) {
            String key = vectorToString(blockingVectorKey);
            String value = vectorToString(blockingVectors.get(blockingVectorKey));
            Tuple2<String, String> tuple = new Tuple2<>(key, value);
            tuples.add(tuple);
        }
        return tuples;
    }

    private String vectorToString(List<Boolean> vector) {
        StringBuilder result = new StringBuilder();
        for (boolean bit : vector) {
            if (bit) {
                result.append(TRUE_KEY);
            } else {
                result.append(FALSE_KEY);
            }
        }
        return result.toString();
    }


    private List<Integer> getFeatureWeights(FeatureSet featureSet) {
        return featureSet.getFeatures()
                .stream().map(Feature::getFrequency).collect(Collectors.toList());
    }


}
