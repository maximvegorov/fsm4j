package com.github.maximvegorov.fsm4j;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface FsmExecutionContext<S> extends AutoCloseable {
    @Nonnull
    S getState();

    void setState(S value);

    default void close() {
    }
}
