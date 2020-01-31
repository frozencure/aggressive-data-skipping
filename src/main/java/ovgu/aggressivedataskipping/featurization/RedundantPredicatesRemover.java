package ovgu.aggressivedataskipping.featurization;

import com.google.common.collect.Sets;
import ovgu.aggressivedataskipping.featurization.models.Feature;
import ovgu.aggressivedataskipping.featurization.models.Predicate;
import ovgu.aggressivedataskipping.featurization.models.Query;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

public class RedundantPredicatesRemover {

    private List<Feature> features;

    private Set<Query> queries;

    Integer support;

    int maxFeatures;

    public RedundantPredicatesRemover(List<Feature> features, Set<Query> queries, Integer support, int maxFeatures) {
        this.features = features;
        this.queries = queries;
        this.support = support;
        this.maxFeatures = maxFeatures;
    }

    private Set<Query> getSubsumedQueries(Feature feature) {
        Set<Query> subsumedQueries = new HashSet<>();
        for (Query query : queries) {
            if (FeaturizationService.isSubsumed(query.getPredicates(), feature.getPredicates())) {
                subsumedQueries.add(query);
            }
        }
        return subsumedQueries;
    }

    private List<Feature> sortFeaturesBySubsumtion(List<Feature> features) {
        Map<Integer, List<Feature>> groupedFeatures = features.stream().collect(groupingBy(f -> f.getPredicates().size()));
        for(List<Feature> bucketFeatures: groupedFeatures.values()) {
            bucketFeatures.sort(this::isSubsumedCompare);
        }
        List<Integer> sortedKeys = groupedFeatures.keySet().stream().sorted(Integer::compareTo).collect(Collectors.toList());
        List<Feature> finalList = new ArrayList<>();
        sortedKeys.stream().map(groupedFeatures::get).forEach(finalList::addAll);
        return finalList;
    }


    private int isSubsumedCompare(Feature subsumedFeature, Feature subsumesFeature) {
        boolean subsumes = subsumesFeature.isSubsumed(subsumesFeature);
        boolean subsumed = subsumesFeature.isSubsumed(subsumedFeature);
        if(subsumed && subsumes) return 0;
        else if(subsumes && !subsumed) return 1;
        else if(!subsumes && subsumed) return -1;
        return 0;
    }


    public List<Feature> removeRedundantFeatures() {
        Set<Query> finalSubsumedQueries = new HashSet<>();
        ArrayList<Feature> finalFeatures = new ArrayList<>();
        List<Feature> sortedFeatures = sortFeaturesBySubsumtion(features);
        for (Feature feature : sortedFeatures) {
            Set<Query> subsumedQueries = getSubsumedQueries(feature);
            int numberOfAditionalQueries = Sets.difference(subsumedQueries, finalSubsumedQueries).size();
            if(finalSubsumedQueries.size() > queries.size() - support) break;
            if (numberOfAditionalQueries >= support) {
                finalSubsumedQueries.addAll(subsumedQueries);
                feature.setFrequency(numberOfAditionalQueries);
                finalFeatures.add(feature);
            }
            if(finalFeatures.size() >= maxFeatures) break;
        }
        return finalFeatures.stream()
                .sorted((a, b) -> b.getFrequency().compareTo(a.getFrequency())).collect(Collectors.toList());
    }

}
