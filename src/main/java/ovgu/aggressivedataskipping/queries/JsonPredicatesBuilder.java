package ovgu.aggressivedataskipping.queries;

import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitorAdapter;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.ComparisonOperator;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonPredicatesBuilder {
    /**
     * Handles the AND block
     *
     * @param expr
     * @return JSON Object of everything inside AND block
     * @throws JSONException
     */
    Object createAndPredicates(Expression expr) throws JSONException {
        JSONObject ANDPredicate = new JSONObject();
        JSONArray SimplePredicate = new JSONArray();

        JSONArray NestedOR = new JSONArray();
        {

            expr.accept(new ExpressionVisitorAdapter() {


                @Override
                protected void visitBinaryExpression(BinaryExpression expr) {

                    if (expr instanceof ComparisonOperator) {
                        if (expr.getLeftExpression().toString().startsWith("l_") && !(expr.getRightExpression().toString().startsWith("(SELECT"))) {
                            JSONObject PredicateObject = new JSONObject();
                            try {
                                PredicateObject.put("ColumnName", expr.getLeftExpression());
                                PredicateObject.put("Operator", expr.getStringExpression());
                                PredicateObject.put("Value", expr.getRightExpression());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            SimplePredicate.put(PredicateObject);
                            System.out.println("ColumnName:" + expr.getLeftExpression() + "  Operator:" + expr.getStringExpression() + "  Value:" + expr.getRightExpression());
                        }

                    }


                    if (expr.getRightExpression() instanceof InExpression) {   //for handling of IN operator
                        {
                            if (((InExpression) expr.getRightExpression()).getLeftExpression().toString().startsWith("l_")) {
                                List<String> list = Collections.singletonList(((InExpression) expr.getRightExpression()).getRightItemsList().toString());
                                String val = " ";
                                for (Object l : list.toString().split(",")) {
                                    Pattern p = Pattern.compile("\'([^\"]*)\'");
                                    Matcher m = p.matcher(l.toString());
                                    while (m.find()) {
                                        val = (m.group(1));
                                    }
                                    JSONObject PredicateObject = new JSONObject();
                                    try {
                                        PredicateObject.put("ColumnName", ((InExpression) expr.getRightExpression()).getLeftExpression());
                                        PredicateObject.put("Operator", "=");
                                        PredicateObject.put("Value", val);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    NestedOR.put(PredicateObject);
                                    System.out.println("ColumnName:" + expr.getLeftExpression() + "  Operator" + expr.getStringExpression() + "  Value:" + expr.getRightExpression());


                                }

                            }
                        }

                    }
                    if (expr instanceof OrExpression) {
                        try {
                            NestedOR.put(createOrPredicates(expr));
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
                ANDPredicate.put("SimplePredicates", SimplePredicate);
            }
            if (NestedOR.length() > 0) {
                ANDPredicate.put("OR", NestedOR);
            }
        }
        return ANDPredicate;
    }

    Object createOrPredicates(Expression expr) throws JSONException {
        JSONArray SimplePredicate = new JSONArray();
        JSONObject ORPredicate = new JSONObject();
        JSONArray NestedAnd = new JSONArray();
        {

            expr.accept(new ExpressionVisitorAdapter() {

                @Override
                protected void visitBinaryExpression(BinaryExpression expr) {

                    if (expr instanceof ComparisonOperator) {         //only need right expression for the value tag in json file
                        if (expr.getLeftExpression().toString().startsWith("l_") && !(expr.getRightExpression().toString().startsWith("(SELECT"))) {
                            JSONObject PredicateObject = new JSONObject();
                            try {
                                PredicateObject.put("ColumnName", expr.getLeftExpression());
                                PredicateObject.put("Operator", expr.getStringExpression());
                                PredicateObject.put("Value", expr.getRightExpression());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            SimplePredicate.put(PredicateObject);
                            System.out.println("ColumnName:" + expr.getLeftExpression() + "  Operator:" + expr.getStringExpression() + "  Value:" + expr.getRightExpression());
                        }

                    }
                    if (expr instanceof AndExpression) {
                        try {
                            NestedAnd.put(createAndPredicates(expr));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    if (!(expr instanceof AndExpression)) {
                        super.visitBinaryExpression(expr);
                    }
                }
            });
            if (NestedAnd.length() > 0) {
                ORPredicate.put("NestedAND", NestedAnd);
            }
            if (SimplePredicate.length() > 0) {
                ORPredicate.put("SimplePredicates", SimplePredicate);
            }
        }
        return ORPredicate;
    }


}
