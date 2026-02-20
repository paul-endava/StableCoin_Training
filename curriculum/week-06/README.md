# Week 6: Event Indexing and Traceability

## Weekly goal
Ingest ERC-20 `Transfer` logs reliably with replay-safe deduplication.

## Tasks
- [ ] Implement block-range poller.
- [ ] Decode `Transfer(address,address,uint256)` logs.
- [ ] Persist trace tuple: `chain_id, block_number, tx_hash, log_index, contract, from, to, amount`.
- [ ] Add dedupe on `(tx_hash, log_index)`.
- [ ] Add replay endpoint for historical range.

## Validated code example
```java
public String dedupeKey(String txHash, BigInteger logIndex) {
    return txHash + ":" + logIndex;
}
```

```java
@Test
void ignoresDuplicateLog() {
    indexer.index(log);
    indexer.index(log);
    assertEquals(1, repository.count());
}
```

## Validation commands
```bash
./mvnw -pl services/event-indexer test
```
