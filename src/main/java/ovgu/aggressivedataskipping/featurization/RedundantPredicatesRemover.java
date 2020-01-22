package ovgu.aggressivedataskipping.featurization;

import com.google.common.collect.Sets;
import ovgu.aggressivedataskipping.featurization.models.Feature;
import ovgu.aggressivedataskipping.featurization.models.Predicate;
import ovgu.aggressivedataskipping.featurization.models.Query;

import java.util.*;
import java.util.stream.Collectors;

public class RedundantPredicatesRemover {

    private List<Feature> features;

    private Set<Query> queries;

    Integer support;

    public RedundantPredicatesRemover(List<Feature> features, Set<Query> queries, Integer support) {
        this.features = features;
        this.queries = queries;
        this.support = support;
    }

    private Set<Query> getSubsumedQueries(Feature feature) {
        Set<Query> subsumedQueries = new HashSet<>();
        for(Query query: queries) {
            if(isSubsumed(query.getPredicates(), feature.getPredicates())) {
                subsumedQueries.add(query);
            }
        }
        return subsumedQueries;
    }

    public List<Feature> removeRedundantFeatures() {
        Set<Query> finalSubsumedQueries = new HashSet<>();
        ArrayList<Feature> finalFeatures = new ArrayList<>();
        List<Feature> sortedFeatures = features.stream()
                .sorted((a,b) -> Integer.compare(b.getPredicates().size(), a.getPredicates().size()))
                .collect(Collectors.toList());
        for(Feature feature: sortedFeatures) {
            Set<Query> subsumedQueries = getSubsumedQueries(feature);
            int numberOfAditionalQueries = Sets.difference(subsumedQueries, finalSubsumedQueries).size();
            if(numberOfAditionalQueries >= support) {
                finalSubsumedQueries.addAll(subsumedQueries);
                feature.setFrequency(numberOfAditionalQueries);
                finalFeatures.add(feature);
            }
        }
        return finalFeatures.stream()
                .sorted((a,b) -> b.getFrequency().compareTo(a.getFrequency())).collect(Collectors.toList());
    }

    private boolean isSubsumed(List<Predicate> first, List<Predicate> second) {
        for(Predicate queryPredicate: first) {
            boolean isSubsumed = false;
            for(Predicate featurePredicate: second) {
                if(queryPredicate.isSubsumed(featurePredicate)) {
                    isSubsumed = true;
                    break;
                }
            }
            if(isSubsumed) return true;
        }
        return false;
    }


}
