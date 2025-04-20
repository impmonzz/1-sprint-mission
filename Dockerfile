# discodeit 스프링 부트 프로젝트를 위한 Dockerfile

# ================= 1단계: 빌드(Build) =================
# 공식 Gradle 이미지(JDK 17 포함, Alpine 버전으로 크기 축소)를 임시 빌드 환경으로 사용합니다.
# 이 단계를 'builder'로 명명하여 나중에 참조할 수 있도록 합니다.
FROM gradle:8.6-jdk17-alpine AS builder

# 빌드 단계 내의 작업 디렉토리를 설정합니다.
WORKDIR /build-env

# Alpine Linux에는 기본적으로 dos2unix가 없으므로 설치합니다.
# Windows에서 gradlew 스크립트를 생성/수정한 경우에도 올바르게 작동하도록 돕습니다.
# --no-cache 옵션은 패키지 인덱스를 로컬에 저장하지 않아 공간을 절약합니다.
RUN apk add --no-cache dos2unix

# 필요한 Gradle Wrapper 파일만 먼저 복사합니다.
# 필요하지 않다면 전체 'gradle' 디렉토리를 복사하는 것을 피합니다.
COPY gradlew ./gradlew
COPY gradle/wrapper/gradle-wrapper.jar ./gradle/wrapper/gradle-wrapper.jar
COPY gradle/wrapper/gradle-wrapper.properties ./gradle/wrapper/gradle-wrapper.properties

# gradlew 스크립트의 줄 끝 문자를 Unix 형식으로 변환하고 실행 가능하도록 만듭니다.
RUN dos2unix ./gradlew \
    && chmod +x ./gradlew

# 빌드 설정 파일들을 복사합니다.
COPY build.gradle settings.gradle ./

# 프로젝트 의존성 파일들을 다운로드합니다.
# 이 단계는 Docker의 레이어 캐싱을 활용합니다. 만약 build.gradle/settings.gradle 파일이
# 변경되지 않으면, Docker는 이 레이어를 재사용하여 후속 빌드 속도를 높입니다.
# 컨테이너화된 빌드나 CI 환경에서는 --no-daemon 옵션 사용이 권장됩니다.
RUN ./gradlew dependencies --no-daemon

# 애플리케이션 소스 코드를 빌드 환경으로 복사합니다.
COPY src ./src

# 애플리케이션 JAR 파일을 빌드합니다.
# 'clean' 명령어는 이전 빌드 결과물이 영향을 주지 않도록 보장합니다.
# '-x test' 옵션은 Docker 이미지 빌드 중 테스트 실행을 건너뜁니다.
RUN ./gradlew clean build -x test --no-daemon


# ================= 2단계: 실행(Runtime) =================
# 최소한의 JRE 베이스 이미지(Alpine 기반 Amazon Corretto 17)를 사용하여 새롭고 깨끗한 단계를 시작합니다.
# JRE 버전(-jre)을 사용하는 것은 컴파일된 애플리케이션 실행에 충분하며 이미지 크기를 줄여줍니다.
FROM amazoncorretto:17-alpine-jre

# 실행 환경의 작업 디렉토리를 설정합니다.
WORKDIR /app

# 실행 시 필요한 환경 변수들을 정의합니다.
# PROJECT_NAME과 PROJECT_VERSION은 아래에서 복사될 JAR 파일을 찾는 데 사용됩니다 (이름을 변경하긴 하지만).
# 컨테이너 환경에서의 기본 메모리 튜닝을 위해 기본 JVM_OPTS가 제공됩니다.
# 이 값들은 컨테이너 시작 시 쉽게 재정의할 수 있습니다 (예: docker run -e JVM_OPTS="..." ...).
ENV PROJECT_NAME=discodeit \
    PROJECT_VERSION=1.2-M8 \
    JVM_OPTS="-Xmx192m -Xms92m -XX:MaxMetaspaceSize=192m -XX:+UseSerialGC"

# 'builder' 단계에서 빌드된 애플리케이션 JAR 파일*만* 복사합니다.
# 더 간단한 ENTRYPOINT 명령어를 위해 JAR 파일 이름을 'app.jar'로 변경합니다.
# /build-env/build/libs/... 경로는 'builder' 단계의 WORKDIR 및 출력 디렉토리에 해당합니다.
COPY --from=builder /build-env/build/libs/${PROJECT_NAME}-${PROJECT_VERSION}.jar app.jar

# 컨테이너 내부에서 애플리케이션이 리슨할 포트를 노출합니다.
# 스프링 부트는 기본적으로 8080 포트를 사용합니다. 만약 application.properties에서 다른
# server.port (예: 원래 요청된 80)를 설정했다면, 이 EXPOSE 값을 해당 포트로 수정하고
# 애플리케이션 설정도 일치하는지 확인해야 합니다. 여기서는 8080으로 진행합니다.
EXPOSE 8080

# 컨테이너가 시작될 때 실행될 명령어를 정의합니다.
# 'sh -c'를 사용하면 쉘이 $JVM_OPTS 변수를 올바르게 확장하여,
# JAR 파일을 실행하기 전에 Java 메모리/GC 옵션을 적용할 수 있습니다.
ENTRYPOINT ["sh", "-c", "java $JVM_OPTS -jar app.jar"]