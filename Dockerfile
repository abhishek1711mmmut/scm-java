FROM openjdk:23

WORKDIR /app

COPY target/scm-0.0.1-SNAPSHOT.jar /app

EXPOSE 8080

ENTRYPOINT [ "java", "-jar", "scm-0.0.1-SNAPSHOT.jar" ]