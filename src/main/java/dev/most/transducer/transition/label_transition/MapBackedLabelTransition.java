package dev.most.transducer.transition.label_transition;

import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class MapBackedLabelTransition
        <MapType extends Map<Character, T>, T>
        implements LabelTransition<T> {

    private final MapType contents;
    private final Supplier<T> nestedSupplier;

    public MapBackedLabelTransition(Supplier<MapType> supplier, Supplier<T> nestedSupplier) {
        this.contents = supplier.get();
        this.nestedSupplier = nestedSupplier;
    }

    @Override
    public Optional<T> get(Character label) {
        return Optional.ofNullable(contents.get(label));
    }

    @Override
    public Stream<Character> streamLabels() {
        return contents.keySet().stream();
    }

    @Override
    public void forEachLabel(Consumer<Character> consumer) {
        contents.keySet().forEach(consumer);
    }

    @Override
    public T getOrCreate(Character label) {
        if (!contents.containsKey(label)) {
            return contents.put(label, nestedSupplier.get());
        }

        return contents.get(label);
    }
}
