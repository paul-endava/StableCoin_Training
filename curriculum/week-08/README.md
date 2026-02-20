# Week 8: Internal Ledger and Reconciliation

## Weekly goal
Create an immutable journal and reconcile confirmed on-chain transfers.

## Tasks
- [ ] Add journal schema: `entry_id`, `account`, `debit`, `credit`, `reference`.
- [ ] Implement posting rules for principal and fee entries.
- [ ] Reconcile confirmed chain transfer to expected ledger entries.
- [ ] Add discrepancy workflow and API.
- [ ] Add daily close report query.

## Task verification commands

1. Verify journal schema.
```bash
docker exec -it stablecoin-postgres psql -U stablecoin -d stablecoin -c "\d ledger_entry"
```
2. Verify posting rules via tests.
```bash
./mvnw -pl services/ledger-service -Dtest='*Posting*Test' test
```
3. Verify reconciliation results.
```bash
./mvnw -pl services/ledger-service -Dtest='*Reconciliation*Test' test
```
4. Verify discrepancy API.
```bash
curl -i http://localhost:8085/api/v1/reconciliation/discrepancies
```
5. Verify daily close report query.
```bash
docker exec -it stablecoin-postgres psql -U stablecoin -d stablecoin -c "select close_date, status from ledger_daily_close order by close_date desc limit 7;"
```

## Validated code example
```java
public void assertBalanced(List<LedgerEntry> entries) {
    BigDecimal debits = entries.stream().map(LedgerEntry::debit).reduce(BigDecimal.ZERO, BigDecimal::add);
    BigDecimal credits = entries.stream().map(LedgerEntry::credit).reduce(BigDecimal.ZERO, BigDecimal::add);
    if (debits.compareTo(credits) != 0) throw new IllegalStateException("UNBALANCED_JOURNAL");
}
```

```java
@Test
void journalMustBalance() {
    assertDoesNotThrow(() -> ledger.assertBalanced(entries));
}
```

## Validation commands
```bash
./mvnw -pl services/ledger-service test
```
