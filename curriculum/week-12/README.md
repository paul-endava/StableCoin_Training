# Week 12: Capstone Delivery

## Weekly goal
Ship an end-to-end stablecoin payment MVP and demo it from IntelliJ with Docker services.

## Tasks
- [ ] Finalize API set: create payment, query status, query audit trace.
- [ ] Prove full pipeline: validate -> broadcast -> confirm -> reconcile -> settle.
- [ ] Add production-style runbooks: replay, reconciliation, key compromise.
- [ ] Conduct architecture review and capture hardening backlog.
- [ ] Record demo script and expected outputs.

## Task verification commands

1. Verify payment API endpoints.
```bash
curl -i -X POST http://localhost:8080/api/v1/payments -H 'Content-Type: application/json' -d '{"asset":"USDC","amount":"10.00","to":"0xabc"}'
curl -i http://localhost:8080/api/v1/payments/{paymentId}
curl -i http://localhost:8080/api/v1/payments/{paymentId}/audit
```
2. Verify full pipeline execution in logs.
```bash
docker compose logs --tail=200 payment-orchestrator
docker compose logs --tail=200 event-indexer
docker compose logs --tail=200 ledger-service
```
3. Verify runbooks exist.
```bash
ls -la docs/runbooks
```
4. Verify architecture review artifacts.
```bash
ls -la docs/architecture
ls -la docs/adr
```
5. Verify demo script output.
```bash
./mvnw test
```

## Validated code example
```java
public PaymentResponse createPayment(PaymentRequest request) {
    String paymentId = orchestrator.start(request);
    return PaymentResponse.accepted(paymentId);
}
```

```java
@Test
void createPaymentReturnsAcceptedAndId() {
    PaymentResponse response = controller.createPayment(request);
    assertEquals("ACCEPTED", response.status());
    assertNotNull(response.paymentId());
}
```

## Validation commands
```bash
docker compose up -d
./mvnw test
curl -s http://localhost:8080/api/v1/payments/{paymentId}
```
