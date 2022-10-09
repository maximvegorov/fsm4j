package com.github.maximvegorov.fsm4j;

import javax.annotation.Nonnull;

public interface FsmEventArgs {
    static FsmEventArgs empty() {
        return FsmEmptyEventArgs.INSTANCE;
    }
}
