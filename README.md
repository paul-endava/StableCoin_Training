# Stablecoin Bootcamp (Java/Spring)

This repository is a 12-week learning and build track for stablecoin payment orchestration on Ethereum with Java/Spring.

## Quick start

1. Start infrastructure:

```bash
docker compose up -d
```

2. Import into IntelliJ as a Maven project from `StableCoin_Training`.
3. Follow weekly guides under `curriculum/week-XX/README.md`.

## Repo layout

- `services/`: Spring Boot services
- `libs/`: shared Java libraries
- `contracts/`: Solidity contracts and tests
- `infra/`: Docker and local environment config
- `docs/`: architecture notes, ADRs, runbooks
- `curriculum/`: weekly task boards and validation examples
