FROM openjdk:11-jre
MAINTAINER "jayas" <mojunseo49@naver.com>
COPY target/demo-*.jar app.jar


ENV DBPATH ${DBPATH}
ENV DBUSER ${DBUSER}
ENV DBPWD ${DBPWD}
ENTRYPOINT ["java","-jar","app.jar"]