package ovgu.aggressivedataskipping.featurization;

import java.util.regex.Pattern;

public enum OperatorType {

    RANGE("\\<\\=?|\\>\\=?"),
    EQUALITY("\\="),
    IN("\\in");

    private final Pattern pattern;

    private OperatorType(final String regex) {
        this.pattern = Pattern.compile(regex);
    }

    public Pattern getPattern() {
        return pattern;
    }
}
