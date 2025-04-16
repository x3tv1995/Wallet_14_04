FROM openjdk:17-slim
WORKDIR /app
COPY Wallet-0.0.1-SNAPSHOT.jar /app/wallet-service.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/wallet-service.jar"]