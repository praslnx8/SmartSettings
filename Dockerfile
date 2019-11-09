FROM openjdk:8-jre-alpine

ENV APPLICATION_USER ktor
RUN adduser -D -g '' $APPLICATION_USER

RUN mkdir /restApiApp
RUN chown -R $APPLICATION_USER /restApiApp

USER $APPLICATION_USER

COPY restApi/build/libs/restApi-backend-all.jar /restApiApp/restApi.jar
WORKDIR /restApiApp


CMD ["java", "-server", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseCGroupMemoryLimitForHeap", "-XX:InitialRAMFraction=2", "-XX:MinRAMFraction=2", "-XX:MaxRAMFraction=2", "-XX:+UseG1GC", "-XX:MaxGCPauseMillis=100", "-XX:+UseStringDeduplication", "-jar", "restApi.jar"]