FROM eclipse-temurin:17.0.6_10-jre-alpine

# copy content to image
RUN mkdir /app
WORKDIR /app

COPY cs389-0.0.1-SNAPSHOT.jar .

EXPOSE 80

CMD ["java", "-jar", "-Dserver.port=80",  "cs389-0.0.1-SNAPSHOT.jar"]
