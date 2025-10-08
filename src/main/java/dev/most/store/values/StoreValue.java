package dev.most.store.values;

public sealed interface StoreValue
        permits StringValue, IntegerValue, ListValue {

    ValueType getValueType();
}
