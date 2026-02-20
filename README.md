# Stablecoin Bootcamp (Java/Spring)

This repository is a 12-week learning and build track for stablecoin payment orchestration on Ethereum with Java/Spring.

## Docker setup (macOS)

1. Install Docker Desktop for Mac:
   - Download and install from [Docker Desktop](https://www.docker.com/products/docker-desktop/).
2. Start Docker Desktop and wait until the status shows Docker is running.
3. Verify installation:

```bash
docker --version
docker compose version
docker run --rm hello-world
```

4. If you use Apple Silicon (M1/M2/M3), keep the default ARM setup unless a specific image requires x86 emulation.

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
