# Week 5: Payment Orchestration State Machine

## Weekly goal
Implement a robust workflow from `INITIATED` to `SETTLED` with failure handling.

## Tasks
- [ ] Build `PaymentStateMachine` transitions.
- [ ] Persist each transition with timestamp and reason.
- [ ] Add outbox events per transition.
- [ ] Add compensation branch for failed broadcast.
- [ ] Add idempotent command handler.

## Task verification commands

1. Verify state machine transitions.
```bash
./mvnw -pl services/payment-orchestrator -Dtest='*StateMachine*Test' test
```
2. Verify transition persistence records.
```bash
docker exec -it stablecoin-postgres psql -U stablecoin -d stablecoin -c "select payment_id, from_state, to_state, changed_at from payment_transition order by changed_at desc limit 20;"
```
3. Verify outbox events are written.
```bash
docker exec -it stablecoin-postgres psql -U stablecoin -d stablecoin -c "select topic, event_key, created_at from outbox_event order by created_at desc limit 20;"
```
4. Verify compensation path handling.
```bash
./mvnw -pl services/payment-orchestrator -Dtest='*Compensation*Test' test
```
5. Verify idempotent command processing.
```bash
./mvnw -pl services/payment-orchestrator -Dtest='*Idempotent*Test' test
```

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
