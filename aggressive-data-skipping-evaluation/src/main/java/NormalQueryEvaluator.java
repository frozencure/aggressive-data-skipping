import org.apache.spark.sql.SparkSession;

public class NormalQueryEvaluator {

    private long totalSize;

    public NormalQueryEvaluator(long totalSize) {
        this.totalSize = totalSize;
    }

    public EvaluationResult evaluateQuery(String fileName, String query, SparkSession session) {
        return new EvaluationResult(fileName, evaluateQueryTime(query, session), totalSize);
    }


    private Long evaluateQueryTime(String query, SparkSession session) {
        long startTime = System.currentTimeMillis();
        session.sql(query).show(100);
        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }


}
