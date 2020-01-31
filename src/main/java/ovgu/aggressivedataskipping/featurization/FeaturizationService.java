package ovgu.aggressivedataskipping.featurization;

import org.springframework.stereotype.Service;
import ovgu.aggressivedataskipping.featurization.models.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FeaturizationService {

    public QuerySet readQueries(String queriesFile) throws FileNotFoundException {
        WorkloadReader reader = new WorkloadReader(queriesFile);
        return reader.readQueries();
    }

    public QuerySet augmentQueries(String queriesFile) throws FileNotFoundException {
        QuerySet querySet = readQueries(queriesFile);
        Set<Predicate> allPredicates = querySet.getAllPredicates();
        List<Query> allQueries = querySet.getQueries();
        for (Query query : allQueries) {
            Set<Predicate> toAdd = new HashSet<>();
            for(Predicate pred : allPredicates) {
                for(Predicate qPred : query.getPredicates()) {
                    if(qPred.isSubsumed(pred)) {
                        toAdd.add(pred);
                    }
                }
            }
            query.getPredicates().addAll(toAdd);
        }
        return querySet;
    }

    public FeatureSet getFrequentItemSets(Set<Query> queries, int support, int stopEarlyLimit) {
        Set<Set<Predicate>> transactions = queries.stream().map(q -> new HashSet<>(q.getPredicates())).collect(Collectors.toSet());
        AprioriMiner<Predicate> miner = new AprioriMiner<>(transactions, support, stopEarlyLimit);
        Map<Set<Predicate>, Integer> frequentItemSets = miner.getFrequentItemSets();
        List<Feature> frequentItems = frequentItemSets.keySet()
                .stream().map(s -> new Feature(new HashSet<>(s), frequentItemSets.get(s))).collect(Collectors.toList());
        return new FeatureSet(frequentItems);
    }

    public void writeQueriesToFile(FeatureSet featureSet, String output) throws IOException {
        WorkloadWriter wr = new WorkloadWriter();
        wr.writeQueries(featureSet, output);
    }

    public static boolean isSubsumed(Set<Predicate> isSubsumedList, Set<Predicate> subsumedByList) {
        for (Predicate subsumedByPredicate : subsumedByList) {
            for (Predicate isSubsumedPredicate : isSubsumedList) {
                if (isSubsumedPredicate.isSubsumed(subsumedByPredicate)) {
                    return true;
                }
            }
        }
        return false;
    }


}
