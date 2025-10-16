package dev.most.common;

import java.util.Arrays;
import java.util.List;

public record Dictionary(TransducerEntry head, List<TransducerEntry> tail) {

    private Dictionary() {
        this(null);
    }

    private Dictionary(TransducerEntry head) {
        this(head, List.of());
    }

    public static Dictionary of(String key, String value) {
        return new Dictionary(TransducerEntry.of(key, value));
    }

    public static Dictionary of(TransducerEntry entry) {
        return new Dictionary(entry);
    }

    public static Dictionary of(TransducerEntry... entries) {
        if (entries.length == 0) {
            return new Dictionary();
        }

        return new Dictionary(entries[0], Arrays.stream(entries).skip(1).toList());
    }

    public boolean isEmpty() {
        return head != null;
    }
}
