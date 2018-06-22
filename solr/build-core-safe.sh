#!/bin/bash
# Those commands must be executed with the server running and, because of that, the image cannot be built with just a Dockerfile.

#docker-compose up -d orbuculum

#sleep 7
docker-compose exec -T orbuculum bash /opt/solr/bin/solr delete -c orbuculum
sleep 2
docker-compose exec -T orbuculum bash /opt/solr/bin/solr create -c orbuculum
sleep 2

curl -X POST -H 'Content-type:application/json' --data-binary '{"add-field": {"name":"project", "type":"text_general", "multiValued":false, "stored":true}}' http://localhost:8983/solr/orbuculum/schema
curl -X POST -H 'Content-type:application/json' --data-binary '{"add-field": {"name":"path", "type":"text_general", "multiValued":false, "stored":true}}' http://localhost:8983/solr/orbuculum/schema
curl -X POST -H 'Content-type:application/json' --data-binary '{"add-field": {"name":"class", "type":"text_general", "multiValued":false, "stored":true}}' http://localhost:8983/solr/orbuculum/schema
curl -X POST -H 'Content-type:application/json' --data-binary '{"add-field": {"name":"method", "type":"text_general", "multiValued":false, "stored":true}}' http://localhost:8983/solr/orbuculum/schema
curl -X POST -H 'Content-type:application/json' --data-binary '{"add-field": {"name":"method-raw", "type":"text_general", "multiValued":false, "stored":true}}' http://localhost:8983/solr/orbuculum/schema
curl -X POST -H 'Content-type:application/json' --data-binary '{"add-field": {"name":"parameter", "type":"text_general", "multiValued":true, "stored":true}}' http://localhost:8983/solr/orbuculum/schema
curl -X POST -H 'Content-type:application/json' --data-binary '{"add-field": {"name":"javadoc", "type":"text_general", "multiValued":false, "stored":true}}' http://localhost:8983/solr/orbuculum/schema
curl -X POST -H 'Content-type:application/json' --data-binary '{"add-field": {"name":"line-start", "type":"pint", "multiValued":false, "stored":true}}' http://localhost:8983/solr/orbuculum/schema
curl -X POST -H 'Content-type:application/json' --data-binary '{"add-field": {"name":"line-end", "type":"pint", "multiValued":false, "stored":true}}' http://localhost:8983/solr/orbuculum/schema
curl -X POST -H 'Content-type:application/json' --data-binary '{"add-field": {"name":"java-keyword", "type":"text_general", "multiValued":true, "stored":true}}' http://localhost:8983/solr/orbuculum/schema
curl -X POST -H 'Content-type:application/json' --data-binary '{"add-field": {"name":"comment", "type":"text_general", "multiValued":true, "stored":true}}' http://localhost:8983/solr/orbuculum/schema
curl -X POST -H 'Content-type:application/json' --data-binary '{"add-field": {"name":"object", "type":"text_general", "multiValued":true, "stored":true}}' http://localhost:8983/solr/orbuculum/schema
curl -X POST -H 'Content-type:application/json' --data-binary '{"add-field": {"name":"method-call", "type":"text_general", "multiValued":true, "stored":true}}' http://localhost:8983/solr/orbuculum/schema

#sleep 1

#docker-compose stop orbuculum
