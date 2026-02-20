# Week 4: Stablecoin Domain Modeling

## Weekly goal
Scaffold domain and policy boundaries for USDC/USDT/DAI-style processing before implementing full orchestration.

## Build-first approach (recommended)
Model domain and policy interfaces first, then add enforcement logic.

### Scaffold structure
In `services/payment-orchestrator/src/main/java` create:
- `domain/`:
  - `Stablecoin`, `Issuer`, `ReserveModel`, `NetworkPolicy`
- `api/`:
  - `PolicyController`
  - validation request/response DTOs
- `service/`:
  - `PolicyValidationService`
  - `TokenSupportService`
- `persistence/`:
  - repositories/entities for `stablecoin`, `issuer`, `token_network_policy`
- `exception/`:
  - `PolicyViolationException`

In `docs/adr/`:
- ADR for fiat-backed vs crypto-backed operating model tradeoffs

## Tasks
- [ ] Scaffold domain entities and policy service interfaces.
- [ ] Create DB schema for stablecoin policy tables.
- [ ] Seed baseline entries for `USDC`, `USDT`, `DAI`.
- [ ] Add preflight validation scaffold (`token`, `network`, `address`).
- [ ] Add ADR and test scaffolding.

## Task verification commands

1. Build orchestrator module with dependencies.
```bash
cd /Users/phopper/Documents/StableCoin_Training
./mvnw -pl services/payment-orchestrator -am -DskipTests install
```

2. Verify policy schema objects.
```bash
docker exec -it stablecoin-postgres psql -U stablecoin -d stablecoin -c "\\d stablecoin"
docker exec -it stablecoin-postgres psql -U stablecoin -d stablecoin -c "\\d issuer"
docker exec -it stablecoin-postgres psql -U stablecoin -d stablecoin -c "\\d token_network_policy"
```

3. Verify seed data presence.
```bash
docker exec -it stablecoin-postgres psql -U stablecoin -d stablecoin -c "select * from stablecoin order by symbol;"
```

4. Verify policy-validation scaffolding tests.
```bash
cd /Users/phopper/Documents/StableCoin_Training
./mvnw -pl services/payment-orchestrator -Dtest='*Policy*Test,*Validation*Test' test
```

5. Verify ADR exists.
```bash
cd /Users/phopper/Documents/StableCoin_Training
ls -la docs/adr
```

## Notes
- Keep this week focused on model correctness and policy interfaces; full payment flow behavior comes later.
- Prefer explicit error codes in policy exceptions (`TOKEN_DISABLED`, `NETWORK_NOT_ALLOWED`, etc.).
