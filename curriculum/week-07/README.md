# Week 7: Wallet and Custody Abstractions

## Weekly goal
Separate orchestration from signing implementation to support local keys and external custodians.

## Tasks
- [ ] Create `Signer` interface and `SignRequest` model.
- [ ] Implement `LocalSigner` for dev.
- [ ] Implement `ExternalSignerStub` for custody provider integration.
- [ ] Add wallet metadata model (owner, label, purpose, risk tier).
- [ ] Add signing audit trail with immutable records.

## Validated code example
```java
public interface Signer {
    SignedTransaction sign(SignRequest request);
}
```

```java
@Test
void walletServiceUsesInjectedSigner() {
    SignedTransaction tx = walletService.sign(request);
    assertNotNull(tx.rawTransaction());
}
```

## Validation commands
```bash
./mvnw -pl services/wallet-service test
```
