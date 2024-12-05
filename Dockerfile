FROM openjdk:21
ARG jarFile=build/libs/currency-0.0.1-SNAPSHOT.jar
WORKDIR /opt/app
COPY ${jarFile} curApp.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "curApp.jar"]