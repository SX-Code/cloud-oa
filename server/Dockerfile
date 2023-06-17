FROM openjdk:8
ADD service-oa.jar service-oa.jar
RUN echo "Asia/Shanghai" > /etc/timezone
EXPOSE 8800
ENTRYPOINT ["java", "-jar", "/service-oa.jar"]
LABEL maintainer="sw-code"