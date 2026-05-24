# 基于Java 8镜像
FROM openjdk:8-jdk-alpine

# 维护者信息
LABEL maintainer="photo-booking"

# 设置工作目录
WORKDIR /app

# 复制jar包到容器中
COPY target/social-photo-booking-backend-1.0.0.jar app.jar

# 暴露端口
EXPOSE 8080

# 运行jar包
ENTRYPOINT ["java", "-jar", "app.jar"]
