import org.apache.hadoop.util.Classpath;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.RelationalGroupedDataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import scala.Tuple2;

import javax.management.Query;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.stream.IntStream;

public class EvaluationApp {


    public static void main(String... args) throws IOException, URISyntaxException {
        SparkSession session = SparkSession.
                builder().appName("agg-data-skipping-evaluation")
                .master("yarn").enableHiveSupport()
                .getOrCreate();
        QueryReader reader = new QueryReader();
        EvaluationResultWriter writer = new EvaluationResultWriter(args[1]);
        List<String> queries = getQueries(args[0]);
        System.out.println("Started ========================");
        warmUp(session, args[3]);
        if (queries != null) {
            if(args[2].equals("normal")) {
                long totalSize = getTotalSize(session, args[3]);
                evaluateNormalQueries(session, args[0], queries, reader, writer, totalSize);
            } else if(args[2].equals("augmented")) {
                Map<String, Long> partitions = getPartitions(session, args[3], args[4]);
                Long totalSize = partitions.values().stream().reduce(Long::sum).get();
                evaluateAugmentedQueries(session, args[0], queries, reader, writer, partitions, totalSize);
            }
        }
        writer.end();
    }

    private static void evaluateAugmentedQueries(SparkSession session, String folderName,
                                                 List<String> queries, QueryReader reader,
                                                 EvaluationResultWriter writer, Map<String, Long> partitions,
                                                 Long totalSize) throws IOException {
        QueryEvaluator evaluator = new QueryEvaluator(partitions);
        for (String queryFileName : queries) {
            String query = reader.getQueryAsString(folderName + "/" + queryFileName);
            EvaluationResult result = evaluator.evaluateQuery(queryFileName, query, session, totalSize);
            writer.writeResultToFile(result);
        }
    }

    private static Long getTotalSize(SparkSession session, String tableName) {
        return session.table(tableName).count();
    }

    private static void evaluateNormalQueries(SparkSession session, String folderName,
                                              List<String> queries, QueryReader reader, EvaluationResultWriter writer,
                                              Long totalSize) throws IOException {
        NormalQueryEvaluator evaluator = new NormalQueryEvaluator(totalSize);
        for (String queryFileName : queries) {
            String query = reader.getQueryAsString(folderName + "/" + queryFileName);
            EvaluationResult result = evaluator.evaluateQuery(queryFileName, query, session);
            writer.writeResultToFile(result);
        }

    }

    private static void warmUp(SparkSession session, String tableName) {
        IntStream.range(0, 5).forEach(i -> {
            session.sql("select * from " + tableName);
        });
    }

    private static List<String> getQueries(String directoryPath) throws URISyntaxException {
        File file = new File(directoryPath);
        if (file.list() == null) return null;
        return Arrays.asList(file.list());
    }

    private static Map<String, Long> getPartitions(SparkSession session, String tableName, String columnName) {
        Map<String, Long> result = new HashMap<>();
        Dataset<Row> df = session.table(tableName).groupBy(columnName).count();
        List<Tuple2<String, Long>> partitionTuples = df.toJavaRDD().map(s -> new Tuple2<>(s.getString(0), s.getLong(1))).collect();
        partitionTuples.forEach(t -> result.put(t._1, t._2));
        return result;
    }

}
