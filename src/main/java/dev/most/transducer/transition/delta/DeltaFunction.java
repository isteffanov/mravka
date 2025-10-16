package dev.most.transducer.transition.delta;

import dev.most.transducer.transition.State;
import dev.most.transducer.transition.label_transition.LabelTransition;
import dev.most.transducer.transition.state_transition.StateTransition;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;


public class DeltaFunction implements TransitionFunction {

    private final StateTransition<LabelTransition<TransitionEnd>, TransitionEnd> delta;

    private DeltaFunction(StateTransition<LabelTransition<TransitionEnd>, TransitionEnd> delta) {
        this.delta = delta;
    }

    public static DeltaFunction create(TransitionStrategy stateStrategy, TransitionStrategy labelStrategy) {
        StateTransition<LabelTransition<TransitionEnd>, TransitionEnd> delta = TransitionFunctionFactory.generate()
                 .stateTransitionVia(stateStrategy)
                 .labelTransitionVia(labelStrategy)
                 .compose();

        return new DeltaFunction(delta);
    }

    @Override
    public State getDestination(final State state, final Character label) {
        return getTransitionEnd(state, label)
                .map(TransitionEnd::getDestination)
                .orElse(State.NOWHERE);
    }

    @Override
    public String getOutput(final State state, final Character label) {
        return getTransitionEnd(state, label)
                .map(TransitionEnd::getOutput)
                .orElse("");
    }

    @Override
    public Stream<TrailStep> traverse(final State from, final String through) {
        Stream.Builder<TrailStep> trail = Stream.builder();

        forEachInPath(from, through, trail::add);

        return trail.build();
    }

    @Override
    public int findExistingPrefixLengthCovered(State from, String through) {
        return (int) traverse(from, through).count() - 1;
    }

    @Override
    public State findLastStateInPrefixCovered(State from, String through) {
        List<State> states = traverse(from, through).map(TrailStep::to).toList();
        if (states.isEmpty()) {
            return from;
        }

        return states.getLast();
    }

    @Override
    public void prependOutput(State state, Character label, final String prefix) {
        delta.getOrCreate(state).getOrCreate(label).prependOutput(prefix);
    }

    @Override
    public void set(State state, Character label, State destination, String output) {
        delta.getOrCreate(state).getOrCreate(label).set(destination, output);
    }

    @Override
    public void setOutput(State state, Character label, String output) {
        delta.getOrCreate(state).getOrCreate(label).setOutput(output);
    }

    @Override
    public void clearOutput(State state, Character label) {
        TransitionFunction.super.clearOutput(state, label);
    }

    @Override
    public void forEachLabel(State state, Consumer<Character> consumer) {
        delta.get(state)
                .ifPresent(labelTransition -> labelTransition.forEachLabel(consumer));
    }

    @Override
    public void forEachStepInPathReversed(State from, String through, Consumer<TrailStep> consumer) {
        traverse(from, through).toList()
                .reversed()
                .forEach(consumer);
    }

    @Override
    public void delete(State state) {
        delta.delete(state);
    }

    private Optional<TransitionEnd> getTransitionEnd(State state, Character label) {
        return delta.get(state).flatMap(transition -> transition.get(label));
    }

    private void forEachInPath(final State from, final String through, Consumer<TrailStep> consumer) {
        State state = from;
        State destination;
        String output;

        for (Character label : through.toCharArray()) {
            destination = getDestination(state, label);
            if (destination.isNowhere()) {
                break;
            }

            output = getOutput(state, label);

            consumer.accept(new TrailStep(state, label, destination, output));

            state = destination;
        }
    }

    public int numberOfStates() {
        return delta.size();
    }

    public int numberOfFinalStates() {
        return (int) delta.streamStates().filter(State::isFinal).count();
    }

    public int numberOfTransitions() {
        return (int) delta.streamStates()
                .map(delta::getOrCreate)
                .flatMap(LabelTransition::streamLabels)
                .count();
    }

    public int degree() {
        return (int) delta.streamStates()
                .map(delta::getOrCreate)
                .mapToLong(lt -> lt.streamLabels().count())
                .max()
                .orElse(0);
    }
}
