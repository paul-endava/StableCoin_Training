# Week 2: Transaction Submission and Idempotency

## Weekly goal
Add a scaffolded write path for native ETH transfers with deterministic idempotency and nonce coordination.

## Build-first approach (recommended)
Define package structure first, then add endpoint behavior.

### Scaffold structure
Extend `services/chain-adapter-eth/src/main/java`:
- `api/`:
  - `TransferController`
  - `TransferRequest` / `TransferResponse`
- `config/`:
  - extend `EthRpcProperties` with fee + chain fields
- `eth/`:
  - `RawTransactionSender`
  - `NonceProvider`
- `service/`:
  - `TransferService`
  - `IdempotencyService`
  - `NonceManager`
- `persistence/`:
  - `IdempotencyRecord`
  - `IdempotencyRepository`

## Tasks
- [ ] Add transfer API scaffolding (`POST /api/v1/transfers/native`).
- [ ] Add idempotency persistence model and table.
- [ ] Add nonce manager abstraction.
- [ ] Add retry wrapper for transient RPC errors.
- [ ] Validate same request + idempotency key returns same tx hash.

## Task verification commands

1. Build modules required for Week 2 work.
```bash
cd /Users/phopper/Documents/StableCoin_Training
./mvnw -pl libs/common-domain,libs/common-observability,libs/common-testkit,services/chain-adapter-eth -am -DskipTests install
```

2. Run service.
```bash
cd /Users/phopper/Documents/StableCoin_Training/services/chain-adapter-eth
../../mvnw -f pom.xml spring-boot:run
```

3. Verify transfer endpoint wiring.
```bash
curl -i -X POST http://localhost:8081/api/v1/transfers/native \
  -H 'Content-Type: application/json' \
  -H 'Idempotency-Key: k-001' \
  -d '{"from":"0xabc","to":"0xdef","valueWei":"1000"}'
```
Expected for scaffold phase: endpoint returns a structured response (or controlled `4xx`) rather than unhandled `500`.

4. Verify idempotency table exists.
```bash
docker exec -it stablecoin-postgres psql -U stablecoin -d stablecoin -c "\\d idempotency"
```

5. Verify idempotency + nonce tests.
```bash
cd /Users/phopper/Documents/StableCoin_Training
./mvnw -pl services/chain-adapter-eth -Dtest='*Idempotency*Test,*Nonce*Test,*Retry*Test' test
```

## Notes
- Keep runtime command pinned to module POM (`-f pom.xml`) to avoid parent `mainClass` errors.
- Keep `eth.rpc.base-url` on IPv4 (`http://127.0.0.1:8545`).
