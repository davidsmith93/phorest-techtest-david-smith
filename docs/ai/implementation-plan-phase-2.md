# Input:
Onto step 2. I need to expand on the design that has been implemented as part of step 1. The implementation follows the implementation plan that was suggested previously.
Step 2 needs to do the following:

* A machine has a `float` , this is the starting balance of a machine. For simplicity, this should be in pence eg £10.00 would be represented as `1000`and be called `floatInPence`
* When a spin is triggered for the machine, a fixed amount should be added to the machines float, unless the user  has free spins for the machine.
* 2 new payout methods need to be added
   * Full house - if all slots are different colours
   * Small prize - two or more adjacent slots are the same colour
* When a payout is detected then the following should happen:
   * Jackpot - The entire `float` should be paid out and returned. To calculate the current `float` the following equation should be used: `float = 0`
   * Full house - Half the `float` should be paid out and returned. To calculate the current `float` the following equation should be used: `float = float - (float/2)` 
      * This should always round the payout up in case of a decimal
   * Small prize - 5 times the price to pay should be paid out and returned. To calculate the current `float` the following equation should be used: `float = float - (5 * priceToPayInPence)`
* The `float` can **never** be negative.
   * If the `float` would become negative for a small prize, the user should be credited with free spins equal to the payout. This can be calculated via `freeSpins = outstandingBalance/priceToPayInPence`.
      * One example of this is `outstandingBalance = 1000`, `priceToPayInPence = 100`. The `freeSpins` would become `10`and the remaining `balance` on the machine would become `0`.
* A user should be saved.
* A users free spins should be saved and reference the machine and user. 

A new implementation plan should be created for this.

# Output:
# Implementation Plan: Fruit Machine API, Phase 2 (Float, Payouts, Users and Free Spins)

This phase builds on the Phase 1 plan: the OpenAPI-first spin endpoint, the `Colour` and `Outcome` types, `SlotSpinner`, `OutcomeEvaluator`, `FruitMachineService`, `PayoutStrategy`, error handling, integration tests and the Docker image. Each step is small, ends in a verifiable deliverable, and leaves the build green.

## Rules being implemented

