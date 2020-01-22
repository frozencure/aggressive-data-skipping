package ovgu.aggressivedataskipping.featurization.models;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class QuerySet {

    List<Query> queries;

    public QuerySet(List<Query> queries) {
        this.queries = queries;
    }

    public List<Query> getQueries() {
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
