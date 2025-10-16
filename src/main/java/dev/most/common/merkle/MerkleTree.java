package dev.most.common.merkle;

import java.util.*;

public class MerkleTree<T> {

    public final String value;

    private MerkleTree(Set<T> items) {
        this.value = calculateHashValue(items);
    }

    private String calculateHashValue(Set<T> items) {
        return calculateHashValueForHashedElements(hashEveryElements(items));
    }

    private String calculateHashValueForHashedElements(List<String> items) {
        List<String> parentHashList = new ArrayList<>();

        if (items.isEmpty()) {
            return "";
        }

        if (items.size() == 1){
            return items.getFirst();
        }

        for (int i = 0; i < items.size(); i += 2) {
            String hashedString = hashTwoStrings(items.get(i), items.get(i+1));
            parentHashList.add(hashedString);
        }


        if (items.size() % 2 == 1) {
            String lastHash = items.getLast();
            String hashedString = hashTwoStrings(lastHash, lastHash);
            parentHashList.add(hashedString);
        }
        return calculateHashValueForHashedElements(parentHashList);
    }

    private List<String> hashEveryElements(final Set<T> items) {
        return items.stream()
                .map(Objects::hashCode)
                .map(String::valueOf)
                .toList();
    }

    private String hashTwoStrings(String first, String other) {
        return String.valueOf(first.concat(other).hashCode());
    }

    public static class Builder<T extends Comparable<T>> {

        private final Set<T> items;

        public Builder() {
            this.items = new TreeSet<>();
        }

        public Builder<T> add(final T item) {
            this.items.add(item);
            return this;
        }

        public MerkleTree<T> build() {
            return new MerkleTree<>(items);
        }

    }
}
