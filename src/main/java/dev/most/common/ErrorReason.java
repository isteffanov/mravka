package dev.most.common;

public enum ErrorReason {
    NO_KEY("No key"),
    WRONG_TYPE("Wrong type"),

    NO_ERROR("No error");


    public final String message;

    ErrorReason(String message) {
        this.message = message;
    }

}
