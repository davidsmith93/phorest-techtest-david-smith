# Technical Test

- [Problem Specification](docs/spec.md)
- [Decision Log](docs/decisions.md)
- [OpenApi Specification](docs/openapi.yaml)

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
<TODO>

#### Viewing the database
The database can be viewed using the following:
- JDBC URL: `jdbc:postgresql://postgresql:5432/fruitmachine`
- Username: `admin`
- Password: `password`

## Future iterations
<TODO>

## Missing best practices
<TODO>