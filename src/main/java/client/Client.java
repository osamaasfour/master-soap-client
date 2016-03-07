package client;

import generated.HelloWorld;
import generated.HelloWorldService;

import javax.xml.ws.WebServiceException;
import java.net.MalformedURLException;
import java.net.ProxySelector;
import java.net.URL;
import java.util.Scanner;

public class Client {

    private final HelloWorld helloWorld;

    public Client(URL wsdlUrl) {
        this.helloWorld = new HelloWorldService(wsdlUrl).getHelloWorldPort();
    }

    public static void main(String args[]) throws MalformedURLException {

        if (args.length < 1) {
            printUsage();
            System.exit(1);
        }

        URL wsdlUrl = new URL(args[0]);

        Client client = new Client(wsdlUrl);
        client.commandLoop();
    }

    private void commandLoop() {
        String input = "";
        Scanner scanner = new Scanner(System.in);

        while (!input.equals("exit")) {
            System.out.print("Input > ");
            input = scanner.nextLine();
            parseCommand(input);
        }

        System.out.println("Exiting..");
    }

    private void parseCommand(String input) {
        String args[] = input.split(" ");
        if (input.startsWith("hello ")) {
            helloWorld(args[1]);
        } else if(input.startsWith("request ")) {
            requestMessage(args[1]);
        } else {
            System.out.println("Unknown command: " + args[0]);
        }
    }

    private void requestMessage(String arg) {
        long t1 = System.nanoTime();
        String result = helloWorld.requestMessage(Integer.parseInt(arg));
        System.out.println("Received response. String length: " + result.length());

        long t2 = System.nanoTime();
        System.out.println("Execution time: " + ((t2 - t1) * 1e-6) + " milliseconds");
    }

    private void helloWorld(String from) {
        try {
            String response = helloWorld.sayHelloWorldFrom(from);
            System.out.println(response);
        } catch(WebServiceException e ){
            System.out.println(e.toString());
        }
    }

    static void printUsage(){
        System.out.println("Usage: Client wsdlUrl");
    }
}
