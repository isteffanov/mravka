package dev.most.transducer.transition.delta;

import dev.most.transducer.transition.label_transition.LabelTransition;
import dev.most.transducer.transition.label_transition.MapBackedLabelTransition;
import dev.most.transducer.transition.state_transition.MapBackedStateTransition;
import dev.most.transducer.transition.state_transition.StateTransition;

import java.util.HashMap;
import java.util.function.Supplier;

public class TransitionFunctionFactory {

    static TransitionFunctionBuilder generate() {
        return new TransitionFunctionBuilder();
    }

    static class TransitionFunctionBuilder {

        private Supplier<StateTransition<LabelTransition<TransitionEnd>, TransitionEnd>> stateTransitionSupplier;
        private Supplier<LabelTransition<TransitionEnd>> labelTransitionSupplier;

//        new MapBackedStateTransition<>(
//        HashMap::new,
//                () -> new MapBackedLabelTransition<>(HashMap::new));

        TransitionFunctionBuilder stateTransitionVia(TransitionStrategy strategy) {
            stateTransitionSupplier = () -> switch (strategy) {
                case HASH_MAP_STRATEGY -> new MapBackedStateTransition<>(HashMap::new, labelTransitionSupplier);
                case ARRAY_STRATEGY,
                     TREE_MAP_STRATEGY,
                     BTREE_STRATEGY,
                     UNROLLED_LINKED_LIST_STRATEGY -> throw new IllegalArgumentException("not implemented yet");

            };

            return this;
        }

        TransitionFunctionBuilder labelTransitionVia(TransitionStrategy strategy) {
            labelTransitionSupplier = () -> switch (strategy) {
                case HASH_MAP_STRATEGY -> new MapBackedLabelTransition<>(HashMap::new, TransitionEnd::new);
                case ARRAY_STRATEGY,
                     TREE_MAP_STRATEGY,
                     BTREE_STRATEGY,
                     UNROLLED_LINKED_LIST_STRATEGY -> throw new IllegalArgumentException("not implemented yet");
            };

            return this;
        }

        StateTransition<LabelTransition<TransitionEnd>, TransitionEnd> compose() {
            return stateTransitionSupplier.get();
        }
    }
}
