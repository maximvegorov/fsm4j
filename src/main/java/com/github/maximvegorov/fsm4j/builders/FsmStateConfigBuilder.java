package com.github.maximvegorov.fsm4j.builders;

import com.github.maximvegorov.fsm4j.*;
import lombok.*;

import java.util.*;

@Getter(AccessLevel.PACKAGE)
@RequiredArgsConstructor
@ToString
public class FsmStateConfigBuilder<S, E, C extends FsmExecutionContext<S>> {
    private final FsmConfigBuilder<S, E, C> parent;
    private final S state;
    private final Map<E, List<TransitionTarget<S, E, C>>> transitions = new HashMap<>();
    private final Map<S, List<TransitionAction<S, E, C>>> beforeActions = new HashMap<>();
    private final Map<S, List<TransitionAction<S, E, C>>> afterActions = new HashMap<>();
    private final List<TransitionAction<S, E, C>> enterActions = new ArrayList<>();
    private final List<TransitionAction<S, E, C>> exitActions = new ArrayList<>();

    public FsmChoiceConfigBuilder<S, E, C> choice() {
        return new FsmChoiceConfigBuilder<>(this);
    }

    public FsmTargetConfigBuilder<S, E, C> on(E event) {
        return choice().on(event);
    }

    public FsmStateConfigBuilder<S, E, C> addBeforeAction(S target, @NonNull TransitionAction<S, E, C> action) {
        beforeActions.computeIfAbsent(target, t -> new ArrayList<>())
                .add(action);
        return this;
    }

    public FsmStateConfigBuilder<S, E, C> addAfterAction(S target, @NonNull TransitionAction<S, E, C> action) {
        afterActions.computeIfAbsent(target, t -> new ArrayList<>())
                .add(action);
        return this;
    }

    public FsmStateConfigBuilder<S, E, C> addEnterAction(@NonNull TransitionAction<S, E, C> action) {
        enterActions.add(action);
        return this;
    }

    public FsmStateConfigBuilder<S, E, C> addExitAction(@NonNull TransitionAction<S, E, C> action) {
        exitActions.add(action);
        return this;
    }

    public FsmStateConfigBuilder<S, E, C> state(S state) {
        return parent.state(state);
    }

    public FsmConfigBuilder<S, E, C> transitionFallback(TransitionFallback<S, E, C> transitionFallback) {
        return parent.transitionFallback(transitionFallback);
    }

    public FsmConfig<S, E, C> build(Set<S> endStates) {
        return parent.build(endStates);
    }
}
