package com.github.maximvegorov.fsm4j.builders;

import com.github.maximvegorov.fsm4j.FsmExecutionContext;
import com.github.maximvegorov.fsm4j.Transition;
import com.github.maximvegorov.fsm4j.TransitionTarget;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter(AccessLevel.PACKAGE)
@RequiredArgsConstructor
public final class FsmChoiceConfigBuilder<S, E, C extends FsmExecutionContext<S>> {
    @NonNull
    private final FsmStateConfigBuilder<S, E, C> parent;

    private final Map<E, List<FsmTargetConfig<S, E, C>>> transitions = new HashMap<>();

    public FsmTargetConfigBuilder<S, E, C> on(E event) {
        return new FsmTargetConfigBuilder<>(this, event);
    }

    public FsmStateConfigBuilder<S, E, C> end() {
        for (var eventAndTargets : transitions.entrySet()) {
            var event = eventAndTargets.getKey();
            var targets = eventAndTargets.getValue();
            var stateTargets = parent.getTransitions()
                    .computeIfAbsent(event, e -> new ArrayList<>(targets.size()));
            for (var target : targets) {
                var transition = Transition.of(parent.getState(), target.getTargetState());
                stateTargets.add(TransitionTarget.of(transition, target.getPredicate()));
                for (var beforeAction : target.getBeforeActions()) {
                    parent.addBeforeAction(target.getTargetState(), beforeAction);
                }
                for (var afterAction : target.getAfterActions()) {
                    parent.addAfterAction(target.getTargetState(), afterAction);
                }
            }
        }
        return parent;
    }
}
