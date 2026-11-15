# AI Overview
Due to not having access to my preferred AI tooling (Claude Code), I instead used the free version of Claude to perform some initial tasks that can live outside the codebase. Claude Code allows Claude to run against a local repository, but the free version does not and as such I didn't feel it would be beneficial to rely on it for actual implementation of code. The model is also restricted so the plans aren't as detailed as I would have liked, and I wasn't asked questions to flesh out the plan properly.

## Implementation plans
- [Phase 1](implementation-plan-phase-1.md)
- [Phase 2](implementation-plan-phase-2.md)

The main use was to summaries the steps in the spec to something that could be picked up by an individual, but specifically each step within the phase could become a dedicated ticket with clear deliverables.

## Docker Compose
- [docker-compose](docker-compose.md)

The main use of this was to generate a genric docker-compose file so that a stand alone database and service could be spun up together opposed to using an in memory database that the default profile uses. The format of these is simple and well understood, so providing the correct image names gave me a functional file.

## Other uses
Due to the limitations of the free version not having direct access to the code I couldn't use it to perform a number of tasks I feel it would have been suitable for:
- Unit tests
  - Including checking for missed coverage
- Integration tests
  - This is present in the MachinesControllerIntegrationTests as a number of validation conditions have been omitted
- Running a local code review of the code base to ensure best practices are adhered to etc
- Implementations of basic endpoints like the CRU endpoints for the machine
- Creation of the docker file itself
- Generation of the openapi spec from the code