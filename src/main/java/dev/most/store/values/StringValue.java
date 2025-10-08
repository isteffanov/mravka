package dev.most.store.values;

public final class StringValue implements StoreValue {

    private String payload;

    private StringValue(String payload) {
        this.payload = payload;
    }

    public static StringValue of(String payload) {
        return new StringValue(payload);
    }

    @Override
    public ValueType getValueType() {
        return ValueType.STRING;
    }
}
