package dev.most.transducer.transition;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Stream;

public class State implements Comparable<State> {

    private final int stateId;
    private boolean isFinalState;
    private String output;
    private int numberOfIncomingTransitions;

    private boolean markedAsDeleted;

//    private static final Stream<Integer> ID_GENERATOR = Stream.iterate(0, i -> i + 1);
    private static /*final*/ int ID_COUNTER = 0;
    private static final PriorityQueue<Integer> DELETED_IDS = new PriorityQueue<>();

    public static final State NOWHERE = new State(-1);

    public State() {
        this.stateId = DELETED_IDS.isEmpty()
                ? ID_COUNTER++
                : DELETED_IDS.remove();

        this.isFinalState = false;
        this.output = "";
        this.numberOfIncomingTransitions = 0;
        this.markedAsDeleted = false;
    }

    private State(int stateId) {
        this(stateId, false, "", 0, false);
    }

    private State(int stateId, boolean isFinalState, String output, int numberOfIncomingTransitions, boolean markedAsDeleted) {
        this.stateId = stateId;
        this.isFinalState = isFinalState;
        this.output = output;

        this.numberOfIncomingTransitions = numberOfIncomingTransitions;
        this.markedAsDeleted = false;
    }

    public static List<State> create(int numberOfNewStates) {
        return Stream.generate(State::new)
                .limit(numberOfNewStates)
                .toList();
    }

    public void delete() {
        DELETED_IDS.add(this.stateId);
        this.markedAsDeleted = true;
    }

    public int index() {
        return stateId;
    }

    public boolean isFinal() {
        return isFinalState;
    }

    public String getOutput() {
        return output;
    }

    public void setFinal(boolean isFinalState) {
        this.isFinalState = isFinalState;
    }

    public void setOutputIfFinal(String output) {
        this.output = output;
    }

    @Override
    public int compareTo(State other) {
        return Comparator.comparing(State::index).compare(this, other);
    }

    public void prependOutputIfFinal(String prefix) {
        if (isFinalState) {
            this.output = prefix + this.output;
        }
    }

    public void clearOutputIfFinal() {
        if (isFinalState) {
            this.output = "";
        }
    }

    public boolean isNowhere() {
        return this.stateId == -1;
    }
}
