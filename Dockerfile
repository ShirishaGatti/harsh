FROM openjdk:17
WORKDIR /app

COPY target/harsh.jar /app/harsh.jar

EXPOSE 8086

ENTRYPOINT ["java" ,"-jar","harsh.jar" ]
