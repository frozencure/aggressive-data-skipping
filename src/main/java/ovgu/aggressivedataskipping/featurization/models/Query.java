package ovgu.aggressivedataskipping.featurization.models;

import java.util.ArrayList;
import java.util.Objects;

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

    public void setPredicates(ArrayList<Predicate> predicates) {
        this.predicates = predicates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Query query = (Query) o;
        return id == query.id &&
                filename.equals(query.filename) &&
                predicates.equals(query.predicates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(filename, id, predicates);
    }
}
