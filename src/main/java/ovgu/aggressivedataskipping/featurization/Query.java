package ovgu.aggressivedataskipping.featurization;

import java.util.ArrayList;
import java.util.List;

public class Query {

    private String filename;

    private int id;

    private ArrayList<Predicate> predicates;

    public String getFilename() {
        return filename;
    }

    public int getId() {
        return id;
    }

    public ArrayList<Predicate> getPredicates() {
        return predicates;
    }
}
