FROM maven:3.6.3-jdk-11 as builder

# Copy local code to the container image.
WORKDIR /app
COPY pom.xml .
COPY src ./src

# Build a release artifact.
#RUN mvn package -D skipTests -D skipAssembly
RUN mvn package -D skipTests

FROM adoptopenjdk/openjdk11

#COPY --from=builder /app/target/*.jar /java.jar
#COPY --from=builder /app/target/dependency /dependency
COPY --from=builder /app/target/bubble-1.0-jar-with-dependencies /java.jar
# Run the web service on container startup.
#CMD ["java", "-Djava.security.egd=file:/dev/./urandom","-Dserver.port=${PORT}","-jar",
#"/java.jar"]

CMD ["java", "-Dserver.port=${PORT}", "-ea", "-cp", "java.jar", "edu.brown.cs.term_project.main.Main"]
