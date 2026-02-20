# Week 8: Internal Ledger and Reconciliation

## Weekly goal
Create an immutable journal and reconcile confirmed on-chain transfers.

## Tasks
- [ ] Add journal schema: `entry_id`, `account`, `debit`, `credit`, `reference`.
- [ ] Implement posting rules for principal and fee entries.
- [ ] Reconcile confirmed chain transfer to expected ledger entries.
- [ ] Add discrepancy workflow and API.
- [ ] Add daily close report query.

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
