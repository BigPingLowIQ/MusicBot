package tk.bigpinglowiq.TCPServer;

import net.dv8tion.jda.api.entities.Guild;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class Echoer extends Thread {
    private Socket socket;

    public Echoer(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

            while(true) {
                String echoString = input.readLine();
                if(echoString==null){
                    continue;
                }

                if(echoString.equals("exit")) {
                    break;
                }

                StringBuilder sb = new StringBuilder();
                if(echoString.equals("1")){
                    for(Guild guild : TCPStart.getDH().getGuilds()){
                        sb.append(guild.getName()).append(' ').append(guild.getIdLong()).append("\t");
                    }

                }else if(echoString.equals("2")) {



                }else{
                        sb.append("Wrong option.");

                }


                output.println(sb);
            }

        } catch(IOException e) {
            System.out.println("Oops: " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch(IOException e) {
                // Oh, well!
            }
        }

    }
}
