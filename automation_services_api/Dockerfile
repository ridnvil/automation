FROM openjdk:14-alpine

#RUN apt-get update && apt-get install -y maven
COPY /target /project
RUN  cd /project

EXPOSE 9002

#run the spring boot application
ENTRYPOINT ["java", "-Xss64m", "-Xmx18G","-jar", "/project/automation-database-services-0.0.1-SNAPSHOT.jar"]