# Week 4: Stablecoin Domain Modeling

## Weekly goal
Create stablecoin domain models and policy validation for USDC/USDT/DAI-like behavior.

## Tasks
- [ ] Add tables: `stablecoin`, `issuer`, `token_network_policy`.
- [ ] Seed entries for `USDC`, `USDT`, `DAI`.
- [ ] Implement payment preflight validation:
  - [ ] token supported
  - [ ] network supported
  - [ ] address format valid
- [ ] Add ADR for reserve model tradeoffs.
- [ ] Add tests for allow/deny outcomes.

## Task verification commands

1. Verify stablecoin policy tables are created.
```bash
docker exec -it stablecoin-postgres psql -U stablecoin -d stablecoin -c "\d stablecoin"
docker exec -it stablecoin-postgres psql -U stablecoin -d stablecoin -c "\d issuer"
docker exec -it stablecoin-postgres psql -U stablecoin -d stablecoin -c "\d token_network_policy"
```
2. Verify seed data for USDC/USDT/DAI.
```bash
docker exec -it stablecoin-postgres psql -U stablecoin -d stablecoin -c "select symbol, issuer_name from stablecoin order by symbol;"
```
3. Verify preflight validation rules.
```bash
./mvnw -pl services/payment-orchestrator -Dtest='*Policy*Test,*Validation*Test' test
```
4. Verify ADR file exists.
```bash
ls -la docs/adr
```
5. Verify allow/deny test outcomes.
```bash
./mvnw -pl services/payment-orchestrator -Dtest='*Allow*Test,*Deny*Test' test
```

## Validated code example
```java
public void validate(PaymentIntent intent, TokenPolicy policy) {
    if (!policy.enabled()) throw new PolicyViolation("TOKEN_DISABLED");
    if (!policy.networks().contains(intent.network())) throw new PolicyViolation("NETWORK_NOT_ALLOWED");
}
```

```java
@Test
void blocksUnsupportedNetwork() {
    assertThrows(PolicyViolation.class, () -> validator.validate(intent, policy));
}
```

## Validation commands
```bash
./mvnw -pl services/payment-orchestrator test
```
