FROM openjdk:8-jdk-alpine
RUN mkdir /opt/app
EXPOSE 8080
COPY build/libs/bootcamp-java-mysql-project-1.0-SNAPSHOT.jar /opt/app
WORKDIR /opt/app
CMD ["java", "-jar", "bootcamp-java-mysql-project-1.0-SNAPSHOT.jar"]
