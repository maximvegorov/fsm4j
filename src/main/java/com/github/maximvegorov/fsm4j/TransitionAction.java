package com.github.maximvegorov.fsm4j;

@FunctionalInterface
public interface TransitionAction<S, E, C extends FsmExecutionContext<S>> {
    void run(C context, Transition<S> transition, E event, FsmEventArgs args);
}
