FROM public.ecr.aws/docker/library/amazoncorretto:17
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
# 도커 컨테이너를 시작할 때 실행할 명령어2
ENTRYPOINT ["java","-jar","/app.jar"]