package dev.most.transducer.transition.delta;

import dev.most.transducer.transition.State;

public class TransitionEnd {

    private /* final */ State destination;
    private /* final */ String output;

    public TransitionEnd() {
        this.destination = State.NOWHERE;
        this.output = "";
    }

    public TransitionEnd(State destination, String output) {
        this.destination = destination;
        this.output = output;
    }

    public State getDestination() {
        return destination;
    }

    public String getOutput() {
        return output;
    }

    public void prependOutput(String prefix) {
        this.output = prefix.concat(this.output);
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public void set(State destination, String output) {
        this.destination = destination;
        this.output = output;
    }
}
