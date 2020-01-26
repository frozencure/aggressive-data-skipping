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
        Dataset<Row> dataset = ctx.sql("SELECT (*) FROM lineitem").withColumn("features", functions.lit(""))
                .where(featureStrings.get(0));
        dataset = dataset.withColumn("features", functions.col("features").plus(functions.lit("1")));

//        for (int i = 0; i < featureStrings.size(); i++
//        ) {
////            if(i==0) {
////                Dataset<Row> returned = dataset.where(featureStrings.get(i))
////                        .withColumn("features", functions.lit("1"));
////                Dataset<Row> rest = dataset.except(returned)
////                        .withColumn("features", functions.lit("0"));
////                dataset = returned.join(rest);
////            } else {
//            break;
//            Dataset<Row> returned = dataset.where(featureStrings.get(i))
//                    .withColumn("features", dataset.col("features").plus(functions.lit("1")));
//            Dataset<Row> rest = dataset.except(returned)
//                    .withColumn("features", dataset.col("features").plus(functions.lit("0")));
//            dataset = returned.union(rest);
//            break;
////            }
//        }
        dataset.write().saveAsTable("test");
        return dataset.count();
    }
}
