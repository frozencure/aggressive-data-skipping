package ovgu.aggressivedataskipping.featurization.models;

import java.util.Objects;
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

    private boolean isSubsumedEquality() {
        return false;
    }

    private boolean isSubsumedRange(Predicate otherPredicate) {
//        if(operator.equals("<") && otherPredicate.operator.equals("<")) return value.compareTo(otherPredicate.value);
//        if(operator.equals("<") && otherPredicate.operator.equals("<=")) return value <= otherPredicate.value;
//        if(operator.equals("<=") && otherPredicate.operator.equals("<=")) return value < otherPredicate.value;
//        if(operator.equals("<=") && otherPredicate.operator.equals("<")) return value < otherPredicate.value;
//        if(operator.equals(">") && otherPredicate.operator.equals(">")) return value > otherPredicate.value;
//        if(operator.equals(">") && otherPredicate.operator.equals(">=")) return value >= otherPredicate.value;
//        if(operator.equals(">=") && otherPredicate.operator.equals(">=")) return value > otherPredicate.value;
//        if(operator.equals(">=") && otherPredicate.operator.equals(">")) return value > otherPredicate.value;
        return false;
    }

    public boolean isSubsumed(Predicate otherPredicate) {
        if(!this.columnName.equals(otherPredicate.columnName)) return false;
        if(!this.getOperatorType().equals(otherPredicate.getOperatorType())) return false;
        if(getOperatorType().equals(OperatorType.EQUALITY)) return false;
        if(getOperatorType().equals(OperatorType.RANGE)) {
            if(operator.equals(otherPredicate.operator)) {
                int predicateValue = Integer.parseInt(value);
                int otherPredicateValue = Integer.parseInt(otherPredicate.value);
                if(operator.equals(">=")) {
                    return predicateValue > otherPredicateValue;
                }
                if(operator.equals("<=")) {
                    return predicateValue < otherPredicateValue;
                }
            }
        }
        if(getOperatorType().equals(OperatorType.IN)) {
            return false;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(columnName, operator, value);
    }
}
