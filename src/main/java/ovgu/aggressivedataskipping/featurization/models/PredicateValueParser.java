package ovgu.aggressivedataskipping.featurization.models;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class PredicateValueParser {

    public Set<String> parseSet(String setString) {
        setString = setString.substring(1, setString.length() - 1);
        String[] stringItems = setString.split(", ");
        List<String> stringItemsList = Arrays.asList(stringItems);
        stringItemsList.forEach(s -> s = s.substring(1, s.length() - 1));
        return new HashSet<>(stringItemsList);
    }

    public Comparable getParsedValue(String value) {
        if(valueIsDate(value)) return LocalDate.parse(value);
        if(valueIsInteger(value)) return Integer.parseInt(value);
        return null;
    }

    public boolean valueIsSet(String value) {
        String regex = "\\(.+\\)";
        return Pattern.matches(regex, value);
    }

    public boolean valueIsInteger(String value) {
        if(value == null) {
            return false;
        }
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean valueIsDate(String value) {
        if(value == null) {
            return false;
        }
        try {
            LocalDate.parse(value);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }


}
