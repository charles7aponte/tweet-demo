FROM amazoncorretto:17

WORKDIR /app

COPY build/libs/tweet-uala-0.1-all.jar /app/tweet-uala.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "tweet-uala.jar"]