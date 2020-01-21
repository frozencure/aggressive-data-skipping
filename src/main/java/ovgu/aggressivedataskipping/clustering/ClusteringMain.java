package ovgu.aggressivedataskipping.clustering;

import com.google.common.collect.Streams;
import org.apache.commons.math3.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class ClusteringMain {

    public static void main(String...args) {
        Boolean[] first = new Boolean[] {true, false, false};
        Boolean[] second = new Boolean[] {false, true, false};
        Boolean[] third = new Boolean[] {false, false, false};
        List<Boolean> firstList = Arrays.asList(first);
        List<Boolean> secondList = Arrays.asList(second);
        List<Boolean> thirdList = Arrays.asList(third);
        List<List<Boolean>> vectors = new ArrayList<>();
        vectors.add(firstList);
        vectors.add(secondList);
        vectors.add(thirdList);
        List<Boolean> unionVector = vectors.stream().reduce(ClusteringMain::union).orElse(new ArrayList<>());
        System.out.println(unionVector.toString());
    }

    private static List<Boolean> union(List<Boolean> first, List<Boolean> second) {
        return Streams.zip(first.stream(), second.stream(), (a,b) -> a || b).collect(Collectors.toList());
    }


}
