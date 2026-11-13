# Decision log
## Phase 1
Phase 1 will contain the following:
- Base project using Java 25 (latest LTS version of Java), Maven and the latest version of Spring Boot
- Api contract for phase 1 endpoints
- A simple endpoint which rolls the slots and reports the outcome and the payout mechanism
- Unit and integration tests
- A dockerfile containing a user who only has access to run the application
  - A dedicated `docker` profile will be used when running the application

### Differences to the generated implementation plan by AI
- Configuration of colours will be hard coded right now, as part of step 3 is to include hundreds of options. Step 4 build upon this to then allow the machines to be configured with colours, slots, cost etc. 
- The suggested contract is `/api/v1/machine/spins`, I will instead favor `/api/machines/{id}/spins` with the version being provided as a header:
  - This allows additional machines to be configured and once one is drained of balance in the future, a new one can be used 
  - A header for versioning is my preferred style as it keeps the endpoint definition bespoke to the subject (in this case a new spin)
- The original outcome states `NO_MATCH` as an outcome, which isn't strictly true, so will be replaced with `LOSS`.
- The original openapi spec used `columns`. To make everything consistent, `slots` has been used instead.

## Phase 2
Phase 2 will contain the following:
- A In memory PostgreSql databases
  - This will be run via test containers for simplicity
  - The machine configuration will be moved here
  - A user will be created which will keep track of the free spins for that user
- 2 new outcome evaluators for full houses and small prize
- New fields on the response object to contain information about the float, payout amount etc
- A new OutcomeStrategy which will be responsible for managing the float and calculating free spins

### Assumptions
- The user will have been verified before it reaches this service, so for simplicity I will relay on a new header `x-user` to provide that context.
  - When not present the request will be rejected
- When a user has free spins available, the float will not increase on the machine.
- The conditions for FULL_HOUSE payout are vague. For a real implementation I would delegate to product to determine the outcome. The following assumptions are being made:
  - When the number of columns do not match the number of possible colours
    - If there are more columns than colours, we just need to see all the colours
    - If there are less columns than colours, then every column needs to be unique
  - When the number of columns do match the number of possible colours
    - All colours must be present

## Phase 3
### Assumptions
- When multiple payouts are achieved, only the first is taken into consideration, based on the ordering of JACKPOT > FULL_HOUSE > SMALL_PRIZE