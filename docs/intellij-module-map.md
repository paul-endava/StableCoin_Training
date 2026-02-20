# IntelliJ Module Map

Import root `StableCoin_Training` as a single Maven project.

## Modules

### Shared libs
- `libs:common-domain`
- `libs:common-observability`
- `libs:common-testkit`

### Services
- `services:api-gateway`
- `services:payment-orchestrator`
- `services:wallet-service`
- `services:chain-adapter-eth`
- `services:event-indexer`
- `services:ledger-service`
- `services:compliance-service`
- `services:notification-service`

## Suggested run configurations
- `chain-adapter-eth` (active profile: `local`)
- `payment-orchestrator` (depends on Postgres)
- `event-indexer` (depends on local Ethereum node)

## Suggested module dependencies
- Service modules depend on `libs:common-domain`
- All runtime services depend on `libs:common-observability`
- All integration tests depend on `libs:common-testkit`

## Local ports
- API gateway: `8080`
- Chain adapter: `8081`
- Orchestrator: `8082`
- Event indexer: `8083`
- Wallet service: `8084`
- Ledger service: `8085`
- Compliance service: `8086`
- Notification service: `8087`
