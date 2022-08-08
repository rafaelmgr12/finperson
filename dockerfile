FROM openjdk:11
COPY ./out/production/finperson/ /tmp
WORKDIR /tmp
ENTRYPOINT ["java","HelloWorld"]