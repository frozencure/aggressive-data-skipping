public class EvaluationResult {


    private String queryFileName;

    private Long executionTime;

    private Long rowsScanned;


    public EvaluationResult(String queryFileName, Long executionTime, Long rowsScanned) {
        this.queryFileName = queryFileName;
        this.executionTime = executionTime;
        this.rowsScanned = rowsScanned;
    }


    public String getQueryFileName() {
        return queryFileName;
    }

    public Long getExecutionTime() {
        return executionTime;
    }

    public Long getRowsScanned() {
        return rowsScanned;
    }
}
