package dev.most.common;

import java.util.Map;

public record TransducerEntry(String input, String output) implements Map.Entry<String, String> {

    public static TransducerEntry of(String input, String output) {
        return new TransducerEntry(input, output);
    }

    @Override
    public String getKey() {
        return input;
    }

    @Override
    public String getValue() {
        return output;
    }

    @Override
    public String setValue(String value) {
        throw new RuntimeException("Transducer Map's Entries are immutable");
    }
}
