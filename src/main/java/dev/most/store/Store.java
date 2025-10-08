package dev.most.store;

import dev.most.store.values.StoreValue;
import dev.most.store.values.ValueType;

import java.util.Optional;

interface Store {

    Optional<StoreValue> get(String key);

    void set(String key, StoreValue value);

    void del(String key);

    Optional<ValueType> type(String key);
}
