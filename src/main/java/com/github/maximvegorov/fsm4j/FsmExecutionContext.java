package com.github.maximvegorov.fsm4j;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Represents an execution context for a finite state machine (FSM).
 * This interface is designed to manage the current state of the FSM and provides a way
 * to manipulate or retrieve the state during the lifecycle of the FSM.
 *
 * @param <S> the type of the state managed by this execution context.
 */
@ParametersAreNonnullByDefault
public interface FsmExecutionContext<S> extends AutoCloseable {
    @Nonnull
    S getState();

    void setState(S value);

    default void close() {
    }
}
