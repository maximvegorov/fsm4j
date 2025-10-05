package com.github.maximvegorov.fsm4j;

/**
 * Represents an empty implementation of the {@link FsmEventArgs} interface.
 * This class is a singleton that provides a default instance of {@code FsmEventArgs}
 * used when no additional arguments are required for state transitions in a finite state machine.
 */
public class FsmEmptyEventArgs implements FsmEventArgs {
    public static final FsmEventArgs INSTANCE = new FsmEmptyEventArgs();
}
