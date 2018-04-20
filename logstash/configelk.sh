#!/usr/bin/env bash

sudo sed -i -e 's/#network.host: 192.168.0.1/network.host: 10.0.15.105/g' /etc/elasticsearch/elasticsearch.yml
sudo sed -i -e 's/#server.host: "localhost"/server.host: "10.0.15.105"/g' /etc/kibana/kibana.yml
sudo sed -i -e 's/#elasticsearch.url: "http:\/\/localhost:9200"/elasticsearch.url: "http:\/\/10.0.15.105:9200"/g' /etc/kibana/kibana.yml