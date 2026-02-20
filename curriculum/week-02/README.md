# Week 2: Transaction Submission and Idempotency

## Weekly goal
Submit Ethereum transactions from Spring with deterministic idempotency and nonce handling.

## Tasks
- [ ] Add RPC config (`rpcUrl`, `chainId`, `minConfirmations`, `maxFeePerGas`).
- [ ] Implement `POST /api/v1/transfers/native` for ETH transfers.
- [ ] Add idempotency table: `idempotency_key`, `request_hash`, `tx_hash`.
- [ ] Implement nonce manager to prevent collisions.
- [ ] Add retry for transient RPC failures.

## Task verification commands

1. Verify RPC configuration is loaded.
```bash
./mvnw -pl services/chain-adapter-eth -Dtest='*Config*Test' test
```
2. Verify native transfer endpoint.
```bash
curl -i -X POST http://localhost:8081/api/v1/transfers/native \
  -H 'Content-Type: application/json' \
  -H 'Idempotency-Key: k-001' \
  -d '{"from":"0xabc","to":"0xdef","valueWei":"1000"}'
```
3. Verify idempotency table and record writes.
```bash
docker exec -it stablecoin-postgres psql -U stablecoin -d stablecoin -c "\d idempotency"
docker exec -it stablecoin-postgres psql -U stablecoin -d stablecoin -c "select * from idempotency order by created_at desc limit 5;"
```
4. Verify nonce manager behavior.
```bash
./mvnw -pl services/chain-adapter-eth -Dtest='*Nonce*Test' test
```
5. Verify retry policy on transient RPC failures.
```bash
./mvnw -pl services/chain-adapter-eth -Dtest='*Retry*Test' test
```

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
