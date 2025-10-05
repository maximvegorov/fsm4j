package com.github.maximvegorov.fsm4j;

/**
 * A marker interface that represents event arguments for transitions in a finite state machine (FSM).
 * This interface can be implemented by custom classes or used as a placeholder for transitions
 * that do not require additional arguments.
 * Static utility methods are provided to facilitate the use of common event argument instances.
 */
public interface FsmEventArgs {
    static FsmEventArgs empty() {
        return FsmEmptyEventArgs.INSTANCE;
    }
}
