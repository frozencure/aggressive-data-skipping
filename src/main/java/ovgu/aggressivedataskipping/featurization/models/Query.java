package ovgu.aggressivedataskipping.featurization.models;

import com.google.common.collect.Sets;
import ovgu.aggressivedataskipping.featurization.FeaturizationService;
import ovgu.aggressivedataskipping.featurization.RedundantPredicatesRemover;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.IntStream;

public class Query {

    private String filename;

    private int id;

    private Set<Predicate> predicates;

    public String getFilename() {
        return filename;
    }

    public int getId() {
        return id;
    }

    public Set<Predicate> getPredicates() {
        return predicates;
    }

    public void setPredicates(Set<Predicate> predicates) {
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

    private void appendBits(List<String> old) {
        List<String> newList = new ArrayList<>();
        old.forEach(s -> {
            newList.add(s + "0");
            newList.add(s + "1");
        });
        old.clear();
        old.addAll(newList);
    }

    public String getAsSelectQueryWithBlockVectors(FeatureSet featureSet, String tableName, String columnName, List<String> blockVectors) {
        StringBuilder builder = new StringBuilder();
        String skipAll = "";
        for(int i=0; i< blockVectors.get(0).length(); i++) skipAll += "1";

        builder.append("SELECT * FROM ").append(tableName).append(" WHERE ");
        if(!skipAll.equals(getBitVector(featureSet))) {
            builder.append(columnName).append(" IN (");
            blockVectors.stream().filter(b -> mustScan(b, featureSet)).forEach(b -> {
                builder.append("'").append(b).append("', ");
            });
            builder.deleteCharAt(builder.length() - 2);
            builder.append(") and ");
        }
        builder.append(toString());
        return builder.toString();
    }

    public String getAsSelectQuery(String tableName) {
        return "SELECT * FROM " + tableName + " WHERE " + " " +
                toString();
    }


    private boolean mustScan(String blockVector, FeatureSet featureSet) {
        String queryVector = getBitVector(featureSet);
        for(int i=0; i < queryVector.toCharArray().length; i++) {
            if (queryVector.charAt(i) == '0' && blockVector.charAt(i) == '0') {
                return false;
            }
        }
        return true;
    }


//    private List<String> getToScanPartitions(FeatureSet featureSet) {
//        String bitVector = getBitVector(featureSet);
//        char[] bits = bitVector.toCharArray();
//        List<Integer> bitPositions = new ArrayList<>();
//        for(int i=0; i < bits.length; i++) {
//                if (bits[i] == '0') {
//                bitPositions.add(i);
//            }
//        }
//        List<String> toScanPartitions = new ArrayList<>();
//        toScanPartitions.add("0");
//        toScanPartitions.add("1");
//        int count = 0;
//        while(count < featureSet.getFeatures().size() - bitPositions.size()) {
//            appendBits(toScanPartitions);
//        }
//        for(String part : toScanPartitions) {
//            StringBuffer buffer = new StringBuffer(part);
//            bitPositions.stream().forEach(pos -> buffer.insert(pos, "1"));
//        }
//        return toScanPartitions;
//    }

    public String getBitVector(FeatureSet featureSet) {
        StringBuilder builder = new StringBuilder();
        for(Feature feature : featureSet.getFeatures()) {
            if(FeaturizationService.isSubsumed(this.predicates, feature.getPredicates())) {
                builder.append("0");
            } else {
                builder.append("1");
            }
        }
        return builder.toString();
    }

    @Override
    public String toString() {
        ArrayList<Predicate> list = new ArrayList<>(predicates);
        StringBuilder builder = new StringBuilder();
        for(int i=0; i < predicates.size(); i++) {
            if(i!=0) builder.append("and ");
            builder.append(list.get(i).getAsCondition()).append(" ");
        }
        return builder.toString();
    }
}
