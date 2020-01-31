import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class EvaluationResultWriter {

        private FileWriter fileWriter;

        public EvaluationResultWriter(String fileName) throws IOException {
            File file = new File(fileName);
            try {
                fileWriter = new FileWriter(file);
                fileWriter.write("Query, Execution time, Rows scanned\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void writeResultToFile(EvaluationResult result) throws IOException {
            fileWriter.write(result.getQueryFileName() + ", " + result.getExecutionTime() + ", " + result.getRowsScanned() + "\n");
            fileWriter.flush();
        }

        public void end() throws IOException {
            fileWriter.close();
        }




}
