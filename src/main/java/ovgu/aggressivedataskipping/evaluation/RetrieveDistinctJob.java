package ovgu.aggressivedataskipping.evaluation;

import org.apache.livy.Job;
import org.apache.livy.JobContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;

public class RetrieveDistinctJob implements Job<String[]> {

    private final String tableName;

    private final String columnName;

    public RetrieveDistinctJob(String tableName, String columnName) {
        this.tableName = tableName;
        this.columnName = columnName;
    }


    @Override
    public String[] call(JobContext jobContext) throws Exception {
        SQLContext ctx = jobContext.sqlctx();
        Dataset<Row> df = ctx.table(tableName).where(columnName).distinct();
        return df.toJavaRDD().map(r -> r.getString(0)).collect().toArray(new String[0]);
    }
}
