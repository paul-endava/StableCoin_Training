# Week 3: ERC-20 Integration

## Weekly goal
Deploy/test an ERC-20 and call it through Java bindings from Spring.

## Tasks
- [ ] Add ERC-20 contract in `contracts/src`.
- [ ] Add deploy script and save contract address to config.
- [ ] Generate Java wrapper and integrate with `chain-adapter-eth`.
- [ ] Implement endpoints: `balanceOf`, `transfer`, `approve`, `transferFrom`.
- [ ] Add integration tests against local Anvil.

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
