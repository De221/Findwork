FROM maven:3.8.3-openjdk-17
WORKDIR /docker
COPY src src
COPY pom.xml pom.xml
COPY Docker/entrypoint.sh .
COPY Docker/findwork-0.0.1-SNAPSHOT.war app.war

# Build the Maven dependencies
#RUN mvn dependency:resolve        mvn package implicitly checks for dependency resolution

EXPOSE 8080

ENTRYPOINT ["sh", "/docker/entrypoint.sh"]