FROM gradle AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle :restApi:build --no-daemon

FROM openjdk:8-jre-slim
EXPOSE 8080
EXPOSE 8443
RUN mkdir /app
COPY --from=build /home/gradle/src/restApi/build/libs/*.jar /app/restApi.jar
WORKDIR /app

CMD ["java", "-server", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseCGroupMemoryLimitForHeap", "-XX:InitialRAMFraction=2", "-XX:MinRAMFraction=2", "-XX:MaxRAMFraction=2", "-XX:+UseG1GC", "-XX:MaxGCPauseMillis=100", "-XX:+UseStringDeduplication", "-jar", "restApi.jar"]