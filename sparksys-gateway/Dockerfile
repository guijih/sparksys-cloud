FROM openjdk:8-jre
MAINTAINER zhouxinlei
RUN cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
    && echo "Asia/Shanghai" > /etc/timezone
VOLUME /tmp
# 声明服务运行在8000端口
EXPOSE 8088
# 将当前目录下的jar包复制到docker容器的/目录下
ADD sparksys-gateway-1.0-SNAPSHOT.jar app.jar
# 指定docker容器启动时运行jar包
ENTRYPOINT ["java","-Xms360m","-Xmx360m","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar","--spring.profiles.active=dev"]
