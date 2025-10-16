package dev.most.transducer;

import dev.most.common.TransducerEntry;

import java.util.Map;

public interface TransducerMap extends Map<String, String> {

    int getNumberOfStates();
    int getNumberOfFinalStates();
    int getNumberOfTransitions();

    String getPrefixOutput();
    int getDegreeOfSpread();
}
