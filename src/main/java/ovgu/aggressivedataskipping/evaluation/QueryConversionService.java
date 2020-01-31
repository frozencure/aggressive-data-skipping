package ovgu.aggressivedataskipping.evaluation;

import org.springframework.stereotype.Service;
import ovgu.aggressivedataskipping.augmentation.FeatureReader;
import ovgu.aggressivedataskipping.featurization.WorkloadReader;
import ovgu.aggressivedataskipping.featurization.models.FeatureSet;
import ovgu.aggressivedataskipping.featurization.models.QuerySet;
import ovgu.aggressivedataskipping.livy.LivyClientWrapper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

@Service
public class QueryConversionService {


    final LivyClientWrapper client;

    public QueryConversionService(LivyClientWrapper client) {
        this.client = client;
    }


    public void convertQueries(String tableName, String columnName, String featuresPath, String oldQueriesPath, String newQueriesPath) throws FileNotFoundException, ExecutionException, InterruptedException {
        FeatureReader featureReader = new
                FeatureReader(featuresPath);
        WorkloadReader workloadReader =
                new WorkloadReader(oldQueriesPath);
        String[] blockVectors = client.getLivyClient().submit(new RetrieveDistinctJob(tableName, columnName)).get();
        FeatureSet featureSet = featureReader.readFeatures();
        QuerySet querySet = workloadReader.readQueries();
        for (int i = 0; i < querySet.getQueries().size(); i++) {
            String select = querySet.getQueries().get(i)
                    .getAsSelectQueryWithBlockVectors(featureSet, tableName, columnName, Arrays.asList(blockVectors));
            writeUsingFileWriter(select, "query" + i, newQueriesPath);
        }
    }

    private void writeUsingFileWriter(String data, String fileName, String newQueriesPath) {
        File file = new File(newQueriesPath + "/" + fileName);
        FileWriter fr = null;
        try {
            fr = new FileWriter(file);
            fr.write(data);
            fr.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
