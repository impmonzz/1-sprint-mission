# ✅ Build Stage
FROM gradle:8.6-jdk17-alpine AS builder

# 작업 디렉토리 설정
WORKDIR /build-env

# Gradle 관련 파일 복사
COPY gradlew ./gradlew
COPY gradle ./gradle

# 설정 파일 복사
COPY build.gradle settings.gradle ./

# dos2unix 설치 및 gradlew 실행권한 부여
RUN apk add --no-cache dos2unix && \
    dos2unix ./gradlew && \
    chmod +x ./gradlew

# 의존성 캐싱
RUN ./gradlew dependencies --no-daemon

# 소스 복사
COPY src ./src

# 빌드 실행 (테스트 제외)
RUN ./gradlew clean build -x test --no-daemon


# ✅ Runtime Stage
FROM amazoncorretto:17-alpine

# 작업 디렉토리 설정
WORKDIR /app

# 빌드된 JAR 복사 (확인된 파일명)
COPY --from=builder /build-env/build/libs/discodeit-0.0.1-SNAPSHOT.jar app.jar

# 포트 노출 (예: 8080)
EXPOSE 8080

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"]
