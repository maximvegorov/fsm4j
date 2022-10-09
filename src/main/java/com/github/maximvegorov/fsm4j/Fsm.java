package com.github.maximvegorov.fsm4j;

import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Getter
@ToString
@Slf4j
public final class Fsm<S, E, C extends FsmExecutionContext<S>> {
    private final FsmConfig<S, E, C> config;
    private final C executionContext;
    private FsmExecutionStatus executionStatus;

    private Fsm(FsmConfig<S, E, C> config, C executionContext) {
        this.config = config;
        this.executionContext = executionContext;
        this.executionStatus = !config.getEndStates().contains(this.executionContext.getState())
                ? FsmExecutionStatus.RUNNING
                : FsmExecutionStatus.TERMINATED;
    }

    public static <S, E, C extends FsmExecutionContext<S>> Fsm<S, E, C> of(
            @NonNull FsmConfig<S, E, C> config,
            @NonNull C executionContext) {
        return new Fsm<>(config, executionContext);
    }

    public boolean fire(E event) {
        return fire(event, FsmEventArgs.empty());
    }

    public boolean fire(@NonNull E event, @NonNull FsmEventArgs args) {
        if (executionStatus != FsmExecutionStatus.RUNNING) {
            throw new IllegalStateException("Must be running");
        }

        var transition = config.tryFindTransition(executionContext, event, args);
        if (transition.isEmpty()) {
            return false;
        }

        return doTransition(transition.get(), event, args);
    }

    private boolean doTransition(Transition<S> transition, E event, FsmEventArgs args) {
        try {
            var exitActions = config.getExitActions(transition.getSource());
            if (!exitActions.isEmpty()) {
                exitActions.forEach(action -> action.run(executionContext, transition, event, args));
            }

            var beforeTransitionActions = config.getBeforeActions(transition);
            if (!beforeTransitionActions.isEmpty()) {
                beforeTransitionActions.forEach(action -> action.run(executionContext, transition, event, args));
            }

            executionContext.setState(transition.getTarget());

            var afterTransitionActions = config.getAfterActions(transition);
            if (!afterTransitionActions.isEmpty()) {
                afterTransitionActions.forEach(action -> action.run(executionContext, transition, event, args));
            }

            var enterActions = config.getEnterActions(transition.getSource());
            if (!enterActions.isEmpty()) {
                enterActions.forEach(action -> action.run(executionContext, transition, event, args));
            }

            if (config.getEndStates().contains(executionContext.getState())) {
                stop(FsmExecutionStatus.TERMINATED);
            }

            return true;
        } catch (RuntimeException e) {
            stop(FsmExecutionStatus.ABORTED);
            throw e;
        }
    }

    private void stop(FsmExecutionStatus status) {
        try {
            executionContext.close();
        } catch (RuntimeException e) {
            log.error("Error while shutdown", e);
        } finally {
            executionStatus = status;
        }
    }
}
