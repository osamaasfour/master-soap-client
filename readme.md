###Compiling
To compile a executable jar file type the following into a terminal:
` $mvn clean install`


###Running with http-proxy
`java -Dhttp.proxyHost=localhost -Dhttp.proxyPort=3001 -Dhttp.nonProxyHosts= -jar target/client-1.0-SNAPSHOT.jar wsdlUrl`
