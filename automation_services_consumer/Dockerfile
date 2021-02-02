FROM openjdk:14-alpine

#RUN apt-get update && apt-get install -y maven
COPY /target /project
RUN chmod +x /project
RUN  cd /project
RUN touch run.sh

EXPOSE 8585

#run the spring boot application
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom" ,"-jar","/project/automation-0.0.1-SNAPSHOT.jar"]