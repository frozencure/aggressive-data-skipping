package ovgu.aggressivedataskipping.featurization.models;

public enum OperatorType {

    RANGE("\\<\\=?|\\>\\=?"),
    EQUALITY("\\="),
    IN("in");

    private final String pattern;

    OperatorType(final String regex) {
        this.pattern = regex;
    }

    public String getPattern() {
        return pattern;
    }
}
