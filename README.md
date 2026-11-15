# Technical Test

- [Problem Specification](docs/spec.md)
- [Decision Log](docs/decisions.md)
- [OpenApi Specification](docs/openapi.yaml)
- [AI Usage](docs/ai)

## How to Run
### Requirements
- Java 25 SDK

### Tests
```shell
sh mvnw clean verify
```

### Server
#### Maven
This will run an in memory postgresql database using test containers.
```shell
sh mvnw spring-boot:run
```

#### Docker
This will run a dedicated postgresql database on port `5432`.
When running by docker ensure the application has been built first:
```shell
sh mvnw clean verify
docker compose up
```

### Instructions for endpoints
#### Create new machine
```
curl --header "Content-Type: application/json" \
 --header "x-version: 1" \
 --request POST \
 --data '{
  "colours": [
    "BLACK",
    "WHITE",
    "GREEN",
    "YELLOW"
  ],
  "numberOfSlots": 4,
  "floatInPence": 1000,
  "spinPriceInPence": 100,
  "smallPrizeAdjacency": 2
}' \
 http://localhost:8080/api/machines
```
#### Retrieve machine
```
curl --header "Content-Type: application/json" \
 --header "x-version: 1" \
 --request GET \
 http://localhost:8080/api/machines/<machineId>
```
#### Update machine
```
curl --header "Content-Type: application/json" \
 --header "x-version: 1" \
 --request PUT \
 --data '{
  "colours": [
    "BLACK",
    "WHITE",
    "GREEN",
    "YELLOW"
  ],
  "numberOfSlots": 4,
  "floatInPence": 1000,
  "spinPriceInPence": 100,
  "smallPrizeAdjacency": 2
}' \
 http://localhost:8080/api/machines/<machineId>
```

#### Spin the machine
```
curl --header "Content-Type: application/json" \
 --header "x-version: 1" \
 --header "x-user: 1" \
 --request POST \
 http://localhost:8080/api/machines/<machineId>
```

#### Viewing the database
The database can be viewed using the following:
- JDBC URL: `jdbc:postgresql://postgresql:5432/fruitmachine`
- Username: `admin`
- Password: `password`

## Future iterations
- Store spin results onto the database
- Add metrics for each spin (who did it, when, what machine)
- Change the Spin strategy to be more in line with reality opposed to random

## Missing best practices
- Missing code coverage
- Missing complete test suite for request validation
- Ideally would like to have a series of converters to remove conversion logic from service layers