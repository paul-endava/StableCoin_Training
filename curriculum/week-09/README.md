# Week 9: Compliance Hooks and Decisioning

## Weekly goal
Integrate policy checks (`ALLOW`, `REVIEW`, `BLOCK`) into payment orchestration.

## Tasks
- [ ] Build compliance decision API.
- [ ] Add reason codes and policy versioning.
- [ ] Enforce pre-broadcast gate in orchestrator.
- [ ] Persist decision audit fields (`decision`, `reason`, `decided_at`, `actor`).
- [ ] Add simulation endpoint for policy dry-runs.

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
