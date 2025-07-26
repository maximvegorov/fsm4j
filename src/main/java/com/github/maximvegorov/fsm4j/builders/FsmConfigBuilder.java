package com.github.maximvegorov.fsm4j.builders;

import com.github.maximvegorov.fsm4j.*;
import lombok.NonNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;

import static java.util.stream.Collectors.toUnmodifiableMap;

@ParametersAreNonnullByDefault
public final class FsmConfigBuilder<S, E, C extends FsmExecutionContext<S>> {
    private final Map<S, FsmStateConfigBuilder<S, E, C>> configs = new HashMap<>();
    private TransitionFallback<S, E, C> transitionFallback;

    public FsmStateConfigBuilder<S, E, C> state(S state) {
        return configs.computeIfAbsent(state, s -> new FsmStateConfigBuilder<>(this, state));
    }

    public FsmConfigBuilder<S, E, C> transitionFallback(TransitionFallback<S, E, C> transitionFallback) {
        this.transitionFallback = transitionFallback;
        return this;
    }

    public FsmConfig<S, E, C> build(@NonNull Set<S> terminalStates) {
        if (terminalStates.isEmpty()) {
            throw new IllegalArgumentException("terminalStates: " + terminalStates);
        }

        var transitions = new HashMap<S, Map<E, List<TransitionTarget<S, E, C>>>>();
        var exitActions = new HashMap<S, List<TransitionAction<S, E, C>>>();
        var beforeActions = new HashMap<Transition<S>, List<TransitionAction<S, E, C>>>();
        var afterActions = new HashMap<Transition<S>, List<TransitionAction<S, E, C>>>();
        var enterActions = new HashMap<S, List<TransitionAction<S, E, C>>>();

        for (var stateConfig : configs.values()) {
            if (!stateConfig.getTransitions().isEmpty()) {
                var stateTransitions = stateConfig.getTransitions()
                        .entrySet()
                        .stream()
                        .map(entry -> Map.entry(entry.getKey(), List.copyOf(entry.getValue())))
                        .collect(toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue));
                transitions.put(stateConfig.getState(), stateTransitions);
            }
            if (!stateConfig.getExitActions().isEmpty()) {
                exitActions.put(stateConfig.getState(), List.copyOf(stateConfig.getExitActions()));
            }
            if (!stateConfig.getBeforeActions().isEmpty()) {
                for (var targetAndBeforeActions : stateConfig.getBeforeActions().entrySet()) {
                    var transition = Transition.of(stateConfig.getState(), targetAndBeforeActions.getKey());
                    beforeActions.computeIfAbsent(transition, e -> new ArrayList<>())
                            .addAll(List.copyOf(targetAndBeforeActions.getValue()));
                }
            }
            if (!stateConfig.getAfterActions().isEmpty()) {
                for (var targetAndAfterActions : stateConfig.getAfterActions().entrySet()) {
                    var transition = Transition.of(stateConfig.getState(), targetAndAfterActions.getKey());
                    afterActions.computeIfAbsent(transition, e -> new ArrayList<>())
                            .addAll(List.copyOf(targetAndAfterActions.getValue()));
                }
            }
            if (!stateConfig.getEnterActions().isEmpty()) {
                enterActions.put(stateConfig.getState(), List.copyOf(stateConfig.getEnterActions()));
            }
        }

        var finalTransitionFallback = Optional.ofNullable(transitionFallback)
                .orElse((c, e, args) -> Optional.empty());

        return new FsmConfig<>(
                transitions,
                exitActions,
                beforeActions,
                afterActions,
                enterActions,
                finalTransitionFallback,
                terminalStates);
    }
}
