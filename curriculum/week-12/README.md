# Week 12: Capstone Delivery

## Weekly goal
Ship an end-to-end stablecoin payment MVP and demo it from IntelliJ with Docker services.

## Tasks
- [ ] Finalize API set: create payment, query status, query audit trace.
- [ ] Prove full pipeline: validate -> broadcast -> confirm -> reconcile -> settle.
- [ ] Add production-style runbooks: replay, reconciliation, key compromise.
- [ ] Conduct architecture review and capture hardening backlog.
- [ ] Record demo script and expected outputs.

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
