import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class QueryEvaluator {

    private Map<String, Long> partionSizes;


    public QueryEvaluator(Map<String, Long> partionSizes) {
        this.partionSizes = partionSizes;
    }

    public EvaluationResult evaluateQuery(String fileName, String query, SparkSession session, Long totalSize) {
        return new EvaluationResult(fileName, evaluateQueryTime(query, session), evaluateNumberOfRowsScanned(query, totalSize));
    }


    private Long evaluateQueryTime(String query, SparkSession session) {
        long startTime = System.currentTimeMillis();
        session.sql(query).show(100);
        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }

    public Long evaluateNumberOfRowsScanned(String query, Long totalSize) {
        Pattern pattern = Pattern.compile("\\(.*\\)");
        Matcher matcher = pattern.matcher(query);
        String partitions = "";
        if (matcher.find())
        {
            partitions = matcher.group(0);
            partitions = partitions.replace("'", "");
            partitions = partitions.substring(1, partitions.length() - 1);
            List<String> partitionList = Arrays.asList(partitions.split(", "));
            List<Long> sizes =  partitionList.stream().map(p -> partionSizes.get(p)).collect(Collectors.toList());
            return sizes.stream().filter(Objects::nonNull).reduce(Long::sum).orElse(0L);
        }
        return totalSize;
    }






}
