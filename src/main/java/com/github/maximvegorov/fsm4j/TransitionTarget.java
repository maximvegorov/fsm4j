package com.github.maximvegorov.fsm4j;

import lombok.*;

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
