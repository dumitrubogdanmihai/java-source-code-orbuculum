#!/bin/bash

# Install docker
wget -qO- https://get.docker.com/ | sh
sudo usermod -aG docker vagrant

#install docker-compose
curl -L https://github.com/docker/compose/releases/download/1.8.0/docker-compose-`uname -s`-`uname -m` > /usr/local/bin/docker-compose
chmod +x /usr/local/bin/docker-compose

sudo curl -sSL https://get.docker.com/ | sh

sudo service docker restart

