# Voice-Assistant Reminder-Skill

## Preface

This project serves as a coding challenge for hiring candidates for the Hybrid Cloud Platform
department at MBition GmbH. Please do not distribute the content, generated artifacts or your
solution, especially not on any publicly available platform.

## Application

This application serves as a REST service which provides endpoints that handle reminders for the
Mercedes Benz Voice-Assistant Platform. It is a multi-module project using Java 11, Spring-Boot and
Maven.

The API is specified in `reminders-api/reminders-api-spec.yaml` and built using
the `openapi-generator-maven-plugin`. The build artifacts serve as a foundation for the actual
implementation in the `reminders-app` module.

### Getting Started

To build and run the application, it is generally advised to use Docker, but running it directly via
JDK is also possible.

#### Build and run with Docker

All commands below assume that the docker host will resolve to `127.0.0.1`(`localhost`) and that port `8080` is
free.

The Docker image can be built using:

```
docker build . -t reminders
```

The built image can be run using:

```
docker run -i -p 8080:8080 reminders
```

#### Build and run with JDK/Maven

While with Docker it is possible to build and run the service without having a JDK installed on the
machine, having a compatible JDK should reduce IDE warnings and enable running smaller builds or
tests via CLI and IDE. The project is targeted to Java 11, but any later JDK version should also
work.

Generate artifacts from the API module (this is also required whenever `reminders-api-spec.yaml` is
changed):

```
./mvnw install -f reminders-api/pom.xml       
```

Build and run the application:

```
./mvnw spring-boot:run -f reminders-app/pom.xml        
```

### Test the service

The service initially contains one endpoint, that can be called with the following URL:
```http://127.0.0.1:8080/dummies/foo?bar=1234```

The project also includes a Swagger-UI to explore and test the API:
```http://127.0.0.1:8080/swagger-ui/index.html#```