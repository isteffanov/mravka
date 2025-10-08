package dev.most.store.values;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public record ListValue (LinkedList<String> payload) implements StoreValue {
    @Override
    public ValueType getValueType() {
        return ValueType.LIST;
    }

    public void prepend(Collection<String> values) {
        values.forEach(payload::addFirst);
    }

    public void append(Collection<String> values) {
        values.forEach(payload::addLast);
    }
}
