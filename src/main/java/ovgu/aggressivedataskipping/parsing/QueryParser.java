package ovgu.aggressivedataskipping.parsing;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitorAdapter;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.ComparisonOperator;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

public class QueryParser {

    private static final String COLUMN_NAME_KEY = "columnName";

    private static final String VALUE_KEY = "value";

    private static final String OPERATOR_KEY = "operator";

    private static final String PREDICATES_KEY = "predicates";
    public static final String FILE_NAME_KEY = "FileName";
    public static final String ID_KEY = "id";


    private JSONArray outarray = new JSONArray();

    private String input;

    private String output;

    private String table;

    public QueryParser(String input, String output, String table) {
        this.input = input;
        this.output = output;
        this.table = table;
    }

    public String exportPredicatesToJson() throws IOException {
        File folder = new File(input);
        File[] listOfFiles = folder.listFiles();

        Hashtable<String, JSONObject> Dict = new Hashtable<>();
        JSONArray JSONMain = new JSONArray();
        File outputFile = null;
        //atleast one file in the folder
        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                JSONObject Predicates = new JSONObject();
                BufferedReader br = new BufferedReader(new FileReader(file));    //we want to access the content line wise
                StringBuilder mainstr = new StringBuilder();
                String st;
                int cnt = 0;  //flag to check for the first where keyword
                String file_content = new String(Files.readAllBytes(Paths.get(String.valueOf(file)))).replaceAll("/t", "").replaceAll(",", "");
                if (file_content.contains(table)) {
                    while ((st = br.readLine()) != null)
                        if (cnt == 0) {
                            if (st.startsWith("where")) {
                                cnt += 1;
                            }
                        } else if (!st.startsWith("go") && !st.startsWith("group") && !st.startsWith("order") && !st.startsWith("set")) {
                            mainstr.append(st).append(" ");
                        }
                }
                if (mainstr.toString().isEmpty()) {
                    continue;
                }
                try {
                    Expression expr;

                    expr = CCJSqlParserUtil.parseCondExpression(mainstr.toString(), true);
                    if (expr instanceof AndExpression) {              //check if the expression is of type AND
                        JSONArray interobj = new JSONArray();
                        interobj = (JSONArray) getANDPredicates(expr, file.getName());
                        if (interobj.length() > 0) {
                            Predicates.put("Predicates", interobj);  //gets the content inside the and block
                        }
                    }
                    if (expr instanceof OrExpression) {                 //checks if expression of type OR
                        JSONArray interobj = new JSONArray();
                        interobj = (JSONArray) getORPredicates(expr, file.getName());
                        if (interobj.length() > 0) {
                            Predicates.put(PREDICATES_KEY, interobj); //gets the content inside the or block
                        }
                    }
                    if (Predicates.length() > 0) {                //append each parsed files predicate along with the filename
                        Dict.put(file.getName(), Predicates);
                        JSONMain.put(Predicates);
                    }
                } catch (JSQLParserException | JSONException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(outarray);
//            JSONObject topLevel = new JSONObject();
//            topLevel.append("queries", outarray);
            outputFile = new File(output);
            FileUtils.writeStringToFile(outputFile, outarray.toString());//json format output
        }
        return outputFile.getAbsolutePath();
    }

