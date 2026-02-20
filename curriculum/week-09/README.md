# Week 9: Compliance Hooks and Decisioning

## Weekly goal
Integrate policy checks (`ALLOW`, `REVIEW`, `BLOCK`) into payment orchestration.

## Tasks
- [ ] Build compliance decision API.
- [ ] Add reason codes and policy versioning.
- [ ] Enforce pre-broadcast gate in orchestrator.
- [ ] Persist decision audit fields (`decision`, `reason`, `decided_at`, `actor`).
- [ ] Add simulation endpoint for policy dry-runs.

## Task verification commands

1. Verify compliance decision API.
```bash
curl -i -X POST http://localhost:8086/api/v1/compliance/decide \
  -H 'Content-Type: application/json' \
  -d '{"from":"0xabc","to":"0xdef","amount":"1000","asset":"USDC"}'
```
2. Verify reason code and policy version persistence.
```bash
docker exec -it stablecoin-postgres psql -U stablecoin -d stablecoin -c "select decision, reason_code, policy_version from compliance_decision order by decided_at desc limit 20;"
```
3. Verify pre-broadcast gate enforcement.
```bash
./mvnw -pl services/payment-orchestrator -Dtest='*ComplianceGate*Test' test
```
4. Verify audit fields are stored.
```bash
docker exec -it stablecoin-postgres psql -U stablecoin -d stablecoin -c "select decision, reason, decided_at, actor from compliance_decision order by decided_at desc limit 20;"
```
5. Verify policy simulation endpoint.
```bash
curl -i -X POST http://localhost:8086/api/v1/compliance/simulate \
  -H 'Content-Type: application/json' \
  -d '{"from":"0xabc","to":"0xdef","amount":"1000","asset":"USDT"}'
```

## Validated code example
```java
public void enforce(ComplianceDecision decision) {
    if (decision == ComplianceDecision.BLOCK) {
        throw new PolicyViolation("COMPLIANCE_BLOCK");
    }
}
```

```java
@Test
void blocksWhenComplianceSaysBlock() {
    assertThrows(PolicyViolation.class, () -> service.enforce(ComplianceDecision.BLOCK));
}
```

## Validation commands
```bash
./mvnw -pl services/compliance-service,services/payment-orchestrator test
```
