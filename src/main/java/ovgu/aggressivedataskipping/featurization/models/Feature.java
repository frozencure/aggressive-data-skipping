package ovgu.aggressivedataskipping.featurization.models;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;

public class Feature {

    private Set<Predicate> predicates;

    private Integer frequency;

    public Feature(Set<Predicate> predicates, Integer frequency) {
        this.predicates = predicates;
        this.frequency = frequency;
    }

    public boolean isSubsumed(Feature otherFeature) {
        for (Predicate otherPredicate : otherFeature.getPredicates()) {
            boolean isSubsumed = false;
            for (Predicate predicate : predicates) {
                if (predicate.isSubsumed(otherPredicate)) {
                    isSubsumed = true;
                    break;
                }
            }
            if (!isSubsumed) return false;
        }
        return true;
    }

    public Set<Predicate> getPredicates() {
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

    public String getFeatureAsCondition() {
        return predicates.stream().map(Predicate::getAsCondition)
                .reduce((a, b) -> a + " AND " + b).<IllegalStateException>orElseThrow(() -> {
                    throw new IllegalStateException("List cannot be empty");
                });
    }

}
