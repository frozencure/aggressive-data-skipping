package ovgu.aggressivedataskipping.featurization.models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class QuerySet {

    ArrayList<Query> queries;

    public ArrayList<Query> getQueries() {
        return queries;
    }

    public Set<Predicate> getAllPredicates() {
        Set<Predicate> predicates = new HashSet<>();
        for (Query query : queries
        ) {
            predicates.addAll(query.getPredicates());
        }
        return predicates;
    }

}
