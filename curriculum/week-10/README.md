# Week 10: Restricted Token Patterns (ERC-1400 Concepts)

## Weekly goal
Add a restricted transfer path and compare with standard ERC-20 operations.

## Tasks
- [ ] Implement a mock restricted token interface (`canTransfer` style pre-check).
- [ ] Extend orchestrator to branch on token type.
- [ ] Add restriction reason model and response mapping.
- [ ] Write ADR comparing ERC-20 and restricted token operations.
- [ ] Add tests for allowed vs denied restricted transfer.

## Task verification commands

1. Verify restricted token pre-check implementation tests.
```bash
./mvnw -pl services/payment-orchestrator -Dtest='*Restricted*Precheck*Test' test
```
2. Verify token-type branch behavior.
```bash
./mvnw -pl services/payment-orchestrator -Dtest='*TokenTypeRouting*Test' test
```
3. Verify restriction reason mapping.
```bash
./mvnw -pl services/payment-orchestrator -Dtest='*RestrictionReason*Test' test
```
4. Verify ADR exists and is reviewed.
```bash
ls -la docs/adr
```
5. Verify allowed and denied restricted transfer tests.
```bash
./mvnw -pl services/payment-orchestrator -Dtest='*RestrictedTransfer*Test' test
```

## Validated code example
```java
public TransferGateResult precheck(RestrictedTransferRequest request) {
    return restrictedTokenClient.canTransfer(request.from(), request.to(), request.amount());
}
```

```java
@Test
void deniedPrecheckPreventsBroadcast() {
    when(client.canTransfer(any(), any(), any())).thenReturn(TransferGateResult.denied("KYC_MISSING"));
    assertThrows(PolicyViolation.class, () -> orchestrator.submitRestricted(request));
}
```

## Validation commands
```bash
./mvnw -pl services/payment-orchestrator test
```
