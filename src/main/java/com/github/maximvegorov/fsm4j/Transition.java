package com.github.maximvegorov.fsm4j;

import lombok.Data;

/**
 * Represents a transition between two states in a finite state machine (FSM).
 *
 * @param <S> the type representing the state.
 *            This type parameter allows the transition to work with any user-defined state type.
 */
@Data(staticConstructor = "of")
public final class Transition<S> {
    private final S source;
    private final S target;
}
