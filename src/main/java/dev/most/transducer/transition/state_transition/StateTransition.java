package dev.most.transducer.transition.state_transition;

import dev.most.transducer.transition.State;
import dev.most.transducer.transition.delta.TrailStep;
import dev.most.transducer.transition.label_transition.LabelTransition;

import java.util.Optional;
import java.util.stream.Stream;

public interface StateTransition<LT extends LabelTransition<T>, T> {

    Optional<LabelTransition<T>> get(final State state);

    LabelTransition<T> getOrCreate(State state);

    void delete(State state);

    int size();

    Stream<State> streamStates();

    Stream<TrailStep> streamTransitions();
}
