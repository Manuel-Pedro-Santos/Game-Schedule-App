FROM openjdk:21-jdk-slim

WORKDIR /usr/app

COPY ./static-content ./static-content


COPY ./build/libs ./libs

CMD ["java", "-jar", "./libs/2324-2-LEIC42D-G05.jar"]