- All money is an integer number of pence (`long`). The machine's float is called `floatInPence`, e.g. £10.00 is `1000`. (`float` is a reserved word in Java, so the suffixed name is used everywhere.)
- A machine has a `floatInPence` that starts at a configured value and changes as it is played. It can never be negative.
- Every spin first adds a fixed `priceToPayInPence` to the machine's float, unless the user has a free spin for that machine. In that case one free spin is used and nothing is added.
- Outcomes and payouts (the float here already includes this spin's price):
  - **Jackpot** (all slots the same colour): pays the entire float. `float = 0`.
  - **Full house** (all slots different colours): pays half the float, **rounded up**. `payout = ceil(float / 2)`, `float = float - payout`.
  - **Small prize** (two or more adjacent slots the same colour): pays `5 * priceToPayInPence`. `float = float - (5 * priceToPayInPence)`.
  - **No match**: nothing is paid.
- If a small prize would take the float below zero, the float becomes `0` and the user is credited free spins: `freeSpins = outstandingBalance / priceToPayInPence`. For example, an outstanding balance of `1000` at a price of `100` gives `10` free spins.
- Users are saved. A user's free spins are saved and reference both the user and the machine.

## Decisions and assumptions to confirm

- **Precedence:** a spin gets one outcome, checked in the order jackpot, full house, small prize, no match. A jackpot is also "adjacent slots the same", so the order matters.
- **Full house rounding:** the payout rounds up and the float keeps the rounded-down half, so `payout + remaining float` always equals the float before the payout. For example, float `1001` pays `501` and leaves `500`. In integer maths, `payout = (float + 1) / 2`.
- **Shortfall handling:** the outstanding balance is the small prize minus the float available. The user is paid the available float in cash, and the shortfall is converted to free spins, rounded down (leftover pence are dropped). Example: small prize `500`, float `300`, price `100` gives cash `300`, outstanding `200`, `2` free spins and float `0`. The response reports the cash actually paid as `payoutInPence`.
- **Jackpot and full house** can never take the float below zero, so the shortfall rule only applies to the small prize.
- **Free spins are used automatically:** if the user has a free spin for the machine, a spin uses it. There is no opt-out. A free spin still evaluates and pays out as normal.
- **Free spin storage:** one row per user and machine holding a count, with a unique constraint on the pair, rather than one row per free spin.
- **Persistence is now needed:** because users and free spins are saved, Spring Data JPA with an H2 in-memory database is used, behind repository interfaces so a real datastore can replace it later. In memory means data is lost on restart. A file-backed or external database would need a writable location, which conflicts with the Docker user having no write access, so that is deferred.
- **Machine identity:** free spins reference a machine, so machines have an id. One machine is seeded at startup from config, and only if it does not already exist, so a restart never resets an existing float.
- **API shape:** `POST /api/v1/users` creates a user. `POST /api/v1/machines/{machineId}/spins` takes a `userId` in the body. Unknown users or machines return 404 problem details. There is no authentication; the user id is trusted as supplied.
- **User fields:** a user has an id (UUID) and a `name`. The brief does not say more.
- **Concurrency:** each spin runs in one transaction that locks the machine row, so concurrent spins on a machine are serialised and the float cannot be corrupted.

## Steps

### 1. Update the OpenAPI spec (contract first)

- Add `POST /api/v1/users` (request `name`, response `id` and `name`).
- Change the spin operation to `POST /api/v1/machines/{machineId}/spins` with a `userId` in the request body.
- Extend the spin response with `payoutInPence`, `floatInPence` and `freeSpinsAwarded`. Extend `outcome` to `JACKPOT`, `FULL_HOUSE`, `SMALL_PRIZE` and `LOSS`.
- Add 400 and 404 problem detail responses, and document the pence convention and payout rules in the field descriptions.
- **Deliverable:** an updated spec that passes the linter and renders in Swagger UI or Redoc.

### 2. Machine configuration

- Add `machine.id`, `machine.float-in-pence` (at least 0) and `machine.price-to-pay-in-pence` (greater than 0, so the free spin division is always safe) to the `@ConfigurationProperties` record, with defaults such as float `1000` and price `100`.
- **Deliverable:** config tests for defaults and invalid values.

### 3. Machine domain (float invariant)

- Add a `Machine` domain type holding its id, price and `floatInPence`, with operations to add the play price and to pay out an amount. The type guarantees the float never goes below zero.
- **Deliverable:** unit tests for adding the price and for payouts that would otherwise go negative.

### 4. Evaluator chain

- Extend the `Outcome` enum with `FULL_HOUSE` and `SMALL_PRIZE`.
- Refactor evaluation into an ordered list of rules, each returning an outcome or nothing. The first match wins, and no match falls through to `NO_MATCH`. Keep the existing jackpot rule first.
- **Deliverable:** the existing jackpot tests still pass, plus a rule-order test.

### 5. Full house rule

- Add a `FullHouseEvaluator`: all slots are different colours, checked with a set whose size equals the slot count.
- **Deliverable:** unit tests for all-different, one repeat, and a slot count greater than the colour count (never a full house).

### 6. Small prize rule

- Add a `SmallPrizeEvaluator`: two or more adjacent slots with the same colour, checked in a single pass over neighbouring pairs.
- **Deliverable:** unit tests for pairs at the start, middle and end, non-adjacent repeats (no prize), and precedence over no match.

### 7. Payout calculation

- Rework the `PayoutStrategy` seam into a pure calculation of the prize owed: the whole float for a jackpot, `ceil(float / 2)` for a full house, `5 * price` for a small prize, and `0` for no match.
- **Deliverable:** unit tests for each outcome, including an odd float to confirm rounding up.

### 8. Settlement (float, shortfall and free spins)

- Add a settlement step that applies the prize to the machine and returns the cash paid, the free spins awarded and the new float.
- Jackpot sets the float to `0`. Full house deducts the rounded-up half. Small prize deducts the prize if the float covers it, otherwise it pays the available float in cash, sets the float to `0` and converts the shortfall to free spins via `shortfall / price`.
- **Deliverable:** unit tests for a sufficient float, an exact float, a shortfall with whole and fractional free spins, and the `1000` / `100` example giving `10` free spins. Add a randomised test confirming the float is never negative.

### 9. Persistence foundation

- Add Spring Data JPA, H2 and Flyway, with a first migration for the schema. Keep JPA entities in an adapter package, separate from the domain types.
- **Deliverable:** the context loads with the schema applied, and a `@DataJpaTest` smoke test passes.

### 10. User persistence

- Add the `User` domain type, a `UserRepository` interface and its JPA adapter, plus a user service that creates a user.
- **Deliverable:** repository tests for save and find, and unit tests for the service, including blank-name validation.

### 11. Machine persistence and seeding

- Add a `MachineRepository` interface and its JPA adapter, with a locking read for the spin transaction.
- Seed the configured machine at startup only when it does not already exist.
- **Deliverable:** tests proving the machine is seeded once and that a restart keeps the existing float.

### 12. Free spin persistence

- Add a `FreeSpins` record referencing the user and the machine, holding a count, with a unique constraint on the pair and foreign keys to both. Add a `FreeSpinRepository` interface and adapter.
- Expose operations to credit free spins and to consume one, which fails if there are none, so the count never goes below zero.
- **Deliverable:** repository tests for credit, consume, the unique constraint, the foreign keys and consuming when empty.

### 13. Spin orchestration

- Update `FruitMachineService.spin(machineId, userId)` to run in one transaction: load the user (not found is an error), lock the machine, then consume a free spin if one exists or otherwise add the price to the float. Then spin, evaluate, calculate the payout, settle, credit any free spins awarded, save, and return the extended `SpinResult`.
- **Deliverable:** unit tests with mocked collaborators for paid and free spins, unknown user and unknown machine, plus a parallel-spin test showing the float accounting stays consistent.

### 14. Controllers, DTOs and errors

- Add the user controller and update the spin controller to match the spec, mapping from the domain so it stays decoupled from the contract. Add problem detail handling for 404 and validation errors.
- **Deliverable:** `@WebMvcTest` slice tests for the new endpoints, fields, enum values and error responses.

### 15. Integration tests

- Use a test configuration with a scripted spinner so each scenario is deterministic.
- Cover end to end: creating a user, a paid spin adding to the float, a free spin not adding to it, a jackpot, a full house with even and odd floats, a small prize, a shortfall awarding free spins that are then used on the next spin, no match, state persisting across requests, and unknown user or machine.
- **Deliverable:** Failsafe (`*IT`) tests passing against the running application.

### 16. Docs and Docker check

- Update the README with the rules, the decision log above and `curl` examples for the new endpoints.
- Confirm the config is overridable under the `docker` profile, and that the container still starts and spins as the non-root user.
- **Deliverable:** `mvn clean verify` passes with coverage thresholds met, and the README and Docker image work from a clean checkout.

## Design notes

- **Extensibility:** each outcome is its own rule and its own payout entry, so a new prize means a new evaluator and a payout mapping, not changes to the service. Repository interfaces keep the datastore swappable.
- **Testing approach:** unit test the rules and settlement as pure logic, using a scripted spinner so outcomes are exact rather than statistical. Integration tests cover persistence and the full flow.
- **Out of scope:** authentication, user update or delete, machine management endpoints, an opt-out for using free spins, and a durable datastore.