package ovgu.aggressivedataskipping.featurization.models;

import ovgu.aggressivedataskipping.augmentation.FeatureReader;
import ovgu.aggressivedataskipping.featurization.WorkloadReader;

import java.io.FileNotFoundException;

public class Main {


//    public static void main(String...args) throws FileNotFoundException {
//        WorkloadReader reader = new WorkloadReader(
//                "E:\\Documente\\master\\Sem III\\DBSE Project\\aggressive-data-skipping\\more-queries.json");
//        QuerySet querySet = reader.readQueries();
//        FeatureReader featureReader = new FeatureReader(
//                "E:\\Documente\\master\\Sem III\\DBSE Project\\aggressive-data-skipping\\features-test.json");
//        for(Query query : querySet.getQueries()) {
//            String asSelect = query.getAsSelectQuery(featureReader.readFeatures(),
//                    "skipping.lineitem", "block_id");
//            System.out.println(asSelect);
//        }
//    }
}
