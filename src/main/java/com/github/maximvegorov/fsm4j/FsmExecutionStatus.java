package com.github.maximvegorov.fsm4j;

/**
 * Represents the execution status of a Finite State Machine (FSM).
 * This enumeration defines the possible states that describe the lifecycle and
 * operational phase of FSM execution.
 */
public enum FsmExecutionStatus {
    /**
     * Indicates that the FSM is actively processing events and transitions.
     */
    RUNNING,
    /**
     * Indicates that the FSM has completed execution and is in a final state.
     */
    TERMINATED,
    /**
     * Indicates that the FSM encountered an error or was interrupted, resulting in an abnormal termination.
     */
    ABORTED
}
