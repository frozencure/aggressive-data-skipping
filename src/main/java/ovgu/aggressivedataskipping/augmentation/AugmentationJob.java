package ovgu.aggressivedataskipping.augmentation;

import org.apache.livy.Job;
import org.apache.livy.JobContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;

public class AugmentationJob implements Job<Long> {

    @Override
    public Long call(JobContext jobContext) throws Exception {
        SQLContext ctx = jobContext.sqlctx();
        Dataset<Row> dataset = ctx.sql("SELECT (*) FROM skipping.lineitem");
        return dataset.count();
    }
}
