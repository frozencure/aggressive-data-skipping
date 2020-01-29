package ovgu.aggressivedataskipping.clustering;

import com.google.common.collect.Streams;
import org.apache.commons.math3.util.Pair;

import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class ClusteringMain {

    public static void main(String...args) {
        Boolean[] first = new Boolean[] {true, false, false};
        Boolean[] second = new Boolean[] {false, true, false};
        Boolean[] third = new Boolean[] {false, false, false};
        Boolean[] fourth = new Boolean[] {true, true, true};
        Boolean[] fifth = new Boolean[] {false, true, true};
//        Boolean[] sixth = new Boolean[] {true, true, false};
        Integer[] weights = new Integer[] {20, 10, 10};
        List<Boolean> firstList = Arrays.asList(first);
        List<Boolean> secondList = Arrays.asList(second);
        List<Boolean> thirdList = Arrays.asList(third);
        List<Boolean> fourthList = Arrays.asList(fourth);
        List<Boolean> fifthList = Arrays.asList(fifth);
//        List<Boolean> sixthList = Arrays.asList(sixth);
//        List<Boolean> seventhList = Arrays.asList(third);

        Map<List<Boolean>, Integer> vectors = new HashMap<>();
        List<Integer> weightsList = Arrays.asList(weights);
        vectors.put(firstList, 2);
        vectors.put(secondList, 3);
        vectors.put(thirdList, 1);
        vectors.put(fourthList, 1);
        vectors.put(fifthList, 1);
//        vectors.put(sixthList, 1);


//        List<Boolean> unionVector = vectors.stream().reduce(ClusteringMain::union).orElse(new ArrayList<>());
        ClusteringService service = new ClusteringService(vectors, weightsList, 100);
        service.mergePartitions();
        System.out.println(service.getVectorWithUnionVector());
    }

    private static List<Boolean> union(List<Boolean> first, List<Boolean> second) {
        return Streams.zip(first.stream(), second.stream(), (a,b) -> a || b).collect(Collectors.toList());
    }


}
