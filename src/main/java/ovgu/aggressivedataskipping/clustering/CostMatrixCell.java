package ovgu.aggressivedataskipping.clustering;

import java.util.List;

public class CostMatrixCell {

    public CostMatrixCell(List<Boolean> firstVector, List<Boolean> secondVector) {
        this.firstVector = firstVector;
        this.secondVector = secondVector;
    }

    private List<Boolean> firstVector;

    private List<Boolean> secondVector;

    private Integer cost;


    public List<Boolean> getFirstVector() {
        return firstVector;
    }

    public List<Boolean> getSecondVector() {
        return secondVector;
    }

    public Integer getCost() {
        return cost;
    }
}