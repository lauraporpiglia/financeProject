FROM openjdk:17-slim
#instruct docker to copy from root into the container
COPY . .
#run the gradle command that builds the jar file
RUN ./gradlew build

CMD ["java", "-jar", "./build/libs/mentoringPrj.jar"]