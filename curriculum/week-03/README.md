# Week 3: ERC-20 Integration

## Weekly goal
Deploy/test an ERC-20 and call it through Java bindings from Spring.

## Tasks
- [ ] Add ERC-20 contract in `contracts/src`.
- [ ] Add deploy script and save contract address to config.
- [ ] Generate Java wrapper and integrate with `chain-adapter-eth`.
- [ ] Implement endpoints: `balanceOf`, `transfer`, `approve`, `transferFrom`.
- [ ] Add integration tests against local Anvil.

## Task verification commands

1. Verify ERC-20 contract compiles.
```bash
cd contracts
# If using Foundry
forge build
```
2. Verify deploy script produces contract address output.
```bash
cd contracts
# Example command; adjust for your script tooling
forge script script/Deploy.s.sol --rpc-url http://localhost:8545 --broadcast
```
3. Verify Java wrapper generation exists.
```bash
rg -n "class .*ERC20" services/chain-adapter-eth
```
4. Verify ERC-20 endpoints.
```bash
curl -i http://localhost:8081/api/v1/tokens/{tokenAddress}/balances/{walletAddress}
curl -i -X POST http://localhost:8081/api/v1/tokens/transfer -H 'Content-Type: application/json' -d '{"token":"0x...","from":"0x...","to":"0x...","amount":"25"}'
```
5. Verify integration tests against local Anvil.
```bash
./mvnw -pl services/chain-adapter-eth -Dtest='*Erc20*Integration*' test
```

## Validated code example
```java
public BigInteger tokenBalance(String holder) throws Exception {
    return erc20.balanceOf(holder).send();
}
```

```java
@Test
void transferEmitsBalanceChange() throws Exception {
    BigInteger before = client.tokenBalance(alice);
    client.transfer(alice, BigInteger.valueOf(25));
    BigInteger after = client.tokenBalance(alice);
    assertEquals(before.add(BigInteger.valueOf(25)), after);
}
```

## Validation commands
```bash
./mvnw -pl services/chain-adapter-eth test
```
