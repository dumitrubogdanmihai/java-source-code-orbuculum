#!/bin/bash

# Install Java 8
echo oracle-java8-installer shared/accepted-oracle-license-v1-1 select true | sudo /usr/bin/debconf-set-selections
echo "\n" | sudo add-apt-repository ppa:webupd8team/java
sudo apt-get -q update
sudo apt-get install -y oracle-java8-installer
echo "export JAVA_HOME=/usr/lib/jvm/java-8-oracle/" >> /home/vagrant/.bashrc
echo "cd /workspace/solr/" >> /home/vagrant/.bashrc

# Install useful dev packages
apt-get -y install \
 maven \
 subversion \
 build-essential

curl -sL https://deb.nodesource.com/setup_7.x | sudo -E bash -
sudo apt-get install -y nodejs
sudo npm install -g bower gulp yarn
