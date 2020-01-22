package ovgu.aggressivedataskipping.featurization.models;

import java.util.ArrayList;
import java.util.Objects;

public class Feature {

    private ArrayList<Predicate> predicates;

    private Integer frequency;

    public Feature(ArrayList<Predicate> predicates, Integer frequency) {
        this.predicates = predicates;
        this.frequency = frequency;
    }

    public Integer isSubsumed(Feature otherFeature) {
        if(predicates.equals(otherFeature.predicates)) {
            return 0;
        }
        for(Predicate predicate: predicates) {
            boolean isSubsumed = false;
            for(Predicate otherPredicate: otherFeature.getPredicates()) {
                if(predicate.isSubsumed(otherPredicate)) {
                    isSubsumed = true;
                    break;
                }
            }
            if(isSubsumed) return 1;
        }
        return -1;
    }

    public ArrayList<Predicate> getPredicates() {
        return predicates;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Feature feature = (Feature) o;
        return predicates.equals(feature.predicates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(predicates);
    }
}
