package ovgu.aggressivedataskipping.featurization;

import com.google.common.collect.Sets;

import java.util.*;
import java.util.stream.Collectors;

public class AprioriMiner<T> {

    private HashMap<T, Integer> itemsWithCounts;

    private Set<Set<T>> transactions;

    private int stopEarlyLimit;

    private int support;

    public AprioriMiner(Set<Set<T>> transactions, int support, int stopEarlyLimit) {
        this.transactions = transactions;
        this.support = support;
        this.stopEarlyLimit = stopEarlyLimit;
        itemsWithCounts = new HashMap<>();
        transactions.forEach(t -> {
            t.forEach(i -> {
                itemsWithCounts.computeIfPresent(i, (k, v) -> v + 1);
                itemsWithCounts.computeIfAbsent(i, k -> 1);
            });
        });
    }

    private boolean isFrequent(Set<T> itemSet) {
            int itemCount = 0;
            for (Set<T> transaction : transactions) {
                if (transaction.containsAll(itemSet)) itemCount++;
            }
        return itemCount >= support;
    }

    private Set<T> combination(Set<T> firstSet, Set<T> secondSet) {
        Set<T> result = new HashSet<>();
        result.addAll(firstSet);
        result.addAll(secondSet);
        return result;
    }

    private Set<Set<T>> combinations(Set<Set<T>> itemSets) {
        Set<Set<T>> result = new HashSet<>();
        for(Set<T> first: itemSets) {
            for(Set<T> second : itemSets) {
                Set<T> combination = combination(first, second);
                if(combination.size() == first.size() + 1) {
                    result.add(combination);
                }
            }
        }
        return result;
    }

    private int getItemSetFrequency(Set<T> itemSet) {
        int itemCount = 0;
        for (Set<T> transaction : transactions) {
            if (transaction.containsAll(itemSet)) itemCount++;
        }
        return itemCount;
    }

    public Map<Set<T>, Integer> getFrequentItemSets() {
        Map<Set<T>, Integer> finalItemSets = new HashMap<>();
        int k = 2;
        Set<T> items = itemsWithCounts.keySet()
                .stream().filter(a -> itemsWithCounts.get(a) >= support)
                .collect(Collectors.toSet());
        Set<Set<T>> itemSets = items.stream().map(i -> {
            Set<T> set = new HashSet<>();
            set.add(i);
            return set;
        }).collect(Collectors.toSet());
        itemSets.forEach(s -> finalItemSets.put(s, getItemSetFrequency(s)));
        while (true) {
            if (itemSets.isEmpty()) break;
            itemSets = combinations(itemSets);
            itemSets = itemSets.stream().filter(this::isFrequent).collect(Collectors.toSet());
            if (itemSets.isEmpty()) break;
            itemSets.forEach(s -> finalItemSets.put(s, getItemSetFrequency(s)));
            if (k == stopEarlyLimit) break;
            k++;
        }
        return finalItemSets;
    }

}
