#FROM payara/server-full:6.2024.4-jdk17
#
#COPY  /build/libs/orgdirectory-service.war /opt/payara/deployments/
#
#EXPOSE 8080


FROM openjdk:17-ea-17-slim
COPY build/libs/orgdirectory-service.war app.war
ENTRYPOINT ["java", "-jar", "/app.war"]