# Input:

I need to create an implementation plan for the following requirements. Each step should be a small and concise feature with a clear deliverable at the end of each step.

    A spring boot application
    Running on Java 25
    Built using maven

The main body of implementation needs to do to the following:

    A fruit machine needs to be accessible via rest api calls
    The machine should have slots which can display one of 4 colours (black, white, green, or yellow)
    When the api is called, all 4 slots should be spun, and the outcomes assessed
    When a match of all 4 slots is detected, then a Jackpot is returned
    When no matches are detected then No Match is returned

Unit and integration tests are required, along with an openapi spec.

The openapi spec should be delivered first and should initially just work on interacting with the machine. The implementation should be extensible in the future, to facilitate database integrations, other payout mechanisms, more columns and more colours.

A few things to note:
- We don't need contract tests as part of this, just unit and integration tests.
- We don't need a database at this point in time.
- We should create a docker file to run the application using the docker profile. This file should be run by a user who only has access to run the application.

# Output:
# Implementation Plan: Fruit Machine API

A Spring Boot (Java 25, Maven) fruit machine exposed via REST. Each step is small, ends in a verifiable deliverable, and leaves the build green.

## Steps

### 1. OpenAPI spec (contract first)

- Write `openapi.yaml` (OpenAPI 3.1) with a versioned `POST /api/v1/machine/spins` operation.
- Response schema: `slots` (array of colour enums), `outcome` (enum: `JACKPOT`, `NO_MATCH`).
- Colours and outcomes are string enums, and slots are an array rather than `slot1..slot4`, so more columns, colours and outcomes are non-breaking later.
- Add a standard error response (RFC 9457 problem details).
- **Deliverable:** a spec that passes a linter (e.g. Spectral or Redocly) and can be rendered in Swagger UI or Redoc.

### 2. Project skeleton

- Create a Maven Spring Boot project targeting Java 25 (`<java.version>25</java.version>`), using the latest Spring Boot release that supports Java 25. Include the `web` and `test` starters.
- Move `openapi.yaml` to `src/main/resources`.
- **Deliverable:** `mvn verify` passes with a context-loads test.

### 3. Domain model

- Add the `Colour` enum (BLACK, WHITE, GREEN, YELLOW), the `Outcome` enum and an immutable `SpinResult` record (slots plus outcome).
- **Deliverable:** domain types with unit tests.

### 4. Machine configuration

- Bind `machine.slot-count` (4) and `machine.colours` (the four colours) to a `@ConfigurationProperties` record, with validation, e.g. a minimum of 2 slots.
- **Deliverable:** config is externalised, with tests for defaults and invalid values.

### 5. Slot spinner

- Define a `SlotSpinner` interface and a `RandomSlotSpinner` implementation, using an injectable `RandomGenerator` so tests can be deterministic.
- It spins `slot-count` slots, each picking uniformly from the configured colours.
- **Deliverable:** unit tests with a seeded generator covering slot count, valid colours and distribution sanity.

### 6. Outcome evaluator

- Define an `OutcomeEvaluator` interface and an `AllSlotsMatchEvaluator`: all slots equal gives `JACKPOT`, otherwise `NO_MATCH`.
- **Deliverable:** unit tests covering all-match, one-different, two pairs, and a different slot count.

### 7. Machine service

- Add `FruitMachineService.spin()`, which calls the spinner then the evaluator and returns a `SpinResult`.
- **Deliverable:** unit tests with mocked collaborators.

### 8. REST controller

- Add `POST /api/v1/machine/spins`, with API DTOs mapped from the domain model so the domain stays decoupled from the contract.
- **Deliverable:** `@WebMvcTest` slice tests asserting status, JSON shape and enum values.

### 9. Error handling

- Add a `@RestControllerAdvice` returning `ProblemDetail` for unexpected failures and unsupported methods.
- **Deliverable:** tests proving error responses match the spec.

### 10. Integration tests

- Add `@SpringBootTest` integration tests with a real HTTP client against a random port, exercising `POST /api/v1/machine/spins`.
- Fix the random seed for deterministic jackpot and no-match cases.
- **Deliverable:** integration tests running via Failsafe (`*IT`), covering both outcomes and the error responses.

### 11. Payout extension seam

- Add a `PayoutStrategy` interface with a default no-op implementation. The service calls it after evaluation.
- No persistence is added in this version.
- **Deliverable:** the service wired through the interface, with a test showing a custom implementation can be swapped in. A future payout mechanism is then a new bean, not a rewrite.

### 12. Docker image

- Add `application-docker.yml` for the `docker` Spring profile (e.g. server port, logging to stdout).
- Add a multi-stage `Dockerfile`: a Maven build stage on a Java 25 JDK image, and a runtime stage on a Java 25 JRE image.
- In the runtime stage, create a dedicated non-root user and group with no login shell and no write access to the application files. The jar is owned by root and read-only, so that user can only run the application.
- Set `USER` to that account and start the app with `SPRING_PROFILES_ACTIVE=docker`. Add a `.dockerignore`.
- **Deliverable:** `docker build` and `docker run` start the app under the `docker` profile as the non-root user, verified with a `curl` to the spins endpoint and `docker exec <container> id`.

### 13. Build quality and docs

- Add JaCoCo coverage thresholds, serve the spec at `/v3/api-docs` or as a static file, and write a README with run, test and Docker instructions and example `curl` calls.
- **Deliverable:** `mvn clean verify` produces a coverage report, and a new developer can run the app from the README alone.

## Design notes

- **Extensibility:** more columns means changing `slot-count`, and more colours means adding enum values and config. New payout rules mean new `OutcomeEvaluator` or `PayoutStrategy` beans. A database is out of scope for now; persistence can later be added behind a new interface. None of these should change the controller or the contract's shape.
- **Test expectations:** with 4 colours and 4 slots, a uniform spin gives 4 jackpot combinations out of 256, or 1 in 64. Use seeded or stubbed spinners for exact assertions rather than statistical ones.
- **Assumption to confirm:** "slots" and "columns" are treated as the same thing, i.e. reels in the machine.