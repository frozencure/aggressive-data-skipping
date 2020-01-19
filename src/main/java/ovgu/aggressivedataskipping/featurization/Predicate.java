package ovgu.aggressivedataskipping.featurization;

import java.util.Objects;

public class Predicate {

    private String columnName;

    private String operator;

    private String value;

    private transient OperatorType operatorType;

    public Predicate(String columnName, String operator, String value) {
        this.columnName = columnName;
        this.operator = operator;
        this.value = value;
        this.operatorType = OperatorType.valueOf(operator);
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

    @Override
    public int hashCode() {
        return Objects.hash(columnName, operator, value);
    }
}
