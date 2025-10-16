package dev.most.store;

import dev.most.store.values.StoreValue;
import dev.most.store.values.ValueType;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class TransducerBackedStore implements Store {

    @Override
    public Optional<StoreValue> get(String key) {
        return Optional.empty();
    }

    @Override
    public void set(String key, StoreValue value) {

    }

    @Override
    public void del(String key) {

    }

    @Override
    public Optional<ValueType> type(String key) {
        return Optional.empty();
    }
}
