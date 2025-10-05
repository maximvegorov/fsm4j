package com.github.maximvegorov.fsm4j.builders;

import com.github.maximvegorov.fsm4j.FsmExecutionContext;
import com.github.maximvegorov.fsm4j.TransitionAction;
import com.github.maximvegorov.fsm4j.TransitionPredicate;
import lombok.*;

import java.util.List;

/**
 * Represents the configuration for a specific target transition in a finite state machine (FSM).
 * This class encapsulates the details related to an event, its preconditions (predicate),
 * actions to be executed before and after the transition, and the target state to transition to.
 *
 * @param <S> the type representing the states in the FSM.
 * @param <E> the type representing the events triggering transitions in the FSM.
 * @param <C> the type extending {@link FsmExecutionContext}, providing contextual information
 *            during the FSM's execution.
 */
@Data
public final class FsmTargetConfig<S, E, C extends FsmExecutionContext<S>> {
    @NonNull
    private final E event;
    @NonNull
    private final TransitionPredicate<S, E, C> predicate;
    @NonNull
    private final List<TransitionAction<S, E, C>> beforeActions;
    @NonNull
    private final List<TransitionAction<S, E, C>> afterActions;
    @NonNull
    private final S targetState;
}
