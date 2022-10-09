package com.github.maximvegorov.fsm4j;

import lombok.Data;

@Data(staticConstructor = "of")
public final class Transition<S> {
    private final S source;
    private final S target;
}
