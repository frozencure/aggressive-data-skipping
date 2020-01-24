package ovgu.aggressivedataskipping.augmentation;

import org.apache.livy.Job;
import org.apache.livy.JobContext;
import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.functions;

public class AugmentationJob implements Job<Long> {

    @Override
    public Long call(JobContext jobContext) throws Exception {
        SQLContext ctx = jobContext.sqlctx();
        ctx.sql("USE skipping");
        Dataset<Row> dataset = ctx.sql("SELECT (*) FROM lineitem");
        dataset.withColumn("newColumn", functions.when(dataset.col("l_quantity").$greater(10), "1").otherwise("0")).write().saveAsTable("test");
        return dataset.count();
    }
}
