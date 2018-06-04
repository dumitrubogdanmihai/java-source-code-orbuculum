#!/bin/bash
# Those commands must be executed with the server running and, because of that the, image cannot be built with just a a Dockerfile.

docker-compose up -d orbuculum

docker-compose exec orbuculum bash /opt/solr/bin/solr delete -c orbuculum
docker-compose exec orbuculum bash /opt/solr/bin/solr create -c orbuculum

curl -X POST -H 'Content-type:application/json' --data-binary '{"add-field": {"name":"class", "type":"text_general", "multiValued":false, "stored":true}}' http://localhost:8983/solr/orbuculum/schema
curl -X POST -H 'Content-type:application/json' --data-binary '{"add-field": {"name":"method", "type":"text_general", "multiValued":false, "stored":true}}' http://localhost:8983/solr/orbuculum/schema
curl -X POST -H 'Content-type:application/json' --data-binary '{"add-field": {"name":"method-q-name", "type":"text_general", "multiValued":false, "stored":true}}' http://localhost:8983/solr/orbuculum/schema
curl -X POST -H 'Content-type:application/json' --data-binary '{"add-field": {"name":"method-raw", "type":"text_general", "multiValued":false, "stored":false}}' http://localhost:8983/solr/orbuculum/schema
curl -X POST -H 'Content-type:application/json' --data-binary '{"add-field": {"name":"parameter", "type":"text_general", "multiValued":true, "stored":true}}' http://localhost:8983/solr/orbuculum/schema
curl -X POST -H 'Content-type:application/json' --data-binary '{"add-field": {"name":"javadoc", "type":"text_general", "multiValued":false, "stored":false}}' http://localhost:8983/solr/orbuculum/schema
curl -X POST -H 'Content-type:application/json' --data-binary '{"add-field": {"name":"offset", "type":"text_general", "multiValued":false, "stored":true}}' http://localhost:8983/solr/orbuculum/schema
curl -X POST -H 'Content-type:application/json' --data-binary '{"add-field": {"name":"java-keyword", "type":"text_general", "multiValued":true, "stored":false}}' http://localhost:8983/solr/orbuculum/schema
curl -X POST -H 'Content-type:application/json' --data-binary '{"add-field": {"name":"comment", "type":"text_general", "multiValued":true, "stored":false}}' http://localhost:8983/solr/orbuculum/schema
curl -X POST -H 'Content-type:application/json' --data-binary '{"add-field": {"name":"object", "type":"text_general", "multiValued":true, "stored":false}}' http://localhost:8983/solr/orbuculum/schema
curl -X POST -H 'Content-type:application/json' --data-binary '{"add-field": {"name":"method-call", "type":"text_general", "multiValued":true, "stored":false}}' http://localhost:8983/solr/orbuculum/schema
