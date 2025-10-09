package dev.most.store;

import dev.most.common.Result;
import dev.most.store.operations.IntegerOperations;
import dev.most.store.operations.ListOperations;
import dev.most.store.values.IntegerValue;
import dev.most.store.values.ListValue;
import dev.most.store.values.StoreValue;
import dev.most.store.values.StringValue;

import java.util.List;
import java.util.Optional;

public class StoreManager implements IntegerOperations, ListOperations {

    private final Store store;

    public StoreManager() {
        this.store = HashMapBackedStore.create();
    }

    public Result<?> set(String key, String value) {
        store.set(key, StringValue.of(value));

        return Result.ok(key);
    }

    public Result<?> get(String key) {
        return store.get(key)
                .map(Result::ok)
                .orElse(Result.error(new IllegalArgumentException("No such key")));
    }

    public Result<?> del(String key) {
        store.del(key);

        return Result.ok(key);
    }

    public Result<?> type(String key) {
        return store.type(key)
                .map(Result::ok)
                .orElse(Result.error(new IllegalArgumentException("No such key")));
    }

    // Integer interface

    @Override
    public Result<?> set(String key, Integer value) {
        store.set(key, IntegerValue.of(value));

        return Result.ok(key);
    }

    @Override
    public Result<?> increment(String key) {
        Optional<StoreValue> value = store.get(key);
        if (value.isEmpty()) {
            return Result.error(new IllegalArgumentException("No such key"));
        }

        if (!(value.get() instanceof IntegerValue integerValue)) {
            return Result.error(new UnsupportedOperationException("Not an integer"));
        }

        return Result.ok(integerValue.increment());
    }

    // List interface

    @Override
    public Result<?> prepend(String key, String... values) {
        Optional<StoreValue> value = store.get(key);

        if (value.isEmpty()) {
            return Result.error(new IllegalArgumentException("No such key"));
        }

        if (!(value.get() instanceof ListValue listValue)) {
            return Result.error(new UnsupportedOperationException("Not an integer"));
        }

        listValue.prepend(List.of(values));
        return Result.ok(key);
    }

    @Override
    public Result<?> append(String key, String... values) {
        Optional<StoreValue> value = store.get(key);

        if (value.isEmpty()) {
            return Result.error(new IllegalArgumentException("No such key"));
        }

        if (!(value.get() instanceof ListValue listValue)) {
            return Result.error(new UnsupportedOperationException("Not an integer"));
        }

        listValue.append(List.of(values));
        return Result.ok(key);
    }
}
