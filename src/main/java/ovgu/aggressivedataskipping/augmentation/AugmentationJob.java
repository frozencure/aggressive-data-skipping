package ovgu.aggressivedataskipping.augmentation;

import org.apache.livy.Job;
import org.apache.livy.JobContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.functions;
import ovgu.aggressivedataskipping.featurization.models.FeatureSet;

import java.util.List;

public class AugmentationJob implements Job<Long> {

    private final List<String> featureStrings;

    public AugmentationJob(List<String> featureStrings) {
        this.featureStrings = featureStrings;
    }

    @Override
    public Long call(JobContext jobContext) throws Exception {
        SQLContext ctx = jobContext.sqlctx();
        ctx.sql("USE skipping");
        Dataset<Row> dataset = ctx.sql("SELECT (*) FROM lineitem").withColumn("features", functions.lit(""));
        for(String featureString: featureStrings) {
            Dataset<Row> returned = dataset.where(featureString)
                    .withColumn("features", functions.concat(dataset.col("features"), functions.lit("1")));
            Dataset<Row> rest = dataset.where("NOT " + featureString)
                    .withColumn("features", functions.concat(dataset.col("features"), functions.lit("0")));
            dataset = returned.union(rest);
        }
        dataset.write().saveAsTable("test");
        return dataset.count();
    }
}
