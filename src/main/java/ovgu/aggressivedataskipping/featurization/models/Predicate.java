package ovgu.aggressivedataskipping.featurization.models;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

public class Predicate {

    private String columnName;

    private String operator;

    private String value;

    public Predicate(String columnName, String operator, String value) {
        this.columnName = columnName;
        this.operator = operator;
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Predicate predicate = (Predicate) o;
        return columnName.equals(predicate.columnName) &&
                operator.equals(predicate.operator) &&
                value.equals(predicate.value);
    }

    public OperatorType getOperatorType() {
        if (Pattern.matches(OperatorType.EQUALITY.getPattern(), operator)) {
            return OperatorType.EQUALITY;
        }
        if (Pattern.matches(OperatorType.RANGE.getPattern(), operator)) {
            return OperatorType.RANGE;
        }
        if (Pattern.matches(OperatorType.IN.getPattern(), operator)) {
            return OperatorType.IN;
        }
        return null;
    }

    private boolean isSubsumedSet(Predicate otherPredicate) {
        PredicateValueParser parser = new PredicateValueParser();
        Set<String> thisSet = parser.parseSet(value);
        Set<String> otherSet = parser.parseSet(otherPredicate.value);
        return otherSet.containsAll(thisSet);
    }

    private boolean isSubsumedRange(Predicate otherPredicate) {
        PredicateValueParser parser = new PredicateValueParser();
        Comparable thisValue = parser.getParsedValue(value);
        Comparable otherValue = parser.getParsedValue(otherPredicate.value);
        if(!thisValue.getClass().equals(otherValue.getClass())) return false;
        if(operator.equals("<") && otherPredicate.operator.equals("<")) return thisValue.compareTo(otherValue) <= 0;
        if(operator.equals("<") && otherPredicate.operator.equals("<=")) return thisValue.compareTo(otherValue) <= 0;
        if(operator.equals("<=") && otherPredicate.operator.equals("<=")) return thisValue.compareTo(otherValue) <= 0;
        if(operator.equals("<=") && otherPredicate.operator.equals("<")) return thisValue.compareTo(otherValue) <= 0;
        if(operator.equals(">") && otherPredicate.operator.equals(">")) return thisValue.compareTo(otherValue) >= 0;
        if(operator.equals(">") && otherPredicate.operator.equals(">=")) return thisValue.compareTo(otherValue) >= 0;
        if(operator.equals(">=") && otherPredicate.operator.equals(">=")) return thisValue.compareTo(otherValue) >= 0;
        if(operator.equals(">=") && otherPredicate.operator.equals(">")) return thisValue.compareTo(otherValue) >= 0;
        return false;
    }

    public boolean isSubsumed(Predicate otherPredicate) {
        if(!this.columnName.equals(otherPredicate.columnName)) return false;
        if(!this.getOperatorType().equals(otherPredicate.getOperatorType())) return false;
        if(getOperatorType().equals(OperatorType.EQUALITY)) return this.equals(otherPredicate);
        if(getOperatorType().equals(OperatorType.IN)) return isSubsumedSet(otherPredicate);
        if(getOperatorType().equals(OperatorType.RANGE)) return isSubsumedRange(otherPredicate);
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(columnName, operator, value);
    }

    @Override
    public String toString() {
        return "Predicate{" +
                "columnName='" + columnName + '\'' +
                ", operator='" + operator + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

    public String getAsCondition() {
        return columnName + operator + value;
    }
}
