package dev.most.common;

import dev.most.dao.StoreDao;

public record Result (ResultType type, Object payload, String message) {

    private static final String DEFAULT_MESSAGE = "No information provided";

    public static Result success(Object payload) {
        return new Result(ResultType.SUCCESS, payload, DEFAULT_MESSAGE);
    }

    public static Result failure(String reason) {
        return new Result(ResultType.FAILURE, null, reason);
    }

    private enum ResultType {
        SUCCESS,
        FAILURE
    }
}
