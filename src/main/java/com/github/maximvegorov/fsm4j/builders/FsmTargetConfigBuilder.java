package com.github.maximvegorov.fsm4j.builders;

import com.github.maximvegorov.fsm4j.FsmExecutionContext;
import com.github.maximvegorov.fsm4j.TransitionAction;
import com.github.maximvegorov.fsm4j.TransitionPredicate;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.*;

import static java.util.Collections.emptyList;

@RequiredArgsConstructor
@ToString
public final class FsmTargetConfigBuilder<S, E, C extends FsmExecutionContext<S>> {
    @NonNull
    private final FsmChoiceConfigBuilder<S, E, C> parent;
    @NonNull
    private final E event;
    private TransitionPredicate<S, E, C> predicate = (context, e, args) -> true;
    private List<TransitionAction<S, E, C>> beforeActions;
    private List<TransitionAction<S, E, C>> afterActions;
    private S target;

    public FsmTargetConfigBuilder<S, E, C> and(@NonNull TransitionPredicate<S, E, C> predicate) {
        this.predicate = predicate;
        return this;
    }

    public FsmTargetConfigBuilder<S, E, C> before(@NonNull TransitionAction<S, E, C> action) {
        if (beforeActions == null) {
            beforeActions = new ArrayList<>();
        }
        beforeActions.add(action);
        return this;
    }

    public FsmTargetConfigBuilder<S, E, C> action(@NonNull Runnable action) {
        return before((c, t, e, args) -> action.run());
    }

    public FsmTargetConfigBuilder<S, E, C> action(@NonNull TransitionAction<S, E, C> action) {
        return before(action);
    }

    public FsmTargetConfigBuilder<S, E, C> after(@NonNull TransitionAction<S, E, C> action) {
        if (afterActions == null) {
            afterActions = new ArrayList<>();
        }
        afterActions.add(action);
        return this;
    }

    public FsmChoiceConfigBuilder<S, E, C> moveTo(@NonNull S target) {
        this.target = target;
        var transitionConfig = new FsmTargetConfig<>(
                event,
                predicate,
                Optional.ofNullable(beforeActions).orElse(emptyList()),
                Optional.ofNullable(afterActions).orElse(emptyList()),
                Objects.requireNonNull(target, "target is null"));
        parent.getTransitions().computeIfAbsent(event, e -> new ArrayList<>())
                .add(transitionConfig);
        return parent;
    }
}
