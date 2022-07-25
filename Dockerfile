FROM adoptopenjdk/openjdk11

ADD target/CloudStorage-0.0.1-SNAPSHOT.jar cloudapi.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "/cloudapi.jar"]