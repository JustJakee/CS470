# CS470
Project codes for Computer Networks course.

  Our protocol was designed to have each machine send to every other machine all the 
IPs stored in the config file except for the IP of the machine that it is sending to.
When a machine goes down a timeout will occur and the subsequent machines IP Address 
will be removed from the config text file. With this implementation when a machine is
added to the cluster it is also added to the config file using a write method. 

	A reason our protocol is very helpful is that not every machine needs the full IP 
config list to start and can easily obtain all the machines in the cluster without 
having to connect to them first. The protocol is also very scalable and allows for 
many machines. When no other machines are running the lone machine will act as the 
initial host and remain idle.
