# -*- mode: ruby -*-
# vi: set ft=ruby :

Vagrant.configure("2") do |config|
  config.vm.box = "centos/7"
  config.vm.network :private_network, ip: "10.0.15.105"

  # Enable provisioning with a shell script.
   config.vm.provision "shell", inline: <<-SHELL

# install packages
sudo yum install -y maven wget socat

# install oracle java
# cd /tmp
# wget --no-cookies --no-check-certificate --header "Cookie: gpw_e24=http%3A%2F%2Fwww.oracle.com%2F; oraclelicense=accept-securebackup-cookie" "http://download.oracle.com/otn-pub/java/jdk/8u161-b12/2f38c3b165be4555a1fa6e98c45e0808/jdk-8u161-linux-x64.rpm"
# sudo yum install -y jdk-8u161-linux-x64.rpm

# setup java binaries
# sudo alternatives --install /usr/bin/java java /usr/java/latest/bin/java 2
# sudo alternatives --install /usr/bin/javac javac /usr/java/latest/bin/javac 2
# sudo alternatives --set java /usr/java/latest/bin/java
# sudo alternatives --set javac /usr/java/latest/bin/javac

# package espercli using maven
cd /vagrant/espercli
mvn package
sudo cp /vagrant/espercli/espercli /usr/local/bin/

# install IPFIXcol
sudo wget 'https://copr.fedorainfracloud.org/coprs/g/CESNET/IPFIXcol/repo/epel-7/group_CESNET-IPFIXcol-epel-7.repo' -O /etc/yum.repos.d/COPR-IPFIXcol.repo
sudo yum install -y ipfixcol ipfixcol-json-output

sudo wget https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-6.2.4.rpm -O /tmp/elastic.rpm
sudo wget https://artifacts.elastic.co/downloads/kibana/kibana-6.2.4-x86_64.rpm -O /tmp/kibana.rpm
sudo wget https://artifacts.elastic.co/downloads/logstash/logstash-6.2.4.rpm -O /tmp/logstash.rpm

sudo rpm -ivh /tmp/elastic.rpm
sudo rpm -ivh /tmp/kibana.rpm
sudo rpm -ivh /tmp/logstash.rpm

sudo wget https://raw.githubusercontent.com/CSIRT-MU/FlowCEP/master/logstash/01-logs.conf -O /etc/logstash/conf.d/01-logs.conf

sudo systemctl enable elasticsearch
sudo systemctl enable kibana
sudo systemctl enable logstash

sudo systemctl start elasticsearch
sudo systemctl start kibana
sudo systemctl start logstash

   SHELL
end
