# Week 1: Blockchain and Node Connectivity

## Weekly goal
Stand up a runnable Spring Boot scaffold for `chain-adapter-eth`, connect it to local Anvil, and validate baseline read APIs.

## Build-first approach (recommended)
Start with scaffolding and wiring, then implement/extend endpoints.

### Scaffold structure
Create and keep this package layout in `services/chain-adapter-eth/src/main/java`:
- `api/`:
  - `ChainController`
  - `ApiExceptionHandler`
  - request/response DTOs
- `config/`:
  - `EthRpcProperties`
  - `EthClientConfig`
- `eth/`:
  - `EthereumRpcClient`
  - `RpcException`
- `service/`:
  - `ChainQueryService`
  - `TxStatusResolver`
  - `TxStatus` enum

## Tasks
- [ ] Ensure Docker daemon is running and infra containers are up.
- [ ] Verify Anvil JSON-RPC is reachable on IPv4.
- [ ] Build/install dependent modules + chain adapter module.
- [ ] Run `chain-adapter-eth` from module POM.
- [ ] Validate API responses for latest block and unknown valid tx hash.

## Task verification commands

1. Start infra.
```bash
cd /Users/phopper/Documents/StableCoin_Training
docker compose up -d
docker compose ps
```

2. Verify Anvil RPC (use IPv4).
```bash
curl -v -X POST http://127.0.0.1:8545 \
  -H 'Content-Type: application/json' \
  -d '{"jsonrpc":"2.0","method":"eth_chainId","params":[],"id":1}'
```
Expected body:
```json
{"jsonrpc":"2.0","id":1,"result":"0x7a69"}
```

3. Build needed modules.
```bash
cd /Users/phopper/Documents/StableCoin_Training
./mvnw -pl libs/common-domain,libs/common-observability,libs/common-testkit,services/chain-adapter-eth -am -DskipTests install
```

4. Run service from module POM.
```bash
cd /Users/phopper/Documents/StableCoin_Training/services/chain-adapter-eth
../../mvnw -f pom.xml spring-boot:run
```

5. Validate latest block endpoint.
```bash
curl -i http://localhost:8081/api/v1/chain/blocks/latest
```
Expected shape (example):
```http
HTTP/1.1 200
Content-Type: application/json

{"number":58,"hash":"0xe47e163c8f0dd63d827de39e4e673f6de2bbd27a479dbc4310b50fa503ebe60a","parentHash":"0x5d6f83c94aea12806c1235d4f6611a088a123faa12ead0ebd8270eec386720c5","timestamp":"0x699882fa","transactionCount":0}
```

6. Validate unknown tx hash (valid 32-byte format).
```bash
curl -i http://localhost:8081/api/v1/chain/transactions/0x0000000000000000000000000000000000000000000000000000000000000000
```
Expected shape (example):
```http
HTTP/1.1 200
Content-Type: application/json

{"hash":"0x0000000000000000000000000000000000000000000000000000000000000000","from":null,"to":null,"value":null,"nonce":null,"blockNumber":null,"status":"PENDING","confirmations":0}
```

## Notes
- If `spring-boot:run` fails with parent main-class error, run from module POM using `-f pom.xml` as shown above.
- If tx lookup uses malformed hash like `0x123`, Ethereum returns RPC error (`odd number of digits`).
- Keep `eth.rpc.base-url` as `http://127.0.0.1:8545` to avoid localhost/IPv6 issues.
