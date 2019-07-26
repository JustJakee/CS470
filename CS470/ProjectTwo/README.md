PROJECT TWO - JAVA PROXY SERVER

In this project we set out to design and develop a web proxy server. 

A web proxy delivers content on behalf of remote web servers. In this project we will develop a multithreaded web proxy that supports a subset of functionalities as per RFC 2616.

Client -> Web Proxy -> Web Server (Request)

Client <- Web Proxy <- Web Server (Response)

The proxy server receives request from the client, forwards it to the web server and receives a response. It then sends this response back to the Client. 

A proxy server also performs caching along with being a forwarding agent. Due to caching the proxy server stores the fetched content in the local disk and provides the client this local copy in response to a request. The server however periodically checks for the newer version with the web server and if detects the change then retrieves the latest copy. 
