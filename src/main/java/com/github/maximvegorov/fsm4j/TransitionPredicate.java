package com.github.maximvegorov.fsm4j;

@FunctionalInterface
public interface TransitionPredicate<S, E, C extends FsmExecutionContext<S>> {
    boolean test(C context, E event, FsmEventArgs args);
}
