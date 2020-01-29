package ovgu.aggressivedataskipping.featurization;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import netscape.javascript.JSObject;
import org.apache.commons.io.FileUtils;
import ovgu.aggressivedataskipping.featurization.models.Feature;
import ovgu.aggressivedataskipping.featurization.models.FeatureSet;
import ovgu.aggressivedataskipping.featurization.models.QuerySet;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class FeaturizationMain {

    public static void main(String...args) throws IOException {
        FeaturizationService service = new FeaturizationService();
        QuerySet queries = service
                .augmentQueries("E:\\Documente\\master\\Sem III\\DBSE Project\\aggressive-data-skipping\\more-queries.json");
        FeatureSet featureSet = service.getFrequentItemSets(new HashSet<>(queries.getQueries()), 20);
//        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
//        String json = gson.toJson(queries, QuerySet.class);
//        FileUtils.writeStringToFile(new File("aug-output.json"), json);
//        service.writeQueriesToFile(featureSet, "aug-test.json");
        RedundantPredicatesRemover remover =
                new RedundantPredicatesRemover(featureSet.getFeatures(), new HashSet<>(queries.getQueries()), 20);
        List<Feature> remainingFeatures = remover.removeRedundantFeatures();
        System.out.println(remainingFeatures);
//        Integer[] a =  {1,2,3,4,5};
//        Integer[] b = {3,4,5};
//        Integer[] c = {4,5,6};
//        Integer[] d = {1,2,3,4,5,6};
//        Integer[] e = {2,3,5,6};
//        Set<Integer> aSet = new HashSet<>(Arrays.asList(a));
//        Set<Integer> bSet = new HashSet<>(Arrays.asList(b));
//        Set<Integer> cSet = new HashSet<>(Arrays.asList(c));
//        Set<Integer> dSet = new HashSet<>(Arrays.asList(d));
//        Set<Integer> eSet = new HashSet<>(Arrays.asList(e));
//        Set<Set<Integer>> setOfSets = new HashSet<>();
//        setOfSets.add(aSet);
//        setOfSets.add(bSet);
//        setOfSets.add(cSet);
//        setOfSets.add(dSet);
//        setOfSets.add(eSet);
//        AprioriMiner<Integer> miner = new AprioriMiner<>(setOfSets, 4);
//        Map<Set<Integer>,  Integer> itemsets = miner.getFrequentItemSets();
//        System.out.println(itemsets.toString());
    }

}
