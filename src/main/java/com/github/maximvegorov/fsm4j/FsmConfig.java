package com.github.maximvegorov.fsm4j;

import com.github.maximvegorov.fsm4j.builders.FsmConfigBuilder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;

@RequiredArgsConstructor
@ToString
public final class FsmConfig<S, E, C extends FsmExecutionContext<S>> {
    @NonNull
    private final Map<S, Map<E, List<TransitionTarget<S, E, C>>>> transitions;
    @NonNull
    private final Map<S, List<TransitionAction<S, E, C>>> exitActions;
    @NonNull
    private final Map<Transition<S>, List<TransitionAction<S, E, C>>> beforeActions;
    @NonNull
    private final Map<Transition<S>, List<TransitionAction<S, E, C>>> afterActions;
    @NonNull
    private final Map<S, List<TransitionAction<S, E, C>>> enterActions;
    @NonNull
    private final TransitionFallback<S, E, C> transitionFallback;
    @NonNull
    private final Set<S> endStates;

    public static <S, E, C extends FsmExecutionContext<S>> FsmConfigBuilder<S, E, C> builder() {
        return new FsmConfigBuilder<>();
    }

    public Optional<Transition<S>> tryFindTransition(C context, E event, FsmEventArgs args) {
        var targets = transitions.getOrDefault(context.getState(), emptyMap())
                .get(event);
        if (targets == null) {
            return transitionFallback.get(context, event, args);
        }
        for (var target : targets) {
            if (target.getPredicate().test(context, event, args)) {
                return Optional.of(target.getTransition());
            }
        }
        return Optional.empty();
    }

    public List<TransitionAction<S, E, C>> getExitActions(S state) {
        return exitActions.getOrDefault(state, emptyList());
    }

    public List<TransitionAction<S, E, C>> getBeforeActions(Transition<S> transition) {
        return beforeActions.getOrDefault(transition, emptyList());
    }

    public List<TransitionAction<S, E, C>> getAfterActions(Transition<S> transition) {
        return afterActions.getOrDefault(transition, emptyList());
    }

    public List<TransitionAction<S, E, C>> getEnterActions(S state) {
        return enterActions.getOrDefault(state, emptyList());
    }

    public Set<S> getEndStates() {
        return endStates;
    }
}
