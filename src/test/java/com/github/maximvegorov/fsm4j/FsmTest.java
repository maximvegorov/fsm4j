package com.github.maximvegorov.fsm4j;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class FsmTest {
    @Test
    void fire() {
        var fsmConfig = FsmConfig.<States, Events, FsmSimpleExecutionContext<States>>builder()
                .state(States.NEW)
                    .on(Events.SAY_HELLO)
                        .action(() -> System.out.print("Hello, "))
                        .moveTo(States.SAID_HELLO)
                    .end()
                .state(States.SAID_HELLO)
                    .on(Events.SAY_WORLD)
                        .action(() -> System.out.println("World!"))
                        .moveTo(States.END)
                    .end()
                .build(Set.of(States.END));

        var fsm = Fsm.of(fsmConfig, new FsmSimpleExecutionContext<>(States.NEW));
        fsm.fire(Events.SAY_HELLO);
        fsm.fire(Events.SAY_WORLD);

        assertThat(fsm.getExecutionStatus())
                .isEqualTo(FsmExecutionStatus.TERMINATED);
    }

    enum States {
        NEW,
        SAID_HELLO,
        END
    }

    enum Events {
        SAY_HELLO,
        SAY_WORLD
    }
}
