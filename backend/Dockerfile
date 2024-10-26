FROM openjdk:17-jdk-alpine

WORKDIR /src

CMD ["./gradlew", "clean", "build"]
#ARG JAR_FILE=build/libs/*.jar

#ARG JAR_FILE=C:\Users\pingppung\Documents\GitHub\ContentHub\backend\build\libs\*.jar

#COPY ${JAR_FILE} /app.jar
# JAR 파일을 복사
COPY build/libs/backend-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","./app.jar"]