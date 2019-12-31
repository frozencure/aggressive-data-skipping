package ovgu.aggressivedataskipping.queries;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class QueryParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(QueryParser.class);

    private final static String SQL_EXTENSTION = ".sql";

    private String queriesFolderPath;

    private String jsonOutputFile;

    private String tableName;

    public QueryParser(String queriesFolderPath, String jsonOutputFile, String tableName) {
        this.queriesFolderPath = queriesFolderPath;
        this.jsonOutputFile = jsonOutputFile;
        this.tableName = tableName;
    }

    private static List<File> readFiles(String folderPath) {
        File folder = new File(folderPath);
        File[] files = folder.listFiles((File f) -> f.getName().endsWith(SQL_EXTENSTION));
        return Arrays.asList(files);
    }

    private static boolean fileContainsString(File file, String content) {
        try {
            List<String> fileContent = Files.readAllLines(Paths.get(String.valueOf(file)))
                    .stream().filter(s -> s.contains(content)).collect(Collectors.toList());
            return fileContent.size() > 0;
        } catch (IOException e) {
            LOGGER.error("Query file cannot be found", e);
        }
        return false;
    }

    private static String getPredicatesFromSelectClause(File file) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            boolean didReadWhere = false;
            while ((line = bufferedReader.readLine()) != null)
                if (!didReadWhere) {
                    if (line.startsWith("where")) didReadWhere = true;
                } else if (!line.startsWith("go") && !line.startsWith("group") && !line.startsWith("order") && !line.startsWith("set")) {
                    stringBuilder.append(line).append(" ");
                }
            return stringBuilder.toString();
        } catch (IOException e) {
            LOGGER.error("Query file cannot be found", e);
        }
        return "";
    }

    public String exportPredicatesToJson() {
        JSONObject json = parseQueries();
        try {
            Path outputPath = Paths.get(jsonOutputFile);
            Files.write(outputPath, json.toString().getBytes());
            return outputPath.toAbsolutePath().toString();
        } catch (IOException e) {
            LOGGER.error("Json output path does not exist", e);
        }
        return "";
    }

    public JSONObject parseQueries() {
        List<File> listOfFiles = readFiles(queriesFolderPath);
        JSONArray jsonOutputArray = new JSONArray();
        JsonPredicatesBuilder predicatesBuilder = new JsonPredicatesBuilder();

        for (File file : listOfFiles) {
            JSONObject predicates = new JSONObject();
            String predicatesString = "";
            if (fileContainsString(file, tableName))  // if line item present
            {
                predicatesString = getPredicatesFromSelectClause(file);
            }
            System.out.println(predicatesString);
            if (predicatesString.isEmpty()) {
                continue;
            }
            try {
                Expression expr = CCJSqlParserUtil.parseCondExpression(predicatesString, true);
                if (expr instanceof AndExpression) {              //check if the expression is of type AND
                    JSONObject interobj = (JSONObject) predicatesBuilder.createAndPredicates(expr);
                    addPredicate(file, predicates, interobj);

                }
                if (expr instanceof OrExpression) {
                    JSONObject interobj = (JSONObject) predicatesBuilder.createOrPredicates(expr);
                    addPredicate(file, predicates, interobj);
                }
                if (predicates.length() > 0) {
                    //Dict.put(file.getName(),predicates);
                    jsonOutputArray.put(predicates);
                    System.out.println(predicates);
                }
            } catch (JSQLParserException | JSONException e) {
                LOGGER.error("Error while parsing queries", e);
            }


        }
        JSONObject jsonOutput = new JSONObject();
        jsonOutput.put("Queries", jsonOutputArray);
        //json format output
//        try (Writer writer = new FileWriter("MainCsvFile.csv")) {               //csv format output
//            writer.append("id").append(',').append("Filename").append(',').append("Predicates").append("\n");
//            int i=1;
//            for (Map.Entry<String, JSONObject> entry : Dict.entrySet()) {
//                String val=entry.getValue().toString();
//                writer.append(""+i).append(',').append(entry.getKey())
//                        .append(',')
//                        .append(entry.getValue().toString())
//                        .append("\n");
//                i++;
//            }
//        } catch (IOException ex) {
//            ex.printStackTrace(System.err);
//        }
        return jsonOutput;
    }

    private void addPredicate(File file, JSONObject predicates, JSONObject interobj) {
        if (interobj.length() > 0) {
            predicates.put("QueryNumber", file.getName());    //puts the query number as file name
            predicates.put("ANDPredicates", interobj);  //gets the content inside the and block
        }
    }
}
