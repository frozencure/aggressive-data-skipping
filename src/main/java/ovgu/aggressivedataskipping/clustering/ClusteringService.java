package ovgu.aggressivedataskipping.clustering;

import com.google.common.collect.Lists;
import com.google.common.collect.Streams;
import org.apache.commons.math3.util.Pair;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ClusteringService {

    private Map<List<Boolean>, List<Boolean>> vectorWithUnionVector;

    private Map<List<Boolean>, Integer> currentPartitions;

    private Map<Integer, Integer> featuresCost;

    public ClusteringService(Map<List<Boolean>, List<Boolean>> vectorWithUnionVector, Map<List<Boolean>, Integer> currentPartitions) {
        this.vectorWithUnionVector = vectorWithUnionVector;
        this.currentPartitions = currentPartitions;
    }

    public Map<List<Boolean>, List<Boolean>> getVectorWithUnionVector() {
        return vectorWithUnionVector;
    }

    public Map<List<Boolean>, Integer> getCurrentPartitions() {
        return currentPartitions;
    }

    private List<CostMatrixCell> computeCostMatrix() {
        List<CostMatrixCell> cells = new ArrayList<>();
//        for(int i=0; i<currentPartitions.size(); i++) {
//            for(int j=i; j<currentPartitions.size(); j++) {
//                cells.add(new CostMatrixCell(currentPartitions[i], currentPartitions[j]))
//            }
//        }
        // itertate over current partitions and create costmatrixcells for each (vector, vector) tuple
        throw new NotImplementedException();
    }

    private CostMatrixCell selectPartitionsForMerge(List<CostMatrixCell> cells) {
        return cells.stream().min(Comparator.comparing(CostMatrixCell::getCost)).get();
    }

    private void mergePartitions() {
        // 1: get partitions to merge
        // 2: compute union vector for partions
        // 3: search for vectors (values) in vectorWithUnionVector and update the union vectors (value)
//        CostMatrixCell costMatrixCell = new CostMatrixCell();
//        int firstSize = currentPartitions.get(costMatrixCell.getFirstVector());
//        int secondSize = currentPartitions.get(costMatrixCell.getSecondVector());
        // 4: compute size of new partition: firstSize + secondSize
        // 5: remove the old partitions:
            //currentPartitions.remove(cell.getFirstVector());
        // 6: check if new size < minSize:
        //              add new partition to current partitions
        // stopping condition: if current partions size < 2
    }

    private List<Boolean> computeUnionVector(List<Boolean> firstPartition, List<Boolean> secondPartition) {
        return Streams.zip(firstPartition.stream(), secondPartition.stream(), (a,b) -> a || b).collect(Collectors.toList());
    }


}
