package dev.most.store.operations;

import dev.most.common.Result;

public interface IntegerOperations {

    Result set(String key, Integer value);

    Result increment(String key);
}
