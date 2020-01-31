package ovgu.aggressivedataskipping.augmentation;

import org.apache.livy.Job;
import org.apache.livy.JobContext;
import org.apache.spark.sql.*;

import java.util.List;

public class AugmentationJob implements Job<Long> {

    private final List<String> featureStrings;

    private final String databaseName;

    private final String fromTableName;

    private final String newTableName;

    private final String newColumnName;

    private final int firstFeatureId;

    private final int batchSize;

    private final boolean isFromOld;

    public AugmentationJob(List<String> featureStrings, String databaseName, String fromTableName,
                           String newTableName, String newColumnName, int firstFeatureId, int batchSize,
                           boolean isFromOld) {
        this.featureStrings = featureStrings;
        this.databaseName = databaseName;
        this.fromTableName = fromTableName;
        this.newTableName = newTableName;
        this.newColumnName = newColumnName;
        this.firstFeatureId = firstFeatureId;
        this.batchSize = batchSize;
        this.isFromOld = isFromOld;
    }

    @Override
    public Long call(JobContext jobContext) throws Exception {
        SQLContext ctx = jobContext.sqlctx();
        ctx.sql("USE " + databaseName);
        Dataset<Row> dataset = ctx.sql("SELECT * FROM " + fromTableName);
        if(isFromOld) {
            dataset = dataset.withColumn(newColumnName, functions.lit(""));
        }
        for(int i=firstFeatureId; i < featureStrings.size() && i < firstFeatureId + batchSize; i++) {
            dataset = appendBit(dataset, newColumnName, featureStrings.get(i));
        }
        dataset.write().mode(SaveMode.Overwrite).saveAsTable(newTableName);
        return 10L;
    }

    private Dataset<Row> appendBit(Dataset<Row> dataset, String columnName, String featureString) {
        Dataset<Row> returned = dataset.where(featureString)
                .withColumn(columnName, functions.concat(dataset.col(columnName), functions.lit("1")));
        Dataset<Row> rest = dataset.where("NOT (" + featureString + ")")
                .withColumn(columnName, functions.concat(dataset.col(columnName), functions.lit("0")));
        dataset = returned.union(rest);
        return dataset;
    }

}
