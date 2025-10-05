package com.github.maximvegorov.fsm4j;

/**
 * Functional interface representing a predicate that determines whether a specified
 * transition between states in a finite state machine (FSM) should occur.
 * The predicate evaluates the provided execution context, event, and optional event arguments
 * to determine if the conditions for the transition are satisfied. Implementations of this
 * interface should ensure that the logic evaluates to true or false based on the defined rules.
 *
 * @param <S> the type representing the states within the FSM.
 * @param <E> the type representing the events triggering transitions in the FSM.
 * @param <C> the type of the execution context used during the FSM's lifecycle,
 *            which must extend {@link FsmExecutionContext}.
 */
@FunctionalInterface
public interface TransitionPredicate<S, E, C extends FsmExecutionContext<S>> {
    boolean test(C context, E event, FsmEventArgs args);
}
