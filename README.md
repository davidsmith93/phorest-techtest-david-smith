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
```shell
sh mvnw spring-boot:run
```

#### Docker
When running by docker ensure the application has been build first:
```shell
sh mvnw clean compile jar:jar spring-boot:repackage
docker build . -t phorest-techtest/david-smith
docker run -p 8080:8080 phorest-techtest/david-smith
```

### Instructions for endpoints
<TODO>

#### Viewing the database
The database can be viewed at http://localhost:8080/h2-console and accessed using the following:
- JDBC URL: `jdbc:h2:mem:tech-test`
- Username: `username`
- Password: `password`

## Future iterations
<TODO>

## Missing best practices
<TODO>