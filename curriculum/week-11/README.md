# Week 11: Observability, Resilience, and Security

## Weekly goal
Make the system operable: traces, metrics, failure tests, and key controls.

## Tasks
- [ ] Add OpenTelemetry traces with `paymentId` correlation.
- [ ] Add metrics: confirmation latency, broadcast failures, reorg count.
- [ ] Add structured JSON logging.
- [ ] Run resilience tests for RPC downtime and duplicate events.
- [ ] Enforce secret handling and signing ACL checks.

## Validated code example
```java
public Timer.Sample startTimedOperation(MeterRegistry meterRegistry) {
    return Timer.start(meterRegistry);
}

public void stopTimedOperation(Timer.Sample sample, MeterRegistry meterRegistry, String op) {
    sample.stop(Timer.builder("payment.operation.latency").tag("operation", op).register(meterRegistry));
}
```

```java
@Test
void metricsRecordedForBroadcast() {
    service.broadcast(request);
    assertTrue(meterRegistry.find("payment.operation.latency").timer() != null);
}
```

## Validation commands
```bash
./mvnw test
```
