package dev.most.store.operations;

import dev.most.common.result.Result;

public interface ListOperations {

    Result prepend(String key, String... values);

    Result append(String key, String... values);
}
