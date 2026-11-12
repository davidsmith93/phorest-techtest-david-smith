FROM azul/zulu-openjdk:25-latest

RUN addgroup --system spring && adduser --system --ingroup spring spring
USER spring:spring

COPY target/phorest-techtest-david-smith*.jar application.jar

CMD ["sh", "-c", "exec java -jar application.jar --spring.profiles.active=docker"]