FROM openjdk:11
ADD target/user-mysql.jar user-mysql.jar
EXPOSE 8089
ENTRYPOINT ["java", "-jar", "user-mysql.jar"]