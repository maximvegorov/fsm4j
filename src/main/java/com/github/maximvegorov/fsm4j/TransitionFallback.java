package com.github.maximvegorov.fsm4j;

import javax.annotation.Nonnull;
import java.util.Optional;

@FunctionalInterface
public interface TransitionFallback<S, E, C extends FsmExecutionContext<S>> {
    @Nonnull
    Optional<Transition<S>> get(C context, E event, FsmEventArgs args);
}
