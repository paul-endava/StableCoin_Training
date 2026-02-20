# Week 5: Payment Orchestration State Machine

## Weekly goal
Implement a robust workflow from `INITIATED` to `SETTLED` with failure handling.

## Tasks
- [ ] Build `PaymentStateMachine` transitions.
- [ ] Persist each transition with timestamp and reason.
- [ ] Add outbox events per transition.
- [ ] Add compensation branch for failed broadcast.
- [ ] Add idempotent command handler.

## Validated code example
```java
public PaymentStatus next(PaymentStatus current, Event event) {
    return switch (current) {
        case INITIATED -> event == Event.VALIDATED ? PaymentStatus.VALIDATED : PaymentStatus.FAILED;
        case VALIDATED -> event == Event.BROADCASTED ? PaymentStatus.BROADCASTED : PaymentStatus.FAILED;
        case BROADCASTED -> event == Event.CONFIRMED ? PaymentStatus.CONFIRMED : PaymentStatus.FAILED;
        case CONFIRMED -> event == Event.SETTLED ? PaymentStatus.SETTLED : PaymentStatus.FAILED;
        default -> current;
    };
}
```

```java
@Test
void reachesSettledOnHappyPath() {
    assertEquals(PaymentStatus.SETTLED, replayHappyPath());
}
```

## Validation commands
```bash
./mvnw -pl services/payment-orchestrator test
```
