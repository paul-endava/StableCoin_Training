# Week 7: Wallet and Custody Abstractions

## Weekly goal
Separate orchestration from signing implementation to support local keys and external custodians.

## Tasks
- [ ] Create `Signer` interface and `SignRequest` model.
- [ ] Implement `LocalSigner` for dev.
- [ ] Implement `ExternalSignerStub` for custody provider integration.
- [ ] Add wallet metadata model (owner, label, purpose, risk tier).
- [ ] Add signing audit trail with immutable records.

## Task verification commands

1. Verify `Signer` contract and compile.
```bash
./mvnw -pl services/wallet-service -Dtest='*Signer*Test' test
```
2. Verify local signer path.
```bash
./mvnw -pl services/wallet-service -Dtest='*LocalSigner*Test' test
```
3. Verify external signer stub path.
```bash
./mvnw -pl services/wallet-service -Dtest='*ExternalSigner*Test' test
```
4. Verify wallet metadata persistence.
```bash
docker exec -it stablecoin-postgres psql -U stablecoin -d stablecoin -c "select wallet_id, owner, label, risk_tier from wallet_metadata order by updated_at desc limit 20;"
```
5. Verify signing audit records.
```bash
docker exec -it stablecoin-postgres psql -U stablecoin -d stablecoin -c "select wallet_id, request_id, created_at from signing_audit order by created_at desc limit 20;"
```

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
