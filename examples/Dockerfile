FROM java:8-jre-alpine

RUN apk add openssl --no-cache && \
    mkdir /home/wiremock && cd /home/wiremock && \
    wget -O wiremock-standalone.jar https://repo1.maven.org/maven2/com/github/tomakehurst/wiremock-standalone/2.23.2/wiremock-standalone-2.23.2.jar && \
    wget -O wiremock-redis-extension.jar https://github.com/gustajz/wiremock-redis-extension/releases/download/v1.0.0/wiremock-redis-extension-1.0.0-jar-with-dependencies.jar 

WORKDIR /home/wiremock

ENTRYPOINT ["java", "-cp", "wiremock-standalone.jar:wiremock-redis-extension.jar", "com.github.tomakehurst.wiremock.standalone.WireMockServerRunner"]

CMD ["--global-response-templating", "--extensions", "com.github.gustajz.wiremock.extension.Redis", "--https-port", "8081", "--verbose"]