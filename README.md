# Task Manager

A project management web application built with Spring Boot.

## Features
- User registration and login with Spring Security
- Create and manage projects
- Add tasks with status and priority
- Add comments to tasks
- Each user sees only their own projects and tasks

## Tech Stack
- Java 17
- Spring Boot 3.4.5
- Spring Security
- Spring Data JPA / Hibernate
- Flyway (database migrations)
- Thymeleaf
- Bootstrap 5
- H2 (development) / PostgreSQL (production)

## How to run locally
1. Clone the repository
2. Open in IntelliJ IDEA
3. Run `TaskManagerApplication.java`
4. Open `http://localhost:8080`

By default the app runs with the `dev` profile (set in `application.properties`), which uses an in-memory H2 database — no extra setup needed, data resets on every restart.

## Running with PostgreSQL

Production uses PostgreSQL instead of H2, controlled by the `prod` Spring profile (`application-prod.properties`).

To run locally against a real PostgreSQL database instead of H2:
1. Start a local PostgreSQL instance.
2. Set the environment variables it expects: `PGHOST`, `PGPORT`, `PGDATABASE`, `PGUSER`, `PGPASSWORD`.
3. Run the app with `SPRING_PROFILES_ACTIVE=prod`.

In production (Railway), these variables are provided automatically by Railway's PostgreSQL plugin and referenced in the `task-manager` service's variables as `${{Postgres.PGHOST}}`, etc.

## Database migrations

Schema changes are managed with Flyway instead of Hibernate's auto-DDL. Migration files live in `src/main/resources/db/migration/`, named `V1__init.sql`, `V2__description.sql`, and so on. Flyway applies them in order and keeps track of what has already run.

- In the `dev` profile, Flyway is disabled — H2 recreates the schema from the entity classes on every restart.
- In the `prod` profile, Flyway runs on startup and `ddl-auto` is set to `validate`, meaning Hibernate only checks the schema matches the entities instead of modifying it.

To add a schema change: create a new file like `V2__add_something.sql` with the SQL, commit it, and it will run automatically on the next production deploy.

## Live demo
https://task-manager-production-c612.up.railway.app