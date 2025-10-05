package com.github.maximvegorov.fsm4j;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * This functional interface represents a fallback mechanism to handle transitions in a finite state machine (FSM).
 * It is designed to be used when no explicit transition is defined for a given event in a specific context.
 * Implementations can determine how to resolve the next state based on the provided context, event, and event arguments.
 *
 * @param <S> the type representing the states in the FSM.
 * @param <E> the type representing the events in the FSM.
 * @param <C> the type of the finite state machine execution context, extending {@link FsmExecutionContext}.
 */
@FunctionalInterface
public interface TransitionFallback<S, E, C extends FsmExecutionContext<S>> {
    @Nonnull
    Optional<Transition<S>> get(C context, E event, FsmEventArgs args);
}
