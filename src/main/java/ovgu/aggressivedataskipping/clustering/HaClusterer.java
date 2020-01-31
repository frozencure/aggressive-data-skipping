package ovgu.aggressivedataskipping.clustering;

import com.google.common.collect.Streams;

import java.util.*;
import java.util.stream.Collectors;

public class HaClusterer {

    private Map<List<Boolean>, List<Boolean>> vectorsWithBlockingVector;

    private Map<List<Boolean>, Long> currentPartitions;

    private Map<Integer, Integer> featuresCosts;

    private Integer minimumParitionSize;

    public HaClusterer(Map<List<Boolean>, Long> vectorsWithCounts, List<Integer> featureCosts, int minimumParitionSize) {
        this.vectorsWithBlockingVector = new HashMap<>();
        vectorsWithCounts.keySet().forEach(v -> vectorsWithBlockingVector.put(v,v));
        this.currentPartitions = new HashMap<>();
        vectorsWithCounts.keySet().stream()
                .filter(k -> vectorsWithCounts.get(k) <= minimumParitionSize)
                .forEach(v -> currentPartitions.put(v, vectorsWithCounts.get(v)));
        this.minimumParitionSize = minimumParitionSize;
        this.featuresCosts = new HashMap<>();
        for (int i = 0; i < featureCosts.size(); i++) {
            this.featuresCosts.put(i, featureCosts.get(i));
        }
    }

    public Map<List<Boolean>, List<Boolean>> getVectorsWithBlockingVector() {
        return vectorsWithBlockingVector;
    }

    public Map<List<Boolean>, Long> getCurrentPartitions() {
        return currentPartitions;
    }

    private Integer computeCost(List<Boolean> vector) {
        int totalCost = 0;
        for(int i=0; i<vector.size(); i++) {
            boolean bit = vector.get(i);
            if(!bit) {
                totalCost += featuresCosts.get(i);
            }
        }
        return totalCost;
    }

    private Long computePartitionSize(CostMatrixCell cell) {
        long firstPartitionSize = currentPartitions.get(cell.getFirstVector());
        long secondPartitionSize = currentPartitions.get(cell.getSecondVector());
        return (firstPartitionSize + secondPartitionSize);
    }

    private Long computePartitionCost(CostMatrixCell cell) {
        List<Boolean> unionVector = computeUnionVector(cell.getFirstVector(), cell.getSecondVector());
        return computePartitionSize(cell) * computeCost(unionVector);
    }

    private List<CostMatrixCell> computeCostMatrix() {
        List<List<Boolean>> currentPartitionsSet = new ArrayList<>(currentPartitions.keySet());
        List<CostMatrixCell> cells = new ArrayList<>();
        for(int i=0; i<currentPartitions.size(); i++) {
            for(int j=i+1; j<currentPartitions.size(); j++) {
                CostMatrixCell cell = new CostMatrixCell(currentPartitionsSet.get(i), currentPartitionsSet.get(j));
                cell.setCost(computePartitionCost(cell));
                cells.add(cell);
            }
        }
        return cells;
    }

    private CostMatrixCell selectPartitionsForMerge(List<CostMatrixCell> cells) {
        return cells.stream().max(Comparator.comparing(CostMatrixCell::getCost)).get();
    }

    public void mergePartitions() {
        while(currentPartitions.size() > 1) {
            System.out.println(currentPartitions.toString());
            List<CostMatrixCell> cells = computeCostMatrix();
            CostMatrixCell partitionsToMerge = selectPartitionsForMerge(cells);
            long partitionSize = computePartitionSize(partitionsToMerge);
            // 1: get partitions to merge
            // 2: compute union vector for partions
            // 3: search for vectors (values) in vectorWithUnionVector and update the union vectors (value)
//        CostMatrixCell costMatrixCell = new CostMatrixCell();
//        int firstSize = currentPartitions.get(costMatrixCell.getFirstVector());
//        int secondSize = currentPartitions.get(costMatrixCell.getSecondVector());
            // 4: compute size of new partition: firstSize + secondSize
            // 5: remove the old partitions:
            List<Boolean> unionVector = computeUnionVector(partitionsToMerge.getFirstVector(), partitionsToMerge.getSecondVector());
            currentPartitions.remove(partitionsToMerge.getFirstVector());
            currentPartitions.remove(partitionsToMerge.getSecondVector());
//            vectorsWithBlockingVector
//                    .put(partitionsToMerge.getFirstVector(), unionVector);
//            vectorsWithBlockingVector
//                    .put(partitionsToMerge.getSecondVector(), unionVector);
            if (partitionSize <= minimumParitionSize) {
//                vectorsWithBlockingVector
//                        .put(partitionsToMerge.getFirstVector(), unionVector);
//                vectorsWithBlockingVector
//                        .put(partitionsToMerge.getSecondVector(), unionVector);
                updateBlockingVectors(partitionsToMerge.getFirstVector(), unionVector);
                updateBlockingVectors(partitionsToMerge.getSecondVector(), unionVector);
                currentPartitions.putIfAbsent(unionVector, partitionSize);
                currentPartitions.computeIfPresent(unionVector, (k,v) -> {
                    if(k.equals(partitionsToMerge.getFirstVector()) || k.equals(partitionsToMerge.getSecondVector())) {
                        return v;
                    } else {
                        return v + partitionSize;
                    }
                });
            }
//            System.out.println(currentPartitions.toString());
            // 6: check if new size < minSize:
            //              add new partition to current partitions
            // stopping condition: if current partions size < 2
        }
    }

    private void updateBlockingVectors(List<Boolean> oldUnionVector, List<Boolean> newUnionVector) {
        for(List<Boolean> vector : vectorsWithBlockingVector.keySet()) {
            if(vectorsWithBlockingVector.get(vector).equals(oldUnionVector)) {
                vectorsWithBlockingVector.put(vector, newUnionVector);
            }
        }
    }

    private List<Boolean> computeUnionVector(List<Boolean> firstPartition, List<Boolean> secondPartition) {
        return Streams.zip(firstPartition.stream(), secondPartition.stream(), (a,b) -> a || b).collect(Collectors.toList());
    }


}
