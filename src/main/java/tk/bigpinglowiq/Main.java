package tk.bigpinglowiq;


import net.dv8tion.jda.api.JDA;
import tk.bigpinglowiq.TCPServer.TCPStart;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;


public class Main {

    private static JDA jda;

    public static void main(String[] args) {
        final String apiKey;
        try {
            apiKey = args[0];
        } catch (IndexOutOfBoundsException ignored) {
            System.out.println("Please add an api key");
            return;
        }


        waitForInternet();

        TCPStart.init();

        Application app = new Application(apiKey);
        app.start();
        jda = app.getJDA();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down");
            TCPStart.shutdown();
            app.close();
        }));

        while (true) {
            if (app.isCrashed()) {
                waitForInternet();
                app.restart();
                jda = app.getJDA();
            }
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }


    }

    private static boolean checkInternet() {
        try {
            URL url = new URL("http://www.google.com");
            URLConnection connection = url.openConnection();
            connection.connect();
            return true;

        } catch (IOException e) {
            return false;
        }
    }

    private static void waitForInternet() {
        System.out.print("\nWaiting for internet.");
        while (!checkInternet()) {
            System.out.print(".");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("\n");
    }

    public static JDA getJDA(){
        return jda;
    }




}
