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
