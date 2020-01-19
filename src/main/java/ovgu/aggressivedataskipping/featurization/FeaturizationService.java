package ovgu.aggressivedataskipping.featurization;

import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Set;
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
        ArrayList<Query> allQueries = querySet.queries;
        allPredicates.forEach(p -> {
            allQueries.forEach(q -> {
                ArrayList<Predicate> toAddPredicates = new ArrayList<>();
                q.getPredicates().forEach(qp -> {
                    if(qp.isSubsumed(p)) {
                        toAddPredicates.add(p);
                    }
                });
                q.getPredicates().addAll(toAddPredicates);
                toAddPredicates.clear();
            });
        });
        return querySet;
    }

//    public QuerySet aprioriMining(QuerySet inputQuerySet) {
//
//    }

}
