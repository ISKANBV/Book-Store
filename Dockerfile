FROM openjdk:17-jdk-alpine
ENV JAVA_HOME /usr/local/openjdk-17
ENV PATH $JAVA_HOME/bin:$PATH
COPY build/libs/book-user-store-0.0.1-SNAPSHOT.jar /app/
WORKDIR /app/
ENTRYPOINT ["java"]
CMD ["-jar", "/app/book-user-store-0.0.1-SNAPSHOT.jar"]