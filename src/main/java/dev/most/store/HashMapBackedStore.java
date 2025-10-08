package dev.most.store;

import dev.most.store.values.StoreValue;
import dev.most.store.values.ValueType;

import java.util.HashMap;
import java.util.Optional;

public class HashMapBackedStore implements Store {

    private final HashMap<String, StoreValue> hill;

    private HashMapBackedStore(HashMap<String, StoreValue> hill) {
        this.hill = hill;
    }

    static HashMapBackedStore create() {
        HashMap<String, StoreValue> hill = new HashMap<>();

        return new HashMapBackedStore(hill);
    }

    @Override
    public Optional<StoreValue> get(String key) {
        return Optional.ofNullable(hill.get(key));
    }

    @Override
    public void set(String key, StoreValue value) {
        hill.put(key, value);
    }

    @Override
    public void del(String key) {
        hill.remove(key);
    }

    @Override
    public Optional<ValueType> type(String key) {
        return get(key).map(StoreValue::getValueType);
    }
}
