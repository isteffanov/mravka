package dev.most.transducer;

import dev.most.common.Dictionary;
import dev.most.common.TransducerEntry;
import dev.most.common.merkle.MerkleTree;
import dev.most.transducer.transition.State;
import dev.most.transducer.transition.delta.DeltaFunction;
import dev.most.transducer.transition.delta.TrailStep;
import dev.most.transducer.transition.delta.TransitionStrategy;

import java.util.*;
import java.util.stream.Stream;

public class TransducerStructure implements TransducerMap {

    private final State startState;

    private final DeltaFunction delta;
    private final Map<StateSignature, State> cachedSignatures;

    private String prefixOutput;
    private String minimumExceptFor;

    private TransducerStructure(
            State startState,
            DeltaFunction delta,
            String prefixOutput,
            Map<StateSignature, State> cachedSignatures,
            String minimumExceptFor) {

        this.startState = startState;
        this.delta = delta;
        this.prefixOutput = prefixOutput;
        this.cachedSignatures = cachedSignatures;
        this.minimumExceptFor = minimumExceptFor;
    }

    // Transducer Interface

    public static TransducerStructure createEmpty() {
        State startState = new State();

        DeltaFunction transitionFunction = DeltaFunction.create(TransitionStrategy.HASH_MAP_STRATEGY, TransitionStrategy.HASH_MAP_STRATEGY);
        Map<StateSignature, State> cachedSignatures = new HashMap<>();

        String prefixOutput = "";
        String minimumExceptFor = "";

        return new TransducerStructure(startState, transitionFunction, prefixOutput, cachedSignatures, minimumExceptFor);
    }

    public static TransducerStructure createFromOrderedDictionary(final Dictionary dictionary) {
        TransducerStructure transducerMap = createEmpty();

        TransducerEntry head = dictionary.head();
        transducerMap.addInitial(head);

        dictionary.tail().forEach(transducerMap::addInOrder);

        transducerMap.reduceExceptForEmptyString();
        return transducerMap;
    }

    @Override
    public int getNumberOfStates() {
        return delta.numberOfStates();
    }

    @Override
    public int getNumberOfFinalStates() {
        return delta.numberOfFinalStates();
    }

    @Override
    public int getNumberOfTransitions() {
        return delta.numberOfTransitions();
    }

    @Override
    public String getPrefixOutput() {
        return prefixOutput;
    }

    @Override
    public int getDegreeOfSpread() {
        return delta.degree();
    }

    // Map Interface

    @Override
    public int size() {
        return getNumberOfStates();
    }

    @Override
    public boolean isEmpty() {
        return delta.degree() == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        if (key instanceof String input) {
            return delta.findExistingPrefixLengthCovered(startState, input) == input.length()
                    && delta.findLastStateInPrefixCovered(startState, input).isFinal();
        }

        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        throw new UnsupportedOperationException("not implemented yet.");
    }

    @Override
    public String get(Object key) {
        if (containsKey(key)
                && key instanceof String input) {
            return prefixOutput + delta.traverse(startState, input)
                    .map(TrailStep::output)
                    .reduce("", String::concat);
        }

        return "";
    }

    @Override
    public String put(String key, String value) {
        throw new UnsupportedOperationException("not implemented yet.");
    }

    @Override
    public String remove(Object key) {
       throw new UnsupportedOperationException("not implemented yet.");
    }

    @Override
    public void putAll(Map<? extends String, ? extends String> m) {
        m.forEach(this::put);
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("not implemented.");
    }

    @Override
    public Set<String> keySet() {
        throw new UnsupportedOperationException("not implemented yet.");
    }

    @Override
    public Collection<String> values() {
        throw new UnsupportedOperationException("not implemented yet.");
    }

    public Set<Entry<String, String>> entrySet() {
        throw new UnsupportedOperationException("not implemented yet.");
    }

    // Construction of Structure

    private void addInitial(final TransducerEntry entry) {
        addInitial(entry.input(), entry.output());
    }

    private void addInitial(String input, String output) {
        List<State> states = State.create(input.length());
        states.getLast().setFinal(true);

        delta.set(startState, input.charAt(0), states.getFirst(), "");
        for (int i = 0; i < input.length() - 1; ++i) {
            delta.set(states.get(i), input.charAt(i + 1), states.get(i + 1), "");
        }

        prefixOutput = output;
        minimumExceptFor = input;
    }

    private void addInOrder(final TransducerEntry entry) {
        addInOrder(entry.input(), entry.output());
    }

    private void addInOrder(String input, String output) {
        int existingInputPrefixLengthCovered = delta.findExistingPrefixLengthCovered(startState, input);
        reduceMinimumExceptForTo(existingInputPrefixLengthCovered);

        List<State> newPath = buildNewPath(startState, input, existingInputPrefixLengthCovered);

        for (int i = existingInputPrefixLengthCovered; i < input.length(); ++i) {
            delta.set(newPath.get(i), input.charAt(i), newPath.get(i+1), "");
        }

        pushOutputForward(newPath, input, output, existingInputPrefixLengthCovered);
        prefixOutput = commonPrefix(prefixOutput, output);
        minimumExceptFor = input;
    }

