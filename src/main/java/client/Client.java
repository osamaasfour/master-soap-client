package client;

import generated.HelloWorld;
import generated.HelloWorldService;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import javax.xml.ws.WebServiceException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Scanner;

public class Client {

    private final HelloWorld helloWorld;

    public Client(URL wsdlUrl) {
        this.helloWorld = new HelloWorldService(wsdlUrl).getHelloWorldPort();
    }

    public static void main(String args[]) throws MalformedURLException {


        ArgumentParser parser = ArgumentParsers.newArgumentParser("Size requester")
                .defaultHelp(true);
        parser.addArgument("-s")
                .type(Integer.class)
                .help("Number of char to request from server")
                .required(true);
        parser.addArgument("-n").help("Number of times to run").dest("n").setDefault(1).type(Integer.class).required(true);
        parser.addArgument("wsdlURL").help("WSDL URL");

        Namespace ns = null;
        try {
            ns = parser.parseArgs(args);
        } catch (ArgumentParserException e) {
            parser.handleError(e);
            System.exit(1);
        }


        URL wsdlUrl = new URL(ns.getString("wsdlURL"));
        int n = ns.getInt("n");
        int size = ns.getInt("s");

        Client client = new Client(wsdlUrl);
        //client.commandLoop();

        DescriptiveStatistics stats = new DescriptiveStatistics();
        for (int i = 0; i < n; i++) {
            long ts1 = System.currentTimeMillis();
            client.requestMessage(size);
            long ts2 = System.currentTimeMillis();
            stats.addValue(ts2-ts1);
        }

        System.out.println("\nFinished running " + n + " tests");
        System.out.println(LocalDateTime.now().toString());
        System.out.println("Mean: " + stats.getMean());
        System.out.println("Standard Deviation: " + stats.getStandardDeviation());
        System.out.println("Variance: " + stats.getVariance());

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
        } else if(input.startsWith("get ")) {
            requestMessage(Integer.parseInt(args[1]));
        } else {
            System.out.println("Unknown command: " + args[0]);
        }
    }

    private void requestMessage(int size) {
        long t1 = System.nanoTime();
        String result = helloWorld.requestMessage(size);
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
