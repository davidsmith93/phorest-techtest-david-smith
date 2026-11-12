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
- Configuration of colours will be hard coded right now, as part of step 3 is to include hundreds of options, I intend for these to be the hex codes and generated based on the number of columns provided where `the number of colours = the number of columns`
  - This is based on the requirement in step 1 to have 4 columns and 4 colours and due to lack of information on this, right now I intend to keep this logic in place but is subject to change.
  - An alternative approach would be to allow the number of colours to be specified on request and will be explored in greater detail when step 3 is reached.
  - The suggested contract is `/api/v1/machine/spins`, I will instead favor `/api/machines/{id}/spins` with the version being provided as a header:
    - This allows additional machines to be configured and once one is drained of balance in the future, a new one can be used 
    - A header for versioning is my preferred style as it keeps the endpoint definition bespoke to the subject (in this case a new spin)