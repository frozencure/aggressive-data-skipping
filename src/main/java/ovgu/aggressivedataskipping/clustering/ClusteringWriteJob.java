package ovgu.aggressivedataskipping.clustering;

import org.apache.livy.Job;
import org.apache.livy.JobContext;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.sql.*;
import scala.Tuple2;

import java.util.List;
import java.util.Map;

public class ClusteringWriteJob implements Job<String> {

    private final List<Tuple2<String, String>> blockingVectorMap;

    private final String tableName;

    private final String databaseName;

    private final String columnName;


    public ClusteringWriteJob(List<Tuple2<String, String>> blockingVectorMap, String tableName, String databaseName, String columnName) {
        this.tableName = tableName;
        this.databaseName = databaseName;
        this.columnName = columnName;
        this.blockingVectorMap = blockingVectorMap;
    }

    @Override
    public String call(JobContext jobContext) throws Exception {
            SQLContext sqlContext = jobContext.sqlctx();
            sqlContext.sql("USE " + databaseName);
            Dataset<Row> dataset = sqlContext.table(tableName);
            Dataset<Row> unionVectorDataset = sqlContext.createDataset(blockingVectorMap, Encoders.tuple(Encoders.STRING(), Encoders.STRING()))
                    .toDF(columnName, "unionVectors");
            dataset.join(unionVectorDataset, columnName).write().partitionBy("unionVectors").saveAsTable("withUnion");
            return "success";
    }
}
