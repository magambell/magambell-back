FROM public.ecr.aws/docker/library/amazoncorretto:17

ENV JAVA_OPTS="-Djava.security.egd=file:/dev/./urandom"
ENV JAR_FILE=./build/libs/magambell-0.0.1-SNAPSHOT.jar

WORKDIR /app
COPY $JAR_FILE app.jar

EXPOSE 8080
CMD ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
