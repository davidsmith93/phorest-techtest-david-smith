# Input
Next I need to create a docker-compose which contains the following:

* Containers
   * postgresql
      * Image should be the latest for 18, ideally an alpine version
      * Exposes default ports
      * The database should be called `fruitmachine`
      * The username should be `admin`
      * The password should be `password`
   * server
      * Image should be the latest version of the image of the service. The name is `phorest-techtest-david-smith`
      * Exposes ports 8080 on 8080
      * No environment variables are needed as they are baked into the image
* Network
   * A new network should be created to allow the two to communicate
   * The two containers should be running on this network

# Output
services:
  postgresql:
    image: postgres:18.7-alpine
    container_name: postgresql
    environment:
      POSTGRES_DB: fruitmachine
      POSTGRES_USER: admin
      POSTGRES_PASSWORD: password
    ports:
      - "5432:5432"
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U admin -d fruitmachine"]
      interval: 5s
      timeout: 5s
      retries: 10
    networks:
      - fruitmachine-net

  server:
    image: phorest-techtest-david-smith:latest
    container_name: server
    depends_on:
      postgresql:
        condition: service_healthy
    ports:
      - "8080:8080"
    networks:
      - fruitmachine-net

networks:
  fruitmachine-net:
    driver: bridge