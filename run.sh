#bin/bash

# run IPFIXcol
echo "Starting IPFIXcol flow collector"
ipfixcol -d -c /vagrant/ipfixcol/startup.xml

# wait for IPFIXcol initialization
sleep 1

# send data to IPFIXcol
echo "Starting flow data source"
ipfixsend -i /vagrant/data/data.ipfix -R 1.0 -d 127.0.0.1 -t TCP &

# run espercli
echo "Running multi-stage HTTP attack detection using Esper"
socat -u UDP-LISTEN:4444 STDOUT | espercli -m /vagrant/queries/http/Complete_non_grouped.epl
