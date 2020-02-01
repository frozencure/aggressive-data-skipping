package ovgu.aggressivedataskipping.clustering;

import org.apache.livy.Job;
import org.apache.livy.JobContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import scala.Tuple2;

import java.util.*;

public class ClusteringReadJob implements Job<Tuple2<String, Long>[]> {

    private final String tableName;

    private final String databaseName;

    private final String columnName;

    public ClusteringReadJob(String tableName, String databaseName, String columnName) {
        this.tableName = tableName;
        this.databaseName = databaseName;
        this.columnName = columnName;
    }

    @Override
    public Tuple2<String, Long>[] call(JobContext jobContext) throws Exception {
        SQLContext sqlContext = jobContext.sqlctx();
        sqlContext.sql("Use " + databaseName);
        List<Tuple2<String, Long>> dataset = sqlContext.table(tableName)
                .select(columnName).groupBy(columnName)
                .count().toJavaRDD().map(s -> new Tuple2<>(s.getString(0), s.getLong(1))).collect();
        return dataset.toArray(new Tuple2[0]);
    }
}
