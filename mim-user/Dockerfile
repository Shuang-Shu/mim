FROM openjdk:17-jdk-slim
ARG VERSION
ENV APP_VERSION=$VERSION
WORKDIR /app
COPY ./target/mim-user-$APP_VERSION.jar /app/mim-user.jar
EXPOSE 28080
ENTRYPOINT ["java", "-jar", "/app/mim-user.jar"]
