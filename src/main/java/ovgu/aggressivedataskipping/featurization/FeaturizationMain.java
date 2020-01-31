package ovgu.aggressivedataskipping.featurization;

import ovgu.aggressivedataskipping.augmentation.FeatureReader;
import ovgu.aggressivedataskipping.featurization.models.FeatureSet;
import ovgu.aggressivedataskipping.featurization.models.QuerySet;

import java.io.*;
import java.util.*;

public class FeaturizationMain {

    public static void main(String...args) throws IOException {
        File file = new File("E:\\Documente\\master\\Sem III\\DBSE Project\\aggressive-data-skipping\\block-vectors.txt");

        BufferedReader br = new BufferedReader(new FileReader(file));
        String vector;
        List<String> blockVectors = new ArrayList<>();
        while((vector = br.readLine()) != null) {
            blockVectors.add(vector);
        }
        FeatureReader featureReader = new
                FeatureReader("E:\\Documente\\master\\Sem III\\DBSE Project\\aggressive-data-skipping\\features-test1.json");
        WorkloadReader workloadReader =
                new WorkloadReader("E:\\Documente\\master\\Sem III\\DBSE Project\\aggressive-data-skipping\\more-queries.json");
        FeatureSet featureSet = featureReader.readFeatures();
        QuerySet querySet = workloadReader.readQueries();
        for(int i=0; i < querySet.getQueries().size(); i++) {
            String select = querySet.getQueries().get(i)
                    .getAsSelectQuery("skipping.withUnion");
            writeUsingFileWriter(select, "norm-query" + i);
        }
    }

    private static void writeUsingFileWriter(String data, String fileName) {
        File file = new File("E:\\Documente\\master\\Sem III\\DBSE Project\\aggressive-data-skipping\\norm-queries\\"
        + fileName + ".sql");
        FileWriter fr = null;
        try {
            fr = new FileWriter(file);
            fr.write(data);
            fr.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            //close resources
            try {
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


//        FeaturizationService service = new FeaturizationService();
//        QuerySet queries = service
//                .augmentQueries("E:\\Documente\\master\\Sem III\\DBSE Project\\aggressive-data-skipping\\more-queries.json");
//        FeatureSet featureSet = service.getFrequentItemSets(new HashSet<>(queries.getQueries()), 12);
//        RedundantPredicatesRemover remover =
//                new RedundantPredicatesRemover(featureSet.getFeatures(), new HashSet<>(queries.getQueries()), 12);
//        List<Feature> remainingFeatures = remover.removeRedundantFeatures();
//        WorkloadWriter writer = new WorkloadWriter();
//        writer.writeQueries(new FeatureSet(remainingFeatures), "features-test.json");
//        System.out.println(remainingFeatures);


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
//        System.out.println(itemsets.toString()); }

}
