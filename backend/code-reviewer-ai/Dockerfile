# official base image
FROM eclipse-temurin:21-jre-alpine

# container working directory
WORKDIR /project/app

# copy source code from project to destination container working directory
COPY ./target/code-reviewer.jar  /project/app

# expose backend port
EXPOSE 9090

# finally run when the container starts
ENTRYPOINT ["java", "-jar", "code-reviewer.jar"]  