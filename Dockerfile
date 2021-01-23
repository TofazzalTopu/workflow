FROM openjdk:8
EXPOSE 8060
ADD target/workflow.jar workflow.jar
ENTRYPOINT ["java", "-jar", "workflow.jar"]