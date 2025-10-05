package com.github.maximvegorov.fsm4j;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * A simple implementation of the {@code FsmExecutionContext} interface, which manages
 * the state of a finite state machine (FSM). This class provides basic functionality
 * for storing and manipulating the current state of the FSM.
 *
 * @param <S> the type representing the state managed by this execution context.
 */
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FsmSimpleExecutionContext<S> implements FsmExecutionContext<S> {
    private S state;

    public FsmSimpleExecutionContext(@NonNull S state) {
        this.state = state;
    }
}
