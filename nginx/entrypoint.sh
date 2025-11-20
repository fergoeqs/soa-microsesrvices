#!/bin/sh
if [ ! -f /certs/server.crt ]; then
#  mkdir -p /certs
#  openssl req -x509 -nodes -days 365 -newkey rsa:2048 \
#    -keyout /certs/server.key \
#    -out /certs/server.crt \
#    -subj "/CN=localhost"
mkdir -p $CERTS_DIR
openssl genrsa -out $CERTS_DIR/ca.key 4096
openssl req -x509 -new -nodes -key $CERTS_DIR/ca.key -sha256 -days 3650 \
  -subj "/C=RU/ST=Moscow/L=Moscow/O=LocalDev/CN=LocalDev-CA" \
  -out $CERTS_DIR/ca.crt


fi

exec nginx -g "daemon off;"
