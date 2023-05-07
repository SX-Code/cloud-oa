FROM java:8
ADD service-oa.jar service-oa.jar
RUN bash -c 'touch /service-oa.jar'
EXPOSE 8800
ENTRYPOINT ["java", "-jar", "/service-oa.jar"]
MAINTAINER sw-code