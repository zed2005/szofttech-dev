FROM maven:3.9.15-eclipse-temurin-21-alpine AS builder
WORKDIR /build

COPY szofttech-test/szofttech-test/pom.xml .
COPY szofttech-test/szofttech-test/src ./src

#RUN useradd uBuild
#USER uBuild

RUN mvn clean package

#RUN javac -cp "target/szofttech-test-1.0.jar" Test.java && \
#    jar uf target/szofttech-test-1.0.jar Test.class

FROM eclipse-temurin:21-jdk AS tester
WORKDIR /test

COPY --from=builder /build/target/szofttech-test-1.0-SNAPSHOT.jar ./app.jar
COPY run_tests.sh .
COPY ./szofttech-backbone/src/test/blackbox-test-files ./test_files

RUN chmod +x run_tests.sh

CMD ["./run_tests.sh" , "app.jar"]