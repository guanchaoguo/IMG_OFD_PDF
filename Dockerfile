FROM openjdk:8-jdk-alpine AS TEMP_BUILD_IMAGE

RUN  set -eux && sed -i 's/dl-cdn.alpinelinux.org/mirrors.ustc.edu.cn/g' /etc/apk/repositories
RUN apk update && apk add --no-cache bash curl wget zip

WORKDIR /app

COPY settings.gradle settings.gradle
COPY gradle gradle
COPY build.gradle build.gradle
COPY gradlew gradlew

RUN  ./gradlew -v
COPY src src
RUN ./gradlew  build --refresh-dependencies
RUN  ./gradlew build
RUN ls   /app/build/libs/


FROM openjdk:8-jdk-alpine
WORKDIR /app
COPY --from=TEMP_BUILD_IMAGE app/build/libs/ofdconverter-0.0.1-SNAPSHOT.jar .
ENTRYPOINT ["java","-jar","ofdconverter-0.0.1-SNAPSHOT.jar"]
CMD ["-Duser.timezone=Asia/Shanghai","-Dspring.profiles.active=prod"]
