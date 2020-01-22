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
        allPredicates.forEach(p -> {
            allQueries.forEach(q -> {
                Predicate toAddPredicate = null;
                for (Predicate qp :
                        q.getPredicates()) {
                    if (qp.isSubsumed(p)) {
                        toAddPredicate = p;
                        break;
                    }
                }
                if(toAddPredicate != null) {
                    if(!q.getPredicates().contains(toAddPredicate)) {
                        q.getPredicates().add(toAddPredicate);
                    }
                }
            });
        });
        return querySet;
    }

    public FeatureSet getFrequentItemSets(Set<Query> queries, int support) {
        Set<Set<Predicate>> transactions = queries.stream().map(q -> new HashSet<>(q.getPredicates())).collect(Collectors.toSet());
        AprioriMiner<Predicate> miner = new AprioriMiner<>(transactions, support);
        Map<Set<Predicate>, Integer> frequentItemSets = miner.getFrequentItemSets();
        List<Feature> frequentItems = frequentItemSets.keySet()
                .stream().map(s -> new Feature(new ArrayList<>(s), frequentItemSets.get(s))).collect(Collectors.toList());
        return new FeatureSet(frequentItems);
    }

    public void writeQueriesToFile(FeatureSet featureSet, String output) throws IOException {
        WorkloadWriter wr = new WorkloadWriter();
        wr.writeQueries(featureSet, output);
    }

}