    private List<State> buildNewPath(State startState, String input, int existingInputPrefixLengthCovered) {
        State lastState = delta.findLastStateInPrefixCovered(startState, input);
        List<State> newStates = State.create(input.length() - existingInputPrefixLengthCovered);

        List<State> newPath = new ArrayList<>();
        newPath.add(lastState);
        newPath.addAll(newStates);

        newStates.getLast().setFinal(true);

        return newPath;
    }

    private void pushOutputForward(List<State> path, String input, String output, int existingPathToInputLengthCovered) {
        String c = commonPrefix(prefixOutput, output);
        String l = remainderSuffix(c, prefixOutput);
        String b = remainderSuffix(c, output);

        State firstStateInPath = path.getFirst();
        delta.forEachLabel(firstStateInPath, label -> {
            if (label == input.charAt(0)) {
                return;
            }

            delta.prependOutput(firstStateInPath, label, l);
        });
        firstStateInPath.prependOutputIfFinal(l);


        for (int i = 0; i < existingPathToInputLengthCovered; ++i) {


            String outputOnCurrentStep = delta.getOutput(path.get(i), input.charAt(i));
            c = commonPrefix(l + outputOnCurrentStep, b);
            l = remainderSuffix(c, l + outputOnCurrentStep);
            b = remainderSuffix(c, b);

            if (i + 1 >= path.size()) {
                break;
            }

            String finalL2 = l;
            delta.forEachLabel(path.get(i + 1), label -> {
                if (label == input.charAt(i + 1)) {
                    return;
                }

                delta.prependOutput(path.get(i + 1), label, finalL2);
            });
            path.get(i + 1).prependOutputIfFinal(l);

            delta.setOutput(path.get(i), input.charAt(i), c);
        }

        if (input.length() > existingPathToInputLengthCovered) {
            delta.setOutput(path.get(existingPathToInputLengthCovered), input.charAt(existingPathToInputLengthCovered), b);
            for (int i = existingPathToInputLengthCovered + 1; i < input.length(); ++i) {
                delta.clearOutput(path.get(i), input.charAt(i));
            }

            path.getLast().clearOutputIfFinal();
        }
        else {
            State lastStateInPath = path.getLast();

            lastStateInPath.setOutputIfFinal(b);
            String finalL1 = l;
            delta.forEachLabel(lastStateInPath, label -> {
                delta.prependOutput(lastStateInPath, label, finalL1);
            });
        }
    }

    private void reduceMinimumExceptForTo(int existingLengthCovered) {
        List<TrailStep> transitionsReversed = delta.traverseFullyReversed(startState, minimumExceptFor, existingLengthCovered);

        transitionsReversed.forEach(trailStep -> {
            State state = trailStep.to();

            State prev = trailStep.from();
            Character label = trailStep.label();
            String output = trailStep.output();

            StateSignature signature = calculateStateSignature(state);

            if (!cachedSignatures.containsKey(signature)) {
                cachedSignatures.put(signature, state);
                return;
            }

            delta.delete(state);

            delta.set(prev, label, cachedSignatures.get(signature), output);
        });

        this.minimumExceptFor = this.minimumExceptFor.substring(0, existingLengthCovered);
    }

    private void reduceExceptForEmptyString() {
        reduceMinimumExceptForTo(0);
    }

    private StateSignature calculateStateSignature(State state) {
        boolean isFinal = state.isFinal();
        String output = state.getOutput();

        MerkleTree.Builder<StateOutgoingMeta> outgoingBuilder = new MerkleTree.Builder<>();

        delta.forEachLabel(state, label -> {
            outgoingBuilder.add(new StateOutgoingMeta(label, delta.getDestination(state, label), delta.getOutput(state, label)));
        });

        String outgoing = outgoingBuilder.build().value;

        return new StateSignature(isFinal, output, outgoing);
    }

    private record StateSignature(boolean isFinal, String output, String outgoingHash) {}
    private record StateOutgoingMeta(Character transitionLabel, State transitionDestination, String transitionOutput) implements Comparable<StateOutgoingMeta> {

        @Override
        public int compareTo(StateOutgoingMeta other) {
            return Comparator.comparing(StateOutgoingMeta::transitionLabel)
                    .thenComparing(StateOutgoingMeta::transitionDestination)
                    .thenComparing(StateOutgoingMeta::transitionOutput)
                    .compare(this, other);
        }
    }

    private record OutputSplit(String c, String l, String b) {

        public OutputSplit() {
            this("", "", "");
        }

        public OutputSplit(OutputSplit other, String outputOnCurrentStep) {
            this(commonPrefix(other.l() + outputOnCurrentStep, other.b()),
                    remainderSuffix(other.c(), other.l() + outputOnCurrentStep),
                    remainderSuffix(other.c(), other.b()));
        }
    }

    private static String commonPrefix(String thiz, String that) {
        int minLength = Math.min(thiz.length(), that.length());

        return Stream.iterate(0, i -> i + 1)
                .limit(minLength)
                .dropWhile(i -> thiz.charAt(i) == that.charAt(i))
                .findFirst()
                .map(i -> thiz.substring(0, i))
                .orElse(thiz.substring(0, minLength));
    }

    private static String remainderSuffix(String thiz, String that) {
        assert thiz.length() <= that.length();
        return that.substring(thiz.length());
    }
}