    /**
     * Handles the AND block
     *
     * @param expr
     * @return JSON Object of everything inside AND block
     * @throws JSONException
     */
    private Object getANDPredicates(Expression expr, String Filename) throws JSONException {
        JSONObject ANDPredicate = new JSONObject();
        JSONArray SimplePredicate = new JSONArray();

        expr.accept(new ExpressionVisitorAdapter() {


            @Override
            protected void visitBinaryExpression(BinaryExpression expr) {

                if (expr instanceof ComparisonOperator) {
                    if ((expr.getLeftExpression().toString().startsWith("l_") && !expr.getRightExpression().toString().contains("_")) && !(expr.getRightExpression().toString().startsWith("(SELECT"))) {
                        JSONObject PredicateObject = new JSONObject();
                        try {
                            PredicateObject.put(COLUMN_NAME_KEY, expr.getLeftExpression());
                            PredicateObject.put(OPERATOR_KEY, expr.getStringExpression());
                            if (expr.getRightExpression().toString().contains("DATE")) {
                                PredicateObject.put(VALUE_KEY, getAdditionExpressionValue(expr.getRightExpression()));
                            } else if (expr.getRightExpression().toString().contains("+")) {
                                PredicateObject.put(VALUE_KEY, getAdditionValue(expr.getRightExpression()));
                            } else {
                                PredicateObject.put(VALUE_KEY, expr.getRightExpression());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        SimplePredicate.put(PredicateObject);
                    }

                }


                if (expr.getRightExpression() instanceof InExpression) {   //for handling of IN operator
                    {
                        if (((InExpression) expr.getRightExpression()).getLeftExpression().toString().startsWith("l_")) {
                            List<String> list = Collections.singletonList(((InExpression) expr.getRightExpression()).getRightItemsList().toString());
                            String val = " ";
                            {
                                JSONObject PredicateObject = new JSONObject();
                                try {
                                    PredicateObject.put(COLUMN_NAME_KEY, ((InExpression) expr.getRightExpression()).getLeftExpression());
                                    PredicateObject.put(OPERATOR_KEY, "in");
                                    PredicateObject.put(VALUE_KEY, ((InExpression) expr.getRightExpression()).getRightItemsList().toString());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                SimplePredicate.put(PredicateObject);
                            }

                        }
                    }

                }
                if (expr instanceof OrExpression) {
                    try {
                        getORPredicates(expr, Filename);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if (!(expr instanceof OrExpression)) {
                    super.visitBinaryExpression(expr);
                }
            }
        });

        if (SimplePredicate.length() > 0) {
            ANDPredicate.put(FILE_NAME_KEY, Filename);
            ANDPredicate.put(ID_KEY, outarray.length() + 1);
            ANDPredicate.put(PREDICATES_KEY, SimplePredicate);
            {
                outarray.put(ANDPredicate);
            }
        }
        return SimplePredicate;
    }

    private Object getORPredicates(Expression expr, String filename) throws JSONException {
        JSONArray SimplePredicate = new JSONArray();
        JSONObject ORPredicate = new JSONObject();
        {

            expr.accept(new ExpressionVisitorAdapter() {

                @Override
                protected void visitBinaryExpression(BinaryExpression expr) {

                    if (expr instanceof ComparisonOperator) {         //only need right expression for the value tag in json file
                        if ((expr.getLeftExpression().toString().startsWith("l_") && !expr.getRightExpression().toString().contains("_")) && !(expr.getRightExpression().toString().startsWith("(SELECT"))) {
                            JSONObject PredicateObject = new JSONObject();
                            try {
                                PredicateObject.put(COLUMN_NAME_KEY, expr.getLeftExpression());
                                PredicateObject.put(OPERATOR_KEY, expr.getStringExpression());
                                if (expr.getRightExpression().toString().contains("DATE")) {
                                    PredicateObject.put(VALUE_KEY, getAdditionExpressionValue(expr.getRightExpression()));
                                } else if (expr.getRightExpression().toString().contains("+")) {
                                    PredicateObject.put(VALUE_KEY, getAdditionValue(expr.getRightExpression()));
                                } else {
                                    PredicateObject.put(VALUE_KEY, expr.getRightExpression());
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            SimplePredicate.put(PredicateObject);
                        }

                    }
                    if (expr instanceof AndExpression) {
                        try {
                            getANDPredicates(expr, filename);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    if (!(expr instanceof AndExpression)) {
                        super.visitBinaryExpression(expr);
                    }
                }
            });
        }
        if (SimplePredicate.length() > 0) {
            ORPredicate.put(FILE_NAME_KEY, filename);
            ORPredicate.put(ID_KEY, outarray.length() + 1);
            ORPredicate.put(PREDICATES_KEY, SimplePredicate);
            outarray.put(ORPredicate);
        }
        return SimplePredicate;
    }

    private static String getAdditionValue(Expression expr) {
        String Value = "";
        String[] Numbers_Expression = expr.toString().replaceAll("'", "").split(" ");
        if (Numbers_Expression.length > 1) {
            Value = String.valueOf(Integer.parseInt(Numbers_Expression[0]) + Integer.parseInt(Numbers_Expression[2]));
        } else {
            Value = String.valueOf(Integer.parseInt(Numbers_Expression[0]));
        }
        return Value;
    }
    
    public static String getAdditionExpressionValue(Expression expr) {
        String result = "";
        {
            String[] dateExpression = expr.toString().replaceAll("'", "").split(" ");
            LocalDate date = LocalDate.parse(dateExpression[1]);
            if (dateExpression.length > 2) {
                if (dateExpression[5].equals("year")) {
                    date = date.plusYears(Integer.parseInt(dateExpression[4]));
                } else if (dateExpression[5].equals("month")) {
                    date = date.plusMonths(Integer.parseInt(dateExpression[4]));
                } else if (dateExpression[5].equals("day")) {
                    date = date.plusDays(Integer.parseInt(dateExpression[4]));
                }
            }
            result = date.toString();
        }
        return result;
    }
}
