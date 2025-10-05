package com.github.maximvegorov.fsm4j;

/**
 * Represents an action to be executed during a state transition in a finite state machine (FSM).
 *
 * <p>
 * A {@code TransitionAction} is a functional interface that defines a single method, {@code run},
 * which is invoked when a specified transition occurs. The method provides access to the context of
 * the FSM, the details of the transition, the triggering event, and optional event arguments.
 * </p>
 *
 * @param <S> the type representing the states of the FSM.
 * @param <E> the type representing the events that trigger transitions in the FSM.
 * @param <C> the type extending {@link FsmExecutionContext} that manages the FSM's current state.
 */
@FunctionalInterface
public interface TransitionAction<S, E, C extends FsmExecutionContext<S>> {
    void run(C context, Transition<S> transition, E event, FsmEventArgs args);
}
