FROM amazoncorretto:17-alpine
MAINTAINER hu.nl
COPY target/board-service-0.0.1-SNAPSHOT.jar board-service.jar
ENTRYPOINT ["java","-jar","/board-service.jar"]
HEALTHCHECK --interval=1m --timeout=3s --start-period=10s \
  CMD wget --quiet --tries=1 --spider http://localhost:8080/health || exit 1