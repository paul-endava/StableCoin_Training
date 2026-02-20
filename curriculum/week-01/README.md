# Week 1: Blockchain and Node Connectivity

## Weekly goal
Build a Spring service that can read blocks and transactions from local Ethereum and persist transaction state transitions.

## Tasks
- [ ] Start Docker stack and verify Postgres + Anvil are healthy.
- [ ] Create `services/chain-adapter-eth` controllers:
  - [ ] `GET /api/v1/chain/blocks/{number}`
  - [ ] `GET /api/v1/chain/transactions/{txHash}`
- [ ] Add entity/table `chain_tx` with fields: `tx_hash`, `status`, `block_number`, `updated_at`.
- [ ] Implement status transitions: `PENDING -> MINED -> CONFIRMED` and `REORGED` path.
- [ ] Add integration test for one tx lifecycle.

## Validated code example
```java
@Service
public class ConfirmationPolicy {
    public TxStatus resolveStatus(@Nullable Long blockNumber, long head, int minConfirmations) {
        if (blockNumber == null) return TxStatus.PENDING;
        long depth = head - blockNumber;
        if (depth < 0) return TxStatus.REORGED;
        return depth >= minConfirmations ? TxStatus.CONFIRMED : TxStatus.MINED;
    }
}
```

```java
@Test
void resolvesConfirmedWhenDepthIsEnough() {
    var policy = new ConfirmationPolicy();
    assertEquals(TxStatus.CONFIRMED, policy.resolveStatus(100L, 112L, 12));
}
```

## Validation commands
```bash
docker compose up -d
./mvnw -pl services/chain-adapter-eth test
curl -s http://localhost:8081/api/v1/chain/blocks/latest
```
