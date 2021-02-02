FROM openjdk:14-alpine

#RUN apt-get update && apt-get install -y maven
COPY /target /project
RUN wget https://download.newrelic.com/newrelic/java-agent/newrelic-agent/current/newrelic-java.zip && unzip newrelic-java.zip -d /opt/
RUN rm /opt/newrelic/newrelic.yml
COPY newrelic.yml /opt/newrelic
RUN  cd /project

EXPOSE 9003

#run the spring boot application
ENTRYPOINT ["java", "-javaagent:/opt/newrelic/newrelic.jar", "-Dnewrelic.config.infinite_tracing.trace_observer.host='http://172.18.0.3:9003'", "-jar","/project/automation-producer-0.0.1-SNAPSHOT.jar"]