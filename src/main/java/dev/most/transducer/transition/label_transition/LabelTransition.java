package dev.most.transducer.transition.label_transition;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;

public interface LabelTransition<T> {

    Optional<T> get(final Character label);

    Stream<Character> streamLabels();

    void forEachLabel(Consumer<Character> consumer);

    T getOrCreate(Character label);
}
