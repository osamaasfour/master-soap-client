package client;

import generated.HelloWorld;
import generated.HelloWorldService;

import javax.xml.ws.WebServiceException;
import java.net.MalformedURLException;
import java.net.ProxySelector;
import java.net.URL;

public class Client {
    public static void main(String args[]) throws MalformedURLException {
       // MyProxySelector ps = new MyProxySelector(ProxySelector.getDefault());
        //ProxySelector.setDefault(ps);

        if (args.length < 1) {
            printUsage();
            System.exit(1);
        }

        URL wsdlUrl = new URL(args[0]);

        try {
            HelloWorld helloWorld = new HelloWorldService(wsdlUrl).getHelloWorldPort();
            String response = helloWorld.sayHelloWorldFrom("Joakimx");
            System.out.println(response);
        } catch(WebServiceException e ){
            System.out.println(e.toString());

        }
    }

    static void printUsage(){
        System.out.println("Usage: Client wsdlUrl");
    }
}
