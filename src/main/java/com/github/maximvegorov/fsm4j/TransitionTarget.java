package com.github.maximvegorov.fsm4j;

import lombok.*;

/**
 * Represents a target for a state transition in a finite state machine (FSM).
 * This class encapsulates a transition and its associated predicate, which determines
 * whether the transition can occur under certain conditions.
 *
 * @param <S> the type representing the states within the FSM.
 * @param <E> the type representing the events triggering transitions in the FSM.
 * @param <C> the type of the execution context used during the FSM's lifecycle,
 *            which must extend {@link FsmExecutionContext}.
 */
@Getter
@RequiredArgsConstructor
@ToString
public final class TransitionTarget<S, E, C extends FsmExecutionContext<S>> {
    @NonNull
    private final Transition<S> transition;
    @NonNull
    private final TransitionPredicate<S, E, C> predicate;

    public static <S, E, C extends FsmExecutionContext<S>> TransitionTarget<S, E, C> of(
            Transition<S> transition,
            TransitionPredicate<S, E, C> predicate) {
        return new TransitionTarget<>(transition, predicate);
    }
}
