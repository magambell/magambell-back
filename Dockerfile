FROM public.ecr.aws/docker/library/amazoncorretto:17

ENV JAVA_OPTS="-Djava.security.egd=file:/dev/./urandom"

WORKDIR /app
COPY build/libs/*.jar app.jar

EXPOSE 8080

CMD ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
