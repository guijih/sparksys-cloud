FROM harbor.en2hr.com:8084/hongneng/libreoffice:latest
MAINTAINER zhouxinlei
VOLUME /tmp
EXPOSE 8901
ADD sparksys-file-server-1.0-SNAPSHOT.jar app.jar

ENTRYPOINT ["java","-Xms360m","-Xmx360m","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar","--spring.profiles.active=prod"]
MAINTAINER zhouxinlei
