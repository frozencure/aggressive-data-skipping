package ovgu.aggressivedataskipping.clustering;

import org.apache.livy.Job;
import org.apache.livy.JobContext;
import org.apache.spark.sql.*;
import scala.Tuple2;

import java.util.List;

public class ClusteringWriteJob implements Job<Void> {

    private final List<Tuple2<String, String>> blockingVectorMap;

    private final String tableName;

    private final String databaseName;

    private final String columnName;

    private final String newTableName;

    private final String blockVectorColumnName;


    public ClusteringWriteJob(List<Tuple2<String, String>> blockingVectorMap, String tableName, String databaseName, String columnName, String newTableName, String blockVectorColumnName) {
        this.tableName = tableName;
        this.databaseName = databaseName;
        this.columnName = columnName;
        this.blockingVectorMap = blockingVectorMap;
        this.newTableName = newTableName;
        this.blockVectorColumnName = blockVectorColumnName;
    }

    @Override
    public Void call(JobContext jobContext) throws Exception {
            SQLContext sqlContext = jobContext.sqlctx();
            sqlContext.sql("USE " + databaseName);
            Dataset<Row> dataset = sqlContext.table(tableName);
            Dataset<Row> unionVectorDataset = sqlContext.createDataset(blockingVectorMap, Encoders.tuple(Encoders.STRING(), Encoders.STRING()))
                    .toDF(columnName, blockVectorColumnName);
            dataset.join(unionVectorDataset, columnName).write().partitionBy(blockVectorColumnName).saveAsTable(newTableName);
        return null;
    }
}
