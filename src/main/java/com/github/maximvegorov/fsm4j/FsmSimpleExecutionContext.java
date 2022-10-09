package com.github.maximvegorov.fsm4j;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FsmSimpleExecutionContext<S> implements FsmExecutionContext<S> {
    private S state;

    public FsmSimpleExecutionContext(@NonNull S state) {
        this.state = state;
    }
}
