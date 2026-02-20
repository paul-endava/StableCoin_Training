# Week 3: ERC-20 Integration

## Weekly goal
Scaffold token integration boundaries, then implement ERC-20 reads/transfers through dedicated client/service classes.

## Build-first approach (recommended)
Create token-focused package boundaries first, then wire endpoints.

### Scaffold structure
Extend `services/chain-adapter-eth/src/main/java`:
- `api/`:
  - `TokenController`
  - `TokenTransferRequest` / `TokenBalanceResponse`
- `eth/`:
  - `Erc20Client`
  - `ContractCallException`
- `service/`:
  - `TokenQueryService`
  - `TokenTransferService`
- `config/`:
  - `TokenConfigProperties` (contract addresses / defaults)

Also add contract scaffolding under `contracts/`:
- `contracts/src/` (ERC-20 contract)
- `contracts/script/` (deploy script)
- `contracts/test/` (contract tests)

## Tasks
- [ ] Compile ERC-20 contract scaffold.
- [ ] Add deploy script scaffold and capture deployed address.
- [ ] Add `Erc20Client` abstraction in Java.
- [ ] Add token API scaffolding (`balanceOf`, `transfer`).
- [ ] Add integration test skeletons against Anvil.

## Task verification commands

1. Verify Anvil is reachable.
```bash
curl -s -X POST http://127.0.0.1:8545 \
  -H 'Content-Type: application/json' \
  -d '{"jsonrpc":"2.0","method":"eth_chainId","params":[],"id":1}'
```
Expected:
```json
{"jsonrpc":"2.0","id":1,"result":"0x7a69"}
```

2. Verify contract compile (Foundry path).
```bash
cd /Users/phopper/Documents/StableCoin_Training/contracts
forge build
```

3. Verify deploy script wiring (example).
```bash
cd /Users/phopper/Documents/StableCoin_Training/contracts
forge script script/Deploy.s.sol --rpc-url http://127.0.0.1:8545 --broadcast
```

4. Verify Java ERC-20 scaffolding exists.
```bash
cd /Users/phopper/Documents/StableCoin_Training
rg -n "class Erc20Client|class TokenController|class TokenQueryService" services/chain-adapter-eth/src/main/java
```

5. Verify token integration tests.
```bash
cd /Users/phopper/Documents/StableCoin_Training
./mvnw -pl services/chain-adapter-eth -Dtest='*Erc20*Test,*Token*Integration*' test
```

## Notes
- Scaffold first; endpoint payload contracts should be stable before full chain write logic.
- Use `127.0.0.1` for RPC in deploy scripts and service config.
