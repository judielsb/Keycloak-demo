#
FROM maven:3.6.0-jdk-11-slim 
WORKDIR /home/app
COPY ./builtin-wallet-authentication/src /home/app/src
COPY ./builtin-wallet-authentication/pom.xml /home/app
RUN mvn clean package

COPY ./keycloak-database-federation/src /home/app/keycloak-database-federation/src
COPY ./keycloak-database-federation/pom.xml /home/app/keycloak-database-federation
RUN mvn clean package -f ./keycloak-database-federation/pom.xml
