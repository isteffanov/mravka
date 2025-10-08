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

    public Result set(String key, String value) {
        store.set(key, StringValue.of(value));

        return Result.success(key);
    }

    public Result get(String key) {
        return store.get(key)
                .map(Result::success)
                .orElse(Result.failure("Could not find key."));
    }

    public Result del(String key) {
        store.del(key);

        return Result.success(key);
    }

    public Result type(String key) {
        return store.type(key)
                .map(Result::success)
                .orElse(Result.failure("Could not find key."));
    }

    // Integer interface

    @Override
    public Result set(String key, Integer value) {
        store.set(key, IntegerValue.of(value));

        return Result.success(key);
    }

    @Override
    public Result increment(String key) {
        Optional<StoreValue> value = store.get(key);
        if (value.isEmpty()) {
            return Result.failure("No key");
        }

        if (!(value.get() instanceof IntegerValue integerValue)) {
            return Result.failure("Not integer");
        }

        return Result.success(integerValue.increment());
    }

    // List interface

    @Override
    public Result prepend(String key, String... values) {
        Optional<StoreValue> value = store.get(key);

        if (value.isEmpty()) {
            return Result.failure("No key");
        }

        if (!(value.get() instanceof ListValue listValue)) {
            return Result.failure("Not integer");
        }

        listValue.prepend(List.of(values));
        return Result.success(key);
    }

    @Override
    public Result append(String key, String... values) {
        Optional<StoreValue> value = store.get(key);

        if (value.isEmpty()) {
            return Result.failure("No key");
        }

        if (!(value.get() instanceof ListValue listValue)) {
            return Result.failure("Not integer");
        }

        listValue.append(List.of(values));
        return Result.success(key);
    }
}
