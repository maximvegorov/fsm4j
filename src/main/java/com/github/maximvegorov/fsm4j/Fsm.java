package com.github.maximvegorov.fsm4j;

import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Represents a Finite State Machine (FSM) that manages and manipulates states and state transitions
 * for a given execution context.
 *
 * @param <S> the type representing the state.
 * @param <E> the type representing the event that triggers state transitions.
 * @param <C> the type representing the execution context which defines the state and allows additional customization.
 */
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

    /**
     * Creates a new instance of the {@code Fsm} class using the provided configuration and execution context.
     *
     * @param config the finite state machine's configuration defining states, transitions, and actions.
     * @param executionContext the execution context for the finite state machine, holding the current state
     *                         and any additional information required during execution.
     * @param <S> the type defining the states within the finite state machine.
     * @param <E> the type defining the events that can trigger state transitions.
     * @param <C> the type of the execution context, which must extend {@code FsmExecutionContext<S>}.
     * @return a new {@code Fsm} instance initialized with the specified configuration and execution context.
     */
    public static <S, E, C extends FsmExecutionContext<S>> Fsm<S, E, C> of(
            @NonNull FsmConfig<S, E, C> config,
            @NonNull C executionContext) {
        return new Fsm<>(config, executionContext);
    }

    /**
     * Triggers a state transition in the finite state machine using the specified event.
     * This method attempts to handle the provided event by matching it to a valid transition
     * in the current state. If a matching transition is found, it executes the associated
     * actions and updates the state. If no matching transition is found, the method will
     * return false without making any state changes.
     *
     * @param event the event used to trigger a state transition.
     * @return true if the event triggered a valid state transition, false otherwise.
     * @throws IllegalStateException if the finite state machine is not in the running state.
     */
    public boolean fire(E event) {
        return fire(event, FsmEventArgs.empty());
    }

    /**
     * Attempts to trigger a state transition in the finite state machine using the specified event and arguments.
     * This method checks for a valid transition using the provided event and arguments. If a matching transition
     * is found, the associated state actions are executed, and the state machine transitions to the target state.
     * If no matching transition is found, the method returns false without making any state changes.
     *
     * @param event the event used to trigger a state transition
     * @param args additional arguments to be passed during the transition
     * @return true if a valid transition was executed, false otherwise
     * @throws IllegalStateException if the finite state machine is not in the running state
     */
    public boolean fire(@NonNull E event, @NonNull FsmEventArgs args) {
        if (executionStatus != FsmExecutionStatus.RUNNING) {
            throw new IllegalStateException("Must be running");
        }

        var transition = config.tryFindTransition(executionContext, event, args);

        return transition.filter(sTransition -> doTransition(sTransition, event, args))
                .isPresent();

    }

    /**
     * Closes the finite state machine and changes its execution status to {@code TERMINATED}, if it is
     * not already in the {@code RUNNING} state.
     * This method is part of the {@link AutoCloseable} interface and provides a way to
     * gracefully stop the finite state machine. If the current execution status is not {@code RUNNING},
     * the underlying {@code stop} method is invoked with {@code TERMINATED} as the parameter. This
     * ensures that any necessary cleanup or finalization logic is performed during the termination process.
     */
    @Override
    public void close() {
        if (executionStatus != FsmExecutionStatus.RUNNING) {
            stop(FsmExecutionStatus.TERMINATED);
        }
    }

    private boolean doTransition(Transition<S> transition, E event, FsmEventArgs args) {
        log.debug("Transition {} fired by event {} with args {}", transition, event, args);
        try {
            log.debug("Executing exit actions");
            var exitActions = config.getExitActions(transition.getSource());
            runActions(exitActions, transition, event, args);

            log.debug("Executing before transition actions");
            var beforeTransitionActions = config.getBeforeActions(transition);
            runActions(beforeTransitionActions, transition, event, args);

            executionContext.setState(transition.getTarget());

            log.debug("Executing after transition actions");
            var afterTransitionActions = config.getAfterActions(transition);
            runActions(afterTransitionActions, transition, event, args);

            log.debug("Executing enter actions");
            var enterActions = config.getEnterActions(transition.getTarget());
            runActions(enterActions, transition, event, args);

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

    private void runActions(List<TransitionAction<S, E, C>> actions, Transition<S> transition, E event, FsmEventArgs args) {
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
