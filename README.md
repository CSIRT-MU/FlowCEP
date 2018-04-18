# FlowCEP

FlowCEP aims to simplify network traffic analysis using Complex Event Processing and flows.
This repository provides a set of tools (IPFIXcol and Esper) accompanied by scripts and
configuration files that provide a complete toolchain for converting flow records to security events.

A multi-stage HTTP brute-force attack is a model example here.

Use of Vagrant is recommended for easy environment setup.

## Short Guide

Setup virtual environment by running
```
vagrant up
```

Log into the virtual machine
```
vagrant ssh
```

Run the demo script
```
/vagrant/run.sh
```

Results of the detection should be visible after few minutes.

## Detailed Guide

Setup the virtual environmend just like in the Short Guide
```
vagrant up
vagrant ssh
```

There are three tools used in FlowCEP. First, a data source is needed. While it is simple to
utilize and external data source, it is even easier to provide static data for demonstration purposes.
The data source provides flow records in the IPFIX format, such as those that can be generated
by network devices. An `ipfixsend` tool is used to replay a captured sample in an infinite loop.

Second tools is a flow collector that can receive IPFIX data and convert them for further processing
by Esper engine. A secondary function of the flow collector is that it updates timestamps of the
replayed flows so that the Esper processes the data in a correct sequence. `ipfixcol` is used as
the flow collector.

The third tool is the Esper engine. Since it is available only as a Java library, we have created
a commandline tool `espercli` that is used to load and execute the queries written in the Event
Processing Language (EPL).

Now that we have introduced the tools, let us show how they play together. This is what the `run.sh`
script does:

Start the flow collector as a daemon. Receive on port TCP/4739 and send json messages to port UDP/4444.
```
ipfixcol -d -c /vagrant/ipfixcol/startup.xml
```

Start `ipfixsend` flow data source, replay with original speed ratio, send to localhost TCP/4739.
```
ipfixsend -i /vagrant/data/data.ipfix -R 1.0 -d 127.0.0.1 -t TCP &
```

Run `espercli`, load a specific EPL script. Note that the espercli does read from standard input.
```
socat -u UDP-LISTEN:4444 STDOUT | espercli -m /vagrant/queries/11-Multiphase_non_grouped.epl
```

The results of the queries are printed on standard output.

## Acknowledgement

This software package is an attachment to the demo paper "Rapid Prototyping of Flow-Based Detection Methods Using Complex Event Processing" presented at IFIP/IEEE NOMS 2018 conference.

Petr Velan, Martin Husák, Daniel Tovarňák: "Rapid Prototyping of Flow-Based Detection Methods Using Complex Event Processing" In Proceedings of IEEE/IFIP Network Operations and Management Symposium. Taipei. 2018. IEEE.

More information on the sample attack can be found in the earlier research paper "Security monitoring of HTTP traffic using extended flows" presented at FARES workshop of ARES 2015 conference.

Martin Husák, Petr Velan, and Jan Vykopal. Security monitoring of HTTP traffic using extended flows. In 2015 10th International Conference on Availability, Reliability and Security, pages 258--265, Toulouse, 2015. IEEE.
