package com.github.maximvegorov.fsm4j.builders;

import com.github.maximvegorov.fsm4j.FsmExecutionContext;
import com.github.maximvegorov.fsm4j.TransitionAction;
import com.github.maximvegorov.fsm4j.TransitionPredicate;
import lombok.*;

import java.util.List;

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
