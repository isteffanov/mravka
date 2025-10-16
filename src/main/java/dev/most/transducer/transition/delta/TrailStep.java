package dev.most.transducer.transition.delta;

import dev.most.transducer.transition.State;

public record TrailStep(State from, Character label, State to, String output) { }
