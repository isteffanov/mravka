package dev.most.store.values;

public record IntegerValue(Integer payload) implements StoreValue {

    public static IntegerValue of(Integer integer) {
        return new IntegerValue(integer);
    }

    public IntegerValue increment() {
        return new IntegerValue(payload + 1);
    }

    @Override
    public ValueType getValueType() {
        return ValueType.INTEGER;
    }
}
