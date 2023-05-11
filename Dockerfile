# Build stage
FROM quay.io/keycloak/keycloak:20.0.5 as builder

# Enable health and metrics support
ENV KC_HEALTH_ENABLED=true
ENV KC_METRICS_ENABLED=true

WORKDIR /opt/keycloak
# for demonstration purposes only, please make sure to use proper certificates in production instead
RUN keytool -genkeypair -storepass password -storetype PKCS12 -keyalg RSA -keysize 2048 -dname "CN=server" -alias server -ext "SAN:c=DNS:localhost,IP:127.0.0.1" -keystore conf/server.keystore
RUN /opt/keycloak/bin/kc.sh build

COPY ./jars/ /opt/keycloak/providers/

COPY ./keycloak-demo-realm.json /opt/keycloak/data/import/keycloak-demo-realm.json
FROM quay.io/keycloak/keycloak:20.0.3
COPY --from=builder /opt/keycloak/ /opt/keycloak/


ENTRYPOINT ["/opt/keycloak/bin/kc.sh", "start-dev", "--import-realm", " --proxy edge --http-relative-path /auth"]