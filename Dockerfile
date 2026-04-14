FROM maven:latest-eclipse-temurin-21 AS builder
WORKDIR /build

COPY szofttech-test/pom.xml .
COPY szofttech-test/szofttech-test/src ./src
COPY szofttech-backbone/src/Test.java .

RUN useradd uBuild
USER uBuild

RUN mvn clean package

RUN javac -cp "target/szofttech-test-1.0.jar" Test.java && \
    jar uf target/szofttech-test-1.0.jar Test.class

FROM eclipse-temurin:21-jre AS tester
WORKDIR /test


COPY --from=builder /build/target/szofttech-test-1.0.jar ./app.jar
COPY run_tests.sh .
COPY /szofttech-backbone/test/blackbox_test_files ./test_files

RUN useradd uTest
USER uTest

RUN chmod +x run_tests.sh

CMD [ "./run_tests.sh" ]