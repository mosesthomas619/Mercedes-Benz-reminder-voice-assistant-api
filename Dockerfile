# Create builder image
FROM openjdk:11.0.9.1-jdk-slim as builder

# Copy maven wrapper
COPY mvnw .
COPY .mvn .mvn
RUN chmod +x ./mvnw

# Load Maven dependencies before touching any code in order to cache them for upcoming builds.
# Cache is invalidated as soon as any pom.xml changes.
COPY pom.xml .
COPY reminders-api/pom.xml reminders-api/
COPY reminders-app/pom.xml reminders-app/
RUN ./mvnw dependency:go-offline

# build the image
COPY . /reminders
WORKDIR /reminders
RUN chmod +x ./mvnw
RUN ./mvnw -U clean install -DskipTests=true

# Create runtime image
FROM openjdk:11.0.9.1-jre-slim

# Add non-root user
RUN addgroup --system spring && adduser --system spring --ingroup spring
USER spring:spring

# Add actual application
COPY --from=builder /reminders/reminders-app/target/reminders-app-0.0.1-SNAPSHOT.jar /app/service.jar

EXPOSE 8080
CMD ["java", "-jar", "/app/service.jar"]
