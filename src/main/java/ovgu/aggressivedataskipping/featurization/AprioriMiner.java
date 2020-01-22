package ovgu.aggressivedataskipping.featurization;

import com.google.common.collect.Sets;

import java.util.*;
import java.util.stream.Collectors;

public class AprioriMiner<T> {

    private HashMap<T, Integer> itemsWithCounts;

    private Set<Set<T>> transactions;

    private int support;

    public AprioriMiner(Set<Set<T>> transactions, int support) {
        this.transactions = transactions;
        this.support = support;
        itemsWithCounts = new HashMap<>();
        transactions.forEach(t -> {
            t.forEach(i -> {
                itemsWithCounts.computeIfPresent(i, (k, v) -> v + 1);
                itemsWithCounts.computeIfAbsent(i, k -> 1);
            });
        });
    }

    private boolean isFrequent(Set<T> itemSet) {
        for (T item : itemSet) {
            int itemCount = 0;
            for (Set<T> transaction : transactions) {
                if (transaction.contains(item)) itemCount++;
            }
            if (itemCount < support) return false;
        }
        return true;
    }

    private int getItemSetFrequency(Set<T> itemSet) {
        int minItemCount = Integer.MAX_VALUE;
        for (T item : itemSet) {
            int itemCount = 0;
            for (Set<T> transaction : transactions) {
                if (transaction.contains(item)) itemCount++;
            }
            if (itemCount <= minItemCount) minItemCount = itemCount;
        }
        return minItemCount;
    }

    private Set<Set<T>> combinations(Set<T> items, int k) {
        return Sets.combinations(items, k);
    }

    public Map<Set<T>, Integer> getFrequentItemSets() {
        Map<Set<T>, Integer> finalItemSets = new HashMap<>();
        int k = 2;
        Set<T> items = itemsWithCounts.keySet()
                .stream().filter(a -> itemsWithCounts.get(a) >= support)
                .collect(Collectors.toSet());
        items.stream().map(i -> {
            Set<T> set = new HashSet<T>();
            set.add(i);
            return set;
        }).forEach(s -> finalItemSets.put(s, getItemSetFrequency(s)));
        while (true) {
            if (items.isEmpty()) break;
            Set<Set<T>> itemSets = combinations(items, k);
            itemSets = itemSets.stream().filter(this::isFrequent).collect(Collectors.toSet());
            if (itemSets.isEmpty()) break;
            itemSets.forEach(s -> finalItemSets.put(s, getItemSetFrequency(s)));
            if (k == items.size()) break;
            k++;
        }
        return finalItemSets;
    }

}
