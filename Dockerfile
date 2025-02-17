# Build Stage
FROM maven:3.8.3-openjdk-17
MAINTAINER saal
WORKDIR /app
COPY pom.xml /app/pom.xml
COPY src /app/src
COPY serenity.properties app/serenity.properties
RUN mvn dependency:go-offline
ENV environment qa
ENTRYPOINT ["mvn"]
CMD ["verify", "-Dmaven.test.failure.ignore=true"]
