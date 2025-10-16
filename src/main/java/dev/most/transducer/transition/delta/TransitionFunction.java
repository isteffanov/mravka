package dev.most.transducer.transition.delta;

import dev.most.transducer.transition.State;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

public interface TransitionFunction {

    State getDestination(final State state, final Character label);

    String getOutput(final State state, final Character label);

    Stream<TrailStep> traverse(final State from, final String through);

    int findExistingPrefixLengthCovered(final State from, final String through);

    List<State> findPathThroughKey(final State from, final String through);

    void prependOutput(State state, Character label, String prefix);

    void set(State state, Character label, State destination, String output);

    void setOutput(State state, Character label, String output);

    default void clearOutput(State state, Character label) {
        setOutput(state, label, "");
    }

    void forEachLabel(State state, Consumer<Character> consumer);

    void forEachStepInPathReversedExcludingLast(State from, String through, int exclude, Consumer<TrailStep> consumer);

    void delete(State state);
}
