# Week 6: Event Indexing and Traceability

## Weekly goal
Ingest ERC-20 `Transfer` logs reliably with replay-safe deduplication.

## Tasks
- [ ] Implement block-range poller.
- [ ] Decode `Transfer(address,address,uint256)` logs.
- [ ] Persist trace tuple: `chain_id, block_number, tx_hash, log_index, contract, from, to, amount`.
- [ ] Add dedupe on `(tx_hash, log_index)`.
- [ ] Add replay endpoint for historical range.

## Task verification commands

1. Verify poller job runs.
```bash
./mvnw -pl services/event-indexer -Dtest='*Poller*Test' test
```
2. Verify `Transfer` log decoding.
```bash
./mvnw -pl services/event-indexer -Dtest='*Decode*Test,*Transfer*Test' test
```
3. Verify trace tuple persistence.
```bash
docker exec -it stablecoin-postgres psql -U stablecoin -d stablecoin -c "select chain_id, block_number, tx_hash, log_index from token_transfer_event order by block_number desc limit 20;"
```
4. Verify dedupe guard.
```bash
./mvnw -pl services/event-indexer -Dtest='*Dedupe*Test' test
```
5. Verify replay endpoint.
```bash
curl -i -X POST "http://localhost:8083/api/v1/replay?fromBlock=1&toBlock=100"
```

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
