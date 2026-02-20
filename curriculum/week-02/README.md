# Week 2: Transaction Submission and Idempotency

## Weekly goal
Submit Ethereum transactions from Spring with deterministic idempotency and nonce handling.

## Tasks
- [ ] Add RPC config (`rpcUrl`, `chainId`, `minConfirmations`, `maxFeePerGas`).
- [ ] Implement `POST /api/v1/transfers/native` for ETH transfers.
- [ ] Add idempotency table: `idempotency_key`, `request_hash`, `tx_hash`.
- [ ] Implement nonce manager to prevent collisions.
- [ ] Add retry for transient RPC failures.

## Validated code example
```java
@Transactional
public String submit(TransferRequest request, String idempotencyKey) {
    return idempotencyRepository.findByKey(idempotencyKey)
        .map(IdempotencyRecord::txHash)
        .orElseGet(() -> sender.sendNewTransaction(request, idempotencyKey));
}
```

```java
@Test
void returnsSameHashForRepeatedIdempotentRequest() {
    String first = service.submit(req, "k-001");
    String second = service.submit(req, "k-001");
    assertEquals(first, second);
}
```

## Validation commands
```bash
./mvnw -pl services/chain-adapter-eth test
curl -X POST http://localhost:8081/api/v1/transfers/native \
  -H 'Idempotency-Key: k-001' -H 'Content-Type: application/json' \
  -d '{"from":"0x...","to":"0x...","valueWei":"1000000000000000"}'
```
