# Overwiev

The aml-check-service project provides a lightweight screening solution for marking money transfers as potentially suspicious based on a given text input.

If we consider this service to be called for every transfer in a real payment-processing system, it means it must respond as fast as possible and avoid consuming excessive memory.
Since the list of terrorists or sanctioned persons is theoretically limited only by the world’s population, an ideal solution should not load all data into memory just to iterate through it and discard most of it. The response time should also remain independent of the total number of database records.

To address these challenges, aml-check-service relies on PostgreSQL’s pg_trgm extension - in particular, on the GIN (Generalized Inverted Index) for trigrams.
This allows the system to quickly preselect only the most relevant records up to a certain limit. Then, the application refines those candidates by calculating a final similarity score and determining the best match.

## Core technologies

- Java 21
- Spring Boot 3.5.6 
- Spring JDBC Template - gives more flexible data access without ORM overhead, also chose it because you're using it 
- PostgreSQL
- Liquibase 
- Docker
- Swagger UI (Springdoc)

## Testing
- JUnit 5
- Testcontainers (PostgreSQL) - for integration tests against real database containers 
- Mockito
- AssertJ

## Algorithmic components
Initial candidate scoring: PostgreSQL similarity() function from the pg_trgm extension
Final score calculation: Java implementation of Jaro–Winkler Similarity

## 🚀 Running locally
The project is fully containerized - no manual setup is required.  
The only requirement is to have Docker installed on your machine.

Build and start all services:

```bash
cd aml-check-service
docker compose up --build
```
The initial build may take longer because Maven needs to download dependencies.

Once the containers are up, open the Swagger UI at:
http://localhost:8882/swagger-ui/index.html

<small>
PS: Since returning only a boolean value (true / false) is not sufficient for proper validation,
the response also includes additional fields with detailed information.

There are several possible match levels - for example, if a name does not reach the score required to be marked as a positive match,
but still achieves a relatively high similarity, the response will indicate this with a match type value: SUSPICIOUS.
</small>