FROM public.ecr.aws/docker/library/amazoncorretto:17

# 루트 권한으로 실행 (생략해도 기본 root지만 명시적으로)
USER root

# 빌드 시 전달되는 인자 (GitHub Actions에서 --build-arg 로 전달됨)
ARG JAR_FILE
ARG SPRING_PROFILES_ACTIVE
ARG MARIA_PASSWORD
ARG MARIA_USERNAME
ARG SERVER_HOST
ARG AWS_ECR_ACCESS_KEY
ARG AWS_ECR_SECRET_KEY

# 런타임 환경 변수로 설정 (docker run -e 로도 override 가능)
ENV SPRING_PROFILES_ACTIVE=$SPRING_PROFILES_ACTIVE \
    MARIA_PASSWORD=$MARIA_PASSWORD \
    MARIA_USERNAME=$MARIA_USERNAME \
    SERVER_HOST=$SERVER_HOST \
    AWS_ECR_ACCESS_KEY=$AWS_ECR_ACCESS_KEY \
    AWS_ECR_SECRET_KEY=$AWS_ECR_SECRET_KEY \
    JAVA_OPTS="-Djava.security.egd=file:/dev/./urandom"

WORKDIR /app

# 빌드된 JAR 복사
COPY ${JAR_FILE} app.jar

# 필요시 변경
EXPOSE 8080

# 실행 명령
CMD ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
