package com.github.maximvegorov.fsm4j;

import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Getter
@ToString
@Slf4j
public final class Fsm<S, E, C extends FsmExecutionContext<S>> implements AutoCloseable {
    private final FsmConfig<S, E, C> config;
    private final C executionContext;
    private FsmExecutionStatus executionStatus;

    private Fsm(FsmConfig<S, E, C> config, C executionContext) {
        this.config = config;
        this.executionContext = executionContext;
        this.executionStatus = !config.getTerminalStates().contains(this.executionContext.getState())
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

    @Override
    public void close() {
        stop(FsmExecutionStatus.TERMINATED);
    }

    private boolean doTransition(Transition<S> transition, E event, FsmEventArgs args) {
        log.debug("Transition {} fired by event {} with args {}", transition, event, args);
        try {
            log.debug("Executing exit actions");
            var exitActions = config.getExitActions(transition.getSource());
            runAllActions(exitActions, transition, event, args);

            log.debug("Executing before transition actions");
            var beforeTransitionActions = config.getBeforeActions(transition);
            runAllActions(beforeTransitionActions, transition, event, args);

            executionContext.setState(transition.getTarget());

            log.debug("Executing after transition actions");
            var afterTransitionActions = config.getAfterActions(transition);
            runAllActions(afterTransitionActions, transition, event, args);

            log.debug("Executing enter actions");
            var enterActions = config.getEnterActions(transition.getTarget());
            runAllActions(enterActions, transition, event, args);

            if (config.getTerminalStates().contains(executionContext.getState())) {
                stop(FsmExecutionStatus.TERMINATED);
            }

            log.debug("Transition completed");

            return true;
        } catch (RuntimeException e) {
            log.debug("Error while transition", e);
            stop(FsmExecutionStatus.ABORTED);
            throw e;
        }
    }

    private void runAllActions(List<TransitionAction<S, E, C>> actions, Transition<S> transition, E event, FsmEventArgs args) {
        if (!actions.isEmpty()) {
            actions.forEach(action -> action.run(executionContext, transition, event, args));
        }
    }

    private void stop(FsmExecutionStatus status) {
        log.debug("Stopping with execution status {}", status);
        try {
            executionContext.close();

            log.debug("Stopped");
        } catch (RuntimeException e) {
            log.error("Error while stopping", e);
        } finally {
            executionStatus = status;
        }
    }
}
