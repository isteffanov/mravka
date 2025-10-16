package dev.most.transducer.transition.state_transition;

import dev.most.transducer.transition.State;
import dev.most.transducer.transition.delta.TrailStep;
import dev.most.transducer.transition.label_transition.LabelTransition;

import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class MapBackedStateTransition
        <MapType extends Map<State, LT>, LT extends LabelTransition<T>, T>
        implements StateTransition<LT, T> {

    private final MapType contents;
    private final Supplier<LT> labelTransitionSupplier;

    public MapBackedStateTransition(Supplier<MapType> supplier, Supplier<LT> nestedSupplier) {
        this.contents = supplier.get();
        this.labelTransitionSupplier = nestedSupplier;
    }

    @Override
    public Optional<LabelTransition<T>> get(State state) {
        return Optional.ofNullable(contents.get(state));
    }

    @Override
    public LabelTransition<T> getOrCreate(State state) {
        if (!contents.containsKey(state)) {
            return contents.put(state, labelTransitionSupplier.get());
        }

        return contents.get(state);
    }

    @Override
    public void delete(State state) {
        contents.remove(state);
    }

    @Override
    public int size() {
        return contents.size();
    }

    @Override
    public Stream<State> streamStates() {
        return contents.keySet().stream();
    }

    @Override
    public Stream<TrailStep> streamTransitions() {
        return contents.
    }
}
