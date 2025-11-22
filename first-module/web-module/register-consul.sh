#!/bin/sh

CONSUL_URL="http://consul:8500"
SERVICE_NAME="organization-service"
SERVICE_ID="organization-service-1"
SERVICE_ADDRESS="wildfly"
SERVICE_PORT=8080

while ! curl -s $CONSUL_URL/v1/status/leader >/dev/null; do
    echo "Waiting for Consul..."
    sleep 2
done

cat <<EOF > /tmp/service.json
{
  "ID": "${SERVICE_ID}",
  "Name": "${SERVICE_NAME}",
  "Address": "${SERVICE_ADDRESS}",
  "Port": ${SERVICE_PORT},
  "Check": {
    "HTTP": "http://${SERVICE_ADDRESS}:${SERVICE_PORT}/web-module/api/organizations/health",
    "Interval": "10s"
  }
}
EOF

curl -X PUT --data @/tmp/service.json $CONSUL_URL/v1/agent/service/register
echo "Service registered in Consul"
