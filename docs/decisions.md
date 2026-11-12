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