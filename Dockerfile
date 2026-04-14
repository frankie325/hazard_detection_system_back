# ============================================
# Stage 1: Build the JAR
# ============================================
FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /app

# 复制所有 pom.xml（parent + 3个子模块）
COPY pom.xml ./
COPY expressway-common/pom.xml expressway-common/
COPY expressway-pojo/pom.xml expressway-pojo/
COPY expressway-server/pom.xml expressway-server/

# 复制源码
COPY expressway-common/src expressway-common/src
COPY expressway-pojo/src expressway-pojo/src
COPY expressway-server/src expressway-server/src

# 从 /app 根目录运行，parent pom 定义了 <modules>，-pl 选取目标模块，-am 同时构建其依赖
WORKDIR /app
RUN mvn clean package -DskipTests -pl expressway-server -am

# ============================================
# Stage 2: Runtime
# ============================================
FROM eclipse-temurin:21-jre-alpine

# 创建非 root 用户（安全最佳实践）
RUN addgroup -S expressway && adduser -S expressway -G expressway

WORKDIR /app

# 从构建阶段复制 fat jar
COPY --from=builder /app/expressway-server/target/expressway-server-*.jar app.jar

# 切换到非 root 用户
USER expressway

# JRE 只读目录，需要写权限的地方挂载 volume
# /app/logs 由 docker-compose 的 volume 挂载

EXPOSE 8080

# SPRING_PROFILES_ACTIVE=prod 可在 docker-compose 中通过 environment 指定
ENTRYPOINT ["java", "-jar", "app.jar"]